package com.elevensef.createautotrack;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreateAutoTrack.MOD_ID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChangeState extends Block {
    public ChangeState(Properties p_49795_) {
        super(p_49795_);
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();

        if (!level.isClientSide()){
            if (player.isShiftKeyDown() && event.getHand() == InteractionHand.MAIN_HAND && player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.AIR)){
                if (level.getBlockState(pos).is(Register.BUILDER_BLOCK_B.get())){
                    if (level.getBlockState(pos) == Register.BUILDER_BLOCK_B.get().defaultBlockState().setValue(BuilderBlock.AXIS, States.X)){
                        level.setBlock(pos,Register.BUILDER_BLOCK_B.get().defaultBlockState().setValue(BuilderBlock.AXIS,States.Z),3);
                    }
                    else {
                        level.setBlock(pos,Register.BUILDER_BLOCK_B.get().defaultBlockState().setValue(BuilderBlock.AXIS,States.X),3);
                    }
                }
            }
        }
    }
}
