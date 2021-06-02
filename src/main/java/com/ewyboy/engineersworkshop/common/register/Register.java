package com.ewyboy.engineersworkshop.common.register;

import com.ewyboy.engineersworkshop.common.content.block.WorkshopBlock;
import com.ewyboy.engineersworkshop.common.content.item.Upgrade;
import com.ewyboy.engineersworkshop.common.content.item.UpgradeItem;
import com.ewyboy.engineersworkshop.common.content.tile.WorkshopTile;
import com.google.common.collect.Sets;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityType;

public class Register {

    public static final class BLOCKS {
        public static final WorkshopBlock WORKSHOP = new WorkshopBlock(AbstractBlock.Properties.create(Material.ANVIL));
    }

    public static final class ITEMS {
        public static final UpgradeItem BLANK = new UpgradeItem(Upgrade.BLANK);
        public static final UpgradeItem AUTO_CRAFTER = new UpgradeItem(Upgrade.AUTO_CRAFTER);
        public static final UpgradeItem STORAGE = new UpgradeItem(Upgrade.STORAGE);
        public static final UpgradeItem CHARGED = new UpgradeItem(Upgrade.CHARGED);
        public static final UpgradeItem SPEED = new UpgradeItem(Upgrade.SPEED);
        public static final UpgradeItem QUEUE = new UpgradeItem(Upgrade.QUEUE);
        public static final UpgradeItem AUTO_TRANSFER = new UpgradeItem(Upgrade.AUTO_TRANSFER);
        public static final UpgradeItem FILTER = new UpgradeItem(Upgrade.FILTER);
        public static final UpgradeItem TRANSFER = new UpgradeItem(Upgrade.TRANSFER);
    }

    public static final class TILES {
        public static final TileEntityType<WorkshopTile> WORKSHOP = new TileEntityType<>(WorkshopTile :: new, Sets.newHashSet(BLOCKS.WORKSHOP), null);
    }

}
