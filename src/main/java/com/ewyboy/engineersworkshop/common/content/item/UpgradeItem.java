package com.ewyboy.engineersworkshop.common.content.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class UpgradeItem extends Item {

    public Upgrade upgrade;

    public UpgradeItem(Upgrade upgrade) {
        super(upgrade.createSettings());
        this.upgrade = upgrade;
    }

    public static Upgrade getUpgrade(ItemStack item) {
        if(item.getItem() instanceof UpgradeItem){
            return ((UpgradeItem) item.getItem()).upgrade;
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        Upgrade upgrade = getUpgrade(stack);
        upgrade.addInfo(tooltip);
    }
}
