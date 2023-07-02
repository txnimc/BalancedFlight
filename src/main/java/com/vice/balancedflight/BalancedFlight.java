package com.vice.balancedflight;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import com.vice.balancedflight.config.Config;
import com.vice.balancedflight.proxy.ClientProxy;
import com.vice.balancedflight.proxy.IProxy;
import com.vice.balancedflight.proxy.ServerProxy;
import com.vice.balancedflight.util.ModItemTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("balancedflight")
public class BalancedFlight {
    public static final String MODID = "balancedflight";
    public static final Logger LOGGER = LogManager.getLogger();

    public static IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    private static final BalancedFlightRegistrate REGISTRATE = BalancedFlightRegistrate.create(BalancedFlight.MODID);
    public static BalancedFlightRegistrate registrate() {
        return REGISTRATE;
    }

    public BalancedFlight() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);

        Config.init();
        MinecraftForge.EVENT_BUS.register(this);
        Registry.init();
        Lang.init();

        modEventBus.addListener(EventPriority.LOWEST, BalancedFlight::gatherData);
    }

    public static void gatherData(GatherDataEvent event) {
        REGISTRATE.creativeModeTab(() -> ModItemTab.tab);
        REGISTRATE.addDataGenerator(ProviderType.LANG, prov -> {
            prov.add(ModItemTab.tab, "Balanced Flight");
            prov.add("curios.identifier.flight_ring", "Flight Ring");
        });
    }

}
