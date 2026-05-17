package com.moneymantra.storageshield.client;

import com.moneymantra.storageshield.StorageShieldConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class StorageShieldOcclusion {
    private StorageShieldOcclusion() {}

    public static boolean isBlocked(BlockPos targetPos) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return false;
        }

        int maxChecks = StorageShieldConfig.GENERAL.maxWallChecksPerTick.get();
        if (maxChecks <= 0 || StorageShieldStats.wallChecks() >= maxChecks) {
            StorageShieldStats.wallCheckLimitHit();
            return false;
        }

        Entity cameraEntity = minecraft.getCameraEntity();
        Vec3 cameraPos = minecraft.gameRenderer.getMainCamera().getPosition();
        Vec3 target = Vec3.atCenterOf(targetPos);

        StorageShieldStats.wallCheck();
        BlockHitResult result = minecraft.level.clip(new ClipContext(
                cameraPos,
                target,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                cameraEntity
        ));

        if (result.getType() != HitResult.Type.BLOCK) {
            return false;
        }

        BlockPos hitPos = result.getBlockPos();
        return !hitPos.equals(targetPos);
    }
}
