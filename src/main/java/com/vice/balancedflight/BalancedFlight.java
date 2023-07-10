package com.vice.balancedflight;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.tterrag.registrate.providers.ProviderType;
import com.vice.balancedflight.foundation.config.BalancedFlightConfig;
import com.vice.balancedflight.foundation.data.recipe.BalancedFlightRecipeGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
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

    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(BalancedFlight.MODID) {
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(AllBlocks.FLIGHT_ANCHOR.get());
        }
    };

    public BalancedFlight() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        CREATE_REGISTRATE.registerEventListeners(modEventBus);

        BalancedFlightConfig.init();
        MinecraftForge.EVENT_BUS.register(this);

        AllBlocks.init();
        AllBlockEntities.init();
        AllItems.init();
        AllLangMessages.init();

        modEventBus.addListener(EventPriority.LOWEST, BalancedFlight::gatherData);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> BalancedFlightClient.onCtorClient(modEventBus, forgeEventBus));
    }

    public static void gatherData(GatherDataEvent event) {
        CREATE_REGISTRATE.addDataGenerator(ProviderType.LANG, prov -> {
            prov.add(CREATIVE_TAB, "Create: Balanced Flight");
            prov.add("curios.identifier.flight_ring", "Flight Ring");
        });

        DataGenerator gen = event.getGenerator();
        if (event.includeServer()) {
            gen.addProvider(new BalancedFlightRecipeGen(gen));
        }

        AllPonderScenes.register();
        PonderLocalization.provideRegistrateLang(CREATE_REGISTRATE);
    }

    static {
        CREATE_REGISTRATE.setTooltipModifierFactory((item) -> {
            return (new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)).andThen(TooltipModifier.mapNull(KineticStats.create(item)));
        });
    }
}
