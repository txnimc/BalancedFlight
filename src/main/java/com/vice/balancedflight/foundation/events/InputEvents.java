package com.vice.balancedflight.foundation.events;

import com.vice.balancedflight.AllKeybinds;
import com.vice.balancedflight.content.flightAnchor.FlightController;
import com.vice.balancedflight.foundation.config.BalancedFlightConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class InputEvents {

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.Key event) {
        if (AllKeybinds.TAKE_OFF_KEY.isDown() && BalancedFlightConfig.enableTakeOff.get()) {
            assert Minecraft.getInstance().player != null;
            LocalPlayer player = Minecraft.getInstance().player;

            if (player.onGround() && !player.isFallFlying()) {

                if (!FlightController.AllowedFlightModes(player, true).canElytraFly())
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