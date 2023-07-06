package com.vice.balancedflight.foundation.render;

import com.vice.balancedflight.BalancedFlight;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class ConfiguredGeoModel extends GeoModel
{
    private String name;

    public ConfiguredGeoModel(String name) {

        this.name = name;
    }

    @Override
    public ResourceLocation getModelResource(GeoAnimatable object)
    {
        return new ResourceLocation(BalancedFlight.MODID, "geo/" + name + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeoAnimatable object)
    {
        return new ResourceLocation(BalancedFlight.MODID, "textures/block/" + name + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeoAnimatable object)
    {
        return new ResourceLocation(BalancedFlight.MODID, "animations/" + name + ".animation.json");
    }
}