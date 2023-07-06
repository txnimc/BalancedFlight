package com.vice.balancedflight.foundation.util;


import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class BlockRecipe
{
    Consumer<FinishedRecipe> consumer;
    ItemLike BlockItem;

    public BlockRecipe(Consumer<FinishedRecipe> consumer, ItemLike BlockItem)
    {
        this.consumer = consumer;
        this.BlockItem = BlockItem;
    }

    public void MadeFrom(ItemLike IngotItem)
    {
        // block recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockItem)
                .pattern("xxx")
                .pattern("xxx")
                .pattern("xxx")
                .define('x', IngotItem)
                .unlockedBy(IngotItem.toString(), InventoryChangeTrigger.TriggerInstance.hasItems(IngotItem))
                .save(consumer);

        // ingot recipe
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IngotItem, 9)
                .requires(BlockItem, 1)
                .unlockedBy(IngotItem.toString(), InventoryChangeTrigger.TriggerInstance.hasItems(BlockItem))
                .save(consumer);
    }
}
