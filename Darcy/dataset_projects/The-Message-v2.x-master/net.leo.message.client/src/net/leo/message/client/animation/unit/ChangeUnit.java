package net.leo.message.client.animation.unit;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import static net.leo.message.client.utility.CalculationUtility.round;

public class ChangeUnit extends AnimationUnit {

	protected final Image next;
	protected final Rectangle bounds;

	@Override
	protected Rectangle getClip() {
		return bounds;
	}

	@Override
	public void paint(Graphics g) {
		final double d = getProgressRate();
		if (d >= 1d) {
			g.drawImage(next, bounds.x, bounds.y, bounds.width, bounds.height, null);
		}
		else {
			final int xBoundary = bounds.x + round(bounds.width * d);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setComposite(AlphaComposite.SrcOver.derive((float) d));
			g2d.drawImage(next, xBoundary - bounds.width, bounds.y, bounds.width, bounds.height, null);
			g2d.setComposite(AlphaComposite.SrcOver.derive((float) (1d - d)));
			g2d.drawImage(image, xBoundary, bounds.y, bounds.width, bounds.height, null);
		}
	}

	public ChangeUnit(Image orgImage, Image nextImage, Rectangle bounds, int nFrame, Component comp) {
		super(orgImage, nFrame, comp);
		if (nextImage == null) {
			throw new NullPointerException();
		}
		this.bounds = bounds.getBounds();
		this.next = nextImage;
	}
}
