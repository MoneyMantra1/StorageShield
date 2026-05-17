package com.moneymantra.storageshield.client;

import com.moneymantra.storageshield.StorageShieldConfig;
import com.moneymantra.storageshield.StorageShieldProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class StorageShieldClientState {
    private static StorageShieldProfile runtimeProfile = StorageShieldProfile.PERFORMANCE;
    private static boolean initializedFromConfig = false;
    private static boolean joinedWorldMessageShown = false;

    private StorageShieldClientState() {}

    public static void initFromConfigIfNeeded() {
        if (!initializedFromConfig) {
            runtimeProfile = StorageShieldProfile.parse(StorageShieldConfig.GENERAL.defaultRuntimeProfile.get());
            initializedFromConfig = true;
        }
    }

    public static boolean isActive() {
        initFromConfigIfNeeded();
        return StorageShieldConfig.GENERAL.enabled.get() && runtimeProfile != StorageShieldProfile.OFF;
    }

    public static StorageShieldProfile profile() {
        initFromConfigIfNeeded();
        return runtimeProfile;
    }

    public static void setProfile(StorageShieldProfile profile, boolean sendMessage) {
        runtimeProfile = profile == null ? StorageShieldProfile.PERFORMANCE : profile;
        if (sendMessage) {
            message("StorageShield profile: " + runtimeProfile.displayName());
        }
    }

    public static void cycleProfile() {
        setProfile(profile().next(), true);
    }

    public static void onClientTick() {
        initFromConfigIfNeeded();

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            joinedWorldMessageShown = false;
            return;
        }

        if (!joinedWorldMessageShown && StorageShieldConfig.GENERAL.showStartupMessage.get()) {
            joinedWorldMessageShown = true;
            message("StorageShield loaded. Current profile: " + runtimeProfile.displayName() + ". Press O to cycle.");
        }
    }

    public static void message(String text) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            minecraft.player.sendSystemMessage(Component.literal("§8[§aStorageShield§8] §f" + text));
        }
    }
}
