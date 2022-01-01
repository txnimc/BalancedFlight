package com.vice.balancedflight.util;

import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.items.FlightRing;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class ModItemTab {

    public static class ModCreativeTab extends CreativeModeTab
    {

        public ModCreativeTab(String name) {
            super(name);
        }

        @Override
        public boolean hasSearchBar() {
            return false;
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(FlightRing.ASCENDED.get());
        }
    }

    @Nonnull
    public static final CreativeModeTab tab = new ModCreativeTab(BalancedFlight.MODID);

}