# SpawnSway Minecraft Plugin

SpawnSway is a Minecraft server plugin that creates a locked spawn camera effect with fixed player position and rotation plus a gentle vertical bobbing motion. It is a Paper Minecraft plugin built with the Bukkit API for Paper 1.21.11 and Java 21.

## Install

1. Build the plugin with Maven:

   ```bash
   mvn package
   ```

2. Copy the generated `target/plugin.jar` file into your server's `plugins` folder.

3. Start or restart a Paper 1.21.11 server running Java 21.

4. Confirm that SpawnSway appears as enabled in the server console.

## Use

1. Join the server or respawn after installing the plugin.
2. Your player is anchored at the current spawn or respawn location.
3. The plugin continuously applies a small up-and-down bobbing motion.
4. Player movement and camera rotation are returned to the stored anchor location while the camera session is active.
5. The effect starts automatically when the plugin enables, when a player joins, and after a player respawns.
6. No commands, permissions, configuration files, or additional setup are required.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/076bfdc6-6f20-4087-9bb5-0aad68ed66e0?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=076bfdc6-6f20-4087-9bb5-0aad68ed66e0
