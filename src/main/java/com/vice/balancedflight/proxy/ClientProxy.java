package com.vice.balancedflight.proxy;


import net.minecraft.world.level.Level;

public class ClientProxy implements IProxy {
    @Override
    public Level getClientLevel() {
        // Not required anymore.
        return null;
    }
}
