package com.vice.balancedflight;

import com.vice.balancedflight.content.flightAnchor.entity.FlightAnchorEntity;
import com.vice.balancedflight.content.flightAnchor.FlightAnchorItem;
import com.vice.balancedflight.foundation.render.ConfiguredGeoModel;
import com.vice.balancedflight.foundation.render.KineticGeckoRenderInfo;
import com.vice.balancedflight.content.flightAnchor.render.*;

public class AllGeckoRenderers
{
    public static KineticGeckoRenderInfo<FlightAnchorEntity, ?> FlightAnchorGeckoRenderer =
            new KineticGeckoRenderInfo<FlightAnchorEntity, FlightAnchorItem>(
                    new ConfiguredGeoModel("flight_anchor"),
                    new FlightAnchorSafeRenderer(),
                    BalancedFlight.FLIGHT_ANCHOR_BLOCK.get().defaultBlockState(),
                    new FlightAnchorBeamRenderer());

}


