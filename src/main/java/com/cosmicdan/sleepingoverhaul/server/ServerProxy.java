package com.cosmicdan.sleepingoverhaul.server;

import com.cosmicdan.cosmiclib.annotations.ForgeEntryPoint;
import com.cosmicdan.sleepingoverhaul.common.CommonProxy;
import lombok.extern.log4j.Log4j2;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Events that are only for dedicated servers.
 * LEGACY ONLY. Use the new RegistryEvent system where possible!
 */
@ForgeEntryPoint
@Log4j2(topic = "SleepingOverhaul/ServerProxy")
public class ServerProxy extends CommonProxy {
	@Override
	public final void preInit(final FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public final void init(final FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public final void postInit(final FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}
