package com.vice.balancedflight.network;

import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.mixins.ElytraRocketShiftKeyMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class CustomNetworkMessage
{
    private final UUID player;
    private final String message;

    public CustomNetworkMessage(PacketBuffer buffer)
    {
        player = buffer.readUUID();
        message = buffer.readUtf(Short.MAX_VALUE);
    }

    public CustomNetworkMessage(UUID player, String message)
    {
        this.player = player;
        this.message = message;
    }

    public void toBytes(PacketBuffer buf)
    {
        buf.writeUUID(player);
        buf.writeUtf(this.message);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            if ("FIRE_ROCKET".equals(this.message))
            {
                FireRocket(this.player);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void Send(World world, PlayerEntity player, String message) {
        if (world.isClientSide) {
            BalancedFlightNetwork.INSTANCE.sendToServer(new CustomNetworkMessage(player.getGameProfile().getId(), message));
        }
        if (!world.isClientSide) {
            BalancedFlightNetwork.INSTANCE.send(
                    PacketDistributor.PLAYER.with(
                            () -> (ServerPlayerEntity) player
                    ),
                    new CustomNetworkMessage(player.getGameProfile().getId(), "FIRE_ROCKET"));
        }
    }


    private static void FireRocket(UUID uuid)
    {
        PlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);

        if (player == null)
            return;

        ItemStack itemstack = new ItemStack(Items.FIREWORK_ROCKET, 64);
        player.level.addFreshEntity(new FireworkRocketEntity(player.level, itemstack, player));
    }
}
