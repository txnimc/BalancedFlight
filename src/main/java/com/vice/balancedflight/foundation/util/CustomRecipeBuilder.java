package com.vice.balancedflight.foundation.util;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CustomRecipeBuilder
{
    Consumer<FinishedRecipe> consumer;
    ItemLike BlockItem;

    private final HashMap<ItemLike, Character> definitionLookup = new HashMap<>();
    private final List<List<ItemLike>> Rows = new ArrayList<>();

    private ItemLike UnlockedBy;

    public CustomRecipeBuilder(Consumer<FinishedRecipe> consumer, ItemLike BlockItem)
    {
        this.consumer = consumer;
        this.BlockItem = BlockItem;
    }

    public CustomRecipeBuilder UnlockedBy(ItemLike unlockedBy)
    {
        UnlockedBy = unlockedBy;
        return this;
    }

    public CustomRecipeBuilder Row(@Nullable ItemLike item1, @Nullable ItemLike item2, @Nullable ItemLike item3)
    {
        ArrayList<ItemLike> row = new ArrayList<>(3);
        Rows.add(row);

        AddItem(row, item1);
        AddItem(row, item2);
        AddItem(row, item3);

        return this;
    }

    public CustomRecipeBuilder Row(@Nullable ItemLike item1, @Nullable ItemLike item2)
    {
        ArrayList<ItemLike> row = new ArrayList<>(2);
        Rows.add(row);

        AddItem(row, item1);
        AddItem(row, item2);

        return this;
    }

    public CustomRecipeBuilder Row(@Nullable ItemLike item1)
    {
        ArrayList<ItemLike> row = new ArrayList<>(1);
        Rows.add(row);

        AddItem(row, item1);

        return this;
    }

    private void AddItem(ArrayList<ItemLike> row, ItemLike item)
    {
        row.add(item);

        if (item != null)
        {
            if (!definitionLookup.containsKey(item))
            {
                boolean flag = true;
                while (flag)
                {
                    char letter = RandomLetter();
                    if (!definitionLookup.containsValue(letter))
                    {
                        flag = false;
                        definitionLookup.put(item, letter);
                    }
                }
            }
        }
    }

    public void Save()
    {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockItem);

        for (List<ItemLike> row : Rows)
        {
            String pattern = row
                    .stream()
                    .map(i -> (i == null) ? " " : definitionLookup.get(i).toString())
                    .collect(Collectors.joining());

            builder.pattern(pattern);
        }

        for (HashMap.Entry<ItemLike, Character> definition : definitionLookup.entrySet())
        {
            builder.define(definition.getValue(), definition.getKey());
        }

        builder.unlockedBy(UnlockedBy.toString(), InventoryChangeTrigger.TriggerInstance.hasItems(UnlockedBy));
        builder.save(consumer);
    }

    private final Random r = new Random();
    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private char RandomLetter()
    {
        return alphabet.charAt(r.nextInt(alphabet.length()));
    }

}
