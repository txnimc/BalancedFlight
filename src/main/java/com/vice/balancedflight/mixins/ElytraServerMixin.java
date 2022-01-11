package com.vice.balancedflight.mixins;

import com.vice.balancedflight.compat.CuriosCompat;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.effect.MobEffects;
import com.vice.balancedflight.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ElytraServerMixin
{
    @Shadow
    public ServerPlayer player;

    @Inject(at = @At(value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/server/level/ServerPlayer;tryToStartFallFlying()Z"),
            method = "handlePlayerCommand", cancellable = true)

    private void startFallFlying(CallbackInfo ci)
    {
        if (!player.isFallFlying() && !player.isInWater() && !player.hasEffect(MobEffects.LEVITATION))
        {
            CuriosCompat.FlightMode allowed = CuriosCompat.AllowedFlightModes(player, true);
            if (!allowed.canElytraFly())
                return;

            player.startFallFlying();
            ci.cancel();
        }
    }
}
