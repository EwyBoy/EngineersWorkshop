package com.ewyboy.engineersworkshop.common.content;

import com.ewyboy.bibliotheca.common.content.block.BaseTileBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;

public class WorkshopBlock extends BaseTileBlock<WorkshopTile> {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    @Override
    protected Class<WorkshopTile> getTileClass() {
        return WorkshopTile.class;
    }

    public WorkshopBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
