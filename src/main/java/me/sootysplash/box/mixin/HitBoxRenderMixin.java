package me.sootysplash.box.mixin;

import me.sootysplash.box.Config;
import me.sootysplash.box.Main;
import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;


@Mixin(EntityHitboxDebugRenderer.class)
public abstract class HitBoxRenderMixin {

    @Inject(method = "showHitboxes", at = @At("HEAD"), cancellable = true)
    private void onDrawHitbox(Entity entity, float tickProgress, boolean inLocalServer, CallbackInfo ci) {
        if (inLocalServer) {// they want to debug, let them
            return;
        }
        Config config = Config.getInstance();
        if (!config.enabled) {
            return;
        }
        ci.cancel();
        if (config.hideFireworks && entity instanceof FireworkRocketEntity) {
            return;
        }
        if (config.hideItems && entity instanceof ItemEntity) {
            return;
        }

        float lineWidth = Main.mc.player != null && Main.mc.player.distanceTo(entity) > config.distFor2 ? config.line2 : config.line1;

        render_1_21_1_boxes(lineWidth, entity, tickProgress,
                new Color(config.eyeColor, true),
                new Color(config.lookColor, true),
                new Color(config.hitBoxColor, true),
                new Color(config.targetBoxColor, true),
                new Color(config.hurtBoxColor, true),
                new Color(config.outlineColor, true),
                config.changeTargetColor,
                config.hitBoxHurt,
                config.renderEyeHeight,
                config.renderLookDir,
                config.lineLookDir,
                config.outlineEnabled,
                config.outlineMultiplier);
    }

    @Unique
    private static void render_1_21_1_boxes(float lineWidth, Entity entity, float tickProgress,
                                            Color eyeHeight,
                                            Color lookDir,
                                            Color main,
                                            Color ifTarget,
                                            Color ifHurt,
                                            Color outlineColor,
                                            boolean targetCol,
                                            boolean hurtCol,
                                            boolean renderEyeHeight,
                                            boolean renderLookDir,
                                            boolean lineLookDir,
                                            boolean outlineEnabled,
                                            float outlineMultiplier) {
        Vec3 vec3d = entity.position();
        Vec3 vec3d2 = entity.getPosition(tickProgress);
        Vec3 vec3d3 = vec3d2.subtract(vec3d);
        Color outer = entity instanceof LivingEntity le && le.hurtTime != 0 && hurtCol ? ifHurt : (targetCol && Main.mc.hitResult instanceof EntityHitResult ehr && ehr.getEntity() == entity ? ifTarget : main);
        int i = outer.getRGB();
        if (outlineEnabled) {
            Gizmos.cuboid(entity.getBoundingBox().move(vec3d3), GizmoStyle.stroke(outlineColor.getRGB(), lineWidth * outlineMultiplier));
        }
        Gizmos.cuboid(entity.getBoundingBox().move(vec3d3), GizmoStyle.stroke(i, lineWidth));
        Gizmos.point(vec3d2, i, 2.0F);
        Entity entity2 = entity.getVehicle();
        if (entity2 != null) {
            float f = Math.min(entity2.getBbWidth(), entity.getBbWidth()) / 2.0F;
            float g = 0.0625F;
            Vec3 vec3d4 = entity2.getPassengerRidingPosition(entity).add(vec3d3);
            Gizmos.cuboid(new AABB(vec3d4.x - f, vec3d4.y, vec3d4.z - f, vec3d4.x + f, vec3d4.y + g, vec3d4.z + f), GizmoStyle.stroke(-256, lineWidth));
        }

        if (entity instanceof LivingEntity && renderEyeHeight) {
            AABB box = entity.getBoundingBox().move(vec3d3);
            float g = 0.01F;
            Gizmos.cuboid(
                    new AABB(box.minX, box.minY + entity.getEyeHeight() - g, box.minZ, box.maxX, box.minY + entity.getEyeHeight() + g, box.maxZ),
                    GizmoStyle.stroke(eyeHeight.getRGB(), lineWidth)
            );
        }

        if (entity instanceof EnderDragon enderDragonEntity) {
            for (EnderDragonPart enderDragonPart : enderDragonEntity.getSubEntities()) {
                Vec3 vec3d5 = enderDragonPart.position();
                Vec3 vec3d6 = enderDragonPart.getPosition(tickProgress);
                Vec3 vec3d7 = vec3d6.subtract(vec3d5);
                Gizmos.cuboid(enderDragonPart.getBoundingBox().move(vec3d7), GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 0.25F, 1.0F, 0.0F)));
            }
        }

        Vec3 vec3d8 = vec3d2.add(0.0, entity.getEyeHeight(), 0.0);
        Vec3 vec3d9 = entity.getViewVector(tickProgress);
        if (renderLookDir) {
            if (lineLookDir) {
                Gizmos.line(vec3d8, vec3d8.add(vec3d9.scale(2.0)), lookDir.getRGB(), lineWidth);
            } else {
                Gizmos.arrow(vec3d8, vec3d8.add(vec3d9.scale(2.0)), lookDir.getRGB(), lineWidth);
            }
        }
    }
}
