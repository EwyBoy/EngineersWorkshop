package engineers.workshop.client.container.slot;

import engineers.workshop.client.page.Page;
import engineers.workshop.common.items.Upgrade;
import engineers.workshop.common.table.TileTable;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.ItemStack;

public class SlotFuel extends SlotTable {

	public SlotFuel(TileTable table, Page page, int id, int x, int y) {
		super(table, page, id, x, y);
	}

	@Override
	public boolean canAcceptItem(ItemStack stack) {
		String[] upgrades = {};
		return super.canAcceptItem(stack) && FurnaceBlockEntity.canUseAsFuel(stack) && !(Upgrade.ParentType.CRAFTING.isValidParent(stack) || Upgrade.ParentType.SMELTING.isValidParent(stack) || Upgrade.ParentType.STORAGE.isValidParent(stack));
	}
}
