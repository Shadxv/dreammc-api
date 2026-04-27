# Architecture

## Module dependency graph

```
         ┌─────────┐
         │ shared  │  Platform-agnostic interfaces only
         └────┬────┘
              │
         ┌────▼────┐
         │   api   │  Core business logic
         └────┬────┘
       ┌──────┴──────┐
  ┌────▼────┐   ┌────▼──────┐
  │  paper  │   │ velocity  │
  └─────────┘   └───────────┘
```

Neither `paper` nor `velocity` has a dependency on each other — coordination happens exclusively through Redis packets.

---

## Cross-server communication

All inter-server messages travel as typed `Packet` objects serialised by `PacketCodec` and published on Redis pub/sub channels.

```
Paper server                Redis               Velocity proxy
─────────────               ─────               ──────────────
RegisterServerRequestPacket ──►  dreammc:lobby:1:REGISTER_SERVER  ──► ServerGroupManager
TransferPlayerPacket        ──►  dreammc:*:*:PLAYER_TRANSFER       ──► ConnectionManager
                            ◄──  TransferPlayerRequestPacket        ◄── (routed back)
```

Channel pattern: `{group}:{name}:{id}:{PacketType}`

Wildcard subscriptions on `{group}:*:*:{PacketType}` enable network-wide broadcasts.

---

## Player profile lifecycle

```
PlayerJoinEvent
      │
      ▼
PaperProfileManager.loadProfile(uuid)
      │  cache miss?
      ▼
MongoService.find("dreammcMain", "profiles", uuid)
      │
      ▼
ProfileModel (in-memory, observable via ValueWatcher)
      │  any field write
      ▼
sendUpdateToDatabase()  ──►  MongoDB
      │  value changed
      ▼
ProfileValueChangedEvent  ──►  game plugins react
```

On server switch the profile is serialised into a `TransferPlayerProfilePacket` and forwarded to the destination server before the player is routed.

---

## Server-group tree (Velocity)

`ServerGroupManager` maintains a tree of `ServerGroupNode` objects. Each node holds:

- minimum replica count
- maximum player capacity
- default-server flag
- child groups

When a Paper server registers itself via `RegisterServerRequestPacket`, the proxy resolves its group in the tree (creating lazy placeholder nodes as needed) and registers a live Velocity `RegisteredServer`.

---

## UI component model (Paper)

```
InventoryMenu / PagedMenu / CraftingMenu / UnlockedMenu
      │  stores per-player state
      ▼
InventoryManager (UUID → open menu)
      │
CustomInventoryClickEvent  ──►  per-slot InventoryItem handler
```

Holograms and NPCs follow the same pattern: a manager class holds a map of active instances keyed by a string ID, and each instance exposes `spawn(player)` / `destroy()` methods. Client-side variants use the ProtocolLib/packet layer to avoid broadcasting to unintended players.

---

## Static registry

`Registry` (shared module) provides a single, globally accessible container for four singletons:

| Slot | Type | Populated by |
|---|---|---|
| `messageSender` | `IMessageSender` | Paper / Velocity on enable |
| `logger` | `ILogger` | Paper / Velocity on enable |
| `languageManager` | `ILanguageManager` | Paper / Velocity on enable |
| `service` | `BaseService` | Each platform's own service class |

This avoids hard coupling between modules while keeping injection simple.
