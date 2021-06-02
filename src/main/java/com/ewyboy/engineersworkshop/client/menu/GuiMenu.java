package engineers.workshop.client.menu;

import engineers.workshop.client.GuiBase;
import engineers.workshop.client.component.Button;
import engineers.workshop.common.table.TileTable;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiMenu {

	protected List<Button> buttons;
	protected TileTable table;

	public GuiMenu(TileTable table) {
		this.table = table;
		buttons = new ArrayList<>();
		buttons.add(new Button("Save", 150, 230) {
			@Override
			public void clicked() {
				save();
				close();
			}
		});

		buttons.add(new Button("Cancel", 200, 230) {
			@Override
			public void clicked() {
				close();
			}
		});
	}

	public void draw(GuiBase gui, int mX, int mY) {
		for (Button button : buttons) {
			button.draw(gui, mX, mY);
		}
	}

	public void onClick(GuiBase gui, double mX, double mY) {
		for (Button button : buttons) {
			button.onClick(gui, mX, mY);
		}
	}

	public void onRelease(GuiBase gui, int mX, int mY) {}

	public void onKeyStroke(GuiBase gui, int c, int k) {}

	protected abstract void save();

	protected void close() {
		table.setMenu(null);
	}
}
