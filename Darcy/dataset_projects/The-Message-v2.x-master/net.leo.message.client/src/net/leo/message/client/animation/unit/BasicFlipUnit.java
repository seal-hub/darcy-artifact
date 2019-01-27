package net.leo.message.client.animation.unit;

import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import static net.leo.message.client.utility.CalculationUtility.round;

public class BasicFlipUnit extends FlipUnit {

	protected final Rectangle bounds;

	@Override
	protected Rectangle getClip() {
		int imageWidth = round(Math.abs(bounds.width * Math.cos(getAngle())));
		int imageX = bounds.x + (bounds.width - imageWidth) / 2;
		return new Rectangle(imageX, bounds.y, imageWidth, bounds.height);
	}

	public BasicFlipUnit(Image orgImage, Image nextImage, Rectangle bounds, int nFrame, Component comp) {
		super(orgImage, nextImage, nFrame, comp);
		this.bounds = bounds.getBounds();
	}
}
