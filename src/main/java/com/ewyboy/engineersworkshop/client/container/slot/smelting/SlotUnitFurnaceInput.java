package engineers.workshop.client.container.slot.smelting;

import engineers.workshop.client.container.slot.SlotUnit;
import engineers.workshop.client.page.Page;
import engineers.workshop.common.table.TileTable;
import engineers.workshop.common.unit.Unit;
import engineers.workshop.common.util.RecipeHelpers;
import net.minecraft.item.ItemStack;

public class SlotUnitFurnaceInput extends SlotUnit {

	public SlotUnitFurnaceInput(TileTable table, Page page, int id, int x, int y, Unit unit) {
		super(table, page, id, x, y, unit);
	}

	@Override
	public boolean canAcceptItem(ItemStack itemstack) {
		return super.canAcceptItem(itemstack) && !RecipeHelpers.getFurnaceRecipe(itemstack).isEmpty();
	}

	@Override
	public boolean canShiftClickInto(ItemStack item) {
		return true;
	}
}
