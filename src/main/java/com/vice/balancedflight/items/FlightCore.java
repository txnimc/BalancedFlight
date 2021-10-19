package com.vice.balancedflight.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.blocks.FlightAnchor;
import com.vice.balancedflight.util.ModItemTab;
import com.vice.balancedflight.util.RecipeHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class FlightCore extends Item
{
    public FlightCore(Item.Properties props) { super(props);}

    public static final RegistryEntry<? extends Item> REGISTRATION = BalancedFlight.registrate()
            .object("flight_core")
            .item(FlightCore::new)
            .initialProperties(() -> new Item.Properties().stacksTo(1).tab(ModItemTab.tab))
            .recipe((gen, provider) -> RecipeHelper.Shaped(provider, gen.get())
                    .Row(Items.END_STONE_BRICKS, Items.GOLD_BLOCK, Items.END_STONE_BRICKS)
                    .Row(Items.GOLD_BLOCK , Items.NETHER_STAR, Items.GOLD_BLOCK)
                    .Row(Items.END_STONE_BRICKS, Items.GOLD_BLOCK, Items.END_STONE_BRICKS)
                    .UnlockedBy(Items.FEATHER)
                    .Save())
            .register();
}
