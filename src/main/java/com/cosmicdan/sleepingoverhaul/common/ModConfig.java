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
	}

	@Config.LangKey("sleepingoverhaul.config.TimelapseMode")
	public static final TimelapseMode TIMELAPSE_MODE = new TimelapseMode();

	public static class TimelapseMode {
		@Config.LangKey("sleepingoverhaul.config.TimelapseMode.rate")
		@Config.Comment("Maximum tickrate for Timelapse.\nNote that Tickrate Changer will throttle back tickrate as necessary to avoid total CPU saturation.")
		public float rate = 1000.0f;
	}

	@Config.LangKey("sleepingoverhaul.config.CoreModCompanion")
	public static final CoreModCompanion CMC = new CoreModCompanion();

	public static class CoreModCompanion {
		@Config.LangKey("sleepingoverhaul.config.CoreModCompanion.allowSleepDuringDay")
		@Config.Comment("If true, players will NOT be automatically woken-up during the day and can sleep at any time.")
		public boolean allowSleepDuringDay = false;

		// TODO: Disabled for now. Requires coremod to BlockBed to allow use in non-respawnable dimensions and HELL (and not explode)
		//@Config.LangKey("sleepingoverhaul.config.CoreModCompanion.sleepAnywhere")
		//@Config.Comment("If true, sleeping can be performed in any dimension (e.g. Nether)")
		//public boolean sleepAnywhere = true;
	}

	@UtilityClass
	@Mod.EventBusSubscriber
	public static final class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(ModConstants.MODID)) {
				ConfigManager.sync(ModConstants.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
