package com.vice.balancedflight.foundation;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import lombok.val;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import java.lang.reflect.Field;

public class RegistrateExtensions
{
    public static <T extends Block> ItemBuilder<? extends BlockItem, BlockBuilder<T, com.simibubi.create.foundation.data.CreateRegistrate>> geckoItem(
            BlockBuilder<T, com.simibubi.create.foundation.data.CreateRegistrate> builder,
            NonNullBiFunction<? super T, Item.Properties, ? extends BlockItem> factory)
    {
        val itemBuilder = builder
                .getOwner()
                .item(builder, builder.getName(), (p) -> (BlockItem) factory.apply(builder.getEntry(), p));

        itemBuilder.setData(ProviderType.LANG, NonNullBiConsumer.noop());

        itemBuilder.model((ctx, prov) -> {
            var b = prov.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
//
//            Field privateField;
//            try
//            {
//                privateField = ModelBuilder.class.getDeclaredField("textures");
//                privateField.setAccessible(true);
//
//                var textures = (java.util.Map<String, String>) privateField.get(b);
//                textures.put("1_0", "create:block/axis");
//                textures.put("1_1", "create:block/axis_top");
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
////            "faces": {
////                "north": {"uv": [6, 6, 10, 10], "rotation": 180, "texture": "#1_1"},
////                "east": {"uv": [6, 0, 10, 16], "rotation": 90, "texture": "#1_0"},
////                "south": {"uv": [6, 6, 10, 10], "texture": "#1_1"},
////                "west": {"uv": [6, 0, 10, 16], "rotation": 270, "texture": "#1_0"},
////                "up": {"uv": [6, 0, 10, 16], "texture": "#1_0"},
////                "down": {"uv": [6, 0, 10, 16], "rotation": 180, "texture": "#1_0"}
////            }
//
//            b.element()
//                    .from(6, 6, 0)
//                    .to(10, 10, 16)
//                    .face(Direction.NORTH)
//                        .uvs(6, 6, 10, 10)
//                        .rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN)
//                        .texture("#1_1")
//                        .end()
//                    .face(Direction.EAST)
//                        .uvs(6, 0, 10, 16)
//                        .rotation(ModelBuilder.FaceRotation.CLOCKWISE_90)
//                        .texture("#1_0")
//                        .end()
//                    .face(Direction.SOUTH)
//                        .uvs(6, 6, 10, 10)
//                        .texture("#1_1")
//                        .end()
//                    .face(Direction.WEST)
//                        .uvs(6, 0, 10, 16)
//                        .rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90)
//                        .texture("#1_0")
//                        .end()
//                    .face(Direction.UP)
//                        .uvs(6, 0, 10, 16)
//                        .texture("#1_0")
//                        .end()
//                    .face(Direction.DOWN)
//                        .uvs(6, 0, 10, 16)
//                        .rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN)
//                        .texture("#1_0")
//                        .end()
//                    .end()

                    .transforms()

                    .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                    .rotation(75, 45, 0)
                    .scale(0.375f, 0.375f, 0.375f)
                    .translation(0, 2.5f, 0)
                    .end()
                    .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                    .rotation(75, 45, 0)
                    .scale(0.375f, 0.375f, 0.375f)
                    .translation(0, 2.5f, 0)
                    .end()
                    .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                    .rotation(0, 225, 0)
                    .scale(0.4f, 0.4f, 0.4f)
                    .end()
                    .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                    .rotation(0, 115, 0)
                    .scale(0.4f, 0.4f, 0.4f)
                    .end()
                    .transform(ItemDisplayContext.GROUND)
                    .translation(0, 3, 0)
                    .scale(0.25f, 0.25f, 0.25f)
                    .end()
                    .transform(ItemDisplayContext.GUI)
                    .rotation(30, 137, 0)
                    .translation(0, -3.75f, 0)
                    .scale(0.625f, 0.625f, 0.625f)
                    .end()
                    .transform(ItemDisplayContext.FIXED)
                    .translation(0, -1.5f, 0)
                    .scale(0.5f, 0.5f, 0.5f)
                    .end()
                    .end();
        });

        return itemBuilder;
    }
}
