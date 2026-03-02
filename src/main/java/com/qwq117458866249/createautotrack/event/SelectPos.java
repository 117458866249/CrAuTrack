package com.qwq117458866249.createautotrack.event;


import com.qwq117458866249.createautotrack.CreateAutoTrack;
import com.qwq117458866249.createautotrack.register.Register;
import com.simibubi.create.AllSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreateAutoTrack.MOD_ID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SelectPos extends Block {
    public SelectPos(Properties pProperties) {
        super(pProperties);
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack item = player.getItemInHand(hand);
        BlockState state = level.getBlockState(pos);

        if (!level.isClientSide) {
            if (event.getHand() == InteractionHand.OFF_HAND) return;
            if (player.isShiftKeyDown() && !state.is(Register.BUILDER_BLOCK_B.get()) && item.is(Register.POS_SELECTOR.get())) {

                CompoundTag nbt = new CompoundTag();

                nbt.putInt("target_x",pos.getX());
                nbt.putInt("target_z",pos.getZ());

                item.setTag(nbt);

                level.playSound(
                        null,
                        pos.getX() + 0.5,
                        pos.getY() + 1.0,
                        pos.getZ() + 0.5,
                        AllSoundEvents.CRAFTER_CRAFT.getMainEvent(),
                        SoundSource.VOICE,
                        1,
                        1
                );
                player.sendSystemMessage(Component.translatable("message.create_auto_track.selected"));
            }
        }
    }
}
