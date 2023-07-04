package com.vice.balancedflight.foundation.render;


import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.vice.balancedflight.BalancedFlight;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AnimatedBlockItem<I extends Item & IAnimatable> extends AssemblyOperatorBlockItem implements IAnimatable
{
    private static final String CONTROLLER_NAME = "popupController";
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final int ANIM_OPEN = 0;
    private Supplier<Supplier<? extends GeoItemRenderer<?>>> renderer;

    public AnimatedBlockItem(Block pBlock, Properties properties, Supplier<Supplier<? extends GeoItemRenderer<?>>> renderer) {
        super(pBlock, properties.tab(BalancedFlight.CREATIVE_TAB));
        this.renderer = renderer;
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        // Not setting an animation here as that's handled below
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController controller = new AnimationController(this, CONTROLLER_NAME, 20, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer.get().get();
            }
        });
    }

}
