package com.cosmicdan.sleepingoverhaul.common.interop;

import me.guichaguri.tickratechanger.api.TickrateAPI;

@SuppressWarnings("ClassWithoutLogger")
public class InteropTickrateChanger implements InteropTickrateChangerInterface {
	@Override
	public void changeTickrate(float ticksPerSecond) {
		TickrateAPI.changeServerTickrate(ticksPerSecond);
	}


}
