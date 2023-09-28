package com.vice.balancedflight.foundation.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class CustomNetworkMessage
{
    private final UUID player;
    private final String message;

    public CustomNetworkMessage(FriendlyByteBuf buffer)
    {
        player = buffer.readUUID();
        message = buffer.readUtf(Short.MAX_VALUE);
    }

    public CustomNetworkMessage(UUID player, String message)
    {
        this.player = player;
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf)
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

    public static void Send(Level world, Player player, String message) {
        if (world.isClientSide) {
            BalancedFlightNetwork.INSTANCE.sendToServer(new CustomNetworkMessage(player.getGameProfile().getId(), message));
        }
        if (!world.isClientSide) {
            BalancedFlightNetwork.INSTANCE.send(
                    PacketDistributor.PLAYER.with(
                            () -> (ServerPlayer) player
                    ),
                    new CustomNetworkMessage(player.getGameProfile().getId(), "FIRE_ROCKET"));
        }
    }


    private static void FireRocket(UUID uuid)
    {
        Player player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);

        if (player == null)
            return;

        ItemStack itemstack = new ItemStack(Items.FIREWORK_ROCKET, 64);
        player.level().addFreshEntity(new FireworkRocketEntity(player.level(), itemstack, player));
    }
}
