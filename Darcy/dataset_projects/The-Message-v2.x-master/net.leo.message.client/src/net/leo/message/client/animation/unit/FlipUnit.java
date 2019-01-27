package net.leo.message.client.animation.unit;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public abstract class FlipUnit extends AnimationUnit {

	protected final Image next;

	protected final double getAngle() {
		return Math.PI * getProgressRate();
	}

	@Override
	public final void paint(Graphics g) {
		Rectangle clip = getClip();
		g.drawImage(getProgressRate() > 0.5d ? next : image, clip.x, clip.y, clip.width, clip.height, null);
	}

	public FlipUnit(Image orgImage, Image nextImage, int nFrame, Component comp) {
		super(orgImage, nFrame, comp);
		if (nextImage == null) {
			throw new NullPointerException();
		}
		this.next = nextImage;
	}
}




