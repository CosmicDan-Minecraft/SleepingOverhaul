package com.cosmicdan.immersivesleeping.client;

import com.cosmicdan.cosmiclib.annotations.ForgeEntryPoint;
import com.cosmicdan.immersivesleeping.common.CommonProxy;
import lombok.extern.log4j.Log4j2;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Client-only events
 * LEGACY ONLY. Use the new RegistryEvent system where possible!
 */
@ForgeEntryPoint
@Log4j2(topic = "ImmersiveSleeping/ClientProxy")
public class ClientProxy extends CommonProxy {
	@Override
	public final void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public final void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public final void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}
