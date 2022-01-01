package com.vice.balancedflight;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.RegistryEntry; 
import com.vice.balancedflight.blocks.FlightAnchor;
import com.vice.balancedflight.blocks.FlightAnchorEntity;
import com.vice.balancedflight.items.FlightRing;
import com.vice.balancedflight.util.ModItemTab;
import com.vice.balancedflight.util.RecipeHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class Registry
{
    public static final RegistryEntry<? extends Block> BASIC_FLIGHT_ANCHOR = FlightAnchor.BASIC;
    public static final RegistryEntry<? extends Block> GILDED_FLIGHT_ANCHOR = FlightAnchor.GILDED;
    public static final RegistryEntry<? extends Block> ASCENDED_FLIGHT_ANCHOR = FlightAnchor.ASCENDED;

    public static final BlockEntityEntry<FlightAnchorEntity> FLIGHT_ANCHOR_ENTITY = FlightAnchorEntity.REGISTRATION;

    public static final RegistryEntry<? extends Block> ASCENDED_BLACKSTONE = BalancedFlight.registrate()
            .block("ascended_blackstone", Block::new)
            .properties(props -> BlockBehaviour.Properties.of(Material.STONE).strength(5).sound(SoundType.GILDED_BLACKSTONE))
            .defaultBlockstate()
            .defaultLoot()
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .item()
            .recipe((gen, prov) -> RecipeHelper.Shaped(prov, gen.get())
                    .Row(Items.POLISHED_BLACKSTONE, Items.GOLD_BLOCK, Items.POLISHED_BLACKSTONE)
                    .Row(Items.GOLD_BLOCK, Items.DIAMOND, Items.GOLD_BLOCK)
                    .Row(Items.POLISHED_BLACKSTONE, Items.GOLD_BLOCK, Items.POLISHED_BLACKSTONE)
                    .UnlockedBy(Items.FEATHER)
                    .Save())
            .build()
            .register();

    public static final RegistryEntry<? extends Item> FLIGHT_CORE = BalancedFlight.registrate()
            .object("flight_core")
            .item(Item::new)
            .initialProperties(() -> new Item.Properties().stacksTo(1).tab(ModItemTab.tab))
            .recipe((gen, provider) -> RecipeHelper.Shaped(provider, gen.get())
                    .Row(Registry.ASCENDED_BLACKSTONE.get(), Items.NETHERITE_INGOT, Registry.ASCENDED_BLACKSTONE.get())
                    .Row(Items.NETHERITE_INGOT , Items.NETHER_STAR, Items.NETHERITE_INGOT)
                    .Row(Registry.ASCENDED_BLACKSTONE.get(), Items.NETHERITE_INGOT, Registry.ASCENDED_BLACKSTONE.get())
                    .UnlockedBy(Items.FEATHER)
                    .Save())
            .register();

    public static final RegistryEntry<? extends Item> ASCENDED_FLIGHT_RING = FlightRing.ASCENDED;
    public static final RegistryEntry<? extends Item> BASIC_FLIGHT_RING = FlightRing.BASIC;


    public static void init() {}
}
