package com.vice.balancedflight;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.providers.ProviderType;
import com.vice.balancedflight.foundation.config.BalancedFlightConfig;
import com.vice.balancedflight.foundation.data.recipe.BalancedFlightRecipeGen;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod("balancedflight")
public class BalancedFlight {
    public static final String MODID = "balancedflight";
    public static final Logger LOGGER = LogManager.getLogger();

    private static final CreateRegistrate CREATE_REGISTRATE = com.simibubi.create.foundation.data.CreateRegistrate.create(BalancedFlight.MODID);
    public static CreateRegistrate registrate() {
        return CREATE_REGISTRATE;
    }



    public BalancedFlight() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        //REGISTRATE.registerEventListeners(modEventBus);
        CREATE_REGISTRATE.registerEventListeners(modEventBus);

        BalancedFlightConfig.init();
        MinecraftForge.EVENT_BUS.register(this);

        AllBlocks.init();
        AllBlockEntities.init();
        AllItems.init();
        AllLangMessages.init();

        modEventBus.addListener(EventPriority.LOWEST, BalancedFlight::gatherData);
    }

    public static void gatherData(GatherDataEvent event) {
        CREATE_REGISTRATE.addDataGenerator(ProviderType.LANG, prov -> {
            prov.add(AllCreativeTabs.CREATIVE_TAB.get(), "Create: Balanced Flight");
            prov.add("curios.identifier.flight_ring", "Flight Ring");
        });

        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        if (event.includeServer()) {
            gen.addProvider(true, new BalancedFlightRecipeGen(output));
        }
    }

    static {
        CREATE_REGISTRATE.setTooltipModifierFactory((item) -> {
            return (new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)).andThen(TooltipModifier.mapNull(KineticStats.create(item)));
        });
    }
}
