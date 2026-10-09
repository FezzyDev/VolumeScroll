package dev.fezzy.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.fezzy.client.VolumeMain;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
abstract class PlayerEntityRendererMixin {

    @Inject(method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("HEAD"), cancellable = true)
    private void modifyNamePlateText(CallbackInfo ci, @Local(argsOnly = true) AvatarRenderState state) {
        if (VolumeMain.shouldHideNameTag(state)) {
            ci.cancel();
            return;
        }
        VolumeMain.modifyNamePlateText(state);
    }

}
