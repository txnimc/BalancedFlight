package com.vice.balancedflight.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

class FlightAnchorItem extends BlockItem
{
    FlightAnchor.AnchorTier tier;

    public FlightAnchorItem(FlightAnchor.AnchorTier tier, Block p_i48527_1_, Properties p_i48527_2_)
    {
        super(p_i48527_1_, p_i48527_2_);
        this.tier = tier;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag p_41424_)
    {
        {
            tooltip.add(Component.literal("Allows flight in a " + (int) tier.EffectDistance + " block distance.").withStyle(ChatFormatting.WHITE));
            tooltip.add(Component.literal("Only works in the overworld.").withStyle(ChatFormatting.RED));
        }
    }
}
