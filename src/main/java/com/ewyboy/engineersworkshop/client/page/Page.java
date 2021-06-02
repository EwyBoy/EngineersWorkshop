package engineers.workshop.client.page;

import engineers.workshop.client.GuiBase;
import engineers.workshop.client.GuiTable;
import engineers.workshop.client.container.slot.SlotBase;
import engineers.workshop.common.table.TileTable;
import org.apache.commons.lang3.StringUtils;

public abstract class Page {

	protected TileTable table;
	private String name;
	private int id;

	public Page(TileTable table, String name) {
		this.id = table.getPages().size();
		this.table = table;
		this.name = name;
	}

	public String getName() {
		return name.toUpperCase();
	}

	public int createSlots(int id) {
		return id;
	}

	public String getDesc() {
		return "Add a description!";
	}

	protected void addSlot(SlotBase slot) {
		table.addSlot(slot);
	}

	public void draw(GuiBase gui, int mX, int mY) {
		gui.drawString(StringUtils.capitalize(name), 8, 6, 0x1E1E1E);
	}

	public void onClick(GuiBase gui, double mX, double mY, int button) {
	}

	public int getId() {
		return id;
	}

	public void onUpdate() {
	}

	public void onRelease(GuiTable gui, int mX, int mY, int button) {
	}
}
