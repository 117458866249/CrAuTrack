package com.elevensef.createautotrack;

import com.simibubi.create.content.trains.track.TrackBlock;
import com.simibubi.create.content.trains.track.TrackShape;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = CreateAutoTrack.MOD_ID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BuildTrack extends Block {

    public BuildTrack(Properties pProperties) {
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

        BlockState roadBed;
        BlockState track;

        String turnStart = "";
        String turnFinish = "";

        int[] wayTemp;

        TagKey<Block> roadBedTag = BlockTags.create(ResourceLocation.fromNamespaceAndPath("create_auto_track", "roadbed"));
        TagKey<Block> trackTag = BlockTags.create(ResourceLocation.fromNamespaceAndPath("create", "tracks"));

        boolean isCorrectRoadBed = level.getBlockState(Util.getRelativePos(pos,0,1,0)).is(roadBedTag);
        boolean isCorrectTrack = level.getBlockState(Util.getRelativePos(pos,0,2,0)).is(trackTag);

        if (!level.isClientSide){
            if (event.getHand() == InteractionHand.OFF_HAND) return;
            if (player.isShiftKeyDown() && state.is(Register.BUILDER_BLOCK_B.get()) && item.is(ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath("create","sturdy_sheet")))){
                CompoundTag nbtData = level.getBlockEntity(pos).serializeNBT();
                if ((!isCorrectRoadBed) && (!isCorrectTrack)) {
                    player.sendSystemMessage(Component.translatable("message.create_auto_track.roadbed"));
                    player.sendSystemMessage(Component.translatable("message.create_auto_track.track"));
                    return;
                }
                if (!isCorrectRoadBed) {
                    player.sendSystemMessage(Component.translatable("message.create_auto_track.roadbed"));
                    return;
                }
                if (!isCorrectTrack) {
                    player.sendSystemMessage(Component.translatable("message.create_auto_track.track"));
                    return;
                }

                roadBed = level.getBlockState(Util.getRelativePos(pos,0,1,0));
                track = level.getBlockState(Util.getRelativePos(pos,0,2,0));

                if (level.getBlockState(pos) == Register.BUILDER_BLOCK_B.get().defaultBlockState().setValue(BuilderBlock.AXIS, States.X)){
                    wayTemp = Util.getTrack(pos.getX(),pos.getZ(),nbtData.getInt("target_x"),nbtData.getInt("target_z"));
                } else {
                    wayTemp = Util.getTrack(nbtData.getInt("target_x"),nbtData.getInt("target_z"),pos.getX(),pos.getZ());
                }

                if (Arrays.equals(wayTemp, new int[]{0, 0, 0, 0, 0, 0, 0})){
                    player.sendSystemMessage(Component.translatable("message.create_auto_track.wrong_way"));
                    return;
                }

                if (wayTemp[6] == 9){
                    if (wayTemp[0] == 0){
                        Util.fillBlocks(new int[]{pos.getX()-2,pos.getY(),wayTemp[2],pos.getX()+2,pos.getY(),wayTemp[3]},
                                level,roadBed);
                        Util.fillBlocks(new int[]{pos.getX()-2,pos.getY()+1,wayTemp[2],pos.getX()+2,pos.getY()+6,wayTemp[3]},
                                level, Blocks.AIR.defaultBlockState());
                        Util.fillBlocks(new int[]{pos.getX(),pos.getY()+1,wayTemp[2],pos.getX(),pos.getY()+1,wayTemp[3]},
                                level, track.getBlock().defaultBlockState().
                                        setValue(TrackBlock.SHAPE, TrackShape.ZO));
                        return;
                    } else {
                        Util.fillBlocks(new int[]{wayTemp[0],pos.getY(),pos.getZ()+2,wayTemp[1],pos.getY(),pos.getZ()-2},
                                level,roadBed);
                        Util.fillBlocks(new int[]{wayTemp[0],pos.getY()+1,pos.getZ()+2,wayTemp[1],pos.getY()+6,pos.getZ()-2},
                                level, Blocks.AIR.defaultBlockState());
                        Util.fillBlocks(new int[]{wayTemp[0],pos.getY()+1,pos.getZ(),wayTemp[1],pos.getY()+1,pos.getZ()},
                                level, track.getBlock().defaultBlockState()
                                        .setValue(TrackBlock.SHAPE, TrackShape.XO));
                        return;
                    }
                }

                // This damn dog's NBT has dozens of layers; using commands to call it is still incredibly useful.
                switch (wayTemp[6]){
                    case 1:
                        turnStart = "[shape=xo,turn=true,waterlogged=false]{Connections:[{Axes:[{V:[1.0d,0.0d,0.0d]},{V:[0.0d,0.0d,-1.0d]}],Girder:0b,Material:\"create:andesite\",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{X:0,Y:0,Z:0},{X:8,Y:0,Z:8}],Primary:1b,Starts:[{V:[1.0d,0.0d,0.5d]},{V:[8.5d,0.0d,8.0d]}]}]}";
                        turnFinish = "[shape=zo,turn=true,waterlogged=false]{Connections:[{Axes:[{V:[0.0d,0.0d,-1.0d]},{V:[1.0d,0.0d,0.0d]}],Girder:0b,Material:\"create:andesite\",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{X:0,Y:0,Z:0},{X:-8,Y:0,Z:-8}],Primary:0b,Starts:[{V:[0.5d,0.0d,0.0d]},{V:[-7.0d,0.0d,-7.5d]}]}]}";
                        break;
                    case 2:
                        turnStart = "[shape=xo,turn=true,waterlogged=false]{Connections:[{Axes:[{V:[1.0d,0.0d,0.0d]},{V:[0.0d,0.0d,1.0d]}],Girder:0b,Material:\"create:andesite\",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{X:0,Y:0,Z:0},{X:8,Y:0,Z:-8}],Primary:1b,Starts:[{V:[1.0d,0.0d,0.5d]},{V:[8.5d,0.0d,-7.0d]}]}]}";
                        turnFinish = "[shape=zo,turn=true,waterlogged=false]{Connections:[{Axes:[{V:[0.0d,0.0d,1.0d]},{V:[1.0d,0.0d,0.0d]}],Girder:0b,Material:\"create:andesite\",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{X:0,Y:0,Z:0},{X:-8,Y:0,Z:8}],Primary:0b,Starts:[{V:[0.5d,0.0d,1.0d]},{V:[-7.0d,0.0d,8.5d]}]}]}";
                        break;
                    case 3:
                        turnStart = "[shape=xo,turn=true,waterlogged=false]{Connections:[{Axes:[{V:[-1.0d,0.0d,0.0d]},{V:[0.0d,0.0d,-1.0d]}],Girder:0b,Material:\"create:andesite\",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{X:0,Y:0,Z:0},{X:-8,Y:0,Z:8}],Primary:1b,Starts:[{V:[0.0d,0.0d,0.5d]},{V:[-7.5d,0.0d,8.0d]}]}]}";
                        turnFinish = "[shape=zo,turn=true,waterlogged=false]{Connections:[{Axes:[{V:[0.0d,0.0d,-1.0d]},{V:[-1.0d,0.0d,0.0d]}],Girder:0b,Material:\"create:andesite\",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{X:0,Y:0,Z:0},{X:8,Y:0,Z:-8}],Primary:0b,Starts:[{V:[0.5d,0.0d,0.0d]},{V:[8.0d,0.0d,-7.5d]}]}]}";
                        break;
                    case 4:
                        turnStart = "[shape=xo,turn=true,waterlogged=false]{Connections:[{Axes:[{V:[-1.0d,0.0d,0.0d]},{V:[0.0d,0.0d,1.0d]}],Girder:0b,Material:\"create:andesite\",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{X:0,Y:0,Z:0},{X:-8,Y:0,Z:-8}],Primary:1b,Starts:[{V:[0.0d,0.0d,0.5d]},{V:[-7.5d,0.0d,-7.0d]}]}]}";
                        turnFinish = "[shape=zo,turn=true,waterlogged=false]{Connections:[{Axes:[{V:[0.0d,0.0d,1.0d]},{V:[-1.0d,0.0d,0.0d]}],Girder:0b,Material:\"create:andesite\",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{X:0,Y:0,Z:0},{X:8,Y:0,Z:8}],Primary:0b,Starts:[{V:[0.5d,0.0d,1.0d]},{V:[8.0d,0.0d,8.5d]}]}]}";
                }


                if (level.getBlockState(pos) == Register.BUILDER_BLOCK_B.get().defaultBlockState().setValue(BuilderBlock.AXIS, States.X)){
                    Util.fillBlocks(new int[]{wayTemp[0],pos.getY(),pos.getZ()+2,wayTemp[1],pos.getY(),pos.getZ()-2},
                            level,roadBed);
                    Util.fillBlocks(new int[]{wayTemp[0],pos.getY()+1,pos.getZ()+2,wayTemp[1],pos.getY()+6,pos.getZ()-2},
                            level, Blocks.AIR.defaultBlockState());
                    Util.fillBlocks(new int[]{wayTemp[0],pos.getY()+1,pos.getZ(),wayTemp[1],pos.getY()+1,pos.getZ()},
                            level, track.getBlock().defaultBlockState()
                                    .setValue(TrackBlock.SHAPE, TrackShape.XO));
                    Util.fillBlocks(new int[]{nbtData.getInt("target_x")-2,pos.getY(),wayTemp[2],nbtData.getInt("target_x")+2,pos.getY(),wayTemp[3]},
                            level,roadBed);
                    Util.fillBlocks(new int[]{nbtData.getInt("target_x")-2,pos.getY()+1,wayTemp[2],nbtData.getInt("target_x")+2,pos.getY()+6,wayTemp[3]},
                            level, Blocks.AIR.defaultBlockState());
                    Util.fillBlocks(new int[]{nbtData.getInt("target_x"),pos.getY()+1,wayTemp[2],nbtData.getInt("target_x"),pos.getY()+1,wayTemp[3]},
                            level, track.getBlock().defaultBlockState().
                                    setValue(TrackBlock.SHAPE, TrackShape.ZO));

                    Util.fillBlocks(new int[]{wayTemp[4],pos.getY(),pos.getZ(),nbtData.getInt("target_x")+1,pos.getY(),wayTemp[5]},
                            level,roadBed);
                    Util.fillBlocks(new int[]{wayTemp[4],pos.getY()+1,pos.getZ(),nbtData.getInt("target_x")+6,pos.getY()+1,wayTemp[5]},
                            level, Blocks.AIR.defaultBlockState());
                    Util.runCommandForLevelAndPos("forceload add ~ ~",level,new BlockPos(wayTemp[4],pos.getY()+1,pos.getZ()));
                    Util.runCommandForLevelAndPos("forceload add ~ ~",level,new BlockPos(nbtData.getInt("target_x"),pos.getY()+1,wayTemp[5]));

                    Util.runCommandForLevelAndPos("setblock ~ ~ ~ "+ForgeRegistries.BLOCKS.getKey(track.getBlock())+turnStart,level,new BlockPos(wayTemp[4],pos.getY()+1,pos.getZ()));
                    Util.runCommandForLevelAndPos("setblock ~ ~ ~ "+ForgeRegistries.BLOCKS.getKey(track.getBlock())+turnFinish,level,new BlockPos(nbtData.getInt("target_x"),pos.getY()+1,wayTemp[5]));

                    Util.runCommandForLevelAndPos("forceload remove ~ ~",level,new BlockPos(wayTemp[4],pos.getY()+1,pos.getZ()));
                    Util.runCommandForLevelAndPos("forceload remove ~ ~",level,new BlockPos(nbtData.getInt("target_x")+1,pos.getY(),wayTemp[5]));
                } else {
                    Util.fillBlocks(new int[]{wayTemp[0],pos.getY(),nbtData.getInt("target_z")+2,wayTemp[1],pos.getY(),nbtData.getInt("target_z")-2},
                            level,roadBed);
                    Util.fillBlocks(new int[]{wayTemp[0],pos.getY()+1,nbtData.getInt("target_z")+2,wayTemp[1],pos.getY()+6,nbtData.getInt("target_z")-2},
                            level, Blocks.AIR.defaultBlockState());
                    Util.fillBlocks(new int[]{wayTemp[0],pos.getY()+1,nbtData.getInt("target_z"),wayTemp[1],pos.getY()+1,nbtData.getInt("target_z")},
                            level, track.getBlock().defaultBlockState()
                                    .setValue(TrackBlock.SHAPE, TrackShape.XO));
                    Util.fillBlocks(new int[]{pos.getX()-2,pos.getY(),wayTemp[2],pos.getX()+2,pos.getY(),wayTemp[3]},
                            level,roadBed);
                    Util.fillBlocks(new int[]{pos.getX()-2,pos.getY()+1,wayTemp[2],pos.getX()+2,pos.getY()+6,wayTemp[3]},
                            level, Blocks.AIR.defaultBlockState());
                    Util.fillBlocks(new int[]{pos.getX(),pos.getY()+1,wayTemp[2],pos.getX(),pos.getY()+1,wayTemp[3]},
                            level, track.getBlock().defaultBlockState().
                                    setValue(TrackBlock.SHAPE, TrackShape.ZO));

                    Util.fillBlocks(new int[]{wayTemp[4],pos.getY(),nbtData.getInt("target_z"),pos.getX()+1,pos.getY(),wayTemp[5]},
                            level,roadBed);
                    Util.fillBlocks(new int[]{wayTemp[4],pos.getY()+1,nbtData.getInt("target_z"),pos.getX()+1,pos.getY()+6,wayTemp[5]},
                            level, Blocks.AIR.defaultBlockState());
                    Util.runCommandForLevelAndPos("forceload add ~ ~",level,new BlockPos(wayTemp[4],pos.getY()+1,nbtData.getInt("target_z")));
                    Util.runCommandForLevelAndPos("forceload add ~ ~",level,new BlockPos(pos.getX(),pos.getY()+1,wayTemp[5]));

                    Util.runCommandForLevelAndPos("setblock ~ ~ ~ "+ForgeRegistries.BLOCKS.getKey(track.getBlock())+turnStart,level,new BlockPos(wayTemp[4],pos.getY()+1,nbtData.getInt("target_z")));
                    Util.runCommandForLevelAndPos("setblock ~ ~ ~ "+ForgeRegistries.BLOCKS.getKey(track.getBlock())+turnFinish,level,new BlockPos(pos.getX(),pos.getY()+1,wayTemp[5]));

                    Util.runCommandForLevelAndPos("forceload remove ~ ~",level,new BlockPos(wayTemp[4],pos.getY()+1,nbtData.getInt("target_z")));
                    Util.runCommandForLevelAndPos("forceload remove ~ ~",level,new BlockPos(pos.getX(),pos.getY()+1,wayTemp[5]));
                }
            }
        }
    }
}
