package engineers.workshop.common.network.data;

import engineers.workshop.common.table.TileTable;
import engineers.workshop.common.unit.Unit;
import net.minecraft.nbt.CompoundTag;

public abstract class DataUnit extends DataBase {

	public static final int LENGTH = 8;

	public static int getId(Unit unit) {
		return unit.getId() * 2;
	}

	protected Unit getUnit(TileTable table, int id) {
		id /= 2;

		Unit smelt = table.getMainPage().getSmeltingList().get(id);
		Unit craft = table.getMainPage().getCraftingList().get(id);
		Unit storage = table.getMainPage().getStorageList().get(id);

		if (smelt.isEnabled())
			return smelt;
		else if (storage.isEnabled())
			return storage;
		else
			return craft;
	}

	public static class Progress extends DataUnit {

		@Override
		public void save(TileTable table, CompoundTag dw, int id) {
			dw.putInt("progress", getUnit(table, id).getProductionProgress());
		}

		@Override
		public void load(TileTable table, CompoundTag dr, int id) {
			getUnit(table, id).setProductionProgress(dr.getInt("progress"));
		}
	}

	public static class Charged extends DataUnit {

		@Override
		public void save(TileTable table, CompoundTag dw, int id) {
			dw.putInt("id", id);
			dw.putInt("charge", getUnit(table, id).getChargeCount());
		}

		@Override
		public void load(TileTable table, CompoundTag dr, int id) {
			getUnit(table, id).setChargeCount(dr.getInt("charge"));
		}
	}

}
