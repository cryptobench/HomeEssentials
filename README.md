# EasyHome

Let players save locations and teleport back instantly! Perfect for survival servers.

## Quick Start

1. **Install**: Download the [latest release](../../releases/latest) and drop the JAR into your server's `mods/` folder
2. **Restart** your server
3. **Set a home**: Stand where you want and type `/sethome`
4. **Teleport back**: Type `/home` from anywhere!

That's it! Players can now save and teleport to their favorite spots.

---

## Features

- **Multiple Homes** - Save spots like "base", "mine", "farm" and teleport between them
- **Easy Limits** - Choose how many homes players can have
- **Teleport Delay** - Optional countdown before teleporting (stops combat logging)
- **Simple Setup** - Configure everything in-game, no files to edit

---

## Player Commands

| Command | What it does |
|---------|--------------|
| `/sethome` | Save where you're standing as "home" |
| `/sethome base` | Save where you're standing as "base" |
| `/home` | Teleport to "home" |
| `/home base` | Teleport to "base" |
| `/homes` | See all your saved homes |
| `/delhome base` | Delete "base" |
| `/homehelp` | Show help |

---

## Server Owner Setup

### Step 1: Let players use homes

Run this in your console:
```
perm group add Adventure homes.use
```

Done! Players can now use `/sethome` and `/home`.

### Step 2: Choose how many homes players get

**Want everyone to have 3 homes?**
```
/easyhome admin set default 3
```

**Want to turn off the teleport delay?**
```
/easyhome admin set warmup 0
```

**Want instant teleport for admins only?**
```
perm group add admin homes.bypass.warmup
```

---

## Admin Commands

### See your current settings
```
/easyhome admin config
```

### Change how many homes everyone gets
```
/easyhome admin set default 5
```

### Change the maximum homes allowed
```
/easyhome admin set max 25
```

### Change the teleport delay
```
/easyhome admin set warmup 5
```
Set to `0` for instant teleport.

### Turn permission-based limits on/off
```
/easyhome admin set permissions off
```
When OFF: Everyone gets the same number of homes.

When ON: You can give different groups different limits using permissions.

---

## Giving Different Groups Different Limits

Want VIPs to have more homes than regular players? Here's how:

**First, turn on permission mode:**
```
/easyhome admin set permissions on
```

**Then set up your groups:**
```
perm group add Adventure homes.use
perm group add Adventure homes.limit.1

perm group add vip homes.use
perm group add vip homes.limit.5

perm group add admin homes.use
perm group add admin homes.limit.unlimited
perm group add admin homes.admin
```

Now regular players get 1 home, VIPs get 5, and admins get unlimited!

---

## All Permissions

| Permission | What it does |
|------------|--------------|
| `homes.use` | Can use home commands |
| `homes.admin` | Can use `/easyhome admin` |
| `homes.limit.1` | Can have 1 home |
| `homes.limit.3` | Can have 3 homes |
| `homes.limit.5` | Can have 5 homes |
| `homes.limit.10` | Can have 10 homes |
| `homes.limit.25` | Can have 25 homes |
| `homes.limit.50` | Can have 50 homes |
| `homes.limit.unlimited` | Can have max homes |
| `homes.bypass.warmup` | Teleports instantly (no delay) |

---

## Common Questions

### "How do I give everyone 3 homes without messing with permissions?"
```
/easyhome admin set default 3
/easyhome admin set permissions off
```

### "Players say teleporting takes too long"
```
/easyhome admin set warmup 0
```

### "A player says they can't set more homes"
Either increase the default limit:
```
/easyhome admin set default 5
```
Or give them a higher permission:
```
perm user add PlayerName homes.limit.10
```

---

## License

MIT - Use it however you like!
