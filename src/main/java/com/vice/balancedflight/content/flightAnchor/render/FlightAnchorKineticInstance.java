package com.vice.balancedflight.content.flightAnchor.render;

import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.utility.Iterate;
import com.vice.balancedflight.content.flightAnchor.entity.FlightAnchorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.EnumMap;
import java.util.Map;

public class FlightAnchorKineticInstance extends KineticBlockEntityInstance<FlightAnchorEntity> implements DynamicInstance
{
    protected final EnumMap<Direction, RotatingData> keys = new EnumMap(Direction.class);
    protected Direction sourceFacing;

    public FlightAnchorKineticInstance(MaterialManager materialManager, FlightAnchorEntity blockEntity) {
        super(materialManager, blockEntity);

        Direction.Axis boxAxis = this.blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis();
        this.updateSourceFacing();

        Material<RotatingData> rotatingMaterial = this.getRotatingMaterial();

        for (Direction direction : Iterate.directions)
        {
            Direction.Axis axis = direction.getAxis();
            if (boxAxis != axis && axis != Direction.Axis.Y)
            {
                Instancer<RotatingData> shaft = rotatingMaterial.getModel(AllPartialModels.SHAFT_HALF, this.blockState, direction);
                RotatingData key = shaft.createInstance();
                key.setRotationAxis(Direction.get(Direction.AxisDirection.NEGATIVE, axis).step())
                        .setRotationalSpeed(blockEntity.getSpeed())
                        .setRotationOffset(this.getRotationOffset(axis))
                        .setColor(blockEntity)
                        .setPosition(new Vector3f(getInstancePosition().getX(), getInstancePosition().getY() - 0.5f, getInstancePosition().getZ()))
                        .setBlockLight(this.world.getBrightness(LightLayer.BLOCK, this.pos))
                        .setSkyLight(this.world.getBrightness(LightLayer.SKY, this.pos));

                this.keys.put(direction, key);
            }
        }
    }

    @Override
    public void beginFrame()
    {
        var time = AnimationTickHolder.getRenderTime();

        var placedTime = blockEntity.placedRenderTime;
        if (time - placedTime > 40f || time - placedTime < 25f)
            return;

        placedTime = placedTime + 25f;

        var pos = getInstancePosition();
        var scale = Mth.clampedLerp(0.01F, 1F, Mth.clamp(time - placedTime, 0f, 5f) / 5f) / 2;
        var posF = new Vector3f(pos.getX(), pos.getY() + scale - 0.5f, pos.getZ());

        for (var kvp : keys.values())
        {
            kvp.setPosition(posF);
        }
    }

    protected void updateSourceFacing() {
        if (this.blockEntity.hasSource()) {
            BlockPos source = this.blockEntity.source.subtract(this.pos);
            this.sourceFacing = Direction.getNearest((float)source.getX(), (float)source.getY(), (float)source.getZ());
        }
        else {
            this.sourceFacing = null;
        }
    }

    public void update() {
        this.updateSourceFacing();
        for (Map.Entry<Direction, RotatingData> kvp : this.keys.entrySet())
        {
            this.updateRotation(kvp.getValue(), kvp.getKey().getAxis(), blockEntity.getSpeed());
        }
    }

    public void updateLight() {
        this.relight(this.pos, this.keys.values().stream());
    }

    public void remove() {
        this.keys.values().forEach(InstanceData::delete);
        this.keys.clear();
    }
}