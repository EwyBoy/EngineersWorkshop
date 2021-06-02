package engineers.workshop.client.container.slot;

import engineers.workshop.client.GuiBase;
import engineers.workshop.client.page.setting.Transfer;
import engineers.workshop.common.table.TileTable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.Slot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class SlotBase extends Slot {
	protected TileTable table;
	protected boolean isEnabled = true;
	private int x;
	private int y;
	private Transfer[] input = new Transfer[6];
	private Transfer[] output = new Transfer[6];

	public SlotBase(Inventory inventory, TileTable table, int id, int x, int y) {
		super(inventory, id, x, y);

		this.x = x;
		this.y = y;
		this.table = table;
	}

	protected static boolean shouldHighlight(SlotBase slot, SlotBase other) {
		return MinecraftClient.getInstance().player.inventory.getMainHandStack().isEmpty() && slot != null
			&& !slot.hasStack() && other != null && other.hasStack() && slot.canAcceptItem(other.getStack())
			&& slot.getSlotStackLimit(other.getStack()) > (slot.hasStack() ? slot.getStack().getAmount() : 0);
	}

	public void updateClient(boolean visible) {
		if (visible && isEnabled()) {
			x = getX();
			y = getY();
		} else {
			x = -9000;
			y = -9000;
		}
	}

	public void updateServer() {
		if (!isEnabled() && hasStack()) {
			table.spitOutItem(getStack());
			setStack(ItemStack.EMPTY);
		}

		if (hasStack() && getStack().getAmount() == 0) {
			setStack(ItemStack.EMPTY);
		}
	}

	@Override
	public boolean canInsert(ItemStack itemstack) {
		return isEnabled();
	}

	public boolean isVisible() {
		return table.getMenu() == null;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean f) {
		isEnabled = f;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getTextureIndex(GuiBase gui) {
		return isEnabled() ? 0 : 1;
	}

	public boolean isBig() {
		return false;
	}

	public boolean isOutputValid(int id, ItemStack item) {
		return output[id] != null && output[id].isValid(table, item);
	}

	public boolean isInputValid(int id, ItemStack item) {
		return input[id] != null && input[id].isValid(table, item);
	}

	public void resetValidity(int id) {
		this.output[id] = null;
		this.input[id] = null;
	}

	public void setValidity(int id, Transfer input, Transfer output) {
		this.output[id] = output;
		this.input[id] = input;
	}

	public boolean canAcceptItems() {
		return true;
	}

	public boolean canSupplyItems() {
		return true;
	}

	public boolean canAcceptItem(ItemStack item) {
		return true;
	}

	@Override
	public int getMaxStackAmount() {
		return getSlotStackLimit(ItemStack.EMPTY);
	}

	public int getSlotStackLimit(ItemStack item) {
		return super.getMaxStackAmount();
	}

	public boolean canPickUpOnDoubleClick() {
		return isVisible() && isEnabled();
	}

	public boolean canDragIntoSlot() {
		return true;
	}

	public boolean canShiftClickInto(ItemStack item) {
		return true;
	}

	public boolean shouldSlotHighlightItems() {
		return true;
	}

	public boolean shouldSlotHighlightSelf() {
		return true;
	}

	public boolean shouldDropOnClosing() {
		return true;
	}
}