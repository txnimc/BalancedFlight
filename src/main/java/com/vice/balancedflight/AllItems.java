package com.vice.balancedflight;

import com.simibubi.create.AllCreativeModeTabs;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.vice.balancedflight.content.angelRing.FlightRing;
import net.minecraft.world.item.Item;

public class AllItems
{
     static { BalancedFlight.registrate().useCreativeTab(AllCreativeModeTabs.MAIN_TAB); }

     public static final ItemEntry<? extends Item> ASCENDED_FLIGHT_RING = FlightRing.ASCENDED;

     public static void init() {}
}
