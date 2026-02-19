package com.elevensef.createautotrack;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

public class BuilderBlockItem extends BlockItem {
    public BuilderBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(
                Component.literal("\n")
                        .append(Component.translatable("tooltip.create_auto_track.1"))
                        .append("\n")
                        .append(Component.translatable("tooltip.create_auto_track.2"))
                        .append("\n")
                        .append(Component.translatable("tooltip.create_auto_track.3"))
                        .append("\n")
                        .append(Component.translatable("tooltip.create_auto_track.4"))
                        .append("\n")
                        .append(Component.translatable("tooltip.create_auto_track.5"))
                        .append("\n\n")
                        .append(Component.translatable("tooltip.create_auto_track.6"))
                        .append("\n")
                        .append(Component.translatable("tooltip.create_auto_track.7"))
                        .append("\n")
                        .append(Component.translatable("tooltip.create_auto_track.8"))
                        .withStyle(ChatFormatting.DARK_GRAY));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
