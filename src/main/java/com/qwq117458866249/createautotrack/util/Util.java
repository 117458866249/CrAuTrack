package com.qwq117458866249.createautotrack.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class Util {
    public static int getAbsolveValue(int input) {
        if (input < 0) {
            return -input;
        } else {
            return input;
        }
    }

    public static int[][] getTrack(int pXa,int pZa,int pXb,int pZb,boolean pAxisIsZ){

        // Different
        int xDifferent = pXb-pXa;
        int zDifferent = pZb-pZa;

        // Unexecutable Case
        if (
                getAbsolveValue(xDifferent)<20&&getAbsolveValue(zDifferent)<20||
                        getAbsolveValue(xDifferent)<20&&!pAxisIsZ||
                        getAbsolveValue(zDifferent)<20&&pAxisIsZ
        ){
            return new int[][]{
                    {0,0},
                    {0,0},
                    {0,0},
                    {0,0},
                    {0,0},
                    {0,0},
                    {0,0},
                    {0,0},
                    {0,0},
                    {0,0},
                    {12}
            };
        }

        // Straight Case
        if (getAbsolveValue(xDifferent)<20&&pAxisIsZ){
            return new int[][]{
                    {0,0},
                    {0,0},
                    {pXa-2,pZa},
                    {pXa+2,pZb},
                    {0,0},
                    {0,0},
                    {0,0},
                    {0,0},
                    {pXa,pZa},
                    {pXa,pZb},
                    {8}
            };
        }
        if (getAbsolveValue(zDifferent)<20&&!pAxisIsZ){
            return new int[][]{
                    {pXa,pZa-2},
                    {pXb,pZa+2},
                    {0,0},
                    {0,0},
                    {0,0},
                    {0,0},
                    {pXa,pZa},
                    {pXb,pZa},
                    {0,0},
                    {0,0},
                    {9}
            };
        }

        // Z Axis Case
        if (pAxisIsZ){
            int tempXChange = pXa;
            int tempZChange = pZa;

            pXa=pXb;
            pZa=pZb;

            pXb=tempXChange;
            pZb=tempZChange;

            xDifferent=-xDifferent;
            zDifferent=-zDifferent;
        }

        // Turn Cases
        if (xDifferent>0&&zDifferent>0){
            return new int[][]{
                    {pXa,pZa-2},
                    {pXb-20,pZa+2},
                    {pXb+2,pZb},
                    {pXb-2,pZa+20},
                    {pXb+2,pZa-2},
                    {pXb-19,pZa+19},
                    {pXa,pZa},
                    {pXb-19,pZa},
                    {pXb,pZa+19},
                    {pXb,pZb},
                    {1}
            };
        }
        if (xDifferent>0&&zDifferent<0){
            return new int[][]{
                    {pXa,pZa-2},
                    {pXb-20,pZa+2},
                    {pXb+2,pZb},
                    {pXb-2,pZa-20},
                    {pXb+2,pZa+2},
                    {pXb-19,pZa-19},
                    {pXa,pZa},
                    {pXb-19,pZa},
                    {pXb,pZa-19},
                    {pXb,pZb},
                    {2}
            };
        }
        if (xDifferent<0&&zDifferent>0){
            return new int[][]{
                    {pXa,pZa-2},
                    {pXb+20,pZa+2},
                    {pXb+2,pZb},
                    {pXb-2,pZa+20},
                    {pXb-2,pZa-2},
                    {pXb+19,pZa+19},
                    {pXa,pZa},
                    {pXb+19,pZa},
                    {pXb,pZa+19},
                    {pXb,pZb},
                    {3}
            };
        }
        if (xDifferent<0&&zDifferent<0){
            return new int[][]{
                    {pXa,pZa-2},
                    {pXb+20,pZa+2},
                    {pXb+2,pZb},
                    {pXb-2,pZa-20},
                    {pXb-2,pZa+2},
                    {pXb+19,pZa-19},
                    {pXa,pZa},
                    {pXb+19,pZa},
                    {pXb,pZa-19},
                    {pXb,pZb},
                    {4}
            };
        }
        return new int[][]{
                {0,0},
                {0,0},
                {0,0},
                {0,0},
                {0,0},
                {0,0},
                {0,0},
                {0,0},
                {0,0},
                {0,0},
                {12}
        };
    }

    public static void fillBlocks(int[] pPosList, Level pLevel, BlockState pState){
        if (pLevel.isClientSide()){
            return;
        }

        int[] pPosProgress = new int[6];

        if (pPosList[0]<pPosList[3]){
            pPosProgress[0]=pPosList[0];
            pPosProgress[3]=pPosList[3];
        } else {
            pPosProgress[0]=pPosList[3];
            pPosProgress[3]=pPosList[0];
        }

        if (pPosList[1]<pPosList[4]){
            pPosProgress[1]=pPosList[1];
            pPosProgress[4]=pPosList[4];
        } else {
            pPosProgress[1]=pPosList[4];
            pPosProgress[4]=pPosList[1];
        }

        if (pPosList[2]<pPosList[5]){
            pPosProgress[2]=pPosList[2];
            pPosProgress[5]=pPosList[5];
        } else {
            pPosProgress[2]=pPosList[5];
            pPosProgress[5]=pPosList[2];
        }

        for (int x=pPosProgress[0];x<=pPosProgress[3];x++){
            for (int y=pPosProgress[1];y<=pPosProgress[4];y++){
                for (int z=pPosProgress[2];z<=pPosProgress[5];z++){
                    pLevel.setBlock(new BlockPos(x,y,z),pState,3);
                }
            }
        }
    }

    public static BlockPos getRelativePos(BlockPos pos, int x, int y, int z){
        return new BlockPos(
                pos.getX() + x,
                pos.getY() + y,
                pos.getZ() + z
        );
    }

    public static CompoundTag stringToCompoundTag(String snbt) {
        try {
            return TagParser.parseTag(snbt);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return new CompoundTag();
        }
    }

    public static String getNameSpace(String pFullID){
        StringBuilder output = new StringBuilder();

        for (int i = 0;i<pFullID.length();i++){
            if (pFullID.indexOf(i) != ':'){
                output.append(pFullID.indexOf(i));
            } else break;
        }

        return output.toString();
    }
}
