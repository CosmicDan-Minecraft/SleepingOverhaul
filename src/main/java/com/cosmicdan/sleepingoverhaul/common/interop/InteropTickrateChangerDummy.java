package com.cosmicdan.sleepingoverhaul.common.interop;

import com.cosmicdan.cosmiclib.annotations.ForgeEntryPoint;

@SuppressWarnings("ClassWithoutLogger")
@ForgeEntryPoint
public class InteropTickrateChangerDummy implements InteropTickrateChangerInterface {
	@Override
	public void changeTickrate(final float ticksPerSecond) {}
}
