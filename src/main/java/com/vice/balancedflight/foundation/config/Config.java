package com.vice.balancedflight.foundation.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

import static net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class Config
{
    public static ForgeConfigSpec ConfigSpec;

    public static ConfigValue<Boolean> enableElytraFlightFromGround;
    public static ConfigValue<Boolean> enableTakeOff;
    public static ConfigValue<Boolean> infiniteRockets;

    public static ConfigValue<Boolean> ElytraBasic;
    public static ConfigValue<Boolean> ElytraAscended;

    public static ConfigValue<Boolean> disableFallDamageWithRings;
    public static ConfigValue<Boolean> disableElytraDamageWithRings;

    public static ConfigValue<Boolean> CreativeBasic;
    public static ConfigValue<Boolean> CreativeAscended;

    public static ConfigValue<Double> anchorDistanceMultiplier;

    static
    {
        ConfigBuilder builder = new ConfigBuilder("Balanced Flight Settings");

        builder.Block("Flight Options", b -> {
            CreativeAscended = b.define("Ascended Ring Gives Unlimited Creative Flight (will fall back to Basic tier inside range)", true);
            ElytraAscended = b.define("Ascended Ring Also Works As Elytra", true);

            CreativeBasic = b.define("Basic Ring Gives Creative Flight", true);
            ElytraBasic = b.define("Basic Flight Ring Also Works As Elytra", false);
        });

        builder.Block("Balancing Config", b -> {
            anchorDistanceMultiplier = b.defineInRange("Anchor Distance Multiplier (0->10)", 1.0d, 0.0d, 10.0d);
            disableFallDamageWithRings = b.define("Disable Fall Damage When Wearing Rings", true);
            disableElytraDamageWithRings = b.define("Disable Elytra Damage When Wearing Rings", true);
        });

        builder.Block("Enhanced Elytra Mechanics", b -> {
            enableElytraFlightFromGround = b.define("Enable Elytra Flight From Ground", true);
            enableTakeOff =  b.define("Enable Take Off Mechanic", true);
            infiniteRockets = b.define("Infinite Rockets", true);
        });

        ConfigSpec = builder.Save();
    }

    public static void init() {
        if (ConfigSpec.isLoaded())
            return;

        loadConfig(FMLPaths.CONFIGDIR.get().resolve("balanced_flight.toml"));
    }

    private static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        ConfigSpec.setConfig(configData);
    }
}
