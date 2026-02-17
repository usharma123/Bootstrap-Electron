import { spawn, type ChildProcessWithoutNullStreams } from "node:child_process"
import path from "node:path"
import { fileURLToPath } from "node:url"
import { JsonRpcClient } from "./jsonrpc-client"
import type { HarnessEvent, HarnessRequestMap, HarnessMethod } from "./types"

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)
const repoRoot = path.resolve(__dirname, "../../")

type BridgeState = "idle" | "starting" | "ready" | "error"

export class HarnessBridge {
  private child: ChildProcessWithoutNullStreams | undefined
  private client: JsonRpcClient | undefined
  private state: BridgeState = "idle"
  private workspace = process.cwd()
  private readonly eventHandlers = new Set<(event: HarnessEvent) => void>()

  getWorkspace() {
    return this.workspace
  }

  subscribe(handler: (event: HarnessEvent) => void) {
    this.eventHandlers.add(handler)
    return () => this.eventHandlers.delete(handler)
  }

  async ensureStarted() {
    if (this.state === "ready" && this.client) return
    await this.startChild()
  }

  async setWorkspace(directory: string) {
    this.workspace = directory
    await this.restartChild()
  }

  async request<K extends HarnessMethod>(method: K, params?: HarnessRequestMap[K]["params"]) {
    await this.ensureStarted()
    if (!this.client) throw new Error("Harness bridge unavailable")
    return (await this.client.request(this.toRpcMethod(method), (params ?? {}) as Record<string, unknown>)) as HarnessRequestMap[K]["result"]
  }

  dispose() {
    this.client?.close()
    this.child?.kill()
    this.client = undefined
    this.child = undefined
    this.state = "idle"
  }

  private async restartChild() {
    this.dispose()
    await this.startChild()
  }

  private async startChild() {
    this.state = "starting"

    const command = this.resolveHarnessCommand(this.workspace)
    const child = spawn(command.bin, command.args, {
      cwd: repoRoot,
      stdio: "pipe",
      env: {
        ...process.env,
        FORCE_COLOR: "0",
      },
    })

    child.on("error", (error) => {
      this.state = "error"
      this.emit({ method: "harness.crash", params: { message: error.message } })
    })

    child.on("exit", (code, signal) => {
      if (this.state === "idle") return
      this.state = "error"
      this.emit({
        method: "harness.crash",
        params: {
          code: code ?? -1,
          signal: signal ?? "unknown",
        },
      })
    })

    this.child = child
    this.client = new JsonRpcClient(child.stdin, child.stdout, child.stderr)
    this.client.onNotification((method, params) => {
      this.emit({ method, params })
    })

    try {
      await this.client.request("initialize", {})
      this.state = "ready"
    } catch (error) {
      this.state = "error"
      throw error
    }
  }

  private emit(event: HarnessEvent) {
    for (const handler of this.eventHandlers) {
      handler(event)
    }
  }

  private resolveHarnessCommand(workspace: string) {
    const bunPath = process.env.BUN_BINARY ?? "bun"
    return {
      bin: bunPath,
      args: ["run", "--conditions=browser", "./src/index.ts", "harness", "--cwd", workspace],
    }
  }

  private toRpcMethod(method: HarnessMethod) {
    switch (method) {
      case "initialize":
        return "initialize"
      case "threadList":
        return "thread.list"
      case "threadCreate":
        return "thread.create"
      case "threadGet":
        return "thread.get"
      case "turnStart":
        return "turn.start"
      case "turnCancel":
        return "turn.cancel"
      case "approvalRespond":
        return "approval.respond"
      case "setWorkspace":
        return "initialize"
    }
  }
}
