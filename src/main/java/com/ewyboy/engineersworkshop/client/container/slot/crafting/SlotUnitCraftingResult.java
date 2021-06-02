package engineers.workshop.client.container.slot.crafting;

import engineers.workshop.client.container.slot.SlotUnit;
import engineers.workshop.client.page.Page;
import engineers.workshop.common.items.Upgrade;
import engineers.workshop.common.table.TileTable;
import engineers.workshop.common.unit.UnitCraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class SlotUnitCraftingResult extends SlotUnit {

	public SlotUnitCraftingResult(TileTable table, Page page, int id, int x, int y, UnitCraft unit) {
		super(table, page, id, x, y, unit);
	}

	@Override
	public boolean isBig() {
		return true;
	}

	@Override
	public boolean canInsert(ItemStack itemstack) {
		return false;
	}

	@Override
	public ItemStack onTakeItem(PlayerEntity player, ItemStack item) {
		item = super.onTakeItem(player, item);
		((UnitCraft) unit).onCrafting(player, item);
		return item;
	}

	@Override
	public boolean canAcceptItems() {
		return false;
	}

	@Override
	public int getY() {
		int offset = 0;
		if (table.getUpgradePage().hasUpgrade(unit.getId(), Upgrade.AUTO_CRAFTER)) {
			offset = UnitCraft.RESULT_AUTO_OFFSET;
		}
		return super.getY() + offset;
	}

	@Override
	public boolean canPickUpOnDoubleClick() {
		return false;
	}

	@Override
	public ItemStack takeStack(int count) {
		ItemStack itemstack = getStack();
		if (!itemstack.isEmpty()) {
			setStack(ItemStack.EMPTY);
		}
		return itemstack;
	}

	@Override
	public boolean shouldDropOnClosing() {
		return false;
	}
}
