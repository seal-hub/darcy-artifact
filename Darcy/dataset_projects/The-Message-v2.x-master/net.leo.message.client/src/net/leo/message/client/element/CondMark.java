package net.leo.message.client.element;

import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.leo.message.base.lang.Condition;

/**
 * A condition element.
 */
public enum CondMark {

	/**
	 * "Lock" means that this net.leo.message.client.player must receive intelligence if it arrives to him or her.
	 */
	LOCK_ON("鎖"),
	/**
	 * "Trap" means that this net.leo.message.client.player must not receive intelligence when it arrives to her or him.
	 */
	TRAP("離"),
	/**
	 * "Receive" means that this net.leo.message.client.player is about to receive an intelligence he or she just got by some way.
	 */
	GET("收");

	/**
	 * Ratio of width and height of identity card.
	 */
	public static final double RATIO_WIDTH_HEIGHT = 1d;

	private static Image createDefaultImage(String name) {
		if (name == null) {
			throw new NullPointerException();
		}
		try {
			return ImageIO.read(CondMark.class.getResource("/image/condition/" + name + ".png"));
		}
		catch (IOException e) {
			throw new IllegalArgumentException(name);
		}
	}

	/**
	 * @param code
	 * @return
	 */
	public static CondMark getInstance(Condition code) {
		switch (code) {
		case LOCKED:
			return LOCK_ON;
		case TRAPED:
			return TRAP;
		case ACCEPTED:
			return GET;
		}
		throw new IllegalArgumentException();
	}

	private transient Image defaultImage;
	private transient Map<Dimension, Image> imagePool;
	private final String name;

	CondMark(String name) {
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
