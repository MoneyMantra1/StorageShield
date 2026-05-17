# StorageShield

Client-side NeoForge 1.21.1 storage-room renderer shield for extreme storage rooms.

StorageShield targets storage-room FPS killers by cancelling rendering for supported storage block entities and item frames when they are too far away, hidden behind walls, or globally disabled for testing.

## Supported targets

- Chests
- Trapped chests
- Ender chests
- Shulker boxes
- Barrels
- Item frames / glow item frames
- Signs
- Hanging signs
- Hoppers, if another mod gives them a block-entity renderer

Vanilla hoppers are normal static block models and do not have an expensive vanilla BlockEntityRenderer to cancel. The hopper config exists as a compatibility hook.

## Controls

Default keybind:

- `O` cycles runtime profile: NORMAL -> PERFORMANCE -> EXTREME -> OFF

Client command:

- `/storageshield status`
- `/storageshield toggle`
- `/storageshield profile normal`
- `/storageshield profile performance`
- `/storageshield profile extreme`
- `/storageshield profile off`
- `/storageshield stats`

## Config

The generated client config is:

`config/storageshield-client.toml`

The default profile is performance-oriented for huge storage rooms.

## Build

This repo is GitHub Actions ready. Push it to a fresh repository and run the workflow, or run locally with Java 21:

```bash
gradle build
```

The jar will be built at:

`build/libs/StorageShield-1.0.0-NeoForge-1.21.1.jar`
