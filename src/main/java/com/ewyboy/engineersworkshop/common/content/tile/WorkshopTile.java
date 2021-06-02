package com.ewyboy.engineersworkshop.common.content;

import com.ewyboy.engineersworkshop.common.register.Register;
import net.minecraft.tileentity.TileEntity;

public class WorkshopTile extends TileEntity {

    public WorkshopTile() {
        super(Register.TILES.WORKSHOP);
    }
}
