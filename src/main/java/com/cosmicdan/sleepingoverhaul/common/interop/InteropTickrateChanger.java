package com.cosmicdan.sleepingoverhaul.common.interop;

import me.guichaguri.tickratechanger.api.TickrateAPI;

@SuppressWarnings({"ClassWithoutLogger", "unused"})
public class InteropTickrateChanger implements InteropTickrateChangerInterface {
	@Override
	public void changeTickrate(final float ticksPerSecond) {
		TickrateAPI.changeServerTickrate(ticksPerSecond);
	}
}
