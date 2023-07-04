package com.vice.balancedflight;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.vice.balancedflight.content.flightAnchor.entity.FlightAnchorEntity;

public class AllBlockEntities
{
    public static final BlockEntityEntry<FlightAnchorEntity> FLIGHT_ANCHOR_ENTITY = FlightAnchorEntity.REGISTRATION;

    public static void init() {}
}
