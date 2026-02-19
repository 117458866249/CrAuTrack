package com.elevensef.createautotrack;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BuilderBlockMenu extends AbstractContainerMenu {
    public final BuilderBE blockEntity;
    private final Level level;
    private final ContainerData data;

    public BuilderBlockMenu(int id, Inventory inv, FriendlyByteBuf extraData){
        this(id,inv,inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }
    public BuilderBlockMenu(int id, Inventory inv, BlockEntity entity){
        super(MenuTypes.BB_MENU.get(),id);
        checkContainerSize(inv,0);
        blockEntity = (BuilderBE) entity;
        this.level = inv.player.level();
        this.data = blockEntity;

        addDataSlots(data);

        addPlayerHotbar(inv);
        addPlayerInventory(inv);
    }

    public int getData(int index) {
        return data.get(index);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return new ItemStack(Register.DEBUG.get(),64);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),player, Register.BUILDER_BLOCK_B.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}
