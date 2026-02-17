import { describe, expect, it } from "bun:test"
import { createInitialState, reduceEvent } from "./store"

describe("renderer event reducer", () => {
  it("handles item started, delta, completed", () => {
    let state = createInitialState("/tmp/work")

    state = reduceEvent(state, {
      method: "item.started",
      params: {
        notification: "item.started",
        item: {
          itemId: "item-1",
          threadId: "thread-1",
          turnId: "turn-1",
          type: "assistant_message",
        },
      },
    })

    state = reduceEvent(state, {
      method: "item.delta",
      params: {
        notification: "item.delta",
        itemId: "item-1",
        threadId: "thread-1",
        turnId: "turn-1",
        delta: "hello",
      },
    })

    state = reduceEvent(state, {
      method: "item.completed",
      params: {
        notification: "item.completed",
        itemId: "item-1",
        threadId: "thread-1",
        turnId: "turn-1",
      },
    })

    const timeline = state.timelines["thread-1"]
    expect(timeline).toHaveLength(1)
    expect(timeline[0].content).toBe("hello")
    expect(timeline[0].completed).toBeTrue()
  })

  it("is idempotent for duplicate notifications", () => {
    const initial = createInitialState("/tmp/work")
    const event = {
      method: "turn.started",
      params: {
        notification: "turn.started",
        threadId: "thread-1",
        turnId: "turn-1",
        timestamp: 100,
      },
    }

    const first = reduceEvent(initial, event)
    const second = reduceEvent(first, event)
    expect(Object.keys(second.activeTurns)).toHaveLength(1)
    expect(second.activeTurns["thread-1"].turnId).toBe("turn-1")
  })
})
