package com.vice.balancedflight.foundation.util;
 

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class RecipeHelper
{
    public static BlockRecipe Block(Consumer<FinishedRecipe> consumer, ItemLike BlockItem)
    {
        return new BlockRecipe(consumer, BlockItem);
    }

    public static CustomRecipeBuilder Shaped(Consumer<FinishedRecipe> consumer, ItemLike BlockItem)
    {
        return new CustomRecipeBuilder(consumer, BlockItem);
    }
}


