package com.vice.balancedflight.foundation.render;

import com.vice.balancedflight.BalancedFlight;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ConfiguredAnimatedGeoModel extends AnimatedGeoModel
{
    private String name;

    public ConfiguredAnimatedGeoModel(String name) {

        this.name = name;
    }

    @Override
    public ResourceLocation getModelLocation(Object object)
    {
        return new ResourceLocation(BalancedFlight.MODID, "geo/" + name + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object object)
    {
        return new ResourceLocation(BalancedFlight.MODID, "textures/block/" + name + ".png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object object)
    {
        return new ResourceLocation(BalancedFlight.MODID, "animations/" + name + ".animation.json");
    }
}