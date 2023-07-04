package com.vice.balancedflight.content.flightAnchor;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.foundation.RegistrateExtensions;
import com.vice.balancedflight.content.flightAnchor.entity.FlightAnchorEntity;
import com.vice.balancedflight.foundation.util.ModItemTab;
import com.vice.balancedflight.foundation.util.RecipeHelper;
import lombok.experimental.ExtensionMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@ExtensionMethod({ RegistrateExtensions.class})
@Mod.EventBusSubscriber(modid = BalancedFlight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FlightAnchor extends HorizontalKineticBlock implements IBE<FlightAnchorEntity>, BeaconBeamBlock, IRotate
{

    public static final RegistryEntry<? extends Block> REGISTRY_ENTRY = BalancedFlight.registrate()
            .object("flight_anchor")
            .block(FlightAnchor::new)
            .transform(BlockStressDefaults.setImpact(64.0D))
            .properties(properties -> BlockBehaviour.Properties.of(Material.HEAVY_METAL).strength(100).sound(SoundType.NETHERITE_BLOCK).noOcclusion())
            .defaultLoot()
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .geckoItem(FlightAnchorItem::new)
            .initialProperties(() -> new Item.Properties().stacksTo(1).tab(ModItemTab.tab))
            .recipe((gen, provider) -> RecipeHelper.Shaped(provider, gen.get())
                    .Row(Items.POLISHED_BLACKSTONE_BRICKS, Items.GOLD_BLOCK, Items.POLISHED_BLACKSTONE_BRICKS)
                    .Row(Items.IRON_BLOCK, Items.FEATHER, Items.IRON_BLOCK)
                    .Row(Items.POLISHED_BLACKSTONE_BRICKS, Items.POLISHED_BLACKSTONE_BRICKS, Items.POLISHED_BLACKSTONE_BRICKS)
                    .UnlockedBy(Items.FEATHER)
                    .Save())
            .build()
            .register();

    public FlightAnchor(Properties props) { super(props); }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(HORIZONTAL_FACING).getClockWise() || face == state.getValue(HORIZONTAL_FACING).getClockWise().getOpposite();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown())
            return this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());

        return this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection());
    }

    public @NotNull DyeColor getColor() {
        return DyeColor.WHITE;
    }

    @Override public Class<FlightAnchorEntity> getBlockEntityClass() { return FlightAnchorEntity.class; }
    @Override public BlockEntityType<? extends FlightAnchorEntity> getBlockEntityType() { return FlightAnchorEntity.REGISTRATION.get(); }

    @Override public @NotNull RenderShape getRenderShape(@NotNull BlockState state) { return RenderShape.ENTITYBLOCK_ANIMATED; }
    @Override public Direction.Axis getRotationAxis(BlockState state) { return state.getValue(HORIZONTAL_FACING).getAxis();}
    public IRotate.SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.MEDIUM;
    }
}

