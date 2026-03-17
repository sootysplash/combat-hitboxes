package me.sootysplash.box.mixin;

import me.sootysplash.box.Config;
import net.minecraft.client.renderer.entity.layers.BeeStingerLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeeStingerLayer.class)
public class StuckStingerMixin {
    @Inject(method = "numStuck", at = @At("RETURN"), cancellable = true)
    private void hookArrowCount(AvatarRenderState playerRenderState, CallbackInfoReturnable<Integer> cir) {
        if (Config.getInstance().hideArrow) {
            cir.setReturnValue(0);
        }
    }
}
