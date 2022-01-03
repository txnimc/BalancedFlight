package com.vice.balancedflight.mixins;

import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.compat.CuriosCompat;
import com.vice.balancedflight.config.BalancedFlightConfig;
import com.vice.balancedflight.network.BalancedFlightNetwork;
import com.vice.balancedflight.network.CustomNetworkMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Mixin(ClientPlayerEntity.class)
public class ElytraRocketShiftKeyMixin
{
    private long LastUsedFireworkTime = 0;

    @Inject(at = @At(value = "HEAD"), method = "tick")
    private void setShiftKeyDown(CallbackInfo ci)
    {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;

        if (player.isFallFlying() && BalancedFlightConfig.infiniteRockets.get() && CuriosCompat.HasRingAllowingElytraFlight(player))
        {
            if (player.isSprinting() && player.input.hasForwardImpulse())
            {
                World world = player.level;

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


