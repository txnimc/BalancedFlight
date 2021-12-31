package com.vice.balancedflight;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.vice.balancedflight.compat.ExternalMods;
import com.vice.balancedflight.config.BalancedFlightConfig;
import com.vice.balancedflight.data.BalancedFlightSaveData;
import com.vice.balancedflight.proxy.ClientProxy;
import com.vice.balancedflight.proxy.IProxy;
import com.vice.balancedflight.proxy.ServerProxy;
import com.vice.balancedflight.items.FlightRing;
import com.vice.balancedflight.util.ModItemTab;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

@Mod("balancedflight")
public class BalancedFlight {
    public static final String MODID = "balancedflight";
    public static final Logger LOGGER = LogManager.getLogger();

    public static IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);


    private static final NonNullLazyValue<Registrate> REGISTRATE = new NonNullLazyValue<Registrate>(() -> {
        Registrate ret = Registrate.create(BalancedFlight.MODID).itemGroup(() -> ModItemTab.tab);
        ret.addDataGenerator(ProviderType.LANG, prov -> {
            prov.add(ModItemTab.tab, "Balanced Flight");
            prov.add("curios.identifier.flight_ring", "Flight Ring");
        });
        return ret;
    });

    public static Registrate registrate() {
        return REGISTRATE.get();
    }

    public BalancedFlight() {
        BalancedFlightConfig.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        Registry.init();

        Lang.init();
    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        Objects.requireNonNull(event.getServer().getLevel(World.OVERWORLD))
                .getChunkSource()
                .getDataStorage()
                .computeIfAbsent(BalancedFlightSaveData::new, BalancedFlightSaveData.ID);
    }

}
