# MCHunt

Hide as a block, seek as a player. 

This is based on the original BlockHunt plugin: [https://dev.bukkit.org/projects/blockhunt](https://dev.bukkit.org/projects/blockhunt) 

## Features
- Arena-based Hide and Seek gameplay
- GUI-driven setup and admin menus
- Sign integration for joining/interaction
- Localised messages (English provided)
- WorldGuard regions support

## Requirements
- Java 21 (targets Java 21)
- Server:
  - Spigot:
    - 1.21.x
- Dependencies (Install these plugins):
  - ProtocolLib
  - LibsDisguises
  - WorldGuard
  - WorldEdit (recommended/required by WorldGuard)

## Building
This project uses Gradle and the Shadow plugin to produce a fat JAR.

- Build: `./gradlew build`
- Output: `build/libs/MCHunt-1.0-SNAPSHOT.jar` (classifier is empty due to shadowJar configuration)

If you are on Windows, use `gradlew.bat build`.

## Installation
1. Build the plugin or obtain a release JAR.
2. Place the JAR into your server's `plugins/` directory.
3. Ensure the required dependencies (ProtocolLib, LibsDisguises, WorldGuard, WorldEdit) are also present in `plugins/`.
4. Start or restart the server. The plugin will generate its data folder and any configurations

## Basic Usage
- Command: `/mchunt` (alias: `/mch`)
  - Opens the MCHunt GUI. From there you can access admin/setup menus.

## Permissions
- `mchunt.admin` – Access admin/setup menus

Note: Most actions are performed through the in‑game menus rather than typed commands.

## Configuration & Data
- Arenas data: `plugins/MCHunt/arenas/`
- Arena setup data: `plugins/MCHunt/arena_setup/`
- Localization files (bundled in the JAR):
  - `language_en.properties`
  - `language_en_US.properties`

## Roadmap / TODO
See `TODO.txt` for planned features such as Vault economy support, configurable timings, scoreboard support, and better server stop/fail recovery.

## License
No license has been specified yet. If you intend to distribute or modify this plugin, please add an appropriate LICENSE file.
