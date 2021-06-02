package engineers.workshop.common.network.data;

import engineers.workshop.common.table.TileTable;
import net.minecraft.nbt.CompoundTag;

public class DataPage extends DataBase {

	@Override
	public void save(TileTable table, CompoundTag dw, int id) {
		dw.putInt("page", table.getSelectedPage().getId());
	}

	@Override
	public void load(TileTable table, CompoundTag dr, int id) {
		if (id >= table.getPages().size()) {
			return;
		}
		table.setSelectedPage(table.getPages().get(dr.getInt("page")));
	}
}
