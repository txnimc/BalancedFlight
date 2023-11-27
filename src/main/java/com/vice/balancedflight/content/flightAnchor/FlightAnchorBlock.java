package com.vice.balancedflight.content.flightAnchor;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.block.IBE;
import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.foundation.RegistrateExtensions;
import com.vice.balancedflight.content.flightAnchor.entity.FlightAnchorEntity;
import lombok.experimental.ExtensionMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@ExtensionMethod({ RegistrateExtensions.class})
@Mod.EventBusSubscriber(modid = BalancedFlight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FlightAnchorBlock extends HorizontalKineticBlock implements IBE<FlightAnchorEntity>, BeaconBeamBlock, IRotate
{
    public FlightAnchorBlock(Properties props) { super(props); }

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
    @Override public BlockEntityType<? extends FlightAnchorEntity> getBlockEntityType() { return BalancedFlight.FLIGHT_ANCHOR_BLOCK_ENTITY.get(); }

    @Override public @NotNull RenderShape getRenderShape(@NotNull BlockState state) { return RenderShape.ENTITYBLOCK_ANIMATED; }
    @Override public Direction.Axis getRotationAxis(BlockState state) { return state.getValue(HORIZONTAL_FACING).getAxis();}
    public IRotate.SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.MEDIUM;
    }
}

