package com.vice.balancedflight.foundation;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import lombok.val;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

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
        });

        return itemBuilder;
    }
}
