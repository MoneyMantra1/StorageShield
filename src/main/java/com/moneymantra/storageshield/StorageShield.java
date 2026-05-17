package com.moneymantra.storageshield;

import com.moneymantra.storageshield.client.StorageShieldClientBootstrap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(StorageShield.MOD_ID)
public final class StorageShield {
    public static final String MOD_ID = "storageshield";

    public StorageShield(IEventBus modBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, StorageShieldConfig.SPEC);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            StorageShieldClientBootstrap.init(modBus);
        }
    }
}
