package com.cosmicdan.sleepingoverhaul.coremod;

import com.cosmicdan.sleepingoverhaul.coremod.transformers.*;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.ArrayList;
import java.util.Map;

@IFMLLoadingPlugin.Name(value = "SleepingOverhaulCMC")
@IFMLLoadingPlugin.MCVersion(value = "1.12.2")
@IFMLLoadingPlugin.TransformerExclusions(value = "com.cosmicdan.sleepingoverhaul.")
@IFMLLoadingPlugin.SortingIndex(value = 1001) // How early your core mod is called - Use > 1000 to work with srg names
public class CorePlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        ArrayList<String> transformers = new ArrayList<>();
        transformers.add(EntityPlayerTransformer.class.getName());
        
        /********************************************************************************
         * THIS COREMOD IS OPTIONAL. IT PROVIDES ENHANCED FUNCTIONALITY TO ITS BASE MOD.
         ********************************************************************************/

		return transformers.toArray(new String[0]);
        
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
