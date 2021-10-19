package com.vice.balancedflight.blocks;

import com.tterrag.registrate.util.entry.TileEntityEntry;
import com.vice.balancedflight.BalancedFlight;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class FlightAnchorEntity extends TileEntity implements ITickableTileEntity
{
    public static ArrayList<AnchorConfig> ActiveAnchors = new ArrayList<AnchorConfig>();

    private boolean HasSetPosition = false;

    public FlightAnchorEntity(TileEntityType type)
    {
        super(type);
    }

    public static final TileEntityEntry<TileEntity> REGISTRATION = BalancedFlight.registrate()
            .tileEntity("flight_anchor", type -> new FlightAnchorEntity(type))
            .validBlock(FlightAnchor.ASCENDED::get)
            .validBlock(FlightAnchor.BASIC::get)
            .validBlock(FlightAnchor.GILDED::get)
            .register();


    @Override
    public void setRemoved() {
        super.setRemoved();

        if(level.isClientSide)
            return;

        ActiveAnchors.removeIf(anchor -> anchor.position.equals(this.worldPosition));
    }


    public void AddAnchor() {
        if (ActiveAnchors.stream().anyMatch(existing -> existing.position.equals(this.worldPosition)))
            return;

        ActiveAnchors.add(new AnchorConfig(this.worldPosition, this.getBlockState().getValue(FlightAnchor.TIER)));
    }

    @Override
    public void tick()
    {
        if (HasSetPosition)
            return;

        AddAnchor();
    }

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
