package com.qwq117458866249.createautotrack.objects;

import com.qwq117458866249.createautotrack.any.States;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BuilderBlock extends Block{
    public static final EnumProperty<States> AXIS = EnumProperty.create("axis", States.class);

    public BuilderBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, States.X));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }
}
