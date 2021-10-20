package com.vice.balancedflight.mixins;

import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.compat.CuriosCompat;
import com.vice.balancedflight.items.FlightRing;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.potion.Effects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@Mixin(ClientPlayerEntity.class)
public class ElytraMixin
{
    @Inject(at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/player/ClientPlayerEntity;getItemBySlot(Lnet/minecraft/inventory/EquipmentSlotType;)Lnet/minecraft/item/ItemStack;"),
            method = "aiStep()V", cancellable = true)

    private void tryToStartFallFlying(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if (CuriosCompat.CanFly(player))
        {
            if (!player.isOnGround() && !player.isFallFlying() && !player.isInWater() && !player.hasEffect(Effects.LEVITATION))
            {
                player.startFallFlying();
                player.connection.send(new CEntityActionPacket(player, CEntityActionPacket.Action.START_FALL_FLYING));
                ci.cancel();
            }
        }
    }
}

