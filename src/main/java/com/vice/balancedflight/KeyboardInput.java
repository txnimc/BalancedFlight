package com.vice.balancedflight;

import com.mojang.blaze3d.platform.InputConstants;
import com.vice.balancedflight.blocks.compat.CuriosCompat;
import com.vice.balancedflight.config.Config;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;


@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyboardInput {
    public static final KeyMapping TAKE_OFF_KEY = new KeyMapping("key.take_off",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "key.category." + BalancedFlight.MODID);

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.Key event) {
        if (TAKE_OFF_KEY.isDown() && Config.enableTakeOff.get()) {
            assert Minecraft.getInstance().player != null;
            LocalPlayer player = Minecraft.getInstance().player;

            if (player.isOnGround() && !player.isFallFlying()) {

                if (!CuriosCompat.AllowedFlightModes(player, true).canElytraFly())
                    return;

                Vec3 vector3d = player.getLookAngle();
                double d0 = 1.5D;
                double d1 = 0.1D;
                Vec3 vector3d1 = player.getDeltaMovement();
                player.setDeltaMovement(vector3d1.add(
                        vector3d.x * 0.1D + (vector3d.x * 1.5D - vector3d1.x) * 1.5D,
                        vector3d.y * 0.1D + (vector3d.y * 1.5D - vector3d1.y) * 1.5D,
                        vector3d.z * 0.1D + (vector3d.z * 1.5D - vector3d1.z) * 1.5D));
                player.startFallFlying();
                player.connection.send(new ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
            }
        }
    }
}