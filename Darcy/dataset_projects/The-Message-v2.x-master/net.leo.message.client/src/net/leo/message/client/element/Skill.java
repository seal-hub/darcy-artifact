package net.leo.message.client.element;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import static net.leo.message.client.utility.CalculationUtility.round;

/**
 * A skill element.
 */
public final class Skill {

	private static final Map<Integer, Skill> SINGLETONS = new HashMap<>(60);

	/**
	 * Ratio of width and height of skill image.
	 */
	public static final double RATIO_WIDTH_HEIGHT = 1 / Math.sqrt(2d);

	private static Image createImage(CharCard chr, String name) {
		BufferedImage c, s;
		try {
			c = ImageIO.read(getURL(chr));
			s = ImageIO.read(getSkillURL(name));
		}
		catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		BufferedImage cs = new BufferedImage(c.getWidth(), c.getHeight(), TYPE_INT_ARGB);
		Graphics g = cs.createGraphics();
		g.drawImage(c, 0, 0, c.getWidth(), c.getHeight(), null);
		double r = 1 - (double) s.getHeight() / c.getHeight();
		int y = round(c.getHeight() * r);
		g.drawImage(s, 0, y, c.getWidth(), c.getHeight() - y, null);
		g.dispose();
		return cs;
	}

	private static URL getURL(CharCard card) {
		return Skill.class.getResource("/image/skill/character/" + card.getName() + ".png");
	}

	private static int getId(CharCard character, String name) {
		return character.hashCode() + 17 * name.hashCode();
	}

	private static URL getSkillURL(String name) {
		if (name == null) {
			throw new NullPointerException();
		}
		return Skill.class.getResource("/image/skill/text/" + name + ".png");
	}

	/**
	 * Gets a skill instance by character and skill name.
	 * @param character character
	 * @param name      skill name
	 * @return a skill instance
	 * @throws NullPointerException     if any argument is null
	 * @throws IllegalArgumentException if such skill name does not exist
	 */
	public static Skill getInstance(CharCard character, String name) {
		//by the way check null
		Integer id = getId(character, name);
		Skill skill = SINGLETONS.get(id);
		if (skill == null) {
			synchronized (Skill.class) {
				skill = SINGLETONS.get(id);
				if (skill == null) {
					skill = new Skill(character, name);
					SINGLETONS.put(id, skill);
				}
			}
		}
		return skill;
	}

	private final String name;
	private final CharCard chr;
	private volatile Image image = null;
	private volatile Map<Dimension, Image> imagePool = null;

	private Skill(CharCard character, String name) {
		if (character == null || name == null) {
			throw new NullPointerException();
		}
		this.name = name;
		this.chr = character;
	}

	private Image getDefaultImage() {
		if (image == null) {
			synchronized (this) {
				if (image == null) {
					image = createImage(chr, name);
				}
			}
		}
		return image;
	}

	/**
	 * Gets the character of this skill.
	 * @return character of this skill
	 */
	public CharCard getCharacter() {
		return chr;
	}

	public Image getImage(Dimension size) {
		if (size == null) {
			throw new NullPointerException();
		}
		synchronized (this) {
			if (imagePool == null) {
				imagePool = new HashMap<>();
			}
		}
		Image scaled = imagePool.get(size);
		if (scaled == null) {
			synchronized (this) {
				scaled = imagePool.get(size);
				if (scaled == null) {
					scaled = getDefaultImage().getScaledInstance(size.width, size.height, Image.SCALE_AREA_AVERAGING);
					imagePool.put(size, scaled);
				}
			}
		}
		return scaled;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getClass() + "[" + chr.getName() + ", " + name + "]";
	}
}
