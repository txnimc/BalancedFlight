package com.vice.balancedflight.network;

import com.vice.balancedflight.BalancedFlight;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class BalancedFlightNetwork
{
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(BalancedFlight.MODID, "main_network"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );

        INSTANCE.messageBuilder(CustomNetworkMessage.class, nextID())
                .encoder(CustomNetworkMessage::toBytes)
                .decoder(CustomNetworkMessage::new)
                .consumer(CustomNetworkMessage::handler)
                .add();
    }
}

