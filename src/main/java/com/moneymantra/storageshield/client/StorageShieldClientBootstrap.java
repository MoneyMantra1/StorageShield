package com.moneymantra.storageshield.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.CommandDispatcher;
import com.moneymantra.storageshield.StorageShieldConfig;
import com.moneymantra.storageshield.StorageShieldProfile;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

public final class StorageShieldClientBootstrap {
    private static final String CATEGORY = "key.categories.storageshield";

    private static final KeyMapping CYCLE_PROFILE = new KeyMapping(
            "key.storageshield.cycle_profile",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            CATEGORY
    );

    private StorageShieldClientBootstrap() {}

    public static void init(IEventBus modBus) {
        modBus.addListener(StorageShieldClientBootstrap::registerKeyMappings);
        NeoForge.EVENT_BUS.addListener(StorageShieldClientBootstrap::onClientTickPre);
        NeoForge.EVENT_BUS.addListener(StorageShieldClientBootstrap::onClientTickPost);
        NeoForge.EVENT_BUS.addListener(StorageShieldClientBootstrap::onRegisterClientCommands);
        NeoForge.EVENT_BUS.addListener(StorageShieldClientBootstrap::onRenderGui);
    }

    private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(CYCLE_PROFILE);
    }

    private static void onClientTickPre(ClientTickEvent.Pre event) {
        StorageShieldStats.beginTick();
    }

    private static void onClientTickPost(ClientTickEvent.Post event) {
        StorageShieldClientState.onClientTick();
        while (CYCLE_PROFILE.consumeClick()) {
            StorageShieldClientState.cycleProfile();
        }
    }

    private static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("storageshield")
                .then(Commands.literal("status")
                        .executes(ctx -> {
                            StorageShieldClientState.message(statusText());
                            return 1;
                        }))
                .then(Commands.literal("stats")
                        .executes(ctx -> {
                            StorageShieldClientState.message(StorageShieldStats.summary());
                            return 1;
                        }))
                .then(Commands.literal("toggle")
                        .executes(ctx -> {
                            if (StorageShieldClientState.profile() == StorageShieldProfile.OFF) {
                                StorageShieldClientState.setProfile(StorageShieldProfile.PERFORMANCE, true);
                            } else {
                                StorageShieldClientState.setProfile(StorageShieldProfile.OFF, true);
                            }
                            return 1;
                        }))
                .then(Commands.literal("profile")
                        .then(Commands.literal("normal").executes(ctx -> setProfile(StorageShieldProfile.NORMAL)))
                        .then(Commands.literal("performance").executes(ctx -> setProfile(StorageShieldProfile.PERFORMANCE)))
                        .then(Commands.literal("extreme").executes(ctx -> setProfile(StorageShieldProfile.EXTREME)))
                        .then(Commands.literal("off").executes(ctx -> setProfile(StorageShieldProfile.OFF))))
        );
    }

    private static int setProfile(StorageShieldProfile profile) {
        StorageShieldClientState.setProfile(profile, true);
        return 1;
    }

    private static String statusText() {
        return "enabled=" + StorageShieldConfig.GENERAL.enabled.get()
                + ", profile=" + StorageShieldClientState.profile().displayName()
                + ", overlay=" + StorageShieldConfig.GENERAL.debugOverlay.get();
    }

    private static void onRenderGui(RenderGuiEvent.Post event) {
        if (!StorageShieldConfig.GENERAL.debugOverlay.get()) {
            return;
        }
        if (Minecraft.getInstance().options.hideGui) {
            return;
        }
        if (StorageShieldClientState.profile() == StorageShieldProfile.OFF) {
            return;
        }

        GuiGraphics graphics = event.getGuiGraphics();
        Font font = Minecraft.getInstance().font;
        int x = 6;
        int y = 6;
        graphics.drawString(font, Component.literal("StorageShield: " + StorageShieldClientState.profile().displayName()), x, y, 0xAEEFAE, true);
        graphics.drawString(font, Component.literal("BE skip/render: " + StorageShieldStats.skippedBlockEntities() + "/" + StorageShieldStats.renderedBlockEntities()), x, y + 10, 0xD8DEE9, true);
        graphics.drawString(font, Component.literal("Frames skip/render: " + StorageShieldStats.skippedItemFrames() + "/" + StorageShieldStats.renderedItemFrames()), x, y + 20, 0xD8DEE9, true);
        graphics.drawString(font, Component.literal("Wall checks: " + StorageShieldStats.wallChecks() + "  limit hits: " + StorageShieldStats.wallCheckLimitHits()), x, y + 30, 0xD8DEE9, true);
    }
}
