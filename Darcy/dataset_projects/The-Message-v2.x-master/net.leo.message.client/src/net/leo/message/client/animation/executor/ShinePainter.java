package net.leo.message.client.animation.executor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JComponent;

public class ShinePainter implements Painter {

	private int current;
	private int max;
	private final JComponent comp;

	private void callRepaint() {
		comp.repaint(getClip());
	}

	private Rectangle getClip() {
		Insets inset = comp.getInsets();
		return new Rectangle(inset.left, inset.top, comp.getWidth() - (inset.left + inset.right), comp.getHeight() - (inset.top + inset.bottom));
	}

	@Override
	public void clear() {
		current = 0;
		callRepaint();
	}

	@Override
	public boolean nextFrame() {
		if (current < 0) {
			return false;
		}
		callRepaint();
		return --current >= 0;
	}

	@Override
	public boolean paintAnimation(Graphics g) {
		if (current <= 0) {
			return false;
		}
		Color color = new Color(1f, 1f, 1f, (float) current / max);
		g.setColor(color);
		Rectangle clip = getClip();
		g.fillRect(clip.x, clip.y, clip.width, clip.height);
		return true;
	}

	public void reset(int max) {
		this.max = max;
		current = max;
		callRepaint();
	}

	ShinePainter(JComponent comp) {
		if (comp == null) {
			throw new NullPointerException();
		}
		this.comp = comp;
	}
}
