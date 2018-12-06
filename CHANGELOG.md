# 1.0.x

## Requirements:

- [CosmicLib](https://minecraft.curseforge.com/projects/cosmiclib/files) 0.4.0
- [Tickrate Changer](https://minecraft.curseforge.com/projects/tickratechanger/files) 1.0.14 or later
- Forge 1.12.2-14.23.5.2768 or later
- Optional CoreModCompanion [CMC] of same version for full functionality (see "Additional Files" on bottom of Curse download page for your version)

## Changes:

### 1.0.0

- **IMPORTANT:** If moving from an old beta version, delete the mod config file first!
- Update to latest Forge - fix crashes, and 'Sleep During Day' no longer requires CMC.
- Added Sleep Vote feature with multiple variations. See mod description on Curse or README.md for details.
- [CMC] Add option to unlock sleeping in any dimension (e.g. Nether). Be sure to read notes in config description about it's quirks.
- Clock display during sleep is now drawn near the middle of the screen (avoids overlapping with the chat box)
- Clock display can be enabled/disabled in config
- Improved compatibility with Comforts mod
- Misc. optimisations and improvements

# 0.1.x

## Requirements:

* [CosmicLib](https://minecraft.curseforge.com/projects/cosmiclib/files) 0.3.3
* [Tickrate Changer](https://minecraft.curseforge.com/projects/tickratechanger/files) 1.0.14 or later
* Forge 1.12.2-14.23.2.2611 or later
* Optional CoreModCompanion [CMC] of same version for full functionality (see "Additional Files" on bottom of Curse page for this download)

## Changes:

### 0.1.0

* Completely removes the "skip-to-day" aspect of sleep, replacing it with a "time-lapse mode" - time speeds up while all players are lying in bed. **Not a simulation** - the game loop speed (tick-rate) is actually increased.
  * Time-lapse achieved via "Tickrate Changer" mod (required), credits to Guichaguri for this!
  * Can change the maximum tick-rate in configuration, up to 1000. Note that the real tick-rate will be throttled based on available CPU power.
* Can specify the tick time that night starts for the sake of sleeping being permitted. Note that it cannot currently be set *earlier* than the start of real night-time (optional CMC is needed for daytime sleeping)
* Customizable X/Z radius range check for hostile mobs to prevent sleeping
* [CMC] Option to allow sleeping any time (during the day)
* All configurable in Forge configuration GUI


### 0.1.1

- Coremod/ASM and AT fixes for non-dev environment

### 0.1.2

* Add clock display during sleep
* Show periodic reminder text to non-sleeping players when majority of other players are trying to sleep