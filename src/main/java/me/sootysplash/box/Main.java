package me.sootysplash.box;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static Minecraft mc = Minecraft.getInstance();
    public static final Logger LOGGER = LoggerFactory.getLogger("CombatHitboxes");

    @Override
    public void onInitialize() {
        AutoConfig.register(Config.class, GsonConfigSerializer::new);
        LOGGER.info("CombatHitboxes | Sootysplash was here!");
    }

}