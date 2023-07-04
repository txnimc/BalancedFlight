package com.vice.balancedflight;

import com.simibubi.create.foundation.data.CreateRegistrate;

public class AllLangMessages
{
    public static void init() {
        CreateRegistrate registrate = BalancedFlight.registrate();

        registrate.addLang("tooltip", AllBlocks.FLIGHT_ANCHOR.getId(), "Allows flight in a 25 block radius. Only works in the overworld.");
        registrate.addLang("tooltip", AllItems.ASCENDED_FLIGHT_RING.getId(), "An incredibly dense golden ring, and yet, it allows flight anywhere.");
    }
}
