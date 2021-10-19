package com.vice.balancedflight.compat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.vice.balancedflight.blocks.FlightAnchorEntity;
import com.vice.balancedflight.items.FlightRing;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class CuriosCompat implements ICurio
{
    private final FlightRing parent;

    public CuriosCompat(FlightRing parent)
    {
        this.parent = parent;
    }

    public static void sendImc() {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("flight_ring").build());
    }

    public static ICapabilityProvider initCapabilities(FlightRing stack) {
        ICurio curio = new CuriosCompat(stack);

        return new ICapabilityProvider() {
            private final LazyOptional<ICurio> curioOpt = LazyOptional.of(() -> curio);

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
                                                     @Nullable Direction side) {

                return CuriosCapability.ITEM.orEmpty(cap, curioOpt);
            }
        };

    }


    @Override
    public boolean canEquipFromUse(SlotContext slotContext) {
        return true;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack) {
        //LivingEntity livingEntity = slotContext.getWearer();
        //if (livingEntity instanceof PlayerEntity) {
        //    PlayerEntity player = (PlayerEntity) livingEntity;
        //    startFlying(player);
        //}
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack) {
        LivingEntity livingEntity = slotContext.getWearer();
        if (livingEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity;
            stopFlying(player);
        }
    }

    private void startFlying(PlayerEntity player) {
        if (!player.isCreative() && !player.isSpectator()) {
            player.abilities.mayfly = true;
            player.onUpdateAbilities();
        }
    }

    private void stopFlying(PlayerEntity player) {
        if (!player.isCreative() && !player.isSpectator()) {
            player.abilities.flying = false;
            player.abilities.mayfly = false;
            player.onUpdateAbilities();
        }
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity) {
        if (!(livingEntity instanceof PlayerEntity))
            return;

        PlayerEntity player = ((PlayerEntity) livingEntity);

        if (parent.tier == FlightRing.FlightRingType.ASCENDED)
        {
            if (!player.abilities.mayfly) {
                startFlying(player);
            }
        }
        else if (parent.tier == FlightRing.FlightRingType.BASIC)
        {
            boolean CanFly = FlightAnchorEntity.ActiveAnchors
                .stream()
                .anyMatch(anchor -> distSqr(anchor.position, player.position()) < anchor.tier.EffectDistance * anchor.tier.EffectDistance);

            if (CanFly && !player.abilities.mayfly) {
                startFlying(player);
            }

            if (!CanFly && player.abilities.mayfly) {
                stopFlying(player);
                player.addEffect(new EffectInstance(Effects.SLOW_FALLING, 200));
            }
        }
    }

    public static boolean CanFly(PlayerEntity player) {

        boolean hasAscended = isRingInCuriosSlot(FlightRing.ASCENDED.get(), player);
        if (!hasAscended && !isRingInCuriosSlot(FlightRing.BASIC.get(), player))
            return false;

        if (hasAscended)
            return true;

        return FlightAnchorEntity.ActiveAnchors
                .stream()
                .anyMatch(anchor -> distSqr(anchor.position, player.position()) < anchor.tier.EffectDistance * anchor.tier.EffectDistance);
    }

    public static double distSqr(Vector3i vec, Vector3d other) {
        double d1 = (double)vec.getX() - other.x;
        double d3 = (double)vec.getZ() - other.z;
        return d1 * d1 + d3 * d3;
    }

    @Override
    public boolean canEquip(String identifier, LivingEntity entityLivingBase) {
        return !isRingInCuriosSlot(FlightRing.ASCENDED.get(), entityLivingBase) && !isRingInCuriosSlot(FlightRing.BASIC.get(), entityLivingBase);
    }

    public static boolean isRingInCuriosSlot(Item balancedflight, LivingEntity player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(balancedflight, player).isPresent();
    }

    @Override
    public void playRightClickEquipSound(LivingEntity entityLivingBase) {
        entityLivingBase.playSound(SoundEvents.ARMOR_EQUIP_ELYTRA,
                1.0F, 1.0F);
    }

    @Override
    public boolean showAttributesTooltip(String identifier) {
        return true;
    }

}
