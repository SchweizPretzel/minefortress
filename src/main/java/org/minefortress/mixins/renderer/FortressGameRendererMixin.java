package org.minefortress.mixins.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.minefortress.interfaces.FortressGameRenderer;
import org.minefortress.interfaces.FortressMinecraftClient;
import org.minefortress.selections.SelectionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class FortressGameRendererMixin implements FortressGameRenderer {

    @Shadow
    public abstract Camera getCamera();

    @Shadow
    private double getFov(Camera camera, float f, boolean b) {return 0.0;}

    @Shadow @Final private MinecraftClient client;

    @Override
    public double getFov(float f, boolean b) {
        return this.getFov(this.getCamera(), f, b);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        final SelectionManager selectionManager = ((FortressMinecraftClient) client).getSelectionManager();
        if(((FortressMinecraftClient)client).isFortressGamemode())  {
            if(client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) client.crosshairTarget;
                selectionManager.tickSelectionUpdate(blockHitResult.getBlockPos(), blockHitResult.getSide());
            }
        } else {
            if(selectionManager.isSelecting()) {
                selectionManager.resetSelection();
            }
        }
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void renderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        if(((FortressMinecraftClient)client).isFortressGamemode())
            ci.cancel();
    }

}