package com.vice.balancedflight.content.flightAnchor.entity;

import com.google.common.collect.Lists;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

import java.util.Arrays;
import java.util.List;

public class FlightAnchorBehaviour extends BlockEntityBehaviour
{
    public static final BehaviourType<FlightAnchorBehaviour> TYPE = new BehaviourType();

    public FlightAnchorBehaviour(SmartBlockEntity be)
    {
        super(be);
    }

    @Override
    public BehaviourType<?> getType()
    {
        return TYPE;
    }

    public void setActive(boolean active)
    {
        if (!active)
        {
            FlightAnchorEntity.ActiveAnchors.remove(getPos());
            return;
        }

        if (FlightAnchorEntity.ActiveAnchors.containsKey(getPos()))
            return;

        FlightAnchorEntity.ActiveAnchors.put(getPos(), (FlightAnchorEntity) blockEntity);
    }

    @Override
    public void initialize()
    {
        ((FlightAnchorEntity)blockEntity).placedRenderTime = AnimationTickHolder.getRenderTime(getWorld());
    }

    @Override
    public void unload() { setActive(false); }

    @Override
    public void tick()
    {
        super.tick();

        var flightAnchor = (FlightAnchorEntity) blockEntity;
        var wasActive = flightAnchor.isActive;

        flightAnchor.isActive = flightAnchor.isSpeedRequirementFulfilled();

        if (flightAnchor.isActive && !wasActive) {
            setActive(true);
            playSound(getWorld(), getPos(), SoundEvents.BEACON_ACTIVATE);
        }
        else if (!flightAnchor.isActive && wasActive) {
            setActive(false);
            playSound(getWorld(), getPos(), SoundEvents.BEACON_DEACTIVATE);
            flightAnchor.beamSections.clear();
            flightAnchor.checkingBeamSections.clear();
        }

        if (flightAnchor.isActive)
            beaconTick(getWorld(), getPos(), flightAnchor);
    }

    @Override
    public void lazyTick() {

    }

    public void beaconTick(Level level, BlockPos blockPos, FlightAnchorEntity entity) {
        int i = blockPos.getX();
        int j = blockPos.getY();
        int k = blockPos.getZ();
        BlockPos blockpos;

        if (entity.lastCheckY < j) {
            blockpos = blockPos;
            entity.checkingBeamSections = Lists.newArrayList();
            entity.lastCheckY = blockPos.getY() - 1;
        } else {
            blockpos = new BlockPos(i, entity.lastCheckY + 1, k);
        }

        BeaconBlockEntity.BeaconBeamSection beaconblockentity$beaconbeamsection = entity.checkingBeamSections.isEmpty() ? null : entity.checkingBeamSections.get(entity.checkingBeamSections.size() - 1);
        int l = level.getHeight(Heightmap.Types.WORLD_SURFACE, i, k);

        for(int i1 = 0; i1 < 10 && blockpos.getY() <= l; ++i1) {
            BlockState blockstate = level.getBlockState(blockpos);
            Block block = blockstate.getBlock();
            float[] afloat = blockstate.getBeaconColorMultiplier(level, blockpos, blockPos);
            if (afloat != null) {
                if (entity.checkingBeamSections.size() <= 1) {
                    beaconblockentity$beaconbeamsection = new BeaconBlockEntity.BeaconBeamSection(afloat);
                    entity.checkingBeamSections.add(beaconblockentity$beaconbeamsection);
                } else if (beaconblockentity$beaconbeamsection != null) {
                    if (Arrays.equals(afloat, beaconblockentity$beaconbeamsection.getColor())) {
                        beaconblockentity$beaconbeamsection.increaseHeight();
                    } else {
                        beaconblockentity$beaconbeamsection = new BeaconBlockEntity.BeaconBeamSection(new float[]{(beaconblockentity$beaconbeamsection.getColor()[0] + afloat[0]) / 2.0F, (beaconblockentity$beaconbeamsection.getColor()[1] + afloat[1]) / 2.0F, (beaconblockentity$beaconbeamsection.getColor()[2] + afloat[2]) / 2.0F});
                        entity.checkingBeamSections.add(beaconblockentity$beaconbeamsection);
                    }
                }
            } else {
                if (beaconblockentity$beaconbeamsection == null || blockstate.getLightBlock(level, blockpos) >= 15 && !blockstate.is(Blocks.BEDROCK)) {
                    entity.checkingBeamSections.clear();
                    entity.lastCheckY = l;
                    break;
                }

                beaconblockentity$beaconbeamsection.increaseHeight();
            }

            blockpos = blockpos.above();
            ++entity.lastCheckY;
        }



        if (entity.lastCheckY >= l) {
            entity.lastCheckY = level.getMinBuildHeight() - 1;
            entity.beamSections = entity.checkingBeamSections;

//            if (!level.isClientSide) {
//                if (!flag) {
//                    playSound(level, blockPos, SoundEvents.BEACON_ACTIVATE);
//
//                    for(ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, (new AABB((double)i, (double)j, (double)k, (double)i, (double)(j - 4), (double)k)).inflate(10.0D, 5.0D, 10.0D))) {
//                        CriteriaTriggers.CONSTRUCT_BEACON.trigger(serverplayer, entity.levels);
//                    }
//                }
//                else
//                {
//                    playSound(level, blockPos, SoundEvents.BEACON_DEACTIVATE);
//                }
//            }
        }
    }

    public static void playSound(Level p_155104_, BlockPos p_155105_, SoundEvent p_155106_) {
        p_155104_.playSound(null, p_155105_, p_155106_, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static void applyEffects(Level level, BlockPos blockPos, int radius) {
        if (level.isClientSide)
            return;

        AABB aabb = (new AABB(blockPos)).inflate(radius * 10 + 10).expandTowards(0.0D, level.getHeight(), 0.0D);
        List<Player> list = level.getEntitiesOfClass(Player.class, aabb);

        for(Player player : list) {

        }
    }
}
