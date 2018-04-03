package com.cosmicdan.sleepingoverhaul.common.interop;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import net.minecraftforge.fml.common.Loader;

@SuppressWarnings("PublicField")
@UtilityClass
@Log4j2(topic = "SleepingTweaks/ModAccessors")
public class ModAccessors {
	public static boolean TICKRATE_CHANGER_LOADED = false;
	public static InteropTickrateChangerInterface TICKRATE_CHANGER = new InteropTickrateChangerDummy();

	@SuppressWarnings("OverlyBroadCatchBlock")
	public static void init() {
		try {
			if (Loader.isModLoaded("tickratechanger")) {
				log.info("Tickrate Changer detected OK, time-lapse sleep available!");
				TICKRATE_CHANGER = Class.forName("com.cosmicdan.sleepingoverhaul.common.interop.InteropTickrateChanger").asSubclass(InteropTickrateChangerInterface.class).getConstructor().newInstance();
				TICKRATE_CHANGER_LOADED = true;
			} else {
				log.info("Tickrate Changer NOT detected, time-lapse sleep not available.");
			}
		} catch (Exception e) {
			// shouldn't happen
			throw new RuntimeException(e);
		}
	}
}
