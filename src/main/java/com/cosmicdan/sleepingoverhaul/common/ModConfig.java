package com.cosmicdan.sleepingoverhaul.common;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings({"CanBeFinal", "HardcodedLineSeparator"})
@UtilityClass
@Log4j2(topic = "SleepingTweaks/ModConfig")
@Config(modid = ModConstants.MODID, name = ModConstants.MODNAME, category = "")
public final class ModConfig {
	@Config.LangKey("sleepingoverhaul.config.CustomNightDetection")
	public static final CustomNightDetection CUSTOM_NIGHT_DETECTION = new CustomNightDetection();

	public static class CustomNightDetection {
		@Config.LangKey("sleepingoverhaul.config.CustomNightDetection.customStartEnabled")
		@Config.Comment("If true, manual night start time will be used instead of vanilla skylight check.")
		public boolean customStartEnabled = false;

		@Config.LangKey("sleepingoverhaul.config.CustomNightDetection.customStartValue")
		@Config.Comment("Specify the tick time that night starts for the sake of sleeping being permitted.\n" +
				"Default vanilla value of 12516 is about 18:31 (refer to 'Day-night_cycle' on Gamepedia wiki).\n" +
				"Note that this only works for setting later time - allowing daytime sleep is done via CMC config.")
		public int customStartValue = 12516;
	}

	@Config.LangKey("sleepingoverhaul.config.Restrictions")
	public static final Restrictions RESTRICTIONS = new Restrictions();

	public static class Restrictions {
		@Config.LangKey("sleepingoverhaul.config.Restrictions.safetyCheckXZ")
		@Config.Comment("Horizontal radius to scan for hostile mobs. Set to 0 to disable safety check completely.")
		public double safetyCheckXZ = 8.0D;

		@Config.LangKey("sleepingoverhaul.config.Restrictions.safetyCheckY")
		@Config.Comment("Vertical distance to scan for hostile mobs (both up and down). Set to 0 to disable safety check completely.")
		public double safetyCheckY = 5.0D;

		@Config.LangKey("sleepingoverhaul.config.Restrictions.allowSleepDuringDay")
		@Config.Comment("If true, players can sleep during the day and will NOT be automatically woken-up either.")
		public boolean allowSleepDuringDay = false;

		@Config.LangKey("sleepingoverhaul.config.Restrictions.sleepAnywhere")
		@Config.Comment("REQUIRES CMC. If true, unlocks sleeping ability in any dimension (e.g. Nether).\n" +
				"Note #1: Respawn point will not be reset - you always respawn in your overworld spawn point.\n" +
				"Note #2: Other dimensions have their own idea about night-time, e.g. Nether is always night.")
		public boolean sleepAnywhere = false;
	}

	@Config.LangKey("sleepingoverhaul.config.TimelapseMode")
	public static final TimelapseMode TIMELAPSE_MODE = new TimelapseMode();

	public static class TimelapseMode {
		@Config.LangKey("sleepingoverhaul.config.TimelapseMode.rate")
		@Config.Comment("Maximum tickrate for Timelapse.\nNote that Tickrate Changer will throttle back tickrate as necessary to avoid total CPU saturation.")
		public float rate = 1000.0f;
	}

	@Config.LangKey("sleepingoverhaul.config.ClientSettings")
	public static final ClientSettings CLIENT_SETTINGS = new ClientSettings();

	public static class ClientSettings {
		@Config.LangKey("sleepingoverhaul.config.ClientSettings.showClockInBed")
		@Config.Comment("If false, the clock will not be displayed when a player lies in bed.")
		public boolean showClockInBed = true;
	}

	@Config.LangKey("sleepingoverhaul.config.SleepVote")
	public static final SleepVote SLEEP_VOTE = new SleepVote();

	public static class SleepVote {
		@Config.LangKey("sleepingoverhaul.config.SleepVote.percent")
		@Config.Comment("Minimum percent of players required before sleep vote reminders (and actions, if any) can occur.")
		@Config.RangeInt(min = 0, max = 100)
		public int percent = 50;

		@Config.LangKey("sleepingoverhaul.config.SleepVote.actionTimeout")
		@Config.Comment("Once the percentage of sleeping players is reached, wait this many seconds before performing the configured vote action.\n" +
				"Note that if the sleeping player percentage drops below the configured minimum, the timeout action is cancelled.")
		public int actionTimeout = 60;

		@Config.LangKey("sleepingoverhaul.config.SleepVote.action")
		@Config.Comment("The action to perform once actionTimeout has elapsed (assuming the sleeping player percentage has been maintained).\n" +
				"    NONE: Take no action, just show the reminder.\n" +
				"    TIMELAPSE_ANYWAY: Continue with Timelapse - awake players have to deal with it. Default.\n" +
				"    CMC_KICK_AWAKE: Requires CMC (otherwise skip-to-day will occur after kick). Kick awake players, then continue with Timelapse.\n" +
				"    SKIP_TO_DAY: Skip to the next day (fallback to vanilla Minecraft behavior).")
		public SleepVotePassAction action = SleepVotePassAction.TIMELAPSE_ANYWAY;

		@Config.LangKey("sleepingoverhaul.config.SleepVote.reminderText")
		@Config.Comment("Text that will be periodically displayed to other players when the configured percent has been reached.\n" +
				"(COUNT) will be replaced with (e.g.) '2/3' to display currently-sleeping/total players.\n" +
				"(TIMEOUT) will be replaced with remaining time before sleep action is performed.")
		public String reminderText = "(COUNT) players are trying to sleep. Timelapse will start in (TIMEOUT) seconds.";

		@Config.LangKey("sleepingoverhaul.config.SleepVote.reminderTextDisplayTime")
		@Config.Comment("How many seconds at a time to display a reminder to players.")
		public int reminderTextDisplayTime = 4;

		@Config.LangKey("sleepingoverhaul.config.SleepVote.reminderTextRepeatTime")
		@Config.Comment("How many seconds between successive reminder displays.")
		public int reminderTextRepeatTime = 20;

		@Config.LangKey("sleepingoverhaul.config.SleepVote.allAsleepText")
		@Config.Comment("Text displayed when all players are asleep.")
		public String allAsleepText = "All players asleep: Timelapse active!";

		@Config.LangKey("sleepingoverhaul.config.SleepVote.votePassedText")
		@Config.Comment("Text displayed when vote action has passed.")
		public String votePassedText = "Vote action passed: Timelapse active!";

		@Config.LangKey("sleepingoverhaul.config.SleepVote.cancelledText")
		@Config.Comment("Text to display to players when an active or pending vote action is cancelled for whatever reason.\n" +
				"(COUNT) will be replaced with (e.g.) '2/3' to display currently-sleeping/total players.")
		public String cancelledText = "Timelapse cancelled/finished ((COUNT) players sleeping).";

		@Config.LangKey("sleepingoverhaul.config.SleepVote.kickedText")
		@Config.Comment("Text to display to players (The 'Reason' on the Disconnected screen) when they're kicked as the result of a\n" +
				"vote. Note that due to a Minecraft bug, sometimes a generic reason may still show instead.")
		public String kickText = "Automatic kick from sleep vote timeout.";

		public enum SleepVotePassAction {
			NONE,
			TIMELAPSE_ANYWAY,
			CMC_KICK_AWAKE,
			SKIP_TO_DAY
		}
	}

	@UtilityClass
	@Mod.EventBusSubscriber
	public static final class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(ModConstants.MODID)) {
				ConfigManager.sync(ModConstants.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
