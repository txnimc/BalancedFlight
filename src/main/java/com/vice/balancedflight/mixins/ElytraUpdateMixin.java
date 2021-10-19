package com.vice.balancedflight.mixins;

import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.compat.CuriosCompat;
import com.vice.balancedflight.items.FlightRing;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.potion.Effects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class ElytraUpdateMixin
{

    @Inject(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;getItemBySlot(Lnet/minecraft/inventory/EquipmentSlotType;)Lnet/minecraft/item/ItemStack;"),
            method = "updateFallFlying", cancellable = true)

    private void updateFallFlying(CallbackInfo ci)
    {
        LivingEntity player = (LivingEntity) (Object) this;

        if (player.getClass() != ServerPlayerEntity.class)
            return;

        if (CuriosCompat.CanFly((ServerPlayerEntity) player))
        {
            ci.cancel();
        }
    }
}
