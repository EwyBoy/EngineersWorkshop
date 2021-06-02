package engineers.workshop.client.container;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Optional;

public abstract class RebornContainer extends Container {
	private static HashMap<String, RebornContainer> containerMap = new HashMap<>();
	public HashMap<Integer, Slot> slotMap = new HashMap<>();

	private Optional<BlockEntity> baseTile = Optional.empty();

	@Deprecated //TODO remove in 1.13 to use tile senstive version
	public RebornContainer() {
	}

	public RebornContainer(BlockEntity blockEntity){
		this.baseTile = Optional.of(blockEntity);
	}

	public static RebornContainer createContainer(Class<? extends RebornContainer> clazz, BlockEntity blockEntity, PlayerEntity player) {
		if (player == null && containerMap.containsKey(clazz.getCanonicalName())) {
			return containerMap.get(clazz.getCanonicalName());
		} else {
			try {
				RebornContainer container = null;
				for (Constructor constructor : clazz.getConstructors()) {
					if (constructor.getParameterCount() == 0) {
						container = clazz.newInstance();
						if (container instanceof IContainerLayout) {
							((IContainerLayout) container).setTile(blockEntity);
							((IContainerLayout) container).addInventorySlots();
						}
						continue;
					} else if (constructor.getParameterCount() == 2) {
						Class[] paramTypes = constructor.getParameterTypes();
						if (paramTypes[0].isInstance(blockEntity) && paramTypes[1] == PlayerEntity.class) {
							container = clazz.getDeclaredConstructor(blockEntity.getClass(), PlayerEntity.class).newInstance(blockEntity, player);
							continue;
						} else if (paramTypes[0] == PlayerEntity.class && paramTypes[1].isInstance(blockEntity)) {
							container = clazz.getDeclaredConstructor(PlayerEntity.class, blockEntity.getClass()).newInstance(player, blockEntity);
							continue;
						}
					}
				}
				if (container == null) {
					throw new RuntimeException("Failed to create container for " + clazz.getName() + " bad things may happen, please report to devs");
				}
				containerMap.put(clazz.getCanonicalName(), container);
				return container;
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new RuntimeException("Failed to setup container", e);
			}
		}
	}

	public static boolean canStacksMerge(ItemStack stack1, ItemStack stack2) {
		if (stack1.isEmpty() || stack2.isEmpty()) {
			return false;
		}
		if (!stack1.isEqualIgnoreTags(stack2)) {
			return false;
		}
		if (!ItemStack.areTagsEqual(stack1, stack2)) {
			return false;
		}
		return true;

	}

	@Override
	protected Slot addSlot(Slot slotIn) {
		Slot slot = super.addSlot(slotIn);
		slotMap.put(slot.id,  slot);
		return slot;
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int slotIndex) {
		ItemStack originalStack = ItemStack.EMPTY;
		Slot slot = (Slot) slotMap.get(slotIndex);
		int numSlots = slotMap.size();
		if (slot != null && slot.hasStack()) {
			ItemStack stackInSlot = slot.getStack();
			originalStack = stackInSlot.copy();
			if (slotIndex >= numSlots - 9 * 4 && tryShiftItem(stackInSlot, numSlots)) {
				// NOOP
			} else if (slotIndex >= numSlots - 9 * 4 && slotIndex < numSlots - 9) {
				if (!shiftItemStack(stackInSlot, numSlots - 9, numSlots)) {
					return ItemStack.EMPTY;
				}
			} else if (slotIndex >= numSlots - 9 && slotIndex < numSlots) {
				if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots - 9)) {
					return ItemStack.EMPTY;
				}
			} else if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots)) {
				return ItemStack.EMPTY;
			}
			slot.onStackChanged(stackInSlot, originalStack);
			if (stackInSlot.getAmount() <= 0) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
			if (stackInSlot.getAmount() == originalStack.getAmount()) {
				return ItemStack.EMPTY;
			}
			slot.onTakeItem(player, stackInSlot);
		}
		return originalStack;
	}

	protected boolean shiftItemStack(ItemStack stackToShift, int start, int end) {
		boolean changed = false;
		if (stackToShift.canStack()) {
			for (int slotIndex = start; stackToShift.getAmount() > 0 && slotIndex < end; slotIndex++) {
				Slot slot = (Slot) slotMap.get(slotIndex);
				ItemStack stackInSlot = slot.getStack();
				if (!stackInSlot.isEmpty() && canStacksMerge(stackInSlot, stackToShift)) {
					int resultingStackSize = stackInSlot.getAmount() + stackToShift.getAmount();
					int max = Math.min(stackToShift.getMaxAmount(), slot.getMaxStackAmount());
					if (resultingStackSize <= max) {
						stackToShift.setAmount(0);
						stackInSlot.setAmount(resultingStackSize);
						slot.markDirty();
						changed = true;
					} else if (stackInSlot.getAmount() < max) {
						stackToShift.setAmount(stackToShift.getAmount()-(max-stackInSlot.getAmount()));
						stackInSlot.setAmount(max);
						slot.markDirty();
						changed = true;
					}
				}
			}
		}
		if (stackToShift.getAmount() > 0) {
			for (int slotIndex = start; stackToShift.getAmount() > 0 && slotIndex < end; slotIndex++) {
				Slot slot = (Slot) slotMap.get(slotIndex);
				ItemStack stackInSlot = slot.getStack();
				if (stackInSlot.isEmpty()) {
					int max = Math.min(stackToShift.getMaxAmount(), slot.getMaxStackAmount());
					stackInSlot = stackToShift.copy();
					stackInSlot.setAmount(Math.min(stackToShift.getMaxAmount(), max));
					stackToShift.setAmount(stackToShift.getAmount()-stackInSlot.getAmount());
					slot.setStack(stackInSlot);
					slot.markDirty();
					changed = true;
				}
			}
		}
		return changed;
	}

	private boolean tryShiftItem(ItemStack stackToShift, int numSlots) {
		for (int machineIndex = 0; machineIndex < numSlots - 9 * 4; machineIndex++) {
			Slot slot = (Slot) slotMap.get(machineIndex);
			if (!slot.canInsert(stackToShift))
				continue;
			if (shiftItemStack(stackToShift, machineIndex, machineIndex + 1))
				return true;
		}
		return false;
	}

	public void addPlayersHotbar(PlayerEntity player) {
		int i;
		for (i = 0; i < 9; ++i) {
			this.addSlot(new Slot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	public void addPlayersInventory(PlayerEntity player) {
		int i;
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
	}

	public void drawPlayersInv(PlayerEntity player) {
		drawPlayersInv(player, 8, 81);
		//		int i;
		//		for (i = 0; i < 3; ++i)
		//        {
		//			for (int j = 0; j < 9; ++j)
		//            {
		//				this.addSlotToContainer(new BaseSlot(player.inventory, j + i * 9 + 9, 8 + j * 18, 81 + i * 18));
		//			}
		//		}

	}

	public void drawPlayersHotBar(PlayerEntity player) {
		drawPlayersHotBar(player, 8, 139);
		//		int i;
		//		for (i = 0; i < 9; ++i)
		//        {
		//			this.addSlotToContainer(new BaseSlot(player.inventory, i, 8 + i * 18, 139));
		//		}
	}

	public void drawPlayersInv(PlayerEntity player, int x, int y) {
		int i;
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(player.inventory, j + i * 9 + 9, x + j * 18, y + i * 18));
			}
		}

	}

	public void drawPlayersHotBar(PlayerEntity player, int x, int y) {
		int i;
		for (i = 0; i < 9; ++i) {
			this.addSlot(new Slot(player.inventory, i, x + i * 18, y));
		}
	}

	public void drawPlayersInvAndHotbar(PlayerEntity player) {
		drawPlayersInv(player);
		drawPlayersHotBar(player);
	}

	public void drawPlayersInvAndHotbar(PlayerEntity player, int x, int y) {
		drawPlayersInv(player, x, y);
		drawPlayersHotBar(player, x, y + 58);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		if(baseTile.isPresent()){
			World world = player.getEntityWorld();
			BlockPos pos = baseTile.get().getPos();
			return world.getBlockEntity(pos) == baseTile.get() && player.distanceTo(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
		}
		return true;
	}
}