package com.vice.balancedflight.items;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.LazyRegistryEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.Registry;
import com.vice.balancedflight.compat.CuriosCompat;
import com.vice.balancedflight.compat.ExternalMods;
import com.vice.balancedflight.util.ModItemTab;
import com.vice.balancedflight.util.RecipeHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = BalancedFlight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FlightRing extends Item {

    public enum FlightRingType implements StringRepresentable
    {
        BASIC("basic"),
        ASCENDED("ascended");

        //region Helpers
        private final String name;

        FlightRingType(String name)
        {
            this.name = name;
        }

        @Override
        public String getSerializedName()
        {
            return this.name;
        }
        public String getResourceName()
        {
            return this.name + "_flight_ring";
        }
        //endregion
    }

    public FlightRing(Item.Properties props, FlightRingType tier)
    {
        super(props);
        this.tier = tier;
    }

    public final FlightRingType tier;

    //region Registry
    //private static final LazyValue<ItemLike> FlightCoreItem = new LazyValue(FlightCore.REGISTRATION::get);

    public static final RegistryEntry<? extends Item> ASCENDED = RegCommon(FlightRingType.ASCENDED)
            .recipe((gen, prov) ->
                RecipeHelper.Shaped(prov, gen.get())
                        .Row(Registry.FLIGHT_CORE.get(), Items.GOLD_BLOCK, Registry.FLIGHT_CORE.get())
                        .Row(Items.GOLD_BLOCK, Items.ELYTRA, Items.GOLD_BLOCK)
                        .Row(Registry.FLIGHT_CORE.get(), Items.GOLD_BLOCK, Registry.FLIGHT_CORE.get())
                        .UnlockedBy(Items.FEATHER)
                        .Save())
            .register();


    public static final RegistryEntry<? extends Item> BASIC = RegCommon(FlightRingType.BASIC)
            .recipe((gen, prov) ->
                    RecipeHelper.Shaped(prov, gen.get())
                            .Row(Items.IRON_INGOT, Items.QUARTZ_BLOCK, Items.IRON_INGOT)
                            .Row(Items.QUARTZ_BLOCK, Items.FEATHER, Items.QUARTZ_BLOCK)
                            .Row(Items.IRON_INGOT, Items.QUARTZ_BLOCK, Items.IRON_INGOT)
                            .UnlockedBy(Items.FEATHER)
                            .Save())
            .register();


    public static final ItemBuilder<FlightRing, Registrate> RegCommon(FlightRingType ringtier)
    {
        return BalancedFlight.registrate()
                .item(ringtier.getResourceName(), p -> new FlightRing(p, ringtier))
                .initialProperties(() -> new Item.Properties().stacksTo(1).tab(ModItemTab.tab));
    }
    //endregion

    @SubscribeEvent
    public static void sendImc(InterModEnqueueEvent event) {
        if (ExternalMods.CURIOS.isLoaded())
            CuriosCompat.sendImc();
    }

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, CompoundTag unused) {
        if (ExternalMods.CURIOS.isLoaded()) {
            return CuriosCompat.initCapabilities((FlightRing) stack.getItem());
        }
        return super.initCapabilities(stack, unused);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag p_41424_) {

        if (((FlightRing)stack.getItem()).tier == FlightRingType.ASCENDED)
        {
            tooltip.add(new StringTextComponent("An incredibly dense golden ring. Despite its weight, it allows you to fly anywhere (Angel Ring).").withStyle(TextFormatting.GOLD));
            tooltip.add(new StringTextComponent("Allows both creative and enhanced Elytra flight.").withStyle(TextFormatting.WHITE));
        }
        else {
            tooltip.add(new StringTextComponent("Cheap ring that allows flight around flight anchors (Angel Ring).").withStyle(TextFormatting.WHITE));
            tooltip.add(new StringTextComponent("Allows both creative and enhanced Elytra flight.").withStyle(TextFormatting.WHITE));
            tooltip.add(new StringTextComponent("Only works in the overworld.").withStyle(TextFormatting.RED));
        }
    }


}
