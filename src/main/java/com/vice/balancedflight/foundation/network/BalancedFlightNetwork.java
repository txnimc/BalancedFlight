package com.vice.balancedflight.foundation.network;

import com.vice.balancedflight.BalancedFlight;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

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
                .consumerNetworkThread(CustomNetworkMessage::handler)
                .add();
    }
}

