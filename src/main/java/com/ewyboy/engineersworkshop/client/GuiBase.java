package engineers.workshop.client;

import com.mojang.blaze3d.platform.GlStateManager;
import engineers.workshop.client.container.slot.SlotBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ContainerGui;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.item.TooltipOptions;
import net.minecraft.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static engineers.workshop.common.util.Reference.Info.MODID;

public abstract class GuiBase extends ContainerGui {
	protected static final Identifier BACKGROUND = new Identifier(MODID, "textures/gui/background.png");
	protected static final Identifier ELEMENTS = new Identifier(MODID, "textures/gui/elements_gl.png");
	/**
	 * TODO: private static final ResourceLocation BLOCK_TEXTURE = new
	 * ResourceLocation("textures/atlas/block.png"); public void
	 * drawBlockIcon(IIcon icon, int x, int y) {
	 * mc.getTextureManager().bindTexture(BLOCK_TEXTURE); drawIcon(icon, x, y);
	 * }
	 *
	 * public void drawIcon(IIcon icon, int x, int y) { drawIcon(icon, x, y, 1F,
	 * 1F, 0F, 0F); }
	 *
	 * public void drawIcon(IIcon icon, int targetX, int targetY, float sizeX,
	 * float sizeY, float offsetX, float offsetY) { Tessellator tessellator =
	 * Tessellator.instance; tessellator.startDrawingQuads();
	 *
	 * float x = icon.getMinU() + offsetX * (icon.getMaxU() - icon.getMinU());
	 * float y = icon.getMinV() + offsetY * (icon.getMaxV() - icon.getMinV());
	 * float width = (icon.getMaxU() - icon.getMinU()) * sizeX; float height =
	 * (icon.getMaxV() - icon.getMinV()) * sizeY;
	 *
	 * tessellator.addVertexWithUV(targetX, targetY + 16 * sizeY, this.zLevel,
	 * x, y + height); tessellator.addVertexWithUV(targetX + 16 * sizeX, targetY
	 * + 16 * sizeY, this.zLevel, x + width, y + height);
	 * tessellator.addVertexWithUV(targetX + 16 * sizeX, targetY, this.zLevel, x
	 * + width, y); tessellator.addVertexWithUV(targetX, targetY, this.zLevel,
	 * x, y); tessellator.draw(); }
	 **/

	private static final int ITEM_SIZE = 18;
	private static final int ITEM_SRC_X = 68;
	private static final int ITEM_SRC_Y = 62;
	private static final int ITEM_ITEM_OFFSET = 1;
	public boolean shiftMoveRendered;
	private SlotBase selectedSlot;
	private List<String> mouseOver;

	public GuiBase(Container container) {
		super(container);
	}

	@Override
	public void draw(int x, int y, float f) {
		this.drawBackground();
		selectedSlot = null;
		shiftMoveRendered = false;
		for (Object obj : container.slotList) {
			SlotBase slot = (SlotBase) obj;
			if (inBounds(slot.getX(), slot.getY(), 16, 16, x, y)) {
				selectedSlot = slot;
				break;
			}
		}
		clearMouseOverCache();

		super.draw(x, y, f);
		this.drawMousoverTooltip(x, y);
	}

	public SlotBase getSelectedSlot() {
		return selectedSlot;
	}

	public void prepare() {
		client.getTextureManager().bindTexture(ELEMENTS);
		GlStateManager.color4f(1, 1, 1, 1);
		GlStateManager.disableLighting();
	}

	public boolean inBounds(double x, double y, double w, double h, double mX, double mY) {
		return x <= mX && mX < x + w && y <= mY && mY < y + h;
	}

	public void drawRect(int x, int y, int u, int v, int w, int h) {
		super.drawTexturedRect(x, y, u, v, w, h);
	}

	public void drawString(String str, int x, int y, int color) {
		drawString(str, x, y, 1F, color);
	}

	public void drawString(String str, int x, int y, float multiplier, int color) {
		GlStateManager.pushMatrix();
		GlStateManager.scalef(multiplier, multiplier, 1F);
		fontRenderer.draw(str, (int) (x / multiplier), (int) (y / multiplier), color);

		GlStateManager.popMatrix();
	}

	public void drawCenteredString(String str, int x, int y, int width, float multiplier, int color) {
		drawString(str, x + (width - (int) (fontRenderer.getStringWidth(str) * multiplier)) / 2, y, multiplier, color);
	}

	public void drawItem(ItemStack item, int x, int y) {
		//RenderHelper.enableGUIStandardItemLighting();
		if (!item.isEmpty())
			itemRenderer.renderItemAndGlowInGui(item, x, y);
	}

	public void drawItemWithBackground(ItemStack item, int x, int y, int mX, int mY) {
		boolean hover = inBounds(x, y, ITEM_SIZE, ITEM_SIZE, mX, mY);
		int textureIndexX = hover ? 1 : 0;
		int textureIndexY = !item.isEmpty() ? 1 : 0;

		prepare();
		drawRect(x, y, ITEM_SRC_X + textureIndexX * ITEM_SIZE, ITEM_SRC_Y + textureIndexY * ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
		drawItem(item, x + ITEM_ITEM_OFFSET, y + ITEM_ITEM_OFFSET);

		if (hover) {
			drawMouseOver(getItemDescription(item));
		}
	}

	public int getStringWidth(String str) {
		return fontRenderer.getStringWidth(str);
	}

	public void drawCursor(int x, int y, int z, float size, int color) {
		GlStateManager.pushMatrix();
		GlStateManager.translatef(x, y, z);
		GlStateManager.scalef(size, size, 0);
		GlStateManager.translatef(-x, -y, 0);
		Gui.drawRect(x, y + 1, x + 1, y + 10, color);
		GlStateManager.popMatrix();
	}

	public void drawMouseOver(String str) {
		if (str == null) {
			return;
		}

		List<String> lst = new ArrayList<>();
		Collections.addAll(lst, str.split("\n"));
		drawMouseOver(lst);
	}

	public void drawMouseOver(List<String> str) {
		this.mouseOver = str;
	}

	public void clearMouseOverCache() {
		mouseOver = null;
	}

	@Override
	protected void drawForeground(int mX, int mY) {
		drawCachedMouseOver(mX - left, mY - top );
	}

	public void drawCachedMouseOver(int x, int y) {
		if (mouseOver == null || mouseOver.isEmpty()) {
			return;
		}

		int w = 0;

		for (String line : mouseOver) {
			int l = fontRenderer.getStringWidth(line);

			if (l > w) {
				w = l;
			}
		}

		x += 12;
		y -= 12;
		int h = 8;

		if (mouseOver.size() > 1) {
			h += 2 + (mouseOver.size() - 1) * 10;
		}

		if (left + x + w > this.width) {
			x -= 28 + w;
		}

		if (top + y + h + 6 > this.height) {
			y = this.height - h - 6 - top;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translatef(0, 0, 300);
		this.zOffset = 300.0F;
		int bg = -267386864;
		this.drawGradientRect(x - 3, y - 4, x + w + 3, y - 3, bg, bg);
		this.drawGradientRect(x - 3, y + h + 3, x + w + 3, y + h + 4, bg, bg);
		this.drawGradientRect(x - 3, y - 3, x + w + 3, y + h + 3, bg, bg);
		this.drawGradientRect(x - 4, y - 3, x - 3, y + h + 3, bg, bg);
		this.drawGradientRect(x + w + 3, y - 3, x + w + 4, y + h + 3, bg, bg);
		int border1 = 1347420415;
		int border2 = (border1 & 16711422) >> 1 | border1 & -16777216;
		this.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + h + 3 - 1, border1, border2);
		this.drawGradientRect(x + w + 2, y - 3 + 1, x + w + 3, y + h + 3 - 1, border1, border2);
		this.drawGradientRect(x - 3, y - 3, x + w + 3, y - 3 + 1, border1, border1);
		this.drawGradientRect(x - 3, y + h + 2, x + w + 3, y + h + 3, border2, border2);
		GlStateManager.disableDepthTest();

		for (int i = 0; i < mouseOver.size(); i++) {
			String line = mouseOver.get(i);
			fontRenderer.drawWithShadow(line, x, y, -1);

			if (i == 0) {
				y += 2;
			}

			y += 10;
		}

		this.zOffset = 0.0F;
		GlStateManager.popMatrix();
		GlStateManager.enableDepthTest();
		GlStateManager.color4f(1F, 1F, 1F, 1F);
	}

	public String getItemName(ItemStack item) {
		if (item.isEmpty()) {
			return null;
		}

		try {
			// noinspection unchecked
			return item.getDisplayName().toString();
		} catch (Throwable ignored) {
			return null;
		}
	}

	public List<String> getItemDescription(ItemStack item) {
		if (item.isEmpty()) {
			return null;
		}

		try {
			return item.getTooltipText(MinecraftClient.getInstance().player, MinecraftClient.getInstance().options.advancedItemTooltips ? TooltipOptions.Instance.ADVANCED : TooltipOptions.Instance.NORMAL).stream().map(Object::toString).collect(Collectors.toList());
		} catch (Throwable ignored) {
			return null;
		}
	}
}
