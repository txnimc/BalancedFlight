package com.vice.balancedflight;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.NonNullLazyValue; 
import com.vice.balancedflight.config.Config;
import com.vice.balancedflight.proxy.ClientProxy;
import com.vice.balancedflight.proxy.IProxy;
import com.vice.balancedflight.proxy.ServerProxy;
import com.vice.balancedflight.util.ModItemTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("balancedflight")
public class BalancedFlight {
    public static final String MODID = "balancedflight";
    public static final Logger LOGGER = LogManager.getLogger();

    public static IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);


    private static final NonNullLazyValue<Registrate> REGISTRATE = new NonNullLazyValue<Registrate>(() -> {
        Registrate ret = Registrate.create(BalancedFlight.MODID).creativeModeTab(() -> ModItemTab.tab);
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
        Config.init();
        MinecraftForge.EVENT_BUS.register(this);
        Registry.init();
        Lang.init();
    }

}
