package com.vice.balancedflight;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.entry.TileEntityEntry;
import com.vice.balancedflight.blocks.FlightAnchor;
import com.vice.balancedflight.blocks.FlightAnchorEntity;
import com.vice.balancedflight.items.FlightCore;
import com.vice.balancedflight.items.FlightRing;
import com.vice.balancedflight.util.RecipeHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Tags;
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
                    .Row(Items.POLISHED_BLACKSTONE, Items.GOLD_INGOT, Items.POLISHED_BLACKSTONE)
                    .Row(Items.GOLD_INGOT, Items.DIAMOND, Items.GOLD_INGOT)
                    .Row(Items.POLISHED_BLACKSTONE, Items.GOLD_INGOT, Items.POLISHED_BLACKSTONE)
                    .UnlockedBy(Items.FEATHER)
                    .Save())
            .build()
            .register();

    public static final RegistryEntry<? extends Item> FLIGHT_CORE = FlightCore.REGISTRATION;

    public static final RegistryEntry<? extends Item> ASCENDED_FLIGHT_RING = FlightRing.ASCENDED;
    public static final RegistryEntry<? extends Item> BASIC_FLIGHT_RING = FlightRing.BASIC;


    public static void init() {}
}
