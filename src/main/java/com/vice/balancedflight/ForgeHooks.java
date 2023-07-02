package com.vice.balancedflight;

import com.vice.balancedflight.blocks.compat.CuriosCompat;
import com.vice.balancedflight.config.Config;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeHooks
{
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (Objects.equals(event.getSource().msgId, "flyIntoWall") && Config.disableElytraDamageWithRings.get()) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (CuriosCompat.AllowedFlightModes(player, true) != CuriosCompat.FlightMode.None)
                    event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (Objects.equals(event.getSource().msgId, "fall") && Config.disableFallDamageWithRings.get()) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (CuriosCompat.AllowedFlightModes(player, false) != CuriosCompat.FlightMode.None)
                    event.setCanceled(true);
            }
        }
    }
}
