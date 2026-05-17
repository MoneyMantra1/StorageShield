package com.moneymantra.storageshield.client;

import com.moneymantra.storageshield.StorageShieldConfig;
import com.moneymantra.storageshield.StorageShieldProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.phys.Vec3;

public final class StorageShieldRules {
    private StorageShieldRules() {}

    public static boolean shouldSkipBlockEntity(BlockEntity blockEntity) {
        if (!StorageShieldClientState.isActive()) {
            return false;
        }

        StorageShieldConfig.TargetRule rule = ruleForBlockEntity(blockEntity);
        if (rule == null || !rule.enabled.get()) {
            return false;
        }

        boolean skip = shouldSkipPosition(rule, blockEntity.getBlockPos());
        if (skip) {
            StorageShieldStats.blockEntitySkipped();
        } else {
            StorageShieldStats.blockEntityRendered();
        }
        return skip;
    }

    public static boolean shouldSkipEntity(Entity entity) {
        if (!StorageShieldClientState.isActive()) {
            return false;
        }

        if (!(entity instanceof ItemFrame)) {
            return false;
        }

        StorageShieldConfig.TargetRule rule = StorageShieldConfig.ITEM_FRAMES;
        if (!rule.enabled.get()) {
            return false;
        }

        boolean skip = shouldSkipPosition(rule, entity.blockPosition());
        if (skip) {
            StorageShieldStats.itemFrameSkipped();
        } else {
            StorageShieldStats.itemFrameRendered();
        }
        return skip;
    }

    private static StorageShieldConfig.TargetRule ruleForBlockEntity(BlockEntity blockEntity) {
        if (blockEntity instanceof ChestBlockEntity) {
            Block block = blockEntity.getBlockState().getBlock();
            return block == Blocks.TRAPPED_CHEST ? StorageShieldConfig.TRAPPED_CHESTS : StorageShieldConfig.CHESTS;
        }
        if (blockEntity instanceof EnderChestBlockEntity) {
            return StorageShieldConfig.ENDER_CHESTS;
        }
        if (blockEntity instanceof ShulkerBoxBlockEntity) {
            return StorageShieldConfig.SHULKER_BOXES;
        }
        if (blockEntity instanceof BarrelBlockEntity) {
            return StorageShieldConfig.BARRELS;
        }
        if (blockEntity instanceof SignBlockEntity) {
            return isHangingSign(blockEntity) ? StorageShieldConfig.HANGING_SIGNS : StorageShieldConfig.SIGNS;
        }
        if (blockEntity instanceof HopperBlockEntity) {
            return StorageShieldConfig.HOPPERS;
        }
        return null;
    }

    private static boolean isHangingSign(BlockEntity blockEntity) {
        String blockPath = BuiltInRegistries.BLOCK.getKey(blockEntity.getBlockState().getBlock()).getPath();
        return blockPath.contains("hanging_sign");
    }

    private static boolean shouldSkipPosition(StorageShieldConfig.TargetRule rule, BlockPos pos) {
        if (rule.hideAll.get()) {
            return true;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return false;
        }

        Vec3 cameraPos = minecraft.gameRenderer.getMainCamera().getPosition();
        Vec3 target = Vec3.atCenterOf(pos);
        double distanceSqr = cameraPos.distanceToSqr(target);

        int normalDistance = Math.max(0, rule.normalRenderDistance.get());
        int hideDistance = Math.max(normalDistance, rule.hideBeyondDistance.get());

        StorageShieldProfile profile = StorageShieldClientState.profile();
        if (profile == StorageShieldProfile.EXTREME) {
            hideDistance = Math.min(hideDistance, StorageShieldConfig.GENERAL.extremeRenderDistance.get());
            normalDistance = Math.min(normalDistance, Math.max(1, hideDistance / 2));
        }

        if (distanceSqr > (double) hideDistance * hideDistance) {
            return true;
        }

        if (rule.hideBehindWalls.get() && distanceSqr > (double) normalDistance * normalDistance) {
            return StorageShieldOcclusion.isBlocked(pos);
        }

        return false;
    }
}
