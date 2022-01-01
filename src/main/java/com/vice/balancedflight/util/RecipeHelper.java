package com.vice.balancedflight.util;
 

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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


