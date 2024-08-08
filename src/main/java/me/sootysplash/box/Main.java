package me.sootysplash.box;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class Main implements ModInitializer {
    public static MinecraftClient mc = MinecraftClient.getInstance();
    public static final Logger LOGGER = LoggerFactory.getLogger("CombatHitboxes");
    public static float lineWidth = 2.5f;
    public static final Method normalMethod;
    public static Method next;
    static {
        Method[] methods = VertexConsumer.class.getMethods();
        normalMethod = methods[methods.length - 1];
        for (Method m : methods) {
            if (m.getParameterCount() == 0 && m.getReturnType() == void.class) {
                next = m;
                break;
            }
        }
    }

    public static void normal(VertexConsumer consumer, MatrixStack.Entry o, float x, float y, float z) {
        try {
            try {
                normalMethod.invoke(consumer, o, x, y, z);
            } catch (Exception e) {
                next.invoke(normalMethod.invoke(consumer, o.getNormalMatrix(), x, y, z));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(Config.class, GsonConfigSerializer::new);
        LOGGER.info("CombatHitboxes | Sootysplash was here!");
    }

    public static float getVanillaWidth() {
        return Math.max(2.5f, (float)MinecraftClient.getInstance().getWindow().getFramebufferWidth() / 1920.0f * 2.5f);
    }

}