package com.ewyboy.engineersworkshop.common.content.block;

import com.ewyboy.bibliotheca.common.content.block.BaseTileBlock;
import com.ewyboy.engineersworkshop.common.content.tile.WorkshopTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;

import javax.annotation.Nullable;

public class WorkshopBlock extends BaseTileBlock<WorkshopTile> {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    @Override
    protected Class<WorkshopTile> getTileClass() {
        return WorkshopTile.class;
    }

    public WorkshopBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
    }

    // TODO - Drop items in table when broken

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
