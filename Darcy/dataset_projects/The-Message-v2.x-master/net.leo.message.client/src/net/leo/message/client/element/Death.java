package net.leo.message.client.element;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import net.leo.message.base.lang.Identity;

public enum Death {

	RED_LOSE("潛伏失敗"),
	BLUE_LOSE("軍情失敗"),
	PASSERBY_LOSE("醬油失敗"),
	LOSE("失敗"),
	RED_DEAD("潛伏死亡"),
	BLUE_DEAD("軍情死亡"),
	PASSERBY_DEAD("醬油死亡"),
	DEAD("死亡");

	public static final double RATIO_WIDTH_HEIGHT = 300d / 80;

	private static Image createDefaultImage(String text) {
		//Create image
		BufferedImage image = new BufferedImage(300, 80, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.setStroke(new BasicStroke(4f));

		//Draw round rect
		g2d.drawRoundRect(4, 4, 292, 72, 15, 15);

		//Draw string TODO register font
		g2d.setFont(new Font("文鼎新潮POP體H", Font.PLAIN, 60));
		FontMetrics fm = g2d.getFontMetrics();
		int width = fm.stringWidth(text);
		int height = fm.getHeight();
		int x = (300 - width) / 2;
		int y = (80 - height) / 2 + fm.getAscent();
		g2d.drawString(text, x, y);

		g2d.dispose();
		return image;
	}

	public static Death getInstance(Identity identity, boolean dead) {
		switch (identity) {
		case RED_TEAM:
			return dead ? RED_DEAD : RED_LOSE;
		case BLUE_TEAM:
			return dead ? BLUE_DEAD : BLUE_LOSE;
		case PASSER_BY:
			return dead ? PASSERBY_DEAD : PASSERBY_LOSE;
		}
		return dead ? DEAD : LOSE;
	}

	private transient Image defaultImage;
	private transient Map<Dimension, Image> imagePool;
	private final String name;

	Death(String name) {
		this.name = name;
	}

	/**
	 * Gets an image of given size.
	 * @param size image size
	 * @return image
	 */
	public synchronized Image getImage(Dimension size) {
		if (imagePool == null) {
			imagePool = new HashMap<>(4);
		}
		Image scaled = imagePool.get(size);
		if (scaled == null) {
			if (defaultImage == null) {
				defaultImage = createDefaultImage(name);
			}
			scaled = defaultImage.getScaledInstance(size.width, size.height, Image.SCALE_AREA_AVERAGING);
			imagePool.put(size, scaled);
		}
		return scaled;
	}
}
