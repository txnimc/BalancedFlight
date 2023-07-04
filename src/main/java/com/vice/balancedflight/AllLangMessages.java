package com.vice.balancedflight;

public class AllLangMessages
{
    public static void init() {
        com.simibubi.create.foundation.data.CreateRegistrate registrate = BalancedFlight.registrate();

        registrate.addLang("tooltip", AllBlocks.FLIGHT_ANCHOR.getId(), "Allows flight in a 25 block radius. Only works in the overworld.");
        registrate.addLang("tooltip", AllItems.ASCENDED_FLIGHT_RING.getId(), "An incredibly dense golden ring, and yet, it allows flight anywhere.");

        registrate.addLang("tooltip", AllItems.FLIGHT_CORE.getId(), "Crafting item for the Ascended Flight Ring.");
        registrate.addLang("tooltip", AllBlocks.ASCENDED_BLACKSTONE.getId(), "Crafting item for Flight Cores.");
    }
}
