package com.vice.balancedflight.foundation.render;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import java.util.function.Function;
import java.util.function.Supplier;

public class GeckoRenderInfo<T extends BlockEntity & IAnimatable, I extends Item & IAnimatable, M extends AnimatedGeoModel>
{
    public Function<BlockEntityRendererProvider.Context, GeoBlockRenderer<T>> TileRenderer;
    public Supplier<GeoItemRenderer<I>> ItemRenderer;

    public GeckoRenderInfo(M model)
    {
        ItemRenderer = () -> new GeoItemRenderer<I>(model) { };
        TileRenderer = (dispatch) -> new GeoBlockRenderer<T>(dispatch, model)
        {
            @Override
            public RenderType getRenderType(T animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, ResourceLocation texture)
            {
                return RenderType.entityTranslucent(getTextureLocation(animatable));
            }
        };
    }
}
