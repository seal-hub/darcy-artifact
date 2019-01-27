package net.leo.message.client.element;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.leo.message.base.lang.Identity;

/**
 * Identity card.
 * @author Leo Zeng
 */
public enum IdenCard {

	/**
	 * 身分牌背面
	 */
	COVERED_CARD(null),
	/**
	 * 軍情處
	 */
	BLUE_TEAM(Identity.BLUE_TEAM),
	/**
	 * 潛伏戰線
	 */
	PASSER_BY(Identity.PASSER_BY),
	/**
	 * 醬油
	 */
	RED_TEAM(Identity.RED_TEAM);

	private static final long serialVersionUID = -136357833079097894L;

	/**
	 * Ratio of width and height of identity card.
	 */
	public static final double RATIO_WIDTH_HEIGHT = 0.75d;

	private static final Composite ALPHA_COMPOSITE = AlphaComposite.SrcOver.derive(0.5f);

	private static Image createDefaultImage(Identity identity) {
		URL url;
		if (identity == null) {
			url = IdenCard.class.getResource("/image/identity/身分牌.png");
		}
		else {
			url = IdenCard.class.getResource("/image/identity/" + identity.getName() + ".png");
		}

		try {
			return ImageIO.read(url);
		}
		catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static Image createTransImage(Image image) {
		BufferedImage trans = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = trans.createGraphics();
		g2d.setComposite(ALPHA_COMPOSITE);
		g2d.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), null);
		g2d.dispose();
		return trans;
	}

	/**
	 * Gets an identity card by specific code. See {@link Identity}.
	 * @param identity identity code
	 * @return an identity
	 * @throws IllegalArgumentException if such identity does not exist
	 * @see Identity
	 */
	public static IdenCard getInstance(Identity identity) {
		for (IdenCard ic : values()) {
			if (ic.identity == identity) {
				return ic;
			}
		}
		return COVERED_CARD;
	}

	private transient Map<Dimension, Image> imagePool;
	private transient Map<Dimension, Image> transImagePool;
	private transient Image defaultImage;
	private transient Image transImage;
	private final Identity identity;

	IdenCard(Identity identity) {
		this.identity = identity;
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
				defaultImage = createDefaultImage(identity);
			}
			scaled = defaultImage.getScaledInstance(size.width, size.height, Image.SCALE_AREA_AVERAGING);
			imagePool.put(size, scaled);
		}
		return scaled;
	}

	/**
	 * Gets a translucent image of given size.
	 * @param size image size
	 * @return a translucent image
	 */
	public synchronized Image getTranslucentImage(Dimension size) {
		if (transImagePool == null) {
			transImagePool = new HashMap<>(4);
		}
		Image scaled = transImagePool.get(size);
		if (scaled == null) {
			if (transImage == null) {
				if (defaultImage == null) {
					defaultImage = createDefaultImage(identity);
				}
				transImage = createTransImage(defaultImage);
			}
			scaled = transImage.getScaledInstance(size.width, size.height, Image.SCALE_AREA_AVERAGING);
			transImagePool.put(size, scaled);
		}
		return scaled;
	}
}
