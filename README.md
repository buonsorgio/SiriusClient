# ✦ Sirius Client

A legitimate, non-cheating PVP utility client for Minecraft 1.8.9, built with Minecraft Forge. Focused on performance, cosmetics, and HUD customization — similar to Lunar Client or Badlion Client.

## Features

### HUD Modules
- **FPS Counter** — Displays current FPS
- **CPS Counter** — Shows left and right click CPS
- **Armor Status** — Shows helmet/chest/legs/boots durability with icons
- **Potion Effects** — Active potion effects and duration display
- **Coordinates** — X Y Z position display
- **Direction** — Compass / cardinal direction
- **Clock** — Real world time
- **Ping** — Current server ping
- **Keystrokes** — WASD + LMB/RMB keystroke display (Lunar Client style)
- **Scoreboard** — Custom styled scoreboard renderer
- **BossBar** — Custom styled boss bar

### Visual Modules
- **Hit Color** — Custom hurt color flash (configurable)
- **Crosshair** — Custom crosshair styles (classic, dot, cross)
- **Fullbright** — Maximum gamma
- **TNT Timer** — Countdown above lit TNT
- **Cape Renderer** — Custom cape system

### Utility Modules
- **FPS Boost** — Reduces particles, optimizes rendering
- **Chat Filter** — Filter chat messages by keyword
- **AutoGG** — Sends configurable message on game end
- **Zoom** — C key zoom with scroll wheel FOV adjustment
- **Toggle Sprint** — Hold/toggle sprint mode

### Systems
- **ClickGUI** — Open with RIGHT SHIFT. Dark themed GUI with module toggles and settings
- **Draggable HUD** — Drag HUD elements when ClickGUI is open
- **Config System** — Auto-saves module states and HUD positions to JSON
- **Cape System** — Custom cape rendering with URL-based texture fetching
- **Entity Culling** — Skip rendering entities outside FOV for performance

## Design
- Color palette: Deep Black (#0A0A0A), Dark Red (#8B0000), Silver (#C0C0C0), White (#FFFFFF)
- Clean, modern UI with smooth animations
- "✦ SIRIUS" branding on main menu

## Building

### Requirements
- Java 8 (JDK 1.8)
- Gradle (wrapper included)

### Setup
```bash
# Set JAVA_HOME to Java 8
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

# Setup Forge workspace
./gradlew setupDecompWorkspace

# Build the mod
./gradlew build
```

The built JAR will be in `build/libs/`.

### IDE Setup
```bash
# IntelliJ IDEA
./gradlew idea

# Eclipse
./gradlew eclipse
```

## Installation
1. Install Minecraft Forge 1.8.9 (11.15.1.2318)
2. Place the built JAR in `.minecraft/mods/`
3. Launch Minecraft with the Forge profile

## Configuration
Config is saved to `.minecraft/sirius/config.json`. Cape textures are cached in `.minecraft/sirius/capes/`.

## Controls
- **RIGHT SHIFT** — Open/close ClickGUI
- **C** — Zoom (when Zoom module is enabled)
- Module keybinds are configurable in the ClickGUI

## Technical Details
- Minecraft: 1.8.9
- Forge: 11.15.1.2318
- Java: 8
- Rendering: LWJGL 2 / Minecraft GL pipeline
- Config: Gson JSON serialization

## License
All rights reserved.
