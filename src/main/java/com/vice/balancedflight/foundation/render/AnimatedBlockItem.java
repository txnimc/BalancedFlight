package com.vice.balancedflight.foundation.render;


import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.vice.balancedflight.BalancedFlight;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AnimatedBlockItem<I extends Item & GeoAnimatable> extends AssemblyOperatorBlockItem implements GeoAnimatable
{
    private static final String CONTROLLER_NAME = "popupController";
    public AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final int ANIM_OPEN = 0;
    private Supplier<Supplier<? extends GeoItemRenderer<?>>> renderer;

    public AnimatedBlockItem(Block pBlock, Properties properties, Supplier<Supplier<? extends GeoItemRenderer<?>>> renderer) {
        super(pBlock, properties);
        this.renderer = renderer;
    }

    private <P extends Item & GeoAnimatable> PlayState predicate(AnimationState<P> event) {
        // Not setting an animation here as that's handled below
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        AnimationController controller = new AnimationController(this, CONTROLLER_NAME, 20, this::predicate);
        data.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache()
    {
        return factory;
    }

    @Override
    public double getTick(Object o)
    {
        return 0;
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
