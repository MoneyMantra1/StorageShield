package com.moneymantra.storageshield.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.moneymantra.storageshield.client.StorageShieldRules;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void storageshield$cancelItemFrameRender(E entity, double x, double y, double z, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        if (StorageShieldRules.shouldSkipEntity(entity)) {
            ci.cancel();
        }
    }
}
