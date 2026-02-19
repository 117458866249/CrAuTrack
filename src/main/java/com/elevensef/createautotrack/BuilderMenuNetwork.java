package com.elevensef.createautotrack;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BuilderMenuNetwork {
    private final BlockPos pos;
    private final int index;
    private final int value;

    public BuilderMenuNetwork(BlockPos pos, int index, int value) {
        this.pos = pos;
        this.index = index;
        this.value = value;
    }

    public BuilderMenuNetwork(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.index = buf.readInt();
        this.value = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(index);
        buf.writeInt(value);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                Level level = player.level();
                if (level.getBlockEntity(pos) instanceof BuilderBE be) {
                    be.set(index, value);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
