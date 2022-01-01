package com.vice.balancedflight.blocks;

import com.google.gson.JsonElement;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.Registry;
import com.vice.balancedflight.items.FlightRing;
import com.vice.balancedflight.util.RecipeHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = BalancedFlight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FlightAnchor extends BaseEntityBlock
{
    public FlightAnchor(Properties p_i48440_1_, AnchorTier tier)
    {
        super(p_i48440_1_);
        registerDefaultState(this.getStateDefinition().any().setValue(TIER, tier));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TIER);
        super.createBlockStateDefinition(builder);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new FlightAnchorEntity(FlightAnchorEntity.REGISTRATION.get(), pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    public static final RegistryEntry<? extends Block> BASIC = FlightAnchorCommon("basic", "(Basic)", AnchorTier.BASIC)
            .recipe((gen, provider) -> RecipeHelper.Shaped(provider, gen.get())
                    .Row(Items.POLISHED_BLACKSTONE_BRICKS, Items.GOLD_BLOCK, Items.POLISHED_BLACKSTONE_BRICKS)
                    .Row(Items.IRON_BLOCK, Items.FEATHER, Items.IRON_BLOCK)
                    .Row(Items.POLISHED_BLACKSTONE_BRICKS, Items.POLISHED_BLACKSTONE_BRICKS, Items.POLISHED_BLACKSTONE_BRICKS)
                    .UnlockedBy(Items.FEATHER)
                    .Save())
            .build()
            .register();

    public static final RegistryEntry<? extends Block> GILDED = FlightAnchorCommon("gilded", "(Gilded)", AnchorTier.GILDED)
            .recipe((gen, provider) -> RecipeHelper.Shaped(provider, gen.get())
                    .Row(Items.POLISHED_BLACKSTONE_BRICKS, Items.DIAMOND_BLOCK, Items.POLISHED_BLACKSTONE_BRICKS)
                    .Row(Items.GOLD_BLOCK, Items.FEATHER, Items.GOLD_BLOCK)
                    .Row(Registry.ASCENDED_BLACKSTONE.get(), BASIC.get(), Registry.ASCENDED_BLACKSTONE.get())
                    .UnlockedBy(Items.FEATHER)
                    .Save())
            .build()
            .register();

    public static final RegistryEntry<? extends Block> ASCENDED = FlightAnchorCommon("ascended", "(Ascended)", AnchorTier.ASCENDED)
            .recipe((gen, provider) -> RecipeHelper.Shaped(provider, gen.get())
                    .Row(Registry.ASCENDED_BLACKSTONE.get(), Items.EMERALD_BLOCK, Registry.ASCENDED_BLACKSTONE.get())
                    .Row(Items.DIAMOND_BLOCK, Items.FEATHER, Items.DIAMOND_BLOCK)
                    .Row(Registry.ASCENDED_BLACKSTONE.get(), GILDED.get(), Registry.ASCENDED_BLACKSTONE.get())
                    .UnlockedBy(Items.FEATHER)
                    .Save())
            .build()
            .register();

    private static ItemBuilder<AnchorItem, BlockBuilder<FlightAnchor, Registrate>> FlightAnchorCommon(String prefix, String langtag, AnchorTier tier) {
        return BalancedFlight.registrate()
                .object(tier.getSerializedName() + "_flight_anchor")
                .block(props -> new FlightAnchor(props, tier))
                .blockstate((gen, provider) ->
                {
                    provider.getVariantBuilder(gen.get())
                            .forAllStates(state -> {
                                BlockModelBuilder models = getModelFileForVariant(state.getValue(TIER), provider);

                                return ConfiguredModel.builder().modelFile(models).build();
                            });
                })
                .properties(properties -> BlockBehaviour.Properties.of(Material.HEAVY_METAL).strength(100).sound(SoundType.NETHERITE_BLOCK))
                .defaultLoot()
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .tag(BlockTags.NEEDS_IRON_TOOL)
                .item((block, props) -> new AnchorItem(tier, block, props))
                .initialProperties(() -> new Item.Properties().stacksTo(1))
                .model((ctx, prov) -> {
                    prov.withExistingParent(
                            "item/" + prov.name(ctx::getEntry),
                            new ResourceLocation(prov.modid(ctx::getEntry), "block/" + prov.name(ctx::getEntry)));
                });
    }

    private static BlockModelBuilder getModelFileForVariant(AnchorTier tier, RegistrateBlockstateProvider provider) {
        return provider.models().cubeBottomTop(
                tier.getSerializedName() + "_flight_anchor",
                provider.modLoc("block/" + tier.getResourceName("side")),
                provider.modLoc("block/" + tier.getResourceName("bottom")),
                provider.modLoc("block/" + tier.getResourceName("top"))
        );
    }

    public static EnumProperty<AnchorTier> TIER = EnumProperty.create("tier", AnchorTier.class);


    public enum AnchorTier implements StringRepresentable
    {
        BASIC("basic", 25),
        GILDED("gilded", 50),
        ASCENDED("ascended", 100);

        public final double EffectDistance;
        private final String name;

        AnchorTier(String name, double effectDistance)
        {
            this.name = name;
            EffectDistance = effectDistance;
        }

        @Override
        public String getSerializedName()
        {
            return this.name;
        }

        public String getResourceName(String side)
        {
            return this.name + "_flight_anchor_" + side;
        }
    }



}

class AnchorItem extends BlockItem
{

    FlightAnchor.AnchorTier tier;

    public AnchorItem(FlightAnchor.AnchorTier tier, Block p_i48527_1_, Properties p_i48527_2_)
    {
        super(p_i48527_1_, p_i48527_2_);
        this.tier = tier;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag p_41424_)
    {
        {
            tooltip.add(new TextComponent("Allows flight in a " + (int) tier.EffectDistance + " block distance.").withStyle(ChatFormatting.WHITE));
            tooltip.add(new TextComponent("Only works in the overworld.").withStyle(ChatFormatting.RED));
        }
    }
}