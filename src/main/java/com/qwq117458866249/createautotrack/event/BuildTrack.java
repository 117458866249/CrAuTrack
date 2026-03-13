package com.qwq117458866249.createautotrack.event;

import com.qwq117458866249.createautotrack.CreateAutoTrack;
import com.qwq117458866249.createautotrack.any.States;
import com.qwq117458866249.createautotrack.objects.BuilderBlock;
import com.qwq117458866249.createautotrack.util.Util;
import com.qwq117458866249.createautotrack.register.Register;
import com.simibubi.create.content.trains.track.BezierConnection;
import com.simibubi.create.content.trains.track.TrackBlock;
import com.simibubi.create.content.trains.track.TrackBlockEntity;
import com.simibubi.create.content.trains.track.TrackShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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

        // Temps
        BlockState roadBed;
        BlockState track;

        TagKey<Block> roadBedTag = BlockTags.create(ResourceLocation.fromNamespaceAndPath("create_auto_track", "roadbed"));
        TagKey<Block> trackTag = BlockTags.create(ResourceLocation.fromNamespaceAndPath("create", "tracks"));

        boolean isCorrectRoadBed = level.getBlockState(Util.getRelativePos(pos,0,1,0)).is(roadBedTag);
        boolean isCorrectTrack = level.getBlockState(Util.getRelativePos(pos,0,2,0)).is(trackTag);

        boolean isZ;

        if (!level.isClientSide){
            if (event.getHand() == InteractionHand.OFF_HAND) return;
            if (player.isShiftKeyDown() && state.is(Register.BUILDER_BLOCK_B.get()) && item.is(Register.POS_SELECTOR.get())) {

                // Wrong Roadbed & Track
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

                // Correct RoadBed & Track
                roadBed = level.getBlockState(Util.getRelativePos(pos, 0, 1, 0));
                track = level.getBlockState(Util.getRelativePos(pos, 0, 2, 0));

                // Z Axis
                isZ= level.getBlockState(pos) != Register.BUILDER_BLOCK_B.get().defaultBlockState().setValue(BuilderBlock.AXIS, States.X);

                // Target Pos
                CompoundTag nbt = item.getTag();
                if (nbt == null){
                    return;
                }

                // Way
                int[][] way = Util.getTrack(pos.getX(),pos.getZ(),nbt.getInt("target_x"),nbt.getInt("target_z"),isZ);

                // Wrong Way
                if (way[10][0] == 12) {
                    player.sendSystemMessage(Component.translatable("message.create_auto_track.wrong_way"));
                    return;
                }

                // Straight
                if (way[10][0] == 8){
                    Util.fillBlocks(new int[]{way[2][0], pos.getY(),way[2][1],way[3][0],pos.getY(),way[3][1]},level,roadBed);
                    Util.fillBlocks(new int[]{way[2][0], pos.getY()+1,way[2][1],way[3][0],pos.getY()+6,way[3][1]},level, Blocks.AIR.defaultBlockState());
                    Util.fillBlocks(new int[]{way[8][0], pos.getY()+1,way[8][1],way[9][0],pos.getY()+1,way[9][1]},level,track.getBlock().defaultBlockState().
                            setValue(TrackBlock.SHAPE, TrackShape.ZO));
                    return;
                }
                if (way[10][0] == 9){
                    Util.fillBlocks(new int[]{way[0][0], pos.getY(),way[0][1],way[1][0],pos.getY(),way[1][1]},level,roadBed);
                    Util.fillBlocks(new int[]{way[0][0], pos.getY()+1,way[0][1],way[1][0],pos.getY()+6,way[1][1]},level, Blocks.AIR.defaultBlockState());
                    Util.fillBlocks(new int[]{way[6][0], pos.getY()+1,way[6][1],way[7][0],pos.getY()+1,way[7][1]},level,track.getBlock().defaultBlockState().
                            setValue(TrackBlock.SHAPE, TrackShape.XO));
                    return;
                }

                // Turn
                if (!(way[10][0] >= 1 && way[10][0] <= 4)) return;

                // Normal Blocks
                Util.fillBlocks(new int[]{way[2][0], pos.getY(),way[2][1],way[3][0],pos.getY(),way[3][1]},level,roadBed);
                Util.fillBlocks(new int[]{way[2][0], pos.getY()+1,way[2][1],way[3][0],pos.getY()+6,way[3][1]},level, Blocks.AIR.defaultBlockState());
                Util.fillBlocks(new int[]{way[8][0], pos.getY()+1,way[8][1],way[9][0],pos.getY()+1,way[9][1]},level,track.getBlock().defaultBlockState().
                        setValue(TrackBlock.SHAPE, TrackShape.ZO));
                Util.fillBlocks(new int[]{way[0][0], pos.getY(),way[0][1],way[1][0],pos.getY(),way[1][1]},level,roadBed);
                Util.fillBlocks(new int[]{way[0][0], pos.getY()+1,way[0][1],way[1][0],pos.getY()+6,way[1][1]},level, Blocks.AIR.defaultBlockState());
                Util.fillBlocks(new int[]{way[6][0], pos.getY()+1,way[6][1],way[7][0],pos.getY()+1,way[7][1]},level,track.getBlock().defaultBlockState().
                        setValue(TrackBlock.SHAPE, TrackShape.XO));
                Util.fillBlocks(new int[]{way[4][0], pos.getY(),way[4][1],way[5][0],pos.getY(),way[5][1]},level,roadBed);
                Util.fillBlocks(new int[]{way[4][0], pos.getY()+1,way[4][1],way[5][0],pos.getY()+6,way[5][1]},level, Blocks.AIR.defaultBlockState());

                // Turn Track
                level.setBlock(new BlockPos(way[7][0],pos.getY()+1,way[7][1]),track.getBlock().defaultBlockState().
                        setValue(TrackBlock.SHAPE, TrackShape.XO).setValue(TrackBlock.HAS_BE,true),3);
                level.setBlock(new BlockPos(way[8][0],pos.getY()+1,way[8][1]),track.getBlock().defaultBlockState().
                        setValue(TrackBlock.SHAPE, TrackShape.ZO).setValue(TrackBlock.HAS_BE,true),3);

                String trackMaterial = ((TrackBlock)level.getBlockState(new BlockPos(way[7][0],pos.getY()+1,way[7][1])).getBlock()).getMaterial().resourceName();
                trackMaterial = "\""+ Util.getNameSpace(String.valueOf(BuiltInRegistries.BLOCK.getKey(level.getBlockState(new BlockPos(way[7][0],pos.getY()+1,way[7][1])).getBlock()))) + ":" + trackMaterial + "\"";

                String trackNbt = getTrackNbt(way,trackMaterial)[0];
                String finishNbt = getTrackNbt(way,trackMaterial)[1];

                level.getBlockEntity(new BlockPos(way[7][0],pos.getY()+1,way[7][1])).load(Util.stringToCompoundTag(trackNbt));
                level.getBlockEntity(new BlockPos(way[8][0],pos.getY()+1,way[8][1])).load(Util.stringToCompoundTag(finishNbt));
            }
        }
    }

    private static @NotNull String[] getTrackNbt(int[][] pWay,String pMaterial) {
        String trackNbt = "";
        String finishNbt = "";
        switch (pWay[10][0]){
            case 1:
                trackNbt = "{AlternateModel:0b,Connections:[{Axes:[{V:[1.0d,0.0d,0.0d]},{V:[0.0d,0.0d,-1.0d]}],Girder:0b,Material:"+
                        pMaterial+",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{Pos:[I;0,0,0]},{Pos:[I;19,0,19]}],Primary:1b,ShiftDown:0b,Starts:[{V:[1.0d,0.0d,0.5d]},{V:[19.5d,0.0d,19.0d]}]}]}";

                finishNbt = "{AlternateModel:0b,Connections:[{Axes:[{V:[0.0d,0.0d,-1.0d]},{V:[1.0d,0.0d,0.0d]}],Girder:0b,Material:"+
                        pMaterial+",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{Pos:[I;0,0,0]},{Pos:[I;-19,0,-19]}],Primary:0b,ShiftDown:0b,Starts:[{V:[0.5d,0.0d,0.0d]},{V:[-18.0d,0.0d,-18.5d]}]}]}";
                break;
            case 2:
                trackNbt = "{AlternateModel:0b,Connections:[{Axes:[{V:[1.0d,0.0d,0.0d]},{V:[0.0d,0.0d,1.0d]}],Girder:0b,Material:"+
                        pMaterial+",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{Pos:[I;0,0,0]},{Pos:[I;19,0,-19]}],Primary:1b,ShiftDown:0b,Starts:[{V:[1.0d,0.0d,0.5d]},{V:[19.5d,0.0d,-18.0d]}]}]}";

                finishNbt = "{AlternateModel:0b,Connections:[{Axes:[{V:[0.0d,0.0d,1.0d]},{V:[1.0d,0.0d,0.0d]}],Girder:0b,Material:"+
                        pMaterial+",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{Pos:[I;0,0,0]},{Pos:[I;-19,0,19]}],Primary:0b,ShiftDown:0b,Starts:[{V:[0.5d,0.0d,1.0d]},{V:[-18.0d,0.0d,19.5d]}]}]}";
                break;
            case 3:
                trackNbt = "{AlternateModel:0b,Connections:[{Axes:[{V:[-1.0d,0.0d,0.0d]},{V:[0.0d,0.0d,-1.0d]}],Girder:0b,Material:"+
                        pMaterial+",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{Pos:[I;0,0,0]},{Pos:[I;-19,0,19]}],Primary:1b,ShiftDown:0b,Starts:[{V:[0.0d,0.0d,0.5d]},{V:[-18.5d,0.0d,19.0d]}]}]}";

                finishNbt = "{AlternateModel:0b,Connections:[{Axes:[{V:[0.0d,0.0d,-1.0d]},{V:[-1.0d,0.0d,0.0d]}],Girder:0b,Material:"+
                        pMaterial+",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{Pos:[I;0,0,0]},{Pos:[I;19,0,-19]}],Primary:0b,ShiftDown:0b,Starts:[{V:[0.5d,0.0d,0.0d]},{V:[19.0d,0.0d,-18.5d]}]}]}";
                break;
            case 4:
                trackNbt = "{AlternateModel:0b,Connections:[{Axes:[{V:[-1.0d,0.0d,0.0d]},{V:[0.0d,0.0d,1.0d]}],Girder:0b,Material:"+
                        pMaterial+",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{Pos:[I;0,0,0]},{Pos:[I;-19,0,-19]}],Primary:1b,ShiftDown:0b,Starts:[{V:[0.0d,0.0d,0.5d]},{V:[-18.5d,0.0d,-18.0d]}]}]}";

                finishNbt = "{AlternateModel:0b,Connections:[{Axes:[{V:[0.0d,0.0d,1.0d]},{V:[-1.0d,0.0d,0.0d]}],Girder:0b,Material:"+
                        pMaterial+",Normals:[{V:[0.0d,1.0d,0.0d]},{V:[0.0d,1.0d,0.0d]}],Positions:[{Pos:[I;0,0,0]},{Pos:[I;19,0,19]}],Primary:0b,ShiftDown:0b,Starts:[{V:[0.5d,0.0d,1.0d]},{V:[19.0d,0.0d,19.5d]}]}]}";
                break;
        }
        return new String[]{trackNbt,finishNbt};
    }
}
