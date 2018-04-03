# SleepingOverhaul

Replaces the vanilla "skip-to-day" function of sleep with an immersive time-lapse sleep (speeds up time), among other configurable sleep enhancements.

The time-lapse is *not* a simulation - the actual tick-rate of the world is changed. This is achieved by using the [Tickrate Changer](https://minecraft.curseforge.com/projects/tickratechanger) mod by [Guichaguri](https://minecraft.curseforge.com/members/Guichaguri). See the individual release page for the required version and download link.

This mod additionally has an optional CMC (CoreModCompanion) that allows advanced functionality. Currently this includes:

- The optional ability to sleep at any time of the day, and similarly 
- [Future] The optional ability to sleep in any dimension (e.g. Nether)
- ?

This mod is ALPHA. There are some planned features that are still missing which I consider fairly important - mostly a better multiplayer experience (a vote/kick system) and an alarm clock.

# Compatibility

## Mod list

- ?

## Technical details

By necessity, this mod completely overrides the vanilla sleep event. It tries to do so in as compatible a manner as possible by running last, giving other mods a chance to do their own hooks when players sleep (TODO: Make the priority compatible, so you have a choice to override or respect other mods that hook the sleep event). So there **will** be some incompatibilities with other mods:

- SleepingOverhaul replicates vanilla sleep behaviour by hooking the PlayerSleepInBedEvent, always returning a value - this means the vanilla trySleep method will never run. Coremods which modify this code will not function correctly. *If there are any Coremods out there that do this, they're abusing core modding - that makes Lex angry.*
- Other mods which provide conditional restrictions/overrides on sleeping may conflict, especially if they cancel or change the sleep result before SleepingOverhaul runs (SleepingOverhaul is running at lowest priority). An example of this might be a mod which adds it's own "It's too dangerous to sleep now" or "you can't sleep here" decisions. This is by design - other mods that block or change sleep conditions should still be allowed to do so.
- Other mods which monitor the "allPlayersSleeping" flag in WorldServer will not function correctly (failing to set this flag is how SleepingOverhaul avoids the 'skip-to-day' part in the first place). I will not fix this - these mods should be subscribed to the PlayerWakeUpEvent instead (which is still fired with this mod).
- Other mods with tick event subscriptions that utilize a player's sleepTimer may behave unpredictably. I can't imagine why a mod would want to do this, though.