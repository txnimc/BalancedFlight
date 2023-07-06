package com.vice.balancedflight.foundation.render;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.function.Function;
import java.util.function.Supplier;

public class KineticGeckoRenderInfo<T extends KineticBlockEntity & GeoAnimatable, I extends Item & GeoAnimatable>
{
    public Function<BlockEntityRendererProvider.Context, GeckoCreateRenderer<T>> TileRenderer;
    public Supplier<GeoItemRenderer<I>> ItemRenderer;

    public KineticGeckoRenderInfo(ConfiguredGeoModel model, ICreateSafeRenderer createSafeRenderer, BlockState defaultBlockState)
    {
        this(model, createSafeRenderer, defaultBlockState, null);
    }

    public KineticGeckoRenderInfo(ConfiguredGeoModel model, ICreateSafeRenderer createSafeRenderer, BlockState defaultBlockState, BlockEntityRenderer<T> customRenderer)
    {
        ItemRenderer = () -> new GeoItemRenderer<I>(model)
        {
            @Override
            public void defaultRender(PoseStack poseStack, I animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight)
            {
                super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
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
