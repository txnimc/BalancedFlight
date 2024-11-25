package com.vice.balancedflight.content.flightAnchor;

import com.vice.balancedflight.content.flightAnchor.entity.FlightAnchorEntity;
import com.vice.balancedflight.foundation.compat.AscendedRingCurio;
import com.vice.balancedflight.foundation.config.BalancedFlightConfig;
import net.minecraft.core.Vec3i;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FlightController
{
    public static void tick(Player player)
    {
        FlightMode allowed = AllowedFlightModes(player, false);

        switch (allowed)
        {
            case None, Elytra -> {
                if (player.isSurvival() && player.getAbilities().mayfly)
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

    public static void startFlying(Player player) {
        if (!player.isCreative() && !player.isSpectator()) {
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        }
    }

    public static void stopFlying(Player player) {
        if (!player.isCreative() && !player.isSpectator()) {
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
        }
    }

    public static FlightMode AllowedFlightModes(Player player, boolean onlyCareAboutElytra)
    {
        boolean hasAscended = AscendedRingCurio.HasAscendedRing(player);
        boolean CanElytraFly;
        boolean CanCreativeFly;

        if (hasAscended)
        {
            // fetch from config and look up allowed modes from truth table
            CanElytraFly = BalancedFlightConfig.ElytraAscended.get();
            CanCreativeFly = BalancedFlightConfig.CreativeAscended.get();
            FlightMode allowedModes = FlightMode.fromBools(CanElytraFly, CanCreativeFly);

            // if it's just creative, both, or neither, just return
            if (allowedModes != FlightMode.Elytra)
                return allowedModes;

            // if Elytra doesn't give unlimited creative flight,
            // check if Basic tier is allowed to fly.
            if (!CanCreativeFly && BalancedFlightConfig.CreativeAnchor.get())
            {
                if (IsWithinFlightRange(player))
                    return FlightMode.fromBools(CanElytraFly, true);
            }

            return allowedModes;
        }

        // only has basic ring at this point
        CanElytraFly = BalancedFlightConfig.ElytraAnchor.get();
        CanCreativeFly = BalancedFlightConfig.CreativeAnchor.get();

        if (onlyCareAboutElytra && !CanElytraFly)
            return FlightMode.None;

        if (IsWithinFlightRange(player))
            return FlightMode.fromBools(CanElytraFly, CanCreativeFly);
        else
            return FlightMode.None;
    }

    private static boolean IsWithinFlightRange(Player player)
    {
        if (player.level.dimension() != Level.OVERWORLD)
            return false;

        double anchorDistanceMultiplier = BalancedFlightConfig.anchorDistanceMultiplier.get();

        return FlightAnchorEntity.ActiveAnchors
                .entrySet()
                .stream()
                .anyMatch(anchor -> distSqr(anchor.getKey(), player.position()) < (anchorDistanceMultiplier * anchor.getValue().getSpeed()) * (anchorDistanceMultiplier * anchor.getValue().getSpeed()));
    }

    private static double distSqr(Vec3i vec, Vec3 other) {
        double d1 = (double)vec.getX() - other.x;
        double d3 = (double)vec.getZ() - other.z;
        return d1 * d1 + d3 * d3;
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
}
