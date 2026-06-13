package me.sootysplash.box;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static Minecraft mc = Minecraft.getInstance();
    public static final Logger LOGGER = LoggerFactory.getLogger("CombatHitboxes");

    @Override
    public void onInitialize() {
        LOGGER.info("CombatHitboxes | Sootysplash was here!");
    }

}