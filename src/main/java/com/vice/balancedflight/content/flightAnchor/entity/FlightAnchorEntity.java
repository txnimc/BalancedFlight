package com.vice.balancedflight.content.flightAnchor.entity;

import com.google.common.collect.Lists;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.content.flightAnchor.FlightAnchorBlock;
import com.vice.balancedflight.AllGeckoRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import com.vice.balancedflight.content.flightAnchor.render.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightAnchorEntity extends KineticBlockEntity implements IAnimatable
{
    public static Map<BlockPos, FlightAnchorEntity> ActiveAnchors = new HashMap<>();

    List<BeaconBlockEntity.BeaconBeamSection> beamSections = Lists.newArrayList();
    List<BeaconBlockEntity.BeaconBeamSection> checkingBeamSections = Lists.newArrayList();
    int lastCheckY;
    boolean isActive;
    public float placedRenderTime;

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public AnimationController controller = null;
    private final AnimationBuilder onAnimation = new AnimationBuilder().addAnimation("animation.flight_anchor.deploy", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    //private final AnimationBuilder offAnimation = new AnimationBuilder().addAnimation("animation.hex_extraction_unit.off", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);


    public FlightAnchorEntity(BlockEntityType type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public static final BlockEntityEntry<FlightAnchorEntity> REGISTRATION = BalancedFlight.registrate()
            .blockEntity("flight_anchor", FlightAnchorEntity::new)
            .instance(() -> FlightAnchorKineticInstance::new)
            .validBlock(FlightAnchorBlock.REGISTRY_ENTRY)
            .renderer(() -> AllGeckoRenderers.FlightAnchorGeckoRenderer.TileRenderer::apply)
            .register();


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list)
    {
        list.add(new FlightAnchorBehaviour(this));
    }

    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        controller.setAnimation(onAnimation);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        controller = new AnimationController(this, "controller", 0, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public List<BeaconBlockEntity.BeaconBeamSection> getBeamSections() {
        return this.beamSections;
    }
}
