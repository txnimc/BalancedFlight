package com.vice.balancedflight.foundation.render;


import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import java.util.function.Function;
import java.util.function.Supplier;

public class KineticGeckoRenderInfo<T extends KineticBlockEntity & IAnimatable, I extends Item & IAnimatable>
{
    public Function<BlockEntityRendererProvider.Context, GeckoCreateRenderer<T>> TileRenderer;
    public Supplier<GeoItemRenderer<I>> ItemRenderer;

    public KineticGeckoRenderInfo(ConfiguredAnimatedGeoModel model, ICreateSafeRenderer createSafeRenderer, BlockState defaultBlockState)
    {
        this(model, createSafeRenderer, defaultBlockState, null);
    }

    public KineticGeckoRenderInfo(ConfiguredAnimatedGeoModel model, ICreateSafeRenderer createSafeRenderer, BlockState defaultBlockState, BlockEntityRenderer<T> customRenderer)
    {
        ItemRenderer = () -> new GeoItemRenderer<I>(model)
        {
            @Override
            public void render(I animatable, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack)
            {
                super.render(animatable, poseStack, bufferSource, packedLight, stack);
                createSafeRenderer.renderCreate(null, null, defaultBlockState, poseStack, bufferSource, packedLight);
            }
        };
        TileRenderer = (ctx) -> new GeckoCreateRenderer<T>(ctx, model, customRenderer) {
            @Override
            public void renderCreate(@Nullable KineticBlockEntity te, @Nullable BlockPos pos, BlockState blockState, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay)
            {
                createSafeRenderer.renderCreate(te, pos, blockState, ms, buffer, light);
            }
        };
    }
}
