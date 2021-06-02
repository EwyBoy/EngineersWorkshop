package engineers.workshop.client;

import com.mojang.blaze3d.platform.GlStateManager;
import engineers.workshop.client.container.ContainerTable;
import engineers.workshop.client.container.slot.SlotBase;
import engineers.workshop.client.page.Page;
import engineers.workshop.common.network.PacketHandler;
import engineers.workshop.common.network.PacketId;
import engineers.workshop.common.network.data.DataType;
import engineers.workshop.common.table.TileTable;
import engineers.workshop.common.util.ColorHelper;
import engineers.workshop.common.util.FormattingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class GuiTable extends GuiBase {

	private static final int HEADER_SRC_X = 0;
	private static final int HEADER_SRC_Y = 0;
	private static final int HEADER_FULL_WIDTH = 42;
	private static final int HEADER_WIDTH = 38;
	private static final int HEADER_HEIGHT = 17;
	private static final int HEADER_X = 3;
	private static final int HEADER_Y = 173;
	private static final int HEADER_TEXT_Y = 7;
	private static final int SLOT_SRC_X = 42;
	private static final int SLOT_SRC_Y = 0;
	private static final int SLOT_SIZE = 18;
	private static final int SLOT_OFFSET = -1;
	private static final int SLOT_BIG_SIZE = 26;
	private static final int SLOT_BIG_OFFSET = SLOT_OFFSET - (SLOT_BIG_SIZE - SLOT_SIZE) / 2;
	private static final int POWER_X = 225;
	private static final int POWER_Y = 173;
	private static final int POWER_WIDTH = 18;
	private static final int POWER_HEIGHT = 50;
	private static final int POWER_INNER_WIDTH = 16;
	private static final int POWER_INNER_HEIGHT = 48;
	private static final int POWER_INNER_SRC_X = 0;
	private static final int POWER_INNER_SRC_Y = 64;
	private static final int POWER_SRC_X = 32;
	private static final int POWER_SRC_Y = 62;
	private static final int POWER_INNER_OFFSET_X = (POWER_WIDTH - POWER_INNER_WIDTH) / 2;
	private static final int POWER_INNER_OFFSET_Y = (POWER_HEIGHT - POWER_INNER_HEIGHT) / 2;
	private TileTable table;
	private List<SlotBase> slots;
	private ContainerTable containerTable;
	private boolean closed = true;

	public GuiTable(TileTable table, PlayerEntity player) {
		super(new ContainerTable(table, player));

		containerWidth = 256;
		containerHeight = 256;
		slots = new ArrayList<>();

		for (Object obj : container.slotList) {
			SlotBase slot = (SlotBase) obj;
			slots.add(slot);
			slot.updateClient(slot.isVisible());
		}

		this.table = table;
		this.containerTable = (ContainerTable) this.container;
	}

	@Override
	protected void drawBackground(float v, int mX, int mY) {
		GlStateManager.pushMatrix();
		GlStateManager.translatef(left, top, 0);
		mX -= left;
		mY -= top;

		client.getTextureManager().bindTexture(BACKGROUND);
		GlStateManager.color3f(1F, 1F, 1F);
		drawRect(0, 0, 0, 0, containerWidth, containerHeight);

		drawSlots();
		if (table.getMenu() == null) {
			drawPageHeaders(mX, mY);
			drawPower(mX, mY);
			table.getSelectedPage().draw(this, mX, mY);
		} else {
			table.getMenu().draw(this, mX, mY);
		}
		GlStateManager.popMatrix();
	}

	@Override
	protected void drawForeground(int i, int i1) {

	}

	@Override
	public boolean mouseClicked(double mX, double mY, int button) {
		super.mouseClicked(mX, mY, button);
		mX -= left;
		mY -= top;

		if (table.getMenu() == null) {
			clickPageHeader(mX, mY);
			table.getSelectedPage().onClick(this, mX, mY, button);
		} else {
			table.getMenu().onClick(this, mX, mY);
		}
		return true;
	}

	@Override
	public boolean keyPressed(int c, int k, int var3) {
		if (table.getMenu() == null) {
			return super.keyPressed(c, k, var3);
		} else {
			if (k == 1) {
				this.client.player.closeGui();
			} else {
				table.getMenu().onKeyStroke(this, c, k);
			}
		}
		return true;
	}

	private void drawPageHeaders(int mX, int mY) {
		for (int i = 0; i < table.getPages().size(); i++) {
			Page page = table.getPages().get(i);

			boolean selected = page.equals(table.getSelectedPage());
			int srcY = selected ? HEADER_SRC_Y + HEADER_HEIGHT : HEADER_SRC_Y;
			int y = HEADER_Y + HEADER_HEIGHT * i;
			boolean hover = inBounds(HEADER_X, y, HEADER_FULL_WIDTH, HEADER_HEIGHT, mX, mY);
			if (hover) {
				drawMouseOver(page.getDesc());
			}
			int width = hover ? HEADER_FULL_WIDTH : HEADER_WIDTH;
			int offset = HEADER_FULL_WIDTH - width;

			prepare();
			drawRect(HEADER_X, y, HEADER_SRC_X + offset, srcY, width, HEADER_HEIGHT);

			int invertedOffset = (HEADER_FULL_WIDTH - HEADER_WIDTH) - offset;
			drawCenteredString(page.getName(), HEADER_X + invertedOffset, y + HEADER_TEXT_Y, HEADER_WIDTH, 0.7F, 0x2E2E2E);
		}
	}

	private void clickPageHeader(double mX, double mY) {
		for (int i = 0; i < table.getPages().size(); i++) {
			Page page = table.getPages().get(i);
			int y = HEADER_Y + HEADER_HEIGHT * i;
			if (inBounds(HEADER_X, y, HEADER_FULL_WIDTH, HEADER_HEIGHT, mX, mY)) {
				table.setSelectedPage(page);
				table.updateServer(DataType.PAGE);
				break;
			}
		}
	}

	private void drawSlots() {
		prepare();
		for (SlotBase slot : slots) {
			boolean visible = slot.isVisible();
			slot.updateClient(visible);
			if (visible) {
				boolean isBig = slot.isBig();
				int srcY = isBig ? SLOT_SIZE + SLOT_SRC_Y : SLOT_SRC_Y;
				int size = isBig ? SLOT_BIG_SIZE : SLOT_SIZE;
				int offset = isBig ? SLOT_BIG_OFFSET : SLOT_OFFSET;

				drawRect(slot.getX() + offset, slot.getY() + offset, SLOT_SRC_X + slot.getTextureIndex(this) * size, srcY, size, size);
			}
		}
	}

	private void drawPower(int mX, int mY) {
		prepare();

		drawRect(POWER_X + POWER_INNER_OFFSET_X, POWER_Y + POWER_INNER_OFFSET_Y, POWER_INNER_SRC_X + POWER_INNER_WIDTH, POWER_INNER_SRC_Y, POWER_INNER_WIDTH, POWER_INNER_HEIGHT);

		if(table.maxFuel == 0){
			return;
		}

		int height = POWER_INNER_HEIGHT * containerTable.power / table.maxFuel;
		int offset = POWER_INNER_HEIGHT - height;
		GlStateManager.color3f(ColorHelper.getRed(containerTable.power, getTable().maxFuel), ColorHelper.getGreen(containerTable.power, getTable().maxFuel), ColorHelper.getBlue(containerTable.power, getTable().maxFuel));
		drawRect(POWER_X + POWER_INNER_OFFSET_X, POWER_Y + POWER_INNER_OFFSET_Y + offset, POWER_INNER_SRC_X, POWER_INNER_SRC_Y + offset, POWER_INNER_WIDTH, height);
		drawRect(POWER_X, POWER_Y + POWER_INNER_OFFSET_Y + offset - 1, POWER_SRC_X, POWER_SRC_Y - 1, POWER_WIDTH, 1);
		int srcX = POWER_SRC_X;
		boolean hover = inBounds(POWER_X, POWER_Y, POWER_WIDTH, POWER_HEIGHT, mX, mY);
		if (hover)
			srcX += POWER_WIDTH;
		drawRect(POWER_X, POWER_Y, srcX, POWER_SRC_Y, POWER_WIDTH, POWER_HEIGHT);
		GlStateManager.color3f(1.0f, 1.0f, 1.0f);

		if (hover) {
			String str = ColorHelper.getPowerColor(containerTable.power, getTable().maxFuel) + "Fuel: " + FormattingHelper.formatNumber(containerTable.power) + " / " + FormattingHelper.formatNumber((int) table.maxFuel);
			drawMouseOver(str);
		}
	}

	@Override
	public void close() {
		super.close();
		if (!closed) {
			PacketHandler.sendToServer(PacketHandler.getPacket(table, PacketId.CLOSE));
			closed = true;
		}
	}

	@Override
	public void onScaleChanged(MinecraftClient minecraft, int width, int height) {
		super.onScaleChanged(minecraft, width, height);
		if (closed) {
			closed = false;
			PacketHandler.sendToServer(PacketHandler.getPacket(table, PacketId.RE_OPEN));
		}
	}

	public TileTable getTable() {
		return table;
	}
}
