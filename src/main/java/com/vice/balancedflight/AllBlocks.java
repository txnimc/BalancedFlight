package com.vice.balancedflight;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.vice.balancedflight.content.flightAnchor.FlightAnchorBlock;
import net.minecraft.world.level.block.Block;

public class AllBlocks
{
    public static final RegistryEntry<? extends Block> FLIGHT_ANCHOR = FlightAnchorBlock.REGISTRY_ENTRY;

    public static void init() { }
}
