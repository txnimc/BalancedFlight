package com.vice.balancedflight.mixins;


import com.vice.balancedflight.compat.CuriosCompat;
import com.vice.balancedflight.config.Config;
import com.vice.balancedflight.network.CustomNetworkMessage;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;

@Mixin(LocalPlayer.class)
public class ElytraRocketShiftKeyMixin
{
    private long LastUsedFireworkTime = 0;

    @Inject(at = @At(value = "HEAD"), method = "tick")
    private void setShiftKeyDown(CallbackInfo ci)
    {
        LocalPlayer player = (LocalPlayer) (Object) this;

        if (player.isFallFlying() && Config.infiniteRockets.get())
        {
            CuriosCompat.FlightMode allowed = CuriosCompat.AllowedFlightModes(player, true);
            if (!allowed.canElytraFly())
                return;

            if (player.isSprinting() && player.input.hasForwardImpulse())
            {
                Level world = player.level;

                long now = Instant.now().getEpochSecond();
                if (now - LastUsedFireworkTime > 1)
                {
                    CustomNetworkMessage.Send(world, player, "FIRE_ROCKET");
                    LastUsedFireworkTime = now;
                }
            }

        }
    }
}


