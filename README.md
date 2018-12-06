# SleepingOverhaul

Replaces the vanilla "skip-to-day" function of sleep with a cool and immersive time-lapse sleep (speeds up time), among other configurable sleep enhancements.

The time-lapse is *not* a simulation - the actual tick-rate of the world is sped-up. This is achieved by using the [Tickrate Changer](https://minecraft.curseforge.com/projects/tickratechanger) mod by [Guichaguri](https://minecraft.curseforge.com/members/Guichaguri) (see the download page for the required version and download link).

SleepingOverhaul also has an optional CMC (CoreModCompanion) mod which is required for some functionality. These options and features are marked with "[CMC]" where appropriate.

This mod - like all my mods do/will - requires CosmicLib. If in doubt about which version of CosmicLib is required, check the change log on the Download page.

Full feature list:

- When all players are in a bed, Time-lapse occurs instead of skipping to day - the world speeds up to a maximum of 1000 ticks/second;

- Set a custom "night-time start" for the sake of sleeping (I.e. force players to stay up later than usual);

- The ability to sleep at any time of the day and keep sleeping during the day. This obviously overrides any custom "night-time start" setting. Also note that player(s) *must* manually wake themselves up, otherwise they'll just keep sleeping forever (an alarm clock function may come in future if there's any demand for it);

- [CMC] Unlock the ability to sleep in any dimension (e.g. Nether). Compatibility is not guaranteed with mod-added dimensions.

- Customize the horizontal/vertical safety check distance (I.e. range for "You cannot sleep now" because of nearby mobs);

- Shows a clock on the screen while sleeping/laying in bed (configurable);

- A Sleep Vote system where, when a minimum ratio of players are in a bed, a polite reminder will be periodically sent to players and a countdown to one of several configurable actions will begin for the players who are not in a bed:

  1. No action (just show a reminder every x seconds);
  2. Enable Timelapse anyway (players who remain awake will have to deal with the server tickrate being sped-up);
  3. [CMC] Kick all awake players, then continue with Timelapse (without the CMC, it will incorrectly cause a skip-to-day after kicking the awake players);
  4. Revert to vanilla behaviour (skip-to-day)
  ... timeouts and messages displayed to players are fully customizable in the configuration.

This mod is now considered STABLE and FEATURE COMPLETE but may still have bugs. It shouldn't cause any permanent damage and can be safely removed from existing worlds if it causes issues. Please be sure to report any issues you find.

# Known Issues

- The clock display in the Nether will only show "??:??". I may introduce an option in future to instead show the Overworld timestamp.
- The increased tick-rate under Timelapse may be slow when running on a dedicated server. At least it is for me, when running a server + 3 clients on my dev machine. Not sure how to improve this, nor if it even can be improved. I hope to look into it some day.
- Crop growth and other things based on "random block ticks" do not scale their tick speed as you might expect. This cannot be easily "fixed" as it isn't really a bug, but I will see what I can do in a future release (maybe I can automate the randomTickSpeed gamerule change).

# Compatibility

- **Morpheus**. Sleep-vote doesn't make sense in this mod, but we have our own system for that now anyway.
- **StellarAPI**. Need to manually disable all of StellarAPI's custom wake settings (at *StellarAPI\MainConfig.cfg* under *wake*) since it causes skip to day.

## Technical details

By necessity, this mod completely overrides the vanilla sleep event. It tries to do so in as compatible a manner as possible by running last, giving other mods a chance to do their own hooks when players sleep. There **will** be incompatibilities with other mods that do specific things related to sleep:

- SleepingOverhaul replicates vanilla sleep behaviour by hooking the PlayerSleepInBedEvent, always returning a value - this means the vanilla trySleep method will never run. Coremods which modify this code will not function correctly. *If there are any Coremods out there that do this, they're abusing core modding - that makes Lex angry.*
- Other mods which provide conditional restrictions/overrides on sleeping may conflict, especially if they cancel or change the sleep result before SleepingOverhaul runs (SleepingOverhaul is running at lowest priority). An example of this might be a mod which adds it's own "It's too dangerous to sleep now" or "you can't sleep here" decisions. This is by design - other mods that block or change sleep conditions should still be allowed to do so.
- Other mods which monitor the "allPlayersSleeping" flag in WorldServer will not function correctly (failing to set this flag is how SleepingOverhaul avoids the 'skip-to-day' part in the first place). I will not fix this - these mods should be subscribed to the PlayerWakeUpEvent instead (which is still fired with this mod).
- Other mods with tick event subscriptions that utilize a player's sleepTimer may behave unpredictably. I can't imagine why a mod would want to do this, though.