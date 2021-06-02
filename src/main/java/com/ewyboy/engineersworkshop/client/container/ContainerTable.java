package engineers.workshop.client.container;

import engineers.workshop.client.container.slot.SlotBase;
import engineers.workshop.client.container.slot.SlotPlayer;
import engineers.workshop.common.table.TileTable;
import net.minecraft.container.ContainerListener;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class ContainerTable extends ContainerBase {

	private static final int SLOT_SIZE = 18;
	private static final int SLOTS_PER_ROW = 9;
	private static final int NORMAL_ROWS = 3;
	private static final int PLAYER_X = 48;
	private static final int PLAYER_Y = 174;
	private static final int PLAYER_HOT_BAR_Y = 232;
	public int power;
	private TileTable table;
	public ContainerTable(TileTable table, PlayerEntity player) {
		super(table);
		this.table = table;

		table.getSlots().forEach(this::addSlot);
		PlayerInventory inventory = player.inventory;

		for (int y = 0; y < NORMAL_ROWS; y++) {
			for (int x = 0; x < SLOTS_PER_ROW; x++) {
				addSlot(new SlotPlayer(inventory, table, x + y * SLOTS_PER_ROW + SLOTS_PER_ROW, PLAYER_X + x * SLOT_SIZE, y * SLOT_SIZE + PLAYER_Y));
			}
		}

		for (int x = 0; x < SLOTS_PER_ROW; x++) {
			addSlot(new SlotPlayer(inventory, table, x, PLAYER_X + x * SLOT_SIZE, PLAYER_HOT_BAR_Y));
		}

		this.syncId = 122;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return table.canPlayerUseInv(player);
	}

	@Override
	protected int getSlotStackLimit(Slot slot, ItemStack item) {
		return ((SlotBase) slot).getSlotStackLimit(item);
	}

	@Override
	public boolean method_7615(Slot slot) {
		return ((SlotBase) slot).canDragIntoSlot();
	}

	public TileTable getTable() {
		return table;
	}

	@Override
	public void addListener(ContainerListener listener) {
		super.addListener(listener);
		listener.onContainerInvRegistered(this, table);
	}

	@Override
	public void sendContentUpdates() {
		super.sendContentUpdates();

		for (int i = 0; i < this.listeners.size(); ++i) {
			ContainerListener icontainerlistener = this.listeners.get(i);
			if (this.power != table.getFuel()) {
				icontainerlistener.onContainerPropertyUpdate(this, 0, table.getFuel());
			}
		}
	}

	@Override
	public void setProperty(int id, int data) {
		super.setProperty(id, data);
		if (id == 0) {
			this.power = data;
		}
	}
}
