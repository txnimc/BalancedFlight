package com.vice.balancedflight.blocks;

import com.google.common.collect.Lists;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.vice.balancedflight.BalancedFlight;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightAnchorEntity extends SmartBlockEntity
{
    public static Map<BlockPos, FlightAnchor.AnchorTier> ActiveAnchors = new HashMap<>();

    List<BeaconBlockEntity.BeaconBeamSection> beamSections = Lists.newArrayList();
    List<BeaconBlockEntity.BeaconBeamSection> checkingBeamSections = Lists.newArrayList();
    int lastCheckY;

    public FlightAnchorEntity(BlockEntityType type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);

    }

    public static final BlockEntityEntry<FlightAnchorEntity> REGISTRATION = BalancedFlight.registrate()
            .blockEntity("flight_anchor", FlightAnchorEntity::new)
            .validBlock(FlightAnchor.ASCENDED)
            .validBlock(FlightAnchor.BASIC)
            .validBlock(FlightAnchor.GILDED)
            .register();


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list)
    {
        list.add(new FlightAnchorBehaviour(this));
    }
}
