package net.leo.message.client.utility;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import net.leo.message.client.animation.set.Animation;

public final class CalculationUtility {

	public static boolean checkInRange(double d, double from, double to) {
		// From must larger than to, no check
		return from >= d && d >= to;
	}

	public static boolean checkInRange(float f, float from, float to) {
		// Make sure from is greater than to
		if (from < to) {
			float tmp = from;
			from = to;
			to = tmp;
		}
		return from >= f && f >= to;
	}

	public static float ensureInRange(final float f, float from, float to) {
		if (from < to) {
			float tmp = to;
			to = from;
			from = tmp;
		}
		if (f > from) {
			return from;
		}
		if (f < to) {
			return to;
		}
		return f;
	}

	public static double ensureInRange(final double d, double from, double to) {
		if (from < to) {
			double tmp = to;
			to = from;
			from = tmp;
		}
		if (d > from) {
			return from;
		}
		if (d < to) {
			return to;
		}
		return d;
	}

	public static int ensureInRange(final int i, int from, int to) {
		if (from < to) {
			int tmp = to;
			to = from;
			from = tmp;
		}
		if (i > from) {
			return from;
		}
		if (i < to) {
			return to;
		}
		return i;
	}

	public static int round(double d) {
		return (int) (d + 0.5d);
	}

	public static int round(float f) {
		return (int) (f + 0.5f);
	}

	public static Rectangle ratio(Rectangle r1, Rectangle r2, double ratio) {
		double ratio2 = 1d - ratio;
		int x = round(r2.x * ratio + r1.x * ratio2);
		int y = round(r2.y * ratio + r1.y * ratio2);
		int w = round(r2.width * ratio + r1.width * ratio2);
		int h = round(r2.height * ratio + r1.height * ratio2);
		return new Rectangle(x, y, w, h);
	}

	public static Point ratio(Point p1, Point p2, double ratio) {
		double ratio2 = 1 - ratio;
		int x = round(p2.x * ratio + p1.x * ratio2);
		int y = round(p2.y * ratio + p1.y * ratio2);
		return new Point(x, y);
	}

	public static int speedToFrameCount(Point org, Point dest, int pxPerSecond) {
		return round(org.distance(dest) * 1000 / pxPerSecond / Animation.UPDATE);
	}

	public static Image drawHoles(Color color, Dimension size, Rectangle[] holes) {
		BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();
		g.setColor(color);
		g.fillRect(0, 0, size.width, size.height);
		((Graphics2D) g).setComposite(AlphaComposite.Clear);
		Arrays.stream(holes).forEach(h -> g.fillRect(h.x, h.y, h.width, h.height));
		g.dispose();
		return image;
	}

	private CalculationUtility() {
	}
}
