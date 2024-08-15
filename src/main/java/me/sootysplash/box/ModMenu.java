package me.sootysplash.box;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

import java.awt.*;

public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            Config config = Config.getInstance();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.of("Config"))
                    .setSavingRunnable(config::save);

            ConfigCategory behavior = builder.getOrCreateCategory(Text.of("Behavior"));
            ConfigEntryBuilder cfgent = builder.entryBuilder();


            behavior.addEntry(cfgent.startBooleanToggle(Text.of("Enabled"), config.enabled)
                    .setDefaultValue(true)
                    .setTooltip(Text.of("Modify hitbox rendering?"))
                    .setSaveConsumer(newValue -> config.enabled = newValue)
                    .build());


            behavior.addEntry(cfgent.startBooleanToggle(Text.of("Render Eye Height"), config.renderEyeHeight)
                    .setDefaultValue(true)
                    .setTooltip(Text.of("Render the red line at the entity's eye height"))
                    .setSaveConsumer(newValue -> config.renderEyeHeight = newValue)
                    .build());


            behavior.addEntry(cfgent.startBooleanToggle(Text.of("Render Look Direction"), config.renderLookDir)
                    .setDefaultValue(true)
                    .setTooltip(Text.of("Render the blue line indicating the direction the entity is facing"))
                    .setSaveConsumer(newValue -> config.renderLookDir = newValue)
                    .build());


            behavior.addEntry(cfgent.startBooleanToggle(Text.of("HitBox Hurt"), config.hitBoxHurt)
                    .setDefaultValue(false)
                    .setTooltip(Text.of("Hitbox hurt color when hurt?"))
                    .setSaveConsumer(newValue -> config.hitBoxHurt = newValue)
                    .build());

            behavior.addEntry(cfgent.startBooleanToggle(Text.of("Target HitBox Color"), config.changeTargetColor)
                    .setDefaultValue(true)
                    .setTooltip(Text.of("Target hitbox color on targets?"))
                    .setSaveConsumer(newValue -> config.changeTargetColor = newValue)
                    .build());

            behavior.addEntry(cfgent.startBooleanToggle(Text.of("Hide Stuck Arrows"), config.hideArrow)
                    .setDefaultValue(false)
                    .setTooltip(Text.of("Removes bee stingers and arrows visually from other players"))
                    .setSaveConsumer(newValue -> config.hideArrow = newValue)
                    .build());



            ConfigCategory colors = builder.getOrCreateCategory(Text.of("Colors"));


            colors.addEntry(cfgent.startAlphaColorField(Text.of("Base Color"), config.hitBoxColor)
                    .setDefaultValue(Color.WHITE.getRGB())
                    .setTooltip(Text.of("The base hitbox's color"))
                    .setSaveConsumer(newValue -> config.hitBoxColor = newValue)
                    .build());


            colors.addEntry(cfgent.startAlphaColorField(Text.of("Eye Color"), config.eyeColor)
                    .setDefaultValue(Color.RED.getRGB())
                    .setTooltip(Text.of("The hitbox eye height color"))
                    .setSaveConsumer(newValue -> config.targetBoxColor = newValue)
                    .build());


            colors.addEntry(cfgent.startAlphaColorField(Text.of("Look Direction Color"), config.lookColor)
                    .setDefaultValue(Color.BLUE.getRGB())
                    .setTooltip(Text.of("The hitbox's look direction color"))
                    .setSaveConsumer(newValue -> config.lookColor = newValue)
                    .build());


            colors.addEntry(cfgent.startAlphaColorField(Text.of("Target Color"), config.targetBoxColor)
                    .setDefaultValue(Color.RED.getRGB())
                    .setTooltip(Text.of("The hitbox color when the entity is targeted"))
                    .setSaveConsumer(newValue -> config.targetBoxColor = newValue)
                    .build());


            colors.addEntry(cfgent.startAlphaColorField(Text.of("Hurt Color"), config.hurtBoxColor)
                    .setDefaultValue(Color.MAGENTA.getRGB())
                    .setTooltip(Text.of("The hitbox color when the entity is on hurt tick"))
                    .setSaveConsumer(newValue -> config.hurtBoxColor = newValue)
                    .build());


            ConfigCategory linesWidths = builder.getOrCreateCategory(Text.of("Line Width"));


            linesWidths.addEntry(cfgent.startFloatField(Text.of("Line Width 1"), config.line1)
                    .setMin(0)
                    .setMax(25f)
                    .setDefaultValue(2.5f)
                    .setTooltip(Text.of("The width of the hitbox lines"))
                    .setSaveConsumer(newValue -> config.line1 = newValue)
                    .build());

            linesWidths.addEntry(cfgent.startDoubleField(Text.of("Distance for width 2"), config.distFor2)
                    .setMin(0)
                    .setMax(256)
                    .setDefaultValue(32)
                    .setTooltip(Text.of("The distance for Line Width 2 to be used"))
                    .setSaveConsumer(newValue -> config.distFor2 = newValue)
                    .build());

            linesWidths.addEntry(cfgent.startFloatField(Text.of("Line Width 2"), config.line2)
                    .setMin(0)
                    .setMax(25f)
                    .setDefaultValue(2.5f)
                    .setTooltip(Text.of("The width of the hitbox lines beyond the set distance"))
                    .setSaveConsumer(newValue -> config.line2 = newValue)
                    .build());


            return builder.build();
        };
    }
}
