package engineers.workshop.client.container;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;

import java.util.List;

public interface IContainerLayout<T extends BlockEntity> {

	public void addInventorySlots();

	public void addPlayerSlots();

	public void setTile(T tile);

	public
	T getTile();

	public void setPlayer(PlayerEntity player);

	public
	PlayerEntity getPlayer();

	public List<Integer> getSlotsForSide(Direction direction);
}