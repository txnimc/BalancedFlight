package com.vice.balancedflight.foundation.render;


import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.vice.balancedflight.BalancedFlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.annotation.Nullable;


public class GeckoCreateRenderer<T extends KineticBlockEntity & IAnimatable> extends KineticBlockEntityRenderer implements IGeoRenderer<T>
{
    static {
        AnimationController.addModelFetcher(GeckoCreateRenderer::fetchModel);
    }

    private final BlockEntityRenderer<T> customRenderer;
    protected MultiBufferSource rtb = null;
    private final AnimatedGeoModel<T> modelProvider;

    private static IAnimatableModel<Object> fetchModel(IAnimatable object)
    {
        if (object instanceof BlockEntity tile)
        {
            BlockEntityRenderDispatcher renderManager = Minecraft.getInstance().getBlockEntityRenderDispatcher();
            var renderer = renderManager.getRenderer(tile);

            if (renderer instanceof GeckoCreateRenderer geoRenderer)
            {
                var animatedModel = geoRenderer.getGeoModelProvider();
                return (IAnimatableModel<Object>) animatedModel;
            }
        }

        return null;
    }



    public GeckoCreateRenderer(BlockEntityRendererProvider.Context context, AnimatedGeoModel<T> modelProvider, BlockEntityRenderer<T> customRenderer) {
        super(context);
        this.modelProvider = modelProvider;
        this.customRenderer = customRenderer;
    }

    @Override
    public void render(GeoModel model, T animatable, float partialTick, RenderType type, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        IGeoRenderer.super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        customRenderer.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    protected void renderSafe(KineticBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay)
    {
        this.renderGecko((T) te, partialTicks, ms, buffer, light);
        if (Backend.canUseInstancing(te.getLevel()))
            return;

        renderCreate(te, te.getBlockPos(), te.getBlockState(), partialTicks, ms, buffer, light, overlay);
    }

    public void renderCreate(@Nullable KineticBlockEntity te, @Nullable BlockPos pos, BlockState blockState, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay)
    {

    }

    public void renderGecko(T tile, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        GeoModel model = modelProvider.getModel(modelProvider.getModelResource(tile));
        modelProvider.setCustomAnimations(tile, this.getInstanceId(tile));
        stack.pushPose();
        stack.translate(0, 0.01f, 0);
        stack.translate(0.5, 0, 0.5);

        rotateBlock(getFacing(tile), stack);

        Minecraft.getInstance().textureManager.bindForSetup(getTextureLocation(tile));
        Color renderColor = getRenderColor(tile, partialTicks, stack, bufferIn, null, packedLightIn);
        RenderType renderType = getRenderType(tile, partialTicks, stack, bufferIn, null, packedLightIn, getTextureLocation(tile));

        render(model, tile, partialTicks, renderType, stack, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY,
                (float) renderColor.getRed() / 255f, (float) renderColor.getGreen() / 255f,
                (float) renderColor.getBlue() / 255f, (float) renderColor.getAlpha() / 255);
        stack.popPose();
    }

    @Override
    public void setCurrentRTB(MultiBufferSource bufferSource) {
        this.rtb = bufferSource;
    }

    @Override
    public MultiBufferSource getCurrentRTB() {
        return this.rtb;
    }

    @Override
    public AnimatedGeoModel<T> getGeoModelProvider() {
        return this.modelProvider;
    }

    protected void rotateBlock(Direction facing, PoseStack stack) {
        switch (facing) {
            case SOUTH:
                stack.mulPose(Vector3f.YP.rotationDegrees(180));
                break;
            case WEST:
                stack.mulPose(Vector3f.YP.rotationDegrees(90));
                break;
            case NORTH:
                stack.mulPose(Vector3f.YP.rotationDegrees(0));
                break;
            case EAST:
                stack.mulPose(Vector3f.YP.rotationDegrees(270));
                break;
            case UP:
                stack.mulPose(Vector3f.XP.rotationDegrees(90));
                break;
            case DOWN:
                stack.mulPose(Vector3f.XN.rotationDegrees(90));
                break;
        }
    }

    private Direction getFacing(T tile) {
        BlockState blockState = tile.getBlockState();
        if (blockState.hasProperty(HorizontalDirectionalBlock.FACING)) {
            return blockState.getValue(HorizontalDirectionalBlock.FACING);
        } else if (blockState.hasProperty(DirectionalBlock.FACING)) {
            return blockState.getValue(DirectionalBlock.FACING);
        } else {
            return Direction.NORTH;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(T instance) {
        return this.modelProvider.getTextureResource(instance);
    }

}
