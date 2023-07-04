package com.vice.balancedflight;

import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.providers.ProviderType;
import com.vice.balancedflight.foundation.config.Config;
import com.vice.balancedflight.foundation.util.ModItemTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("balancedflight")
public class BalancedFlight {
    public static final String MODID = "balancedflight";
    public static final Logger LOGGER = LogManager.getLogger();

    //private static final BalancedFlightRegistrate REGISTRATE = BalancedFlightRegistrate.create(BalancedFlight.MODID);
    private static final com.simibubi.create.foundation.data.CreateRegistrate CREATE_REGISTRATE = com.simibubi.create.foundation.data.CreateRegistrate.create(BalancedFlight.MODID);

    public static com.simibubi.create.foundation.data.CreateRegistrate registrate() {
        return CREATE_REGISTRATE;
    }
    public static com.simibubi.create.foundation.data.CreateRegistrate createRegistrate() {
        return CREATE_REGISTRATE;
    }

    public BalancedFlight() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        //REGISTRATE.registerEventListeners(modEventBus);
        CREATE_REGISTRATE.registerEventListeners(modEventBus);

        Config.init();
        MinecraftForge.EVENT_BUS.register(this);

        AllBlocks.init();
        AllBlockEntities.init();
        AllItems.init();
        AllLangMessages.init();

        modEventBus.addListener(EventPriority.LOWEST, BalancedFlight::gatherData);
    }

    public static void gatherData(GatherDataEvent event) {
        CREATE_REGISTRATE.addDataGenerator(ProviderType.LANG, prov -> {
            prov.add(ModItemTab.tab, "Balanced Flight");
            prov.add("curios.identifier.flight_ring", "Flight Ring");
        });
    }

    static {
        CREATE_REGISTRATE.setTooltipModifierFactory((item) -> {
            return (new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)).andThen(TooltipModifier.mapNull(KineticStats.create(item)));
        });
    }
}
