package com.vice.balancedflight.compat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.vice.balancedflight.blocks.FlightAnchorEntity;
import com.vice.balancedflight.items.FlightRing;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
    public ItemStack getStack()
    {
        return new ItemStack(parent);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack) {
        //LivingEntity livingEntity = slotContext.getWearer();
        //if (livingEntity instanceof Player) {
        //    Player player = (Player) livingEntity;
        //    startFlying(player);
        //}
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack) {
        LivingEntity livingEntity = slotContext.getWearer();
        if (livingEntity instanceof Player) {
            Player player = (Player) livingEntity;
            stopFlying(player);
        }
    }

    private void startFlying(Player player) {
        if (!player.isCreative() && !player.isSpectator()) {
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        }
    }

    private void stopFlying(Player player) {
        if (!player.isCreative() && !player.isSpectator()) {
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
        }
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity) {
        if (!(livingEntity instanceof Player))
            return;

        Player player = ((Player) livingEntity);

        if (parent.tier == FlightRing.FlightRingType.ASCENDED)
        {
            if (!player.getAbilities().mayfly) {
                startFlying(player);
            }
        }
        else if (parent.tier == FlightRing.FlightRingType.BASIC)
        {
            if (player.level.dimension() != Level.OVERWORLD)
                return;

            boolean CanFly = FlightAnchorEntity.ActiveAnchors
                .stream()
                .anyMatch(anchor -> distSqr(anchor.position, player.position()) < anchor.tier.EffectDistance * anchor.tier.EffectDistance);

            if (CanFly && !player.getAbilities().mayfly) {
                startFlying(player);
            }

            if (!CanFly && player.getAbilities().mayfly) {
                stopFlying(player);
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200));
            }
        }
    }

    public static boolean CanFly(Player player) {

        boolean hasAscended = isRingInCuriosSlot(FlightRing.ASCENDED.get(), player);
        if (!hasAscended && !isRingInCuriosSlot(FlightRing.BASIC.get(), player))
            return false;

        if (hasAscended)
            return true;

        if (player.level.dimension() != Level.OVERWORLD)
            return false;

        return FlightAnchorEntity.ActiveAnchors
                .stream()
                .anyMatch(anchor -> distSqr(anchor.position, player.position()) < anchor.tier.EffectDistance * anchor.tier.EffectDistance);
    }

    public static double distSqr(Vec3i vec, Vec3 other) {
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
