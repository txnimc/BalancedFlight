package com.vice.balancedflight.mixins;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerEntity.class)
public interface ConnectionAccessor
{
    @Accessor
    public abstract ClientPlayNetHandler getConnection();
}