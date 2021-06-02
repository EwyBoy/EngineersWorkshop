package engineers.workshop.client.container.slot.smelting;

import engineers.workshop.client.container.slot.SlotUnit;
import engineers.workshop.client.page.Page;
import engineers.workshop.common.table.TileTable;
import engineers.workshop.common.unit.Unit;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
public class SlotUnitFurnaceResult extends SlotUnit {

	public SlotUnitFurnaceResult(TileTable table, Page page, int id, int x, int y, Unit unit) {
		super(table, page, id, x, y, unit);
	}

	@Override
	public boolean isBig() {
		return true;
	}

	@Override
	public boolean canAcceptItem(ItemStack itemstack) {
		return false;
	}

	@Override
	public boolean canSupplyItems() {
		return true;
	}

	@Override
	public boolean canAcceptItems() {
		return false;
	}

	@Override
	public ItemStack onTakeItem(PlayerEntity player, ItemStack item) {
		item = super.onTakeItem(player, item);
		item.onCrafted(player.getEntityWorld(), player, item.getAmount());
		givePlayerXP(item, player);
		return item;
	}

	//Taken from vanilla
	public void givePlayerXP(ItemStack stack, PlayerEntity player) {
		if (!player.world.isRemote) {
			int stackSize = stack.getAmount();
			float experience = 1F ;//TODO FurnaceRecipes.instance().getSmeltingExperience(stack);
			if (experience == 0.0F) {
				stackSize = 0;
			} else if (experience < 1.0F) {
				int xpCount = MathHelper.floor((float) stackSize * experience);
				if (xpCount < MathHelper.ceil((float) stackSize * experience) && Math.random() < (double) ((float) stackSize * experience - (float) xpCount)) {
					++xpCount;
				}
				stackSize = xpCount;
			}
			while (stackSize > 0) {
				int xpSplit = ExperienceOrbEntity.roundToOrbSize(stackSize);
				stackSize -= xpSplit;
				player.world.spawnEntity(new ExperienceOrbEntity(player.world, player.x, player.y + 0.5D, player.z + 0.5D, xpSplit));
			}
		}
	}

}
