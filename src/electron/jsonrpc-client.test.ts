import { describe, expect, it } from "bun:test"
import { PassThrough } from "node:stream"
import { JsonRpcClient } from "./jsonrpc-client"

function setup() {
  const stdin = new PassThrough()
  const stdout = new PassThrough()
  const stderr = new PassThrough()
  const written: string[] = []
  stdin.on("data", (chunk) => written.push(chunk.toString()))

  const client = new JsonRpcClient(stdin, stdout, stderr)
  const close = () => {
    client.close()
    stdin.end()
    stdout.end()
    stderr.end()
  }

  return { client, stdout, written, close }
}

describe("json rpc client", () => {
  it("correlates request and response by id", async () => {
    const { client, stdout, written, close } = setup()
    const pending = client.request("thread.list", {}, 1000)

    expect(written[0]).toContain("\"method\":\"thread.list\"")
    stdout.write('{"jsonrpc":"2.0","id":1,"result":{"threads":[]}}\n')

    await expect(pending).resolves.toEqual({ threads: [] })
    close()
  })

  it("parses partial lines from stdout", async () => {
    const { client, stdout, written, close } = setup()
    const pending = client.request("initialize", {}, 1000)
    const id = JSON.parse(written[0]).id

    stdout.write(`{"jsonrpc":"2.0","id":${id},"result":{"version":"0.1"`)
    stdout.write(',"capabilities":{}}}')
    stdout.write("\n")

    await expect(pending).resolves.toEqual({ version: "0.1", capabilities: {} })
    close()
  })

  it("emits notifications", async () => {
    const { client, stdout, close } = setup()
    let value = ""

    client.onNotification((method) => {
      value = method
    })

    stdout.write('{"jsonrpc":"2.0","method":"item.delta","params":{}}\n')
    await Promise.resolve()

    expect(value).toBe("item.delta")
    close()
  })
})
