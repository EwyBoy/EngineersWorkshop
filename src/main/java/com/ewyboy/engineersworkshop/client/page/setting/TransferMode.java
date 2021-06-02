package engineers.workshop.client.page.setting;

import net.fabricmc.fabric.tags.TagRegistry;
import net.minecraft.item.ItemStack;

public enum TransferMode {
	PRECISE("Precise detection") {
		@Override
		public boolean isMatch(ItemStack item1, ItemStack item2) {
			return item1.getItem() == item2.getItem() && ItemStack.areTagsEqual(item1, item2);
		}
	},
	NBT_INDEPENDENT("NBT independent detection") {
		@Override
		public boolean isMatch(ItemStack item1, ItemStack item2) {
			return item1.getItem() == item2.getItem();
		}
	},
	FUZZY("Fuzzy detection") {
		@Override
		public boolean isMatch(ItemStack item1, ItemStack item2) {
			return item1.getItem() == item2.getItem();
		}
	},
	ORE_DICTIONARY("Tag detection") {
		@Override
		public boolean isMatch(ItemStack item1, ItemStack item2) {
			throw new RuntimeException("Add this");
		}
	};

	private String name;

	TransferMode(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public abstract boolean isMatch(ItemStack item1, ItemStack item2);
}
