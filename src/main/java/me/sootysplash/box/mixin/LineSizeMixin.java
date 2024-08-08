package me.sootysplash.box.mixin;

import me.sootysplash.box.Config;
import com.mojang.blaze3d.systems.RenderSystem;
import me.sootysplash.box.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderSystem.class)
public class LineSizeMixin {
	@Inject(at = @At("HEAD"), cancellable = true, method = "getShaderLineWidth", remap = false)
	private static void onCmd(CallbackInfoReturnable<Float> cir) {
		Config config = Config.getInstance();
		if (!config.enabled) {
			return;
		}
		cir.setReturnValue(Main.lineWidth);
		cir.cancel();
	}
}