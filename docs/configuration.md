# Configuration reference

## Environment variables

Both Paper and Velocity nodes require the following variables to be present at startup. The plugins will fail to initialise if either is missing.

| Variable | Format | Example |
|---|---|---|
| `MONGODB_URI` | Standard MongoDB connection string | `mongodb://user:pass@mongo-host:27017` |
| `REDIS_URI` | Redis URL | `redis://user:pass@redis-host:6379` |

### Paper — additional variables

| Variable | Description | Example |
|---|---|---|
| `GROUP_NAME` | Logical group this server belongs to | `lobby` |
| `SERVICE_NAME` | Human-readable name of this server | `lobby-1` |
| `SERVER_ID` | Unique ID within the group | `1` |

These three values are combined to form the Redis channel prefix used by this server instance (`GROUP_NAME:SERVICE_NAME:SERVER_ID`).

---

## config.yml

Both plugins read a `config.yml` from their plugin data folder on startup. Currently the only supported key is the language selector.

**Paper** — `plugins/DreamMCAPI/config.yml`  
**Velocity** — `plugins/dreammc-api/config.yml`

```yaml
# Language code for this node.
# A matching language pack must be registered by a plugin on the network.
lang: en
```

The language pack files themselves are supplied by individual game plugins — the API only provides the loading and lookup mechanism.

---

## MongoDB data layout

| Database | Collection | Contents |
|---|---|---|
| `dreammcMain` | `profiles` | Player profile documents (UUID, ranks, wallet, coins, gems) |

Document schema for a player profile:

```json
{
  "_id": "<uuid-string>",
  "ranks": ["<rank-id>", ...],
  "timeRanks": [{ "rankId": "...", "expiresAt": 0 }],
  "wallet": 0.0,
  "coins": 0,
  "gems": 0
}
```

---

## Logging

Both platforms delegate logging through the `ILogger` interface registered in `Registry`. The underlying implementation is:

- **Paper** — Bukkit `Plugin#getLogger()`
- **Velocity** — SLF4J (provided by Velocity itself)

No additional logging configuration is required.
