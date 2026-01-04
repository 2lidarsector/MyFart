```markdown
# Jarbot — Player Highlighter & Tracers (Fabric) — 1.21.10

This is a client-side Fabric mod that:
- Highlights (glowing outline) other players (client-side).
- Optionally draws tracers (lines) from the camera to each player.
- UI opens with Right Shift by default. The highlight/tracer toggle key is rebindable from the in-game UI.

Target environment
- Fabric Loader + Fabric API (for Minecraft 1.21.10).
- Java 17 (toolchain configured in Gradle)

Quick build in Codespaces or locally
1. Clone repository into Codespaces or locally.
2. Ensure the Gradle wrapper exists. If not, generate it locally:
   - Run `gradle wrapper` on a machine with Gradle installed, or run `./gradlew wrapper` if Gradle is available.
   - Commit the wrapper files.
3. Build:
   - Linux / macOS / Codespaces: `./gradlew build`
   - Windows: `gradlew.bat build`
4. The built mod JAR will be in `build/libs/` and named like `jarbot-1.0.0.jar`.

Install
- Copy the produced JAR to your Fabric client's `mods/` folder.
- Ensure you have Fabric Loader for 1.21.10 and a Fabric API compatible with 1.21.10.

Config
- Config saved to `config/jarbot.json`. This contains:
  - highlightEnabled (boolean)
  - tracersEnabled (boolean)
  - toggleKeyCode (int)
  - toggleScanCode (int)
- The UI accessible via Right Shift allows toggling highlight/tracers and rebinding the toggle key.

Notes / Troubleshooting
- If there are mapping or dependency version mismatches, edit `gradle.properties` to match the exact Minecraft + Fabric API + mappings versions you want.
- If compilation fails with mapping or Loom errors, paste the error here and I will adjust the Gradle config and/or code to match the exact toolchain in your Codespaces environment.

If you want I can provide:
- The Gradle wrapper files as a patch you can apply,
- Or commit everything to your repo branch if you give me push permission.
```