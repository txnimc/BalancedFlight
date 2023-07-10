package com.vice.balancedflight;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.vice.balancedflight.content.flightAnchor.FlightAnchorPonderScene;
import net.minecraft.resources.ResourceLocation;

public class AllPonderScenes
{
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(BalancedFlight.MODID);

    public static final PonderTag FLIGHT_ANCHOR_TAG = create("flight_anchor")
            .item(AllBlocks.FLIGHT_ANCHOR.get())
            .defaultLang("Flight Anchor", "Powered flight with Rotational Force")
            .addToIndex();

    public static void register() {

        HELPER.forComponents(AllBlocks.FLIGHT_ANCHOR)
                .addStoryBoard("flight_anchor", FlightAnchorPonderScene::ponderScene, FLIGHT_ANCHOR_TAG);

        PonderRegistry.TAGS.forTag(FLIGHT_ANCHOR_TAG)
                .add(AllBlocks.FLIGHT_ANCHOR);
    }

    private static PonderTag create(String id) {
        return new PonderTag(new ResourceLocation(BalancedFlight.MODID, id));
    }
}