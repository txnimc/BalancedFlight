package com.vice.balancedflight;

import com.vice.balancedflight.compat.CuriosCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyboardInput {
    public static final KeyBinding TAKE_OFF_KEY = new KeyBinding("key.take_off",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "key.category." + BalancedFlight.MODID);

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        if (TAKE_OFF_KEY.isDown()) {
            assert Minecraft.getInstance().player != null;
            ClientPlayerEntity player = Minecraft.getInstance().player;

            if (player.isOnGround() && !player.isFallFlying() && CuriosCompat.CanFly(player)) {
                Vector3d vector3d = player.getLookAngle();
                double d0 = 1.5D;
                double d1 = 0.1D;
                Vector3d vector3d1 = player.getDeltaMovement();
                player.setDeltaMovement(vector3d1.add(
                        vector3d.x * 0.1D + (vector3d.x * 1.5D - vector3d1.x) * 1.5D,
                        vector3d.y * 0.1D + (vector3d.y * 1.5D - vector3d1.y) * 1.5D,
                        vector3d.z * 0.1D + (vector3d.z * 1.5D - vector3d1.z) * 1.5D));
                player.startFallFlying();
                player.connection.send(new CEntityActionPacket(player, CEntityActionPacket.Action.START_FALL_FLYING));
            }
        }
    }
}