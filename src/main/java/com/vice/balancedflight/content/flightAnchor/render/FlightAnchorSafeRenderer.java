package com.vice.balancedflight.content.flightAnchor.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Iterate;
import com.vice.balancedflight.content.flightAnchor.entity.FlightAnchorEntity;
import com.vice.balancedflight.foundation.render.ICreateSafeRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nullable;


public class FlightAnchorSafeRenderer implements ICreateSafeRenderer
{
    @Override
    public void renderCreate(@Nullable KineticBlockEntity te, @Nullable BlockPos pos, BlockState blockState, PoseStack ms, MultiBufferSource buffer, int light)
    {
        final Direction.Axis boxAxis = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis();
        float time = AnimationTickHolder.getRenderTime(te == null ? Minecraft.getInstance().level : te.getLevel());

        float placedTime = te == null ? 0f : ((FlightAnchorEntity)te).placedRenderTime;
        if (time - placedTime < 25f)
            return;
        placedTime = placedTime + 25f;

        for (Direction direction : Iterate.directions) {
            final Direction.Axis axis = direction.getAxis();
            if (boxAxis == axis || direction == Direction.DOWN || direction == Direction.UP)
                continue;

            var speed = te == null ? 32 : te.getSpeed();
            float angle = (time * speed * 3f / 10) % 360;

            float offset = te == null ? 0f : KineticBlockEntityRenderer.getRotationOffsetForPosition(te, pos, axis);
            angle += offset;
            angle = angle / 180f * (float) Math.PI;

            SuperByteBuffer shaft = CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, blockState, direction);

            if (te != null) {
                KineticBlockEntityRenderer.kineticRotationTransform(shaft, te, axis, angle, light);
                ms.pushPose();
                var scale = Mth.clampedLerp(0.01F, 1F, Mth.clamp(time - placedTime, 0f, 5f) / 5f);

                if (axis == Direction.Axis.X)
                {
                    ms.translate((1 - scale) * 0.5F, 0, 0);
                    ms.scale(scale, 1F, 1F);
                }
                else
                {
                    ms.translate(0, 0, (1 - scale) * 0.5F);
                    ms.scale(1F, 1F, scale);
                }
            }
            else {
                shaft.light(light);
                shaft.rotateCentered(Direction.get(Direction.AxisDirection.POSITIVE, axis), angle);
                shaft.color(Color.WHITE);

                ms.pushPose();
                ms.translate(0, 0.5D, 0);
            }

            shaft.renderInto(ms, buffer.getBuffer(RenderType.solid()));
            ms.popPose();
        }
    }




}