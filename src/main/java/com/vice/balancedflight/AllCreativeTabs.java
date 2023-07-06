package com.vice.balancedflight;

import com.simibubi.create.Create;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AllCreativeTabs
{
    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Create.ID);

    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = TAB_REGISTER.register("base",
            () -> CreativeModeTab.builder()
                    .title(Component.literal("Create: Balanced Flight"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(AllBlocks.FLIGHT_ANCHOR::asStack)
                    .build());

}
