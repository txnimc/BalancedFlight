package com.vice.balancedflight.foundation.render;


import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.function.Function;
import java.util.function.Supplier;

public class GeckoRenderInfo<T extends BlockEntity & GeoAnimatable, I extends Item & GeoAnimatable, M extends GeoModel>
{
    public Function<BlockEntityRendererProvider.Context, GeoBlockRenderer<T>> TileRenderer;
    public Supplier<GeoItemRenderer<I>> ItemRenderer;

    public GeckoRenderInfo(M model)
    {
        ItemRenderer = () -> new GeoItemRenderer<I>(model) { };
        TileRenderer = (dispatch) -> new GeoBlockRenderer<T>(model)
        {
            @Override
            public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick)
            {
                return RenderType.entityTranslucent(getTextureLocation(animatable));
            }
        };
    }
}
