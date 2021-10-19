package com.vice.balancedflight.util;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RecipeHelper
{
    public static BlockRecipe Block(Consumer<IFinishedRecipe> consumer, IItemProvider BlockItem)
    {
        return new BlockRecipe(consumer, BlockItem);
    }

    public static CustomRecipeBuilder Shaped(Consumer<IFinishedRecipe> consumer, IItemProvider BlockItem)
    {
        return new CustomRecipeBuilder(consumer, BlockItem);
    }
}


