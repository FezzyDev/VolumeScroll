package dev.fezzy.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.fezzy.client.VolumeMain;
import net.minecraft.client.MouseHandler;
import org.joml.Vector2i;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
abstract class MouseMixin {

    @Inject(
            method = "onScroll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;isSpectator()Z"
            ),
            cancellable = true
    )
    private void handleMouseScroll(CallbackInfo ci, @Local Vector2i wheelXY) {
        if (VolumeMain.handleScroll(wheelXY)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "onButton",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/MouseHandler;isMiddlePressed:Z",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void handleMiddleClick(CallbackInfo ci) {
        VolumeMain.handleMiddleClick();
    }

}
