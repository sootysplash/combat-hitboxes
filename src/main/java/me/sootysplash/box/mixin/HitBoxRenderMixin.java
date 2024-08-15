package me.sootysplash.box.mixin;

import me.sootysplash.box.Config;
import me.sootysplash.box.Main;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;


@Mixin(EntityRenderDispatcher.class)
public abstract class HitBoxRenderMixin {
    @Unique private static float tickDelta;
    @Unique private static MatrixStack matrices;
    @Unique private static Entity entity;
    @Unique private static VertexConsumerProvider verticeProvider;
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", shift = At.Shift.AFTER))
    private void captureArgs(Entity entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        HitBoxRenderMixin.tickDelta = tickDelta;
        HitBoxRenderMixin.matrices = matrices;
        HitBoxRenderMixin.entity = entity;
        HitBoxRenderMixin.verticeProvider = vertexConsumers;
    }

    @Inject(method = "renderHitbox", at = @At("HEAD"), cancellable = true)
    private static void hitBoxHook(CallbackInfo ci) {
        Config config = Config.getInstance();
        ci.cancel();

        VertexConsumer vertices = verticeProvider.getBuffer(RenderLayer.getLines());

        if (!config.enabled) {
            renderBox(matrices,
                    vertices,
                    entity,
                    tickDelta,
                    Color.RED,
                    Color.BLUE,
                    Color.WHITE,
                    Color.WHITE,
                    Color.PINK,
                    false,
                    false,
                    true,
                    true);
            return;
        }

        Main.lineWidth = Main.mc.player != null && Main.mc.player.distanceTo(entity) > config.distFor2 ? config.line2 : config.line1;


        VertexConsumerProvider.Immediate imm = Main.mc.getBufferBuilders().getEntityVertexConsumers();



        renderBox(matrices,
                vertices,
                entity,
                tickDelta,
                new Color(config.eyeColor, true),
                new Color(config.lookColor, true),
                new Color(config.hitBoxColor, true),
                new Color(config.targetBoxColor, true),
                new Color(config.hurtBoxColor, true),
                config.changeTargetColor,
                config.hitBoxHurt,
                config.renderEyeHeight,
                config.renderLookDir);



        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        imm.draw();

        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        Main.lineWidth = Main.getVanillaWidth();

    }
    @Unique
    private static void renderBox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, Color eyeHeight, Color lookDir, Color main, Color ifTarget, Color ifHurt, boolean targetCol, boolean hurtCol, boolean renderEyeHeight, boolean renderLookDir) {
        Box box = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ());

        Color outer = targetCol && entity instanceof LivingEntity le && le.hurtTime != 0 && hurtCol ? ifHurt : (Main.mc.crosshairTarget instanceof EntityHitResult ehr && ehr.getEntity() == entity ? ifTarget : main);
        WorldRenderer.drawBox(matrices, vertices, box, outer.getRed() / 255f, outer.getGreen() / 255f, outer.getBlue() / 255f, outer.getAlpha() / 255f);


        renderDragon(entity, matrices, tickDelta, vertices);


        if (entity instanceof LivingEntity && renderEyeHeight) {
            float j = 0.01f;
            WorldRenderer.drawBox(matrices, vertices, box.minX, entity.getStandingEyeHeight() - j, box.minZ, box.maxX, entity.getStandingEyeHeight() + j, box.maxZ, eyeHeight.getRed() / 255f, eyeHeight.getGreen() / 255f, eyeHeight.getBlue() / 255f, eyeHeight.getAlpha() / 255f);
        }
        /*Entity entity2;
        if ((entity2 = entity.getVehicle()) != null) {
            float k = Math.min(entity2.getWidth(), entity.getWidth()) / 2.0f;
            float l = 0.0625f;
            Vec3d vec3d = entity2.getPassengerRidingPos(entity).subtract(entity.getPos());
            WorldRenderer.drawBox(matrices, vertices, vec3d.x - (double)k, vec3d.y, vec3d.z - (double)k, vec3d.x + (double)k, vec3d.y + l, vec3d.z + (double)k, 1.0f, 1.0f, 0.0f, 1.0f);
        }*/
        if (renderLookDir) {
            Vec3d vec3d2 = entity.getRotationVec(tickDelta);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            MatrixStack.Entry matrix3f = matrices.peek();

            vertices.vertex(matrix4f, 0.0f, entity.getStandingEyeHeight(), 0.0f).color(lookDir.getRed(), lookDir.getGreen(), lookDir.getBlue(), lookDir.getAlpha()).normal(matrix3f, (float) vec3d2.x, (float) vec3d2.y, (float) vec3d2.z);
            vertices.vertex(matrix4f, (float) (vec3d2.x * 2.0), (float) ((double) entity.getStandingEyeHeight() + vec3d2.y * 2.0), (float) (vec3d2.z * 2.0)).color(lookDir.getRed(), lookDir.getGreen(), lookDir.getBlue(), lookDir.getAlpha()).normal(matrix3f, (float) vec3d2.x, (float) vec3d2.y, (float) vec3d2.z);
        }
    }

    @Unique
    private static void renderDragon(Entity entity, MatrixStack matrices, float tickDelta, VertexConsumer vertices) {
        if (entity instanceof EnderDragonEntity) {
            double d = -MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
            double e = -MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
            double f = -MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());
            for (EnderDragonPart enderDragonPart : ((EnderDragonEntity)entity).getBodyParts()) {
                matrices.push();
                double g = d + MathHelper.lerp(tickDelta, enderDragonPart.lastRenderX, enderDragonPart.getX());
                double h = e + MathHelper.lerp(tickDelta, enderDragonPart.lastRenderY, enderDragonPart.getY());
                double i = f + MathHelper.lerp(tickDelta, enderDragonPart.lastRenderZ, enderDragonPart.getZ());
                matrices.translate(g, h, i);
                WorldRenderer.drawBox(matrices, vertices, enderDragonPart.getBoundingBox().offset(-enderDragonPart.getX(), -enderDragonPart.getY(), -enderDragonPart.getZ()), 0.25f, 1.0f, 0.0f, 1.0f);
                matrices.pop();
            }
        }
    }
}
