package net.leo.message.client.element;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.leo.message.base.lang.Character;

/**
 * Character card.
 * @author Leo Zeng
 */
public enum CharCard {

	/**
	 * 隱藏角色
	 */
	COVERED_CARD(null),
	/**
	 * 鬼姐
	 */
	C01(Character.C01),
	/**
	 * 槍叔
	 */
	C02(Character.C02),
	/**
	 * 金胖子
	 */
	C03(Character.C03),
	/**
	 * 耳機
	 */
	C04(Character.C04),
	/**
	 * 玫瑰
	 */
	C05(Character.C05),
	/**
	 * 30
	 */
	C06(Character.C06),
	/**
	 * 鋼鐵
	 */
	C07(Character.C07),
	/**
	 * 浮萍
	 */
	C08(Character.C08),
	/**
	 * 峨嵋老頭
	 */
	C09(Character.C09),
	/**
	 * 黃雀女神
	 */
	C10(Character.C10),
	/**
	 * 情報處長
	 */
	C11(Character.C11),
	/**
	 * 小白
	 */
	C12(Character.C12),
	/**
	 * 大醜女
	 */
	C13(Character.C13),
	/**
	 * 66
	 */
	C14(Character.C14),
	/**
	 * 殺手
	 */
	C15(Character.C15),
	/**
	 * 馬哥
	 */
	C16(Character.C16),
	/**
	 * 戴老闆
	 */
	C17(Character.C17),
	/**
	 * 摩斯
	 */
	C18(Character.C18),
	/**
	 * 蛇
	 */
	C19(Character.C19),
	/**
	 * 帽子
	 */
	C20(Character.C20),
	/**
	 * 700
	 */
	C21(Character.C21),
	/**
	 * 禮服
	 */
	C22(Character.C22),
	/**
	 * 99
	 */
	C23(Character.C23),
	/**
	 * 刀鋒
	 */
	C24(Character.C24),
	/**
	 * 叛徒
	 */
	C25(Character.C25);

	/**
	 * Ratio of width and height of character card.
	 */
	public static final double RATIO_WIDTH_HEIGHT = 583d / 504;

	private static Image createDefaultImage(Character character) {
		String name = character == null ? "隱藏" : character.getName();
		URL url = CharCard.class.getResource("/image/character/原圖/" + name + ".png");

		//Paint background (583 * 504)
		BufferedImage image;
		try {
			image = ImageIO.read(url);
		}
		catch (IOException e) {
			throw new IllegalArgumentException(e);
		}

		if (character == null) {
			return image;
		}

		//Paint gradient (134 * 504)
		Graphics2D g2d = image.createGraphics();
		g2d.setPaint(new GradientPaint(0, 100, new Color(0f, 0f, 0f, 0.6f), 0, 504, new Color(0f, 0f, 0f, 0f)));
		g2d.fillRect(0, 0, 134, 504);

		//Paint chacater name (font size 96)
		g2d.setPaint(null);
		g2d.setFont(new Font("華康新特明體", Font.PLAIN, 96));
		g2d.setColor(Color.WHITE);
		String[] ss = name.split("");
		for (int c = 0 ; c < ss.length ; c++) {
			g2d.drawString(ss[c], 19, 108 + 96 * c);
		}

		g2d.dispose();
		return image;
	}

	private static Image toGrayScale(Image image) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);

		BufferedImage gray = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2d = gray.createGraphics();
		g2d.drawImage(image, 0, 0, width, height, null);
		g2d.dispose();

		return gray;
	}

	/**
	 * Gets a character card.
	 * @param character character, or null indicates a covered card
	 * @return a character card
	 */
	public static CharCard getInstance(Character character) {
		if (character == null) {
			return COVERED_CARD;
		}

		for (CharCard cc : values()) {
			if (cc.character == character) {
				return cc;
			}
		}

		//Never happens
		throw new IllegalArgumentException();
	}


	private final Character character;
	private transient Image defaultImage;
	private transient Image grayImage;
	private transient Map<Dimension, Image> imagePool;
	private transient Map<Dimension, Image> grayPool;

	CharCard(Character character) {
		this.character = character;
	}

	/**
	 * @param size
	 * @return
	 */
	public synchronized Image getDeath(Dimension size) {
		if (grayPool == null) {
			grayPool = new HashMap<>(4);
		}
		Image scaled = grayPool.get(size);
		if (scaled == null) {
			if (grayImage == null) {
				if (defaultImage == null) {
					defaultImage = createDefaultImage(character);
				}
				grayImage = toGrayScale(defaultImage);
			}
			scaled = grayImage.getScaledInstance(size.width, size.height, Image.SCALE_AREA_AVERAGING);
			grayPool.put(size, scaled);
		}
		return scaled;
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
				defaultImage = createDefaultImage(character);
			}
			scaled = defaultImage.getScaledInstance(size.width, size.height, Image.SCALE_AREA_AVERAGING);
			imagePool.put(size, scaled);
		}
		return scaled;
	}

	public String getName() {
		return character.getName();
	}
}
