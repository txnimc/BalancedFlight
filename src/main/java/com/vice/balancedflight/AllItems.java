package com.vice.balancedflight;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.vice.balancedflight.content.angelRing.FlightRing;
import com.vice.balancedflight.foundation.util.ModItemTab;
import com.vice.balancedflight.foundation.util.RecipeHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class AllItems
{
        public static final RegistryEntry<? extends Item> FLIGHT_CORE = BalancedFlight.registrate()
                .object("flight_core")
                .item(Item::new)
                .initialProperties(() -> new Item.Properties().stacksTo(1).tab(ModItemTab.tab))
                .recipe((gen, provider) -> RecipeHelper.Shaped(provider, gen.get())
                        .Row(AllBlocks.ASCENDED_BLACKSTONE.get(), Items.NETHERITE_INGOT, AllBlocks.ASCENDED_BLACKSTONE.get())
                        .Row(Items.NETHERITE_INGOT , Items.NETHER_STAR, Items.NETHERITE_INGOT)
                        .Row(AllBlocks.ASCENDED_BLACKSTONE.get(), Items.NETHERITE_INGOT, AllBlocks.ASCENDED_BLACKSTONE.get())
                        .UnlockedBy(Items.FEATHER)
                        .Save())
                .register();

        public static final RegistryEntry<? extends Item> ASCENDED_FLIGHT_RING = FlightRing.ASCENDED;

        public static void init() {}
}
