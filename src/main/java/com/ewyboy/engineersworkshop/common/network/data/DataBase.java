package engineers.workshop.common.network.data;

import engineers.workshop.common.table.TileTable;
import net.minecraft.nbt.CompoundTag;

public abstract class DataBase {

	public abstract void save(TileTable table, CompoundTag dw, int id);

	public abstract void load(TileTable table, CompoundTag dr, int id);

	public boolean shouldBounce(TileTable table) {
		return true;
	}

	public boolean shouldBounceToAll(TileTable table) {
		return false;
	}
}
