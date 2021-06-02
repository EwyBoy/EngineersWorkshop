package engineers.workshop.common.network.data;

import engineers.workshop.common.table.TileTable;
import net.minecraft.nbt.CompoundTag;

public class DataLit extends DataBase {
	@Override
	public void save(TileTable table, CompoundTag dw, int id) {
		dw.putBoolean("lit", table.isLit());
	}

	@Override
	public void load(TileTable table, CompoundTag dr, int id) {
		table.setLit(dr.getBoolean("lit"));
	}
}
