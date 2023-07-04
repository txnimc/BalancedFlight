package com.vice.balancedflight;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.vice.balancedflight.content.flightAnchor.FlightAnchor;
import com.vice.balancedflight.foundation.util.ModItemTab;
import com.vice.balancedflight.foundation.util.RecipeHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class AllBlocks
{
    public static final RegistryEntry<? extends Block> FLIGHT_ANCHOR = FlightAnchor.REGISTRY_ENTRY;

    public static final RegistryEntry<? extends Block> ASCENDED_BLACKSTONE = BalancedFlight.registrate()
            .block("ascended_blackstone", Block::new)
            .properties(props -> BlockBehaviour.Properties.of(Material.STONE).strength(5).sound(SoundType.GILDED_BLACKSTONE))
            .defaultBlockstate()
            .defaultLoot()
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .item()
            .initialProperties(() -> new Item.Properties().stacksTo(64).tab(ModItemTab.tab))
            .recipe((gen, prov) -> RecipeHelper.Shaped(prov, gen.get())
                    .Row(Items.POLISHED_BLACKSTONE, Items.GOLD_BLOCK, Items.POLISHED_BLACKSTONE)
                    .Row(Items.GOLD_BLOCK, Items.DIAMOND, Items.GOLD_BLOCK)
                    .Row(Items.POLISHED_BLACKSTONE, Items.GOLD_BLOCK, Items.POLISHED_BLACKSTONE)
                    .UnlockedBy(Items.FEATHER)
                    .Save())
            .build()
            .register();

    public static void init() { }
}
