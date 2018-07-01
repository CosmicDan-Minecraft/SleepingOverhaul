# SleepingOverhaul

Replaces the vanilla "skip-to-day" function of sleep with a cool and immersive time-lapse sleep (speeds up time), among other configurable sleep enhancements.

The time-lapse is *not* a simulation - the actual tick-rate of the world is sped-up. This is achieved by using the [Tickrate Changer](https://minecraft.curseforge.com/projects/tickratechanger) mod by [Guichaguri](https://minecraft.curseforge.com/members/Guichaguri). See the individual release page for the required version and download link.

This mod also has an optional but recommended CMC (CoreModCompanion) mod that is required for some functionality - these options and features are usually marked with "CMC" (or "cmc").

Full feature list:

- Set a custom "night-time start" for the sake of sleeping (I.e. force players to stay up later than usual);

- Customize the horizontal/vertical safety check distance (I.e. range for "You cannot sleep now" because of nearby mobs);

- Shows a clock on the screen while sleeping/laying in bed;

- If half or more of online players are laying in a bed, a polite reminder will be periodically sent to other players to find a bed themselves or log-off (exact message is customizable in config);

- If a new player logs-in or any player leaves the bed while time-lapse sleep is occurring, time-lapse will immediately end;

- [CMC] The ability to sleep at any time of the day and keep sleeping during the day. This obviously overrides any custom "night-time start" setting. Also note that player(s) *must* manually wake themselves up, otherwise they'll just keep sleeping forever;

- [Planned] The option to sleep in any dimension (e.g. Nether)

- [Planned] An alarm clock function that would be particularly useful for the sleep-any-time CMC functionality.

  

This mod is ALPHA. It has not undergone extensive play testing yet, but it shouldn't cause any permanent damage and can be safely removed from existing worlds if it causes issues.

# Compatibility

## Incompatible mod list

- **Morpheus**. Sleep-vote doesn't make sense in this mod. All players are required to sleep for Timelapse to occur.

## Technical details

By necessity, this mod completely overrides the vanilla sleep event. It tries to do so in as compatible a manner as possible by running last, giving other mods a chance to do their own hooks when players sleep (TODO: Make the priority compatible, so you have a choice to override or respect other mods that hook the sleep event). So there **will** be some incompatibilities with other mods:

- SleepingOverhaul replicates vanilla sleep behaviour by hooking the PlayerSleepInBedEvent, always returning a value - this means the vanilla trySleep method will never run. Coremods which modify this code will not function correctly. *If there are any Coremods out there that do this, they're abusing core modding - that makes Lex angry.*
- Other mods which provide conditional restrictions/overrides on sleeping may conflict, especially if they cancel or change the sleep result before SleepingOverhaul runs (SleepingOverhaul is running at lowest priority). An example of this might be a mod which adds it's own "It's too dangerous to sleep now" or "you can't sleep here" decisions. This is by design - other mods that block or change sleep conditions should still be allowed to do so.
- Other mods which monitor the "allPlayersSleeping" flag in WorldServer will not function correctly (failing to set this flag is how SleepingOverhaul avoids the 'skip-to-day' part in the first place). I will not fix this - these mods should be subscribed to the PlayerWakeUpEvent instead (which is still fired with this mod).
- Other mods with tick event subscriptions that utilize a player's sleepTimer may behave unpredictably. I can't imagine why a mod would want to do this, though.