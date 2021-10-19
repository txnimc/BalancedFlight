package com.vice.balancedflight.items;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.LazyRegistryEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.compat.CuriosCompat;
import com.vice.balancedflight.compat.ExternalMods;
import com.vice.balancedflight.util.ModItemTab;
import com.vice.balancedflight.util.RecipeHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.LazyValue;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = BalancedFlight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FlightRing extends Item {

    public enum FlightRingType implements IStringSerializable
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
    //private static final LazyValue<IItemProvider> FlightCoreItem = new LazyValue(FlightCore.REGISTRATION::get);

    public static final RegistryEntry<? extends Item> ASCENDED = RegCommon(FlightRingType.ASCENDED)
            .recipe((gen, prov) ->
                RecipeHelper.Shaped(prov, gen.get())
                        .Row(FlightCore.REGISTRATION.get(), Items.GOLD_BLOCK, FlightCore.REGISTRATION.get())
                        .Row(Items.GOLD_BLOCK, Items.ELYTRA, Items.GOLD_BLOCK)
                        .Row(FlightCore.REGISTRATION.get(), Items.GOLD_BLOCK, FlightCore.REGISTRATION.get())
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
    public ICapabilityProvider initCapabilities(final ItemStack stack, CompoundNBT unused) {
        if (ExternalMods.CURIOS.isLoaded()) {
            return CuriosCompat.initCapabilities((FlightRing) stack.getItem());
        }
        return super.initCapabilities(stack, unused);
    }


}
