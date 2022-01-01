package com.vice.balancedflight.proxy;


import net.minecraft.world.level.Level;

public class ServerProxy implements IProxy {
    @Override
    public Level getClientLevel() {
        throw new IllegalStateException("Only run this on the client!");
    }
}
