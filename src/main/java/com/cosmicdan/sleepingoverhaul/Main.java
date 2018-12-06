package com.cosmicdan.sleepingoverhaul;

import com.cosmicdan.cosmiclib.annotations.ForgeEntryPoint;
import com.cosmicdan.sleepingoverhaul.common.CommonProxy;
import com.cosmicdan.sleepingoverhaul.common.ModConstants;
import lombok.extern.log4j.Log4j2;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Log4j2(topic =  "SleepingOverhaul/Main")
@Mod(name = ModConstants.MODNAME, modid = ModConstants.MODID, version = ModConstants.VERSION,
		dependencies =
				"required-after:cosmiclib;" +
				"required-after:tickratechanger@[1.0.14,)",
		certificateFingerprint = "@jar_fingerprint@")
public class Main {
	@ForgeEntryPoint
	@Mod.Instance(ModConstants.MODID)
	public static Main INSTANCE = null;

	@SuppressWarnings({"CanBeFinal", "WeakerAccess"})
	@ForgeEntryPoint
	@SidedProxy(clientSide="com.cosmicdan.sleepingoverhaul.client.ClientProxy", serverSide="com.cosmicdan.sleepingoverhaul.server.ServerProxy")
	public static CommonProxy PROXY = null;

	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		PROXY.preInit(event);
	}

	@Mod.EventHandler
	public void init(final FMLInitializationEvent event) {
		PROXY.init(event);
	}

	@Mod.EventHandler
	public void postInit(final FMLPostInitializationEvent event) {
		PROXY.postInit(event);
	}

	@Mod.EventHandler
	public void onFingerprintViolation(final FMLFingerprintViolationEvent event) {
		log.warn("Invalid fingerprint detected! The file {} may have been tampered with. This mod will NOT be supported by CosmicDan.", event.getSource().getName());
	}
}
