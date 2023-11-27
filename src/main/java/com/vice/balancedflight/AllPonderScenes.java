package com.vice.balancedflight;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.vice.balancedflight.content.flightAnchor.FlightAnchorPonderScene;
import net.minecraft.resources.ResourceLocation;

public class AllPonderScenes
{
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(BalancedFlight.MODID);

    public static final PonderTag FLIGHT_ANCHOR_TAG = create("flight_anchor")
            .item(BalancedFlight.FLIGHT_ANCHOR_BLOCK.get())
            .defaultLang("Flight Anchor", "Powered flight with Rotational Force")
            .addToIndex();

    public static void register() {

        HELPER.forComponents(BalancedFlight.FLIGHT_ANCHOR_BLOCK)
                .addStoryBoard("flight_anchor", FlightAnchorPonderScene::ponderScene, FLIGHT_ANCHOR_TAG);

        PonderRegistry.TAGS.forTag(FLIGHT_ANCHOR_TAG)
                .add(BalancedFlight.FLIGHT_ANCHOR_BLOCK);
    }

    private static PonderTag create(String id) {
        return new PonderTag(new ResourceLocation(BalancedFlight.MODID, id));
    }
}