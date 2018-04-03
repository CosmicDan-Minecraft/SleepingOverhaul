package com.cosmicdan.sleepingoverhaul.common.interop;

import com.cosmicdan.cosmiclib.annotations.ForgeDynamic;

@SuppressWarnings("ClassWithoutLogger")
@ForgeDynamic
public class InteropTickrateChangerDummy implements InteropTickrateChangerInterface {
	@Override
	public void changeTickrate(float ticksPerSecond) {}
}
