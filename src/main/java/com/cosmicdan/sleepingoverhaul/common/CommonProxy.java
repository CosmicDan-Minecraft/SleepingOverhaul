package com.cosmicdan.sleepingoverhaul.common;

import com.cosmicdan.cosmiclib.annotations.ForgeEntryPoint;
import com.cosmicdan.sleepingoverhaul.common.eventhandlers.PlayerSleepHandler;
import com.cosmicdan.sleepingoverhaul.common.eventhandlers.SleepingVoteHandler;
import com.cosmicdan.sleepingoverhaul.common.interop.ModAccessors;
import lombok.extern.log4j.Log4j2;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Events for both physical servers and clients. Most things will belong here.
 * LEGACY ONLY. Use the new RegistryEvent system where possible!
 */
@ForgeEntryPoint
@Log4j2(topic = "SleepingOverhaul/CommonProxy")
public class CommonProxy {
	/**
	 * Register blocks/items to GameRegistry, (tile) entities ans assign oredict names
	 */
	public void preInit(final FMLPreInitializationEvent event) {}

	/**
	 * Register worldgen, recipes, event handlers and send IMC messages
	 */
	public void init(final FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new PlayerSleepHandler());
		MinecraftForge.EVENT_BUS.register(new SleepingVoteHandler());
	}

	/**
	 * Other stuff e.g. mod integrations, housework
	 */
	public void postInit(final FMLPostInitializationEvent event) {
		ModAccessors.init();
	}
}
