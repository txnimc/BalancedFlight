package com.vice.balancedflight.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

import static net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class BalancedFlightConfig
{
    public static ForgeConfigSpec ConfigSpec;

    public static ConfigValue<Boolean> enableElytraFlightFromGround;
    public static ConfigValue<Boolean> enableTakeOff;
    public static ConfigValue<Boolean> infiniteRockets;
    public static ConfigValue<Boolean> enableElytraFlight;

    static
    {
        ConfigBuilder builder = new ConfigBuilder("Dynamic Lights Settings");

        builder.Block("Misc", b -> {
            enableElytraFlightFromGround = b.define("Enable Elytra Flight From Ground", true);
            enableTakeOff =  b.define("Enable Take Off Mechanic", true);
            infiniteRockets = b.define("Infinite Rockets", true);
            enableElytraFlight = b.define("Flight Ring Also Works As Elytra", true);
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
