# DreamMC API

> **Note:** This repository is a public mirror of the original private repository maintained by the DreamMC Network organization. It is published for portfolio and reference purposes.

A shared Java library that powers the **DreamMC Minecraft network** — providing cross-server communication, player profile management, and rich UI components for both Paper game servers and the Velocity proxy.

---

## Overview

DreamMC API acts as the backbone of the DreamMC server network. It abstracts the infrastructure concerns — Redis messaging, MongoDB persistence, server registration — so that individual game plugins can focus purely on gameplay logic. Both the Paper plugin layer and the Velocity proxy layer consume this library, ensuring consistent behavior across the entire network.

### Key features

| Area | What it provides |
|---|---|
| **Cross-server messaging** | Redis pub/sub packet system with typed, auto-serialized packets |
| **Player profiles** | Persistent profiles (wallet, coins, gems, ranks) backed by MongoDB |
| **Rank management** | Permanent and time-limited rank assignments |
| **Server registry** | Dynamic registration/deregistration of servers via the proxy |
| **UI components** | Custom inventories, paged menus, crafting GUI, scoreboards, holograms, and NPCs |
| **Localisation** | Multi-language pack system shared across all plugins |
| **Inter-server transfers** | Player routing with automatic profile synchronisation |

---

## Module structure

```
dreammc-api/
├── shared/     # Platform-agnostic interfaces (ILogger, IMessageSender, Registry)
├── api/        # Core business logic — Redis, MongoDB, managers, models
├── paper/      # Paper (Bukkit) plugin implementation
└── velocity/   # Velocity proxy plugin implementation
```

The dependency direction is strictly one-way: `paper` / `velocity` → `api` → `shared`. Neither platform module knows about the other.

---

## Requirements

| Requirement | Version |
|---|---|
| Java | 21+ (built against 24) |
| Paper | 1.21.x |
| Velocity | 3.4.x |
| MongoDB | 6.x+ |
| Redis | 7.x+ |

---

## Building

```bash
./gradlew build
```

Build outputs (in `build/libs/`):

| Artifact | Usage |
|---|---|
| `dreammc-api.jar` | Full shadow JAR with all dependencies |
| `api.jar` | API module only (for library consumers) |
| `api-paper.jar` | Drop into Paper server `plugins/` |
| `api-velocity.jar` | Drop into Velocity proxy `plugins/` |

---

## Configuration

### Environment variables (required on every node)

| Variable | Description |
|---|---|
| `MONGODB_URI` | MongoDB connection string |
| `REDIS_URI` | Redis connection URL |

### Paper server — additional variables

| Variable | Description |
|---|---|
| `GROUP_NAME` | Logical group this server belongs to (e.g. `lobby`, `game`) |
| `SERVICE_NAME` | Human-readable name of this server instance |
| `SERVER_ID` | Unique numeric ID within the group |

### Plugin configuration file (`config.yml`)

```yaml
lang: en   # Language code for this node
```

Both the Paper plugin (`plugins/DreamMCAPI/config.yml`) and the Velocity plugin (`plugins/dreammc-api/config.yml`) use the same structure.

---

## Redis channel convention

Packets are routed over channels following this pattern:

```
{serviceGroup}:{serviceName}:{serviceId}:{PacketType}
```

Wildcard subscriptions (`*`) allow broadcasting to all servers within a group or across the entire network.

---

## Branches

| Branch | Purpose |
|---|---|
| `main` | Stable releases |
| `dev-netmanager` | **Prototype / PoC branch** — contains the API version required by the *NetManager* proof-of-concept application (see below) |

### NetManager — proof of concept

The `dev-netmanager` branch targets a prototype **network management application** currently in the proof-of-concept phase. The application provides an external interface for monitoring and controlling the DreamMC server network.

> **Application repository:** <!-- link to be added -->

---

## Technology stack

- **Build:** Gradle 8 (Kotlin DSL)
- **Database:** MongoDB 6 via the official Java Sync Driver
- **Messaging:** Redis via Lettuce (reactive client)
- **Text components:** Kyori Adventure 4
- **Serialisation:** GSON, MongoDB BSON codecs
- **Utilities:** Guava, Lombok, JetBrains Annotations

---

## About

This project was created and is maintained as part of the **DreamMC Network** infrastructure. The source code in this repository represents the public mirror of the internal development repository and may not reflect the latest internal state.
