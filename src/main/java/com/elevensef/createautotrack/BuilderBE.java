package com.elevensef.createautotrack;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BuilderBE extends BlockEntity implements MenuProvider, ContainerData {
    public int targetX;
    public int targetZ;

    public BuilderBE(BlockPos pos, BlockState state){
        super(Register.BUILDER_BLOCK_BE.get(),pos,state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.create_auto_track.builder_block");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BuilderBlockMenu(i,inventory,this);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt){
        nbt.putInt("target_x",targetX);
        nbt.putInt("target_z",targetZ);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt){
        super.load(nbt);
        targetX = nbt.getInt("target_x");
        targetZ = nbt.getInt("target_z");
    }

    @Override
    public int getCount() {
        return 2; // 两个数据槽
    }

    @Override
    public int get(int index) {
        return switch (index) {
            case 0 -> targetX;
            case 1 -> targetZ;
            default -> throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
        };
    }

    @Override
    public void set(int index, int value) {
        switch (index) {
            case 0 -> targetX = value;
            case 1 -> targetZ = value;
            default -> throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
        }
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public int getTargetX() { return targetX; }
    public int getTargetZ() { return targetZ; }
}
