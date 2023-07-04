package com.vice.balancedflight.foundation.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public interface ICreateSafeRenderer
{
    public void renderCreate(@Nullable KineticBlockEntity te, @Nullable BlockPos pos, BlockState blockState, PoseStack ms, MultiBufferSource buffer, int light);
}
