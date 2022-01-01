package com.vice.balancedflight.blocks;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.vice.balancedflight.BalancedFlight;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public class FlightAnchorEntity extends BlockEntity //implements TickingBlockEntity
{
    public static ArrayList<AnchorConfig> ActiveAnchors = new ArrayList<AnchorConfig>();

    //private boolean HasSetPosition = false;

    public FlightAnchorEntity(BlockEntityType type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);

        AddAnchor();
    }

    public static final BlockEntityEntry<FlightAnchorEntity> REGISTRATION = BalancedFlight.registrate()
            .blockEntity("flight_anchor", FlightAnchorEntity::new)
            .validBlock(FlightAnchor.ASCENDED)
            .validBlock(FlightAnchor.BASIC)
            .validBlock(FlightAnchor.GILDED)
            .register();


//    @Override
//    public void setRemoved() {
//        super.setRemoved();
//
//        if(level.isClientSide)
//            return;
//
//        ActiveAnchors.removeIf(anchor -> anchor.position.equals(this.worldPosition));
//    }


    public void AddAnchor() {
        if (ActiveAnchors.stream().anyMatch(existing -> existing.position.equals(this.worldPosition)))
            return;

        ActiveAnchors.add(new AnchorConfig(this.worldPosition, this.getBlockState().getValue(FlightAnchor.TIER)));
    }

//    @Override
//    public void tick()
//    {
//        if (HasSetPosition)
//            return;
//
//        AddAnchor();
//    }

//    @Override
//    public BlockPos getPos()
//    {
//        return this.worldPosition;
//    }

    public class AnchorConfig {
        public final BlockPos position;
        public final FlightAnchor.AnchorTier tier;

        public AnchorConfig(BlockPos position, FlightAnchor.AnchorTier tier)
        {
            this.position = position;
            this.tier = tier;
        }
    }
}
