package me.sootysplash.box;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.shedaniel.autoconfig.ConfigData;
import net.fabricmc.loader.api.FabricLoader;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@me.shedaniel.autoconfig.annotation.Config(name = "combat-hitboxes")
public class Config implements ConfigData {

    //Andy is the goat https://github.com/AndyRusso/pvplegacyutils/blob/main/src/main/java/io/github/andyrusso/pvplegacyutils/PvPLegacyUtilsConfig.java

    private static final Path file = FabricLoader.getInstance().getConfigDir().resolve("combat-hitboxes.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Config instance;

    public boolean enabled = true;
    public boolean hideArrow = false;
    public int eyeColor = Color.RED.getRGB();
    public int lookColor = Color.BLUE.getRGB();
    public int hitBoxColor = Color.WHITE.getRGB();
    public int targetBoxColor = Color.RED.getRGB();
    public boolean hitBoxHurt = false;
    public boolean renderEyeHeight = true;
    public boolean renderLookDir = true;
    public int hurtBoxColor = Color.MAGENTA.getRGB();
    public float line1 = 2.5f;
    public double distFor2 = 32;
    public float line2 = 2.5f;

    public void save() {
        try {
            Files.writeString(file, GSON.toJson(this));
        } catch (IOException e) {
            Main.LOGGER.error("CombatHitboxes could not save the config.");
            throw new RuntimeException(e);
        }
    }

    public static Config getInstance() {
        if (instance == null) {
            try {
                instance = GSON.fromJson(Files.readString(file), Config.class);
            } catch (IOException exception) {
                Main.LOGGER.warn("CombatHitboxes couldn't load the config, using defaults.");
                instance = new Config();
            }
        }

        return instance;
    }

}
