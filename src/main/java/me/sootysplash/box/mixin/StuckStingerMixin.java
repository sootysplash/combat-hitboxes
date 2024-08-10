package me.sootysplash.box.mixin;

import me.sootysplash.box.Config;
import net.minecraft.client.render.entity.feature.StuckStingersFeatureRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StuckStingersFeatureRenderer.class)
public class StuckStingerMixin {
    @Inject(method = "getObjectCount", at = @At("RETURN"), cancellable = true)
    private void hookArrowCount(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (Config.getInstance().hideArrow) {
            cir.setReturnValue(0);
        }
    }
}
