package com.elevensef.createautotrack;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
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

    public static int[] getTrack(int Xa, int Za, int Xb, int Zb) {
        int Xdifference = Xa - Xb;
        int Zdifference = Za - Zb;
        int[] output = new int[]{ 0, 0, 0, 0, 0, 0, 0 };

        // If it's straight
        if (Xdifference == 0)
            if (getAbsolveValue(Zdifference) >= 20)
                if (Zdifference > 0) {
                    return new int[]{ 0, 0, Zb, Za, 0, 0, 9 };
                }
                else {
                    return new int[]{ 0, 0, Za, Zb, 0, 0, 9 };
                }

        if (Zdifference == 0)
            if (getAbsolveValue(Xdifference) >= 20)
                if (Xdifference > 0) {
                    return new int[]{ Xb, Xa, 0, 0, 0, 0, 9 };
                }
                else {
                    return new int[]{ Xa, Xb, 0, 0, 0, 0, 9 };
                }

        // If it don't get a right R
        if (getAbsolveValue(Xdifference) < 10 || getAbsolveValue(Zdifference) < 10)
            return new int[]{ 0, 0, 0, 0, 0, 0, 0 };

        // If it need to turn
        if (Xdifference < 0) {
            Xb = Xb - 9;
            output[0] = Xa;
            output[1] = Xb;
        }
        else {
            Xb = Xb + 9;
            output[0] = Xb;
            output[1] = Xa;
        }

        if (Zdifference < 0) {
            Za = Za + 9;
            output[2] = Za;
            output[3] = Zb;
        }
        else {
            Za = Za - 9;
            output[3] = Za;
            output[2] = Zb;
        }

        // Turn codes
        if (Xdifference < 0) {
            output[4] = Xb + 1;
            if (Zdifference < 0) {
                output[5] = Za - 1;
                output[6] = 1;
            } else {
                output[5] = Za + 1;
                output[6] = 2;
            }
        } else {
            output[4] = Xb - 1;
            if (Zdifference < 0) {
                output[5] = Za + 1;
                output[6] = 3;
                output[2] = output[2]-1;
            } else {
                output[5] = Za - 1;
                output[6] = 4;
                output[3] = output[3]+1;
            }
        }

        return output;
        /*
            What does output[6] infer to?

            1: X
               |  0
               |
               |
            ---0---Z
               |
               |
               |

            2: X
            0  |
               |
               |
            ---0---Z
               |
               |
               |

            3: X
               |
               |
               |
            ---0---Z
               |
               |
               |  0

            4: X
               |
               |
               |
            ---0---Z
               |
               |
            0  |

            Fill output[1] ~ Za output[2] ~ Za XAxisTrack
            Fill Xb ~ output[3] Xb ~ output[4] ZAxisTrack
            output[5] for a nbt turned track
            output6 too
        */
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

    public static void runCommandForLevelAndPos(String command, Level level, BlockPos pos){
        CommandSourceStack src = Objects.requireNonNull(
                level.getServer()
        ).createCommandSourceStack().withSuppressedOutput();

        level.getServer().getCommands().performPrefixedCommand(
                src,
                "execute positioned "
                        +
                        pos.getX()
                        +
                        " "
                        +
                        pos.getY()
                        +
                        " "
                        +
                        pos.getZ()
                        +
                        " in "
                        +
                        level.dimensionType().effectsLocation()
                        +
                        " run "
                        +
                        command
        );
    }
}
