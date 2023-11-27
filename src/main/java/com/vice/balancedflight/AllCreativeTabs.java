package com.vice.balancedflight;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AllCreativeTabs
{
    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BalancedFlight.MODID);

    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = TAB_REGISTER.register("base",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.balancedflight.base"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(BalancedFlight.FLIGHT_ANCHOR_BLOCK::asStack)
                    .displayItems(((pParameters, pOutput) -> {
                        for (var item : BalancedFlight.CREATE_REGISTRATE.getAll(Registries.ITEM))
                            pOutput.accept(new ItemStack(item.get()));

                        for (var block : BalancedFlight.CREATE_REGISTRATE.getAll(Registries.BLOCK))
                            pOutput.accept(new ItemStack(block.get()));
                    }))
                    .build());

    public static void register(IEventBus modEventBus) {
        TAB_REGISTER.register(modEventBus);
    }
}
