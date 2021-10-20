package com.vice.balancedflight;

import com.tterrag.registrate.Registrate;

public class Lang
{
    public static void init() {
        Registrate registrate = BalancedFlight.registrate();

        registrate.addLang("tooltip", Registry.BASIC_FLIGHT_ANCHOR.getId(), "Allows flight in a 25 block radius. Only works in the overworld.");
        registrate.addLang("tooltip", Registry.GILDED_FLIGHT_ANCHOR.getId(), "Allows flight in a 50 block radius. Only works in the overworld.");
        registrate.addLang("tooltip", Registry.ASCENDED_FLIGHT_ANCHOR.getId(), "Allows flight in a 100 block radius. Only works in the overworld.");

        registrate.addLang("tooltip", Registry.BASIC_FLIGHT_RING.getId(), "Allows flight around flight anchors. Only works in the overworld.");
        registrate.addLang("tooltip", Registry.ASCENDED_FLIGHT_RING.getId(), "An incredibly dense golden ring, and yet, it allows flight anywhere.");

        registrate.addLang("tooltip", Registry.FLIGHT_CORE.getId(), "Crafting item for the Ascended Flight Ring.");
        registrate.addLang("tooltip", Registry.ASCENDED_BLACKSTONE.getId(), "Crafting item for Flight Anchors and Flight Cores.");
    }
}
