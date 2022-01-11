package com.vice.balancedflight.mixins;

import com.vice.balancedflight.compat.CuriosCompat;
import com.vice.balancedflight.config.Config;
import com.vice.balancedflight.items.FlightRing;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.potion.Effects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetHandler.class)
public class ElytraServerMixin
{
    @Shadow public ServerPlayerEntity player;

    @Inject(at = @At(value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/entity/player/ServerPlayerEntity;tryToStartFallFlying()Z"),
            method = "handlePlayerCommand", cancellable = true)

    private void startFallFlying(CallbackInfo ci)
    {
        if (!player.isFallFlying() && !player.isInWater() && !player.hasEffect(Effects.LEVITATION))
        {
            CuriosCompat.FlightMode allowed = CuriosCompat.AllowedFlightModes(player, true);
            if (!allowed.canElytraFly())
                return;

            player.startFallFlying();
            ci.cancel();
        }
    }
}
