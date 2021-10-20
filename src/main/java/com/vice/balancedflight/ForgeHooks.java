package com.vice.balancedflight;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeHooks
{
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (Objects.equals(event.getSource().msgId, "flyIntoWall")) {
            event.setCanceled(true);
        }
    }
}
