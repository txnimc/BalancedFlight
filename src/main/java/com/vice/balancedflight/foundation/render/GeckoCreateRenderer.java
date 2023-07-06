package com.vice.balancedflight.foundation.render;


import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.texture.AnimatableTexture;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.event.GeoRenderEvent;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.RenderUtils;

import javax.annotation.Nullable;


public class GeckoCreateRenderer<T extends KineticBlockEntity & GeoAnimatable> extends KineticBlockEntityRenderer implements GeoRenderer<T>
{
    public static Vector3f XN = new Vector3f(-1.0F, 0.0F, 0.0F);
    public static Vector3f XP = new Vector3f(1.0F, 0.0F, 0.0F);
    public static Vector3f YN = new Vector3f(0.0F, -1.0F, 0.0F);
    public static Vector3f YP = new Vector3f(0.0F, 1.0F, 0.0F);
    public static Vector3f ZN = new Vector3f(0.0F, 0.0F, -1.0F);
    public static Vector3f ZP = new Vector3f(0.0F, 0.0F, 1.0F);
    public static Vector3f ZERO = new Vector3f(0.0F, 0.0F, 0.0F);

    private final BlockEntityRenderer<T> customRenderer;
    private final GeoModel<T> modelProvider;
    protected T animatable;
    protected Matrix4f blockRenderTranslations = new Matrix4f();
    protected Matrix4f modelRenderTranslations = new Matrix4f();

//    private GeoModel<T> fetchModel(GeoAnimatable object)
//    {
//        if (object instanceof BlockEntity tile)
//        {
//            BlockEntityRenderDispatcher renderManager = Minecraft.getInstance().getBlockEntityRenderDispatcher();
//            var renderer = renderManager.getRenderer(tile);
//
//            if (renderer instanceof GeckoCreateRenderer geoRenderer)
//            {
//                var animatedModel = geoRenderer.getGeoModelProvider();
//                return (GeoModel<T>) animatedModel;
//            }
//        }
//
//        return null;
//    }



    public GeckoCreateRenderer(BlockEntityRendererProvider.Context context, GeoModel<T> modelProvider, BlockEntityRenderer<T> customRenderer) {
        super(context);
        this.modelProvider = modelProvider;
        this.customRenderer = customRenderer;
    }


    @Override
    public void defaultRender(PoseStack poseStack, T animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        this.animatable = animatable;
        GeoRenderer.super.defaultRender(poseStack, animatable, bufferSource, renderType,buffer, yaw, partialTick, packedLight);

        poseStack.pushPose();
        poseStack.translate(0, 0.01f, 0);
        poseStack.translate(0.5, 0, 0.5);
        customRenderer.render(animatable, partialTick, poseStack, bufferSource, packedLight, getPackedOverlay(animatable, 0.0F));
        poseStack.popPose();
    }

    @Override
    protected void renderSafe(BlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1)
    {
        renderSafe((KineticBlockEntity) blockEntity, v, poseStack, multiBufferSource, i, i1);
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
        //BakedGeoModel model = modelProvider.getBakedModel(modelProvider.getModelResource(tile));
        //modelProvider.setCustomAnimations(tile, this.getInstanceId(tile), new AnimationState<>());
//        stack.pushPose();
//        stack.translate(0, 0.01f, 0);
//        stack.translate(0.5, 0, 0.5);
//
//        rotateBlock(getFacing(tile), stack);

        //Minecraft.getInstance().textureManager.bindForSetup(getTextureLocation(tile));
        //Color renderColor = getRenderColor(tile, partialTicks, packedLightIn);
        //RenderType renderType = getRenderType(tile, getTextureLocation(tile), bufferIn, partialTicks);

        defaultRender(stack, tile, bufferIn, null, null, 0, partialTicks, packedLightIn);
//        render(model, tile, partialTicks, renderType, stack, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY,
//                (float) renderColor.getRed() / 255f, (float) renderColor.getGreen() / 255f,
//                (float) renderColor.getBlue() / 255f, (float) renderColor.getAlpha() / 255);

        //stack.popPose();
    }

    @Override
    public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();

        if (!isReRender) {
            AnimationState<T> animationState = new AnimationState<T>(animatable, 0, 0, partialTick, false);
            long instanceId = getInstanceId(animatable);

            animationState.setData(DataTickets.TICK, animatable.getTick(animatable));
            animationState.setData(DataTickets.BLOCK_ENTITY, animatable);
            this.modelProvider.addAdditionalStateData(animatable, instanceId, animationState::setData);
            poseStack.translate(0, 0.01f, 0);
            poseStack.translate(0.5, 0, 0.5);
            rotateBlock(getFacing(animatable), poseStack);
            this.modelProvider.handleAnimations(animatable, instanceId, animationState);
        }

        this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());

        GeoRenderer.super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick,
                packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    /**
     * Renders the provided {@link GeoBone} and its associated child bones
     */
    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(poseStack.last().pose());
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.blockRenderTranslations);
            Matrix4f worldState = new Matrix4f(localMatrix);
            BlockPos pos = this.animatable.getBlockPos();

            bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            bone.setLocalSpaceMatrix(localMatrix);
            bone.setWorldSpaceMatrix(worldState.translate(new Vector3f(pos.getX(), pos.getY(), pos.getZ())));
        }

        GeoRenderer.super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue,
                alpha);
    }

    protected void rotateBlock(Direction facing, PoseStack stack) {
        switch (facing)
        {
            case SOUTH -> stack.mulPose(Axis.of(YP).rotationDegrees(180));
            case WEST -> stack.mulPose(Axis.of(YP).rotationDegrees(90));
            case NORTH -> stack.mulPose(Axis.of(YP).rotationDegrees(0));
            case EAST -> stack.mulPose(Axis.of(YP).rotationDegrees(270));
            case UP -> stack.mulPose(Axis.of(XP).rotationDegrees(90));
            case DOWN -> stack.mulPose(Axis.of(XN).rotationDegrees(90));
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
    public GeoModel<T> getGeoModel() { return modelProvider; }

    @Override
    public T getAnimatable() { return animatable; }

    @Override
    public ResourceLocation getTextureLocation(T instance) {
        return this.modelProvider.getTextureResource(instance);
    }

    public void updateAnimatedTextureFrame(T animatable) {
        AnimatableTexture.setAndUpdate(this.getTextureLocation(animatable), animatable.getBlockPos().getX() + animatable.getBlockPos().getY() + animatable.getBlockPos().getZ() + (int)(animatable).getTick(animatable));
    }

    public void fireCompileRenderLayersEvent() {}
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {return true;}
    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {}
}
