package com.ewyboy.engineersworkshop.common.content.tile;

import com.ewyboy.engineersworkshop.common.register.Register;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class WorkshopTile extends TileEntity implements ITickableTileEntity {

    public WorkshopTile() {
        super(Register.TILES.WORKSHOP);
    }

    @Override
    public void tick() {

    }

}
