package com.vice.balancedflight;

import com.mojang.blaze3d.platform.InputConstants;
import com.vice.balancedflight.foundation.events.InputEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.KeyboardInput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AllKeybinds
{
    public static final KeyMapping TAKE_OFF_KEY = new KeyMapping("key.take_off",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "key.category." + BalancedFlight.MODID);

    @SubscribeEvent
    public static void onClientSetup(RegisterKeyMappingsEvent event) {
        event.register(TAKE_OFF_KEY);
    }
}