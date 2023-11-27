package com.vice.balancedflight.foundation.data.recipe;

import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.simibubi.create.foundation.data.recipe.*;
import com.vice.balancedflight.BalancedFlight;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class BalancedFlightRecipeGen extends CreateRecipeProvider {
    GeneratedRecipe ASCENDED_FLIGHT_RING;
    GeneratedRecipe FLIGHT_ANCHOR;

    public BalancedFlightRecipeGen(PackOutput dataGenerator) {
        super(dataGenerator);

        ASCENDED_FLIGHT_RING = mechanicalCrafting(BalancedFlight.ASCENDED_FLIGHT_RING::get, 1, "", (b) -> b
                .key('G', Ingredient.of(Blocks.GOLD_BLOCK))
                .key('B', Ingredient.of(Blocks.NETHERITE_BLOCK))
                .key('S', Ingredient.of(Items.NETHERITE_INGOT))
                .key('N', Ingredient.of(Items.NETHER_STAR))
                .key('E', Ingredient.of(Items.ELYTRA))
                .patternLine(" GGGGG ")
                .patternLine("GGGNGGG")
                .patternLine("GGBSBGG")
                .patternLine("GNSESNG")
                .patternLine("GGBSBGG")
                .patternLine("GGGNGGG")
                .patternLine(" GGGGG ")
                .disallowMirrored());

        FLIGHT_ANCHOR = sequencedAssembly(BalancedFlight.FLIGHT_ANCHOR_BLOCK.getId(), (b) -> b
                .require(Blocks.BEACON)
                .transitionTo(Blocks.BEACON)
                .addOutput(BalancedFlight.FLIGHT_ANCHOR_BLOCK.get(), 100.0F)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, (rb) -> rb.require(com.simibubi.create.AllItems.PRECISION_MECHANISM.get()))
                .addStep(DeployerApplicationRecipe::new, (rb) -> rb.require(com.simibubi.create.AllBlocks.RAILWAY_CASING.get()))
                .addStep(DeployerApplicationRecipe::new, (rb) -> rb.require(com.simibubi.create.AllBlocks.BRASS_BLOCK.get()))
                .addStep(DeployerApplicationRecipe::new, (rb) -> rb.require(Blocks.GLASS))
                .addStep(DeployerApplicationRecipe::new, (rb) -> rb.require(Items.FEATHER)));
    }

    @Override
    public @NotNull String getName() {
        return "Balanced Flights's Crafting Recipes";
    }

    GeneratedRecipe mechanicalCrafting(Supplier<ItemLike> result, int amount, String suffix, UnaryOperator<MechanicalCraftingRecipeBuilder> builder) {
        return register(consumer -> {
            MechanicalCraftingRecipeBuilder b = builder.apply(MechanicalCraftingRecipeBuilder.shapedRecipe(result.get(), amount));
            ResourceLocation location = Create.asResource("mechanical_crafting/" + RegisteredObjects.getKeyOrThrow(result.get().asItem()).getPath() + suffix);
            b.build(consumer, location);
        });
    }

    protected GeneratedRecipe sequencedAssembly(ResourceLocation resource, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        GeneratedRecipe generatedRecipe = (c) -> {
            transform.apply(new SequencedAssemblyRecipeBuilder(resource)).build(c);
        };
        this.all.add(generatedRecipe);
        return generatedRecipe;
    }
}
