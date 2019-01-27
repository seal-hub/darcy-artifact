package net.leo.message.client.utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.EtchedBorder;
import static net.leo.message.client.utility.CalculationUtility.round;

public class TimeRoll extends JComponent implements Timable {

	public static final Color UP = new Color(0xCC, 55, 55), DOWN = new Color(99, 55, 55);

	private double rate;

	private Rectangle getClip() {
		Insets inset = getInsets();
		int width = round(rate * (getWidth() - inset.left - inset.right));
		return new Rectangle(inset.left, inset.top, width, getHeight() - (inset.top + inset.bottom));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (rate <= 0d) {
			return;
		}

		Rectangle clip = getClip();
		float y0 = clip.y, y1 = clip.y + clip.height * 0.90f;
		GradientPaint paint = new GradientPaint(0, y0, UP, 0, y1, DOWN);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(paint);
		g2d.fillRect(clip.x, clip.y, clip.width, clip.height);
	}

	@Override
	public Dimension getPreferredSize() {
		return getSize();
	}

	@Override
	public void update(double rate) {
		this.rate = rate;
		repaint();
	}

	public TimeRoll(boolean prepare) {
		super();
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		setOpaque(false);
		rate = prepare ? 1d : 0d;
	}
}
