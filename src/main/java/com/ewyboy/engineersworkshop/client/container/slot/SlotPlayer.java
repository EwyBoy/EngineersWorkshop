package engineers.workshop.client.container.slot;

import engineers.workshop.client.GuiBase;
import engineers.workshop.common.table.TileTable;
import net.minecraft.inventory.Inventory;

public class SlotPlayer extends SlotBase {
	public SlotPlayer(Inventory inventory, TileTable table, int id, int x, int y) {
		super(inventory, table, id, x, y);
	}

	@Override
	public int getTextureIndex(GuiBase gui) {
		return shouldHighlight(gui.getSelectedSlot(), this) && gui.getSelectedSlot().shouldSlotHighlightItems() ? 3
		                                                                                                        : super.getTextureIndex(gui);
	}

	@Override
	public boolean shouldSlotHighlightItems() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
