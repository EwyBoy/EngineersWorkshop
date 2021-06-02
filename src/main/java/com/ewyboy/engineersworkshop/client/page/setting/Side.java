package engineers.workshop.client.page.setting;

import engineers.workshop.common.items.Upgrade;
import net.minecraft.text.TextFormat;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Side {
	private int x;
	private int y;
	private Direction direction;
	private Setting setting;
	private Transfer input;
	private Transfer output;

	public Side(Setting setting, Direction direction, int x, int y) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.setting = setting;

		input = new Transfer(true);
		output = new Transfer(false);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isOutputEnabled() {
		return output.isEnabled();
	}

	public void setOutputEnabled(boolean value) {
		output.setEnabled(value);
	}

	public boolean isInputEnabled() {
		return input.isEnabled();
	}

	public void setInputEnabled(boolean value) {
		input.setEnabled(value);
	}

	public Direction getDirection() {
		return direction;
	}

	public Setting getSetting() {
		return setting;
	}

	public Transfer getOutput() {
		return output;
	}

	public Transfer getInput() {
		return input;
	}

	public List<String> getDescription(boolean selected) {
		List<String> str = new ArrayList<String>();
		str.add(StringUtils.capitalize(direction.getName()));

		if (selected) {
			str.add(TextFormat.YELLOW + "Selected");
		}

		str.add("");
		addTransferInfo(str, input, TextFormat.BLUE);
		addTransferInfo(str, output, TextFormat.RED);

		return str;
	}

	private void addTransferInfo(List<String> lst, Transfer transfer, TextFormat color) {
		String name = transfer.isInput() ? "Input" : "Output";
		if (transfer.isEnabled()) {
			lst.add(color + name + ": Enabled");
			if (transfer.isAuto() && setting.table.getUpgradePage().hasGlobalUpgrade(Upgrade.AUTO_TRANSFER)) {
				lst.add(TextFormat.GRAY + name + " Transfer: " + TextFormat.GREEN + "Auto");
			}
			if (transfer.hasFilter(setting.table)) {
				if (transfer.hasWhiteList()) {
					lst.add(TextFormat.GRAY + name + " Filter: " + TextFormat.WHITE + "White list");
				} else {
					lst.add(TextFormat.GRAY + name + " Filter: " + TextFormat.DARK_GRAY + "Black list");
				}
			}
		} else {
			lst.add(TextFormat.GRAY + name + ": Disabled");
		}
	}
}
