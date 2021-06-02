package engineers.workshop.common.network.data;

import engineers.workshop.client.page.setting.*;
import engineers.workshop.common.table.TileTable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;

public abstract class DataSide extends DataBase {

	private static final int SETTINGS = 5;
	private static final int SIDES = 6;
	private static final int MODES = 2;
	public static final int LENGTH = SETTINGS * SIDES * MODES;

	public static int getId(Setting setting, Side side, Transfer transfer) {
		return setting.getId() + SETTINGS * side.getDirection().ordinal() + (transfer.isInput() ? 0 : SETTINGS * SIDES);
	}

	protected Transfer getTransfer(TileTable table, int id) {
		int settingId = id % SETTINGS;
		id /= SETTINGS;
		int sideId = id % SIDES;
		id /= SIDES;
		int modeId = id;
		Side side = table.getTransferPage().getSettings().get(settingId).getSides().get(sideId);
		if (modeId == 0) {
			return side.getInput();
		} else {
			return side.getOutput();
		}
	}

	public static class Enabled extends DataSide {
		@Override
		public void save(TileTable table, CompoundTag dw, int id) {
			dw.putBoolean("enabled", getTransfer(table, id).isEnabled());
		}

		@Override
		public void load(TileTable table, CompoundTag dr, int id) {
			getTransfer(table, id).setEnabled(dr.getBoolean("enabled"));
		}
	}

	public static class Auto extends DataSide {
		@Override
		public void save(TileTable table, CompoundTag dw, int id) {
			dw.putBoolean("auto", getTransfer(table, id).isAuto());
		}

		@Override
		public void load(TileTable table, CompoundTag dr, int id) {
			getTransfer(table, id).setAuto(dr.getBoolean("auto"));
		}
	}

	public static class WhiteList extends DataSide {
		@Override
		public void save(TileTable table, CompoundTag dw, int id) {
			dw.putBoolean("whitelist", getTransfer(table, id).hasWhiteList());
		}

		@Override
		public void load(TileTable table, CompoundTag dr, int id) {
			getTransfer(table, id).setUseWhiteList(dr.getBoolean("whitelist"));
		}
	}

	public static abstract class FilterBase extends DataSide {
		public static final int LENGTH = DataSide.LENGTH * ItemSetting.ITEM_COUNT;

		public static int getId(Setting setting, Side side, Transfer transfer, ItemSetting itemSetting) {
			return getId(setting, side, transfer) * ItemSetting.ITEM_COUNT + itemSetting.getId();
		}

		protected ItemSetting getSetting(TileTable table, int id) {
			return getTransfer(table, id / ItemSetting.ITEM_COUNT).getItem(id % ItemSetting.ITEM_COUNT);
		}
	}

	public static class Filter extends FilterBase {
		@Override
		public void save(TileTable table, CompoundTag dw, int id) {
			ItemSetting setting = getSetting(table, id);
			ItemStack itemStack = setting.getItem();

			dw.putBoolean("hasItem", !itemStack.isEmpty());
			if (!itemStack.isEmpty()) {
				dw.putInt("id", Registry.ITEM.getRawId(itemStack.getItem()));
				if (itemStack.hasTag()) {
					dw.put("nbt", itemStack.getTag());
				}

			}
		}

		@Override
		public void load(TileTable table, CompoundTag dr, int id) {
			ItemSetting setting = getSetting(table, id);

			if (dr.getBoolean("hasItem")) {
				int itemId = dr.getInt("id");

				ItemStack item = new ItemStack(Registry.ITEM.getInt(itemId), 1);
				if (dr.containsKey("nbt")) {
					item.setTag(dr.getCompound("nbt"));
				}

				setting.setItem(item);
			} else {
				setting.setItem(ItemStack.EMPTY);
			}
		}
	}

	public static class FilterMode extends FilterBase {
		@Override
		public void save(TileTable table, CompoundTag dw, int id) {
			dw.putInt("mode", getSetting(table, id).getMode().ordinal());
		}

		@Override
		public void load(TileTable table, CompoundTag dr, int id) {
			getSetting(table, id).setMode(TransferMode.values()[dr.getInt("mode")]);
		}
	}
}
