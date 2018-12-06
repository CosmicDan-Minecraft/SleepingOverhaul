package com.cosmicdan.sleepingoverhaul.coremod;

import com.cosmicdan.sleepingoverhaul.coremod.transformers.*;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
@IFMLLoadingPlugin.Name("SleepingOverhaulCMC")
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions("com.cosmicdan.")
@IFMLLoadingPlugin.SortingIndex(1001) // How early your core mod is called - Use > 1000 to work with srg names
public class CorePlugin implements IFMLLoadingPlugin {
	@Override
	public String[] getASMTransformerClass() {
		final ArrayList<String> transformers = new ArrayList<>(5);
		transformers.add(CanSleepAtTransformer.class.getName());
		transformers.add(SleepingFlagTransformer.class.getName());
		
		/* *******************************************************************************
		 * THIS COREMOD IS OPTIONAL. IT PROVIDES ENHANCED FUNCTIONALITY TO ITS BASE MOD.
		 ********************************************************************************/

		return transformers.toArray(new String[0]);
		
	}

	@Nullable
	@Override
	public String getModContainerClass() {
		return null;
	}

	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(final Map<String, Object> data) {
	}

	@Nullable
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
