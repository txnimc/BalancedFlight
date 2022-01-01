package com.vice.balancedflight.mixins;

import com.vice.balancedflight.compat.CuriosCompat;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class ElytraUpdateMixin
{

    @Inject(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"),
            method = "updateFallFlying", cancellable = true)

    private void updateFallFlying(CallbackInfo ci)
    {
        LivingEntity player = (LivingEntity) (Object) this;

        if (player.getClass() != ServerPlayer.class)
            return;

        if (CuriosCompat.CanFly((ServerPlayer) player))
        {
            ci.cancel();
        }
    }
}
