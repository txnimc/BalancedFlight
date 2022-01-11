package com.vice.balancedflight.compat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.vice.balancedflight.blocks.FlightAnchorEntity;
import com.vice.balancedflight.config.Config;
import com.vice.balancedflight.items.FlightRing;
import com.vice.balancedflight.mixins.ConnectionAccessor;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
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
        FlightMode allowed = AllowedFlightModes(player, false);

        if (allowed == FlightMode.None || allowed == FlightMode.Elytra) {
            if (!player.isCreative() && player.abilities.mayfly)
            {
                stopFlying(player);
                // handle falling out of sky
                player.addEffect(new EffectInstance(Effects.SLOW_FALLING, 200));
            }
        }
        else {
            if (!player.abilities.mayfly) {
                startFlying(player);
                // handle removing effect cleanly
                if (player.hasEffect(Effects.SLOW_FALLING))
                    player.removeEffect(Effects.SLOW_FALLING);
            }
        }
    }

    public static FlightMode AllowedFlightModes(PlayerEntity player, boolean onlyCareAboutElytra)
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

    private static boolean HasAnyRing(PlayerEntity player) {
        return HasBasicRing(player) || HasAscendedRing(player);
    }

    private static boolean HasBasicRing(PlayerEntity player) {
        return CuriosCompat.isRingInCuriosSlot(FlightRing.BASIC.get(), player);
    }

    private static boolean HasAscendedRing(PlayerEntity player) {
        return CuriosCompat.isRingInCuriosSlot(FlightRing.ASCENDED.get(), player);
    }

    private static boolean IsWithinFlightRange(PlayerEntity player)
    {
        if (player.level.dimension() != World.OVERWORLD)
            return false;

        double anchorDistanceMultiplier = Config.anchorDistanceMultiplier.get();

        return FlightAnchorEntity.ActiveAnchors
                .stream()
                .anyMatch(anchor -> distSqr(anchor.position, player.position()) < (anchorDistanceMultiplier * anchor.tier.EffectDistance) * (anchorDistanceMultiplier * anchor.tier.EffectDistance));
    }

    private static double distSqr(Vector3i vec, Vector3d other) {
        double d1 = (double)vec.getX() - other.x;
        double d3 = (double)vec.getZ() - other.z;
        return d1 * d1 + d3 * d3;
    }

    @Override
    public boolean canEquip(String identifier, LivingEntity entityLivingBase) {
        return !HasAnyRing((PlayerEntity) entityLivingBase);
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
