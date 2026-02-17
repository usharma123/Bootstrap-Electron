import path from "node:path"
import { fileURLToPath } from "node:url"
import { app, BrowserWindow, ipcMain } from "electron"
import { HarnessBridge } from "./harness-bridge"
import type { HarnessMethod, HarnessRequestMap } from "./types"

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)

const rendererHTML = path.resolve(__dirname, "../dist/renderer/index.html")
const preloadPath = path.resolve(__dirname, "preload.js")
const devServerURL = process.env.ELECTRON_RENDERER_URL

let mainWindow: BrowserWindow | undefined
const bridge = new HarnessBridge()

async function createWindow() {
  const window = new BrowserWindow({
    width: 1320,
    height: 880,
    minWidth: 900,
    minHeight: 620,
    backgroundColor: "#0d1218",
    titleBarStyle: "hiddenInset",
    webPreferences: {
      preload: preloadPath,
      contextIsolation: true,
      nodeIntegration: false,
      sandbox: true,
    },
  })

  if (devServerURL) {
    await window.loadURL(devServerURL)
  } else {
    await window.loadFile(rendererHTML)
  }

  mainWindow = window
}

function setupBridgeForwarding() {
  bridge.subscribe((event) => {
    mainWindow?.webContents.send("harness:event", event)
  })
}

function setupIPC() {
  ipcMain.handle("harness:request", async (_event, payload: { method: HarnessMethod; params?: unknown }) => {
    if (payload.method === "setWorkspace") {
      const input = (payload.params ?? {}) as HarnessRequestMap["setWorkspace"]["params"]
      if (!input.directory || typeof input.directory !== "string") {
        throw new Error("setWorkspace requires directory")
      }
      await bridge.setWorkspace(input.directory)
      return { ok: true, directory: input.directory }
    }

    return await bridge.request(payload.method, payload.params as never)
  })

  ipcMain.handle("app:window", (_event, action: "minimize" | "maximize" | "close") => {
    if (!mainWindow) return { ok: false }
    if (action === "minimize") mainWindow.minimize()
    if (action === "maximize") {
      if (mainWindow.isMaximized()) mainWindow.unmaximize()
      else mainWindow.maximize()
    }
    if (action === "close") mainWindow.close()
    return { ok: true }
  })
}

app.whenReady().then(async () => {
  setupBridgeForwarding()
  setupIPC()
  await createWindow()

  app.on("activate", async () => {
    if (BrowserWindow.getAllWindows().length === 0) {
      await createWindow()
    }
  })
})

app.on("window-all-closed", () => {
  bridge.dispose()
  if (process.platform !== "darwin") app.quit()
})
