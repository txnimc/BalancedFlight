package com.vice.balancedflight;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.entry.TileEntityEntry;
import com.vice.balancedflight.blocks.FlightAnchor;
import com.vice.balancedflight.blocks.FlightAnchorEntity;
import com.vice.balancedflight.items.FlightRing;
import com.vice.balancedflight.util.ModItemTab;
import com.vice.balancedflight.util.RecipeHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ToolType;

public class Registry
{
    public static final RegistryEntry<? extends Block> BASIC_FLIGHT_ANCHOR = FlightAnchor.BASIC;
    public static final RegistryEntry<? extends Block> GILDED_FLIGHT_ANCHOR = FlightAnchor.GILDED;
    public static final RegistryEntry<? extends Block> ASCENDED_FLIGHT_ANCHOR = FlightAnchor.ASCENDED;

    public static final TileEntityEntry<TileEntity> FLIGHT_ANCHOR_ENTITY = FlightAnchorEntity.REGISTRATION;

    public static final RegistryEntry<? extends Block> ASCENDED_BLACKSTONE = BalancedFlight.registrate()
            .block("ascended_blackstone", Block::new)
            .properties(props -> AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).strength(5).sound(SoundType.GILDED_BLACKSTONE))
            .defaultBlockstate()
            .defaultLoot()
            .item()
            .recipe((gen, prov) -> RecipeHelper.Shaped(prov, gen.get())
                    .Row(Items.POLISHED_BLACKSTONE_BRICKS, Items.GOLD_BLOCK, Items.POLISHED_BLACKSTONE_BRICKS)
                    .Row(Items.GOLD_BLOCK, Items.DIAMOND, Items.GOLD_BLOCK)
                    .Row(Items.POLISHED_BLACKSTONE_BRICKS, Items.GOLD_BLOCK, Items.POLISHED_BLACKSTONE_BRICKS)
                    .UnlockedBy(Items.FEATHER)
                    .Save())
            .build()
            .register();

    public static final RegistryEntry<? extends Item> FLIGHT_CORE = BalancedFlight.registrate()
            .object("flight_core")
            .item(Item::new)
            .initialProperties(() -> new Item.Properties().stacksTo(1).tab(ModItemTab.tab))
            .recipe((gen, provider) -> RecipeHelper.Shaped(provider, gen.get())
                    .Row(Registry.ASCENDED_BLACKSTONE.get(), Items.GOLD_BLOCK, Registry.ASCENDED_BLACKSTONE.get())
                    .Row(Items.GOLD_BLOCK , Items.NETHERITE_INGOT, Items.GOLD_BLOCK)
                    .Row(Registry.ASCENDED_BLACKSTONE.get(), Items.GOLD_BLOCK, Registry.ASCENDED_BLACKSTONE.get())
                    .UnlockedBy(Items.FEATHER)
                    .Save())
            .register();

    public static final RegistryEntry<? extends Item> ASCENDED_FLIGHT_RING = FlightRing.ASCENDED;
    public static final RegistryEntry<? extends Item> BASIC_FLIGHT_RING = FlightRing.BASIC;


    public static void init() {}
}
