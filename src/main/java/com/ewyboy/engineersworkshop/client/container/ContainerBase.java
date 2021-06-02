package engineers.workshop.client.container;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
    This is a client and therefore extends Container, however, to clean it all up all the Container code is included
    in this class and it's therefore not using any code in the Container class. However, to make this compatible as a
    client for other classes (interfaces and the current open client a player has for instance) it must still
    extend Container.
 */

@SuppressWarnings("unused")
public abstract class ContainerBase extends RebornContainer {

	private static final int MOUSE_LEFT_CLICK = 0;
	private static final int MOUSE_RIGHT_CLICK = 1;
	private static final int FAKE_SLOT_ID = -999;
	private static final int CLICK_MODE_NORMAL = 0;
	private static final int CLICK_MODE_SHIFT = 1;
	private static final int CLICK_MODE_KEY = 2;
	private static final int CLICK_MODE_PICK_ITEM = 3;
	private static final int CLICK_MODE_OUTSIDE = 4;
	private static final int CLICK_DRAG_RELEASE = 5;
	private static final int CLICK_MODE_DOUBLE_CLICK = 6;
	private static final int CLICK_DRAG_MODE_PRE = 0;
	private static final int CLICK_DRAG_MODE_SLOT = 1;
	private static final int CLICK_DRAG_MODE_POST = 2;
	private final Set<Slot> draggedSlots = new HashSet<>();
	private short transactionID;
	private int dragMouseButton = -1;
	private int dragMode;
	private Set<PlayerEntity> invalidPlayers = new HashSet<>();

	public ContainerBase(BlockEntity blockEntity) {
		super(blockEntity);
	}

	private List<ItemStack> getItems() {
		return stackList;
	}

	private List<Slot> getSlots() {
		return slotList;
	}

	@Override
	protected Slot addSlot(Slot slot) {
		slot.id = this.slotMap.size();
		getSlots().add(slot);
		getItems().add(ItemStack.EMPTY);
		super.addSlot(slot);
		return slot;
	}

	@Override
	public DefaultedList<ItemStack> getStacks() {
		DefaultedList<ItemStack> result = DefaultedList.create();
		getSlots().forEach(slot -> result.add(slot.getStack()));
		return result;
	}

	@Override
	public Slot getSlot(int slotId) {

		return getSlots().get(slotId);
	}

	@Override
	public void onContentChanged(Inventory inventory) {
		sendContentUpdates();
	}

	@Override
	public void setStackInSlot(int slotId, ItemStack item) {
		getSlot(slotId).setStack(item);
	}

	public short getNextTransactionID(PlayerInventory inventory) {
		transactionID++;
		return transactionID;
	}

	protected boolean isPlayerValid(PlayerEntity player) {
		return !invalidPlayers.contains(player);
	}

	protected void setValidState(PlayerEntity player, boolean valid) {
		if (valid) {
			invalidPlayers.remove(player);
		} else {
			invalidPlayers.add(player);
		}
	}

	protected void resetDragging() {
		dragMode = 0;
		draggedSlots.clear();
	}

	@Override
	public boolean method_7615(Slot slot) {
		return true;
	}

	protected int getSlotStackLimit(Slot slot, ItemStack itemStack) {
		return slot.getMaxStackAmount();
	}

}