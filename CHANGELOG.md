# 0.1.0

## Requirements:

* [CosmicLib](https://github.com/CosmicDan-Minecraft/CosmicLib/releases/) 0.3.0 or later
* Tickrate Changer [1.0.14](https://minecraft.curseforge.com/projects/tickratechanger/files/2482684) or later
* Forge 1.12.2-14.23.2.2611 or later

## Changes:

* Completely removes the "skip-to-day" aspect of sleep, replacing it with a "time-lapse mode" - time speeds up while all players are lying in bed. **Not a simulation** - the game loop speed (tick-rate) is actually increased.
  * Time-lapse achieved via "Tickrate Changer" mod (required), credits to Guichaguri for this!
  * Can change the maximum tick-rate in configuration, up to 1000. Note that the real tick-rate will be throttled based on available CPU power.
* Can specify the tick time that night starts for the sake of sleeping being permitted. Note that it cannot currently be set *earlier* than the start of real night-time (a future update will allow unrestricted daytime sleeping)
* Customizable X/Z radius range check for hostile mobs to prevent sleeping
* All configurable in Forge configuration GUI

