package com.vice.balancedflight.proxy;

import net.minecraft.world.World;

public class ClientProxy implements IProxy {
    @Override
    public World getClientWorld() {
        // Not required anymore.
        return null;
    }
}
