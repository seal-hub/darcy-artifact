package net.leo.message.client.animation.unit;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class FadeUnit extends AnimationUnit {

	protected final Rectangle bounds;
	protected final int x1;
	protected final int y1;
	protected final int imageX1;
	protected final int imageY1;
	protected final boolean fadeIn;

	@Override
	protected Rectangle getClip() {
		Rectangle r = bounds.getBounds();
		r.width = x1 - bounds.x;
		return r;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		float alpha = fadeIn ? (float) getProgressRate() : 1f - (float) getProgressRate();
		g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
		g2d.drawImage(image, bounds.x, bounds.y, x1, y1, 0, 0, imageX1, imageY1, null);
	}

	public FadeUnit(Image image, Rectangle bounds, boolean fadeIn, int range, int nFrame, Component comp) {
		super(image, nFrame, comp);
		if (range <= 0) {
			throw new IllegalArgumentException();
		}
		range = Math.min(range, bounds.width);
		this.bounds = bounds.getBounds();
		this.x1 = bounds.x + range;
		this.y1 = bounds.y + bounds.height;
		this.imageX1 = image.getWidth(null) * range / bounds.width;
		this.imageY1 = image.getHeight(null);
		this.fadeIn = fadeIn;
	}

	public FadeUnit(Image image, Rectangle bounds, boolean fadeIn, int nFrame, Component comp) {
		super(image, nFrame, comp);
		this.bounds = bounds.getBounds();
		this.x1 = bounds.x + bounds.width;
		this.y1 = bounds.y + bounds.height;
		this.imageX1 = image.getWidth(null);
		this.imageY1 = image.getHeight(null);
		this.fadeIn = fadeIn;
	}
}