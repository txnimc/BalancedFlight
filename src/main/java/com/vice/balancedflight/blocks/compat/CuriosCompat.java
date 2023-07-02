package com.vice.balancedflight.blocks.compat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.vice.balancedflight.blocks.FlightAnchorEntity;
import com.vice.balancedflight.config.Config;
import com.vice.balancedflight.items.FlightRing;
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
        FlightMode allowed = AllowedFlightModes(player, false);

        switch (allowed)
        {
            case None, Elytra -> {
                if (!player.isCreative() && player.getAbilities().mayfly)
                {
                    stopFlying(player);
                    // handle falling out of sky
                    player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200));
                }
            }
            case Creative, Both -> {

                if (!player.getAbilities().mayfly) {
                    startFlying(player);
                    // handle removing effect cleanly
                    if (player.hasEffect(MobEffects.SLOW_FALLING))
                       player.removeEffect(MobEffects.SLOW_FALLING);
                }
            }
        }
    }

    public static FlightMode AllowedFlightModes(Player player, boolean onlyCareAboutElytra)
    {
        boolean hasAscended = HasAscendedRing(player);
        boolean hasBasic = HasBasicRing(player);

        // return none if not wearing any rings
        if (!hasAscended && !hasBasic)
            return FlightMode.None;

        boolean CanElytraFly;
        boolean CanCreativeFly;

        if (hasAscended)
        {
            // fetch from config and look up allowed modes from truth table
            CanElytraFly = Config.ElytraAscended.get();
            CanCreativeFly = Config.CreativeAscended.get();
            FlightMode allowedModes = FlightMode.fromBools(CanElytraFly, CanCreativeFly);

            // if it's just creative, both, or neither, just return
            if (allowedModes != FlightMode.Elytra)
                return allowedModes;

            // if Elytra doesn't give unlimited creative flight,
            // check if Basic tier is allowed to fly.
            if (!CanCreativeFly && Config.CreativeBasic.get())
            {
                if (IsWithinFlightRange(player))
                    return FlightMode.fromBools(CanElytraFly, true);
            }

            return allowedModes;
        }

        // only has basic ring at this point
        CanElytraFly = Config.ElytraBasic.get();
        CanCreativeFly = Config.CreativeBasic.get();

        if (onlyCareAboutElytra && !CanElytraFly)
            return FlightMode.None;

        if (IsWithinFlightRange(player))
            return FlightMode.fromBools(CanElytraFly, CanCreativeFly);
        else
            return FlightMode.None;
    }



    public enum FlightMode {
        None,
        Elytra,
        Creative,
        Both;

        public static FlightMode fromBools(boolean ElytraAllowed, boolean CreativeAllowed) {
            if (ElytraAllowed && CreativeAllowed)
                return Both;

            if (ElytraAllowed)
                return Elytra;

            if (CreativeAllowed)
                return Creative;

            return None;
        }

        public boolean canElytraFly() {
            return this == Elytra || this == Both;
        }

        public boolean canCreativeFly() {
            return this == Creative || this == Both;
        }
    }

    private static boolean HasAnyRing(Player player) {
        return HasBasicRing(player) || HasAscendedRing(player);
    }

    private static boolean HasBasicRing(Player player) {
        return CuriosCompat.isRingInCuriosSlot(FlightRing.BASIC.get(), player);
    }

    private static boolean HasAscendedRing(Player player) {
        return CuriosCompat.isRingInCuriosSlot(FlightRing.ASCENDED.get(), player);
    }

    private static boolean IsWithinFlightRange(Player player)
    {
        if (player.level.dimension() != Level.OVERWORLD)
            return false;

        double anchorDistanceMultiplier = Config.anchorDistanceMultiplier.get();

        return FlightAnchorEntity.ActiveAnchors
                .entrySet()
                .stream()
                .anyMatch(anchor -> distSqr(anchor.getKey(), player.position()) < (anchorDistanceMultiplier * anchor.getValue().EffectDistance) * (anchorDistanceMultiplier * anchor.getValue().EffectDistance));
    }

    private static double distSqr(Vec3i vec, Vec3 other) {
        double d1 = (double)vec.getX() - other.x;
        double d3 = (double)vec.getZ() - other.z;
        return d1 * d1 + d3 * d3;
    }

    @Override
    public boolean canEquip(String identifier, LivingEntity entityLivingBase) {
        return !HasAnyRing((Player) entityLivingBase);
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
