package com.vice.balancedflight.content.angelRing;

import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.foundation.compat.AscendedRingCurio;
import com.vice.balancedflight.foundation.compat.ExternalMods;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = BalancedFlight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FlightRing extends Item {

    public FlightRing(Item.Properties props) { super(props); }

    @SubscribeEvent
    public static void sendImc(InterModEnqueueEvent event) {
        if (ExternalMods.CURIOS.isLoaded())
            AscendedRingCurio.sendImc();
    }

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, CompoundTag unused) {
        if (ExternalMods.CURIOS.isLoaded()) {
            return AscendedRingCurio.initCapabilities((FlightRing) stack.getItem());
        }
        return super.initCapabilities(stack, unused);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag p_41424_) {
        tooltip.add(Component.literal("An incredibly dense golden ring. Despite its weight, it allows you to fly anywhere (Angel Ring).").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("Allows both creative and enhanced Elytra flight.").withStyle(ChatFormatting.WHITE));
    }
}
