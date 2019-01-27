package net.leo.message.client.animation.unit;

import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import static net.leo.message.client.utility.CalculationUtility.ratio;
import static net.leo.message.client.utility.CalculationUtility.round;

public class MoveFlipUnit extends FlipUnit {

	protected final Rectangle org, dest;

	@Override
	protected Rectangle getClip() {
		Rectangle r = ratio(org, dest, getProgressRate());
		int imageWidth = Math.abs(round(r.width * Math.cos(getAngle())));
		int imageX = r.x + (r.width - imageWidth) / 2;
		return new Rectangle(imageX, r.y, imageWidth, r.height);
	}


	public MoveFlipUnit(Image orgImage, Image nextImage, Rectangle org, Rectangle dest, int nFrame, Component comp) {
		super(orgImage, nextImage, nFrame, comp);
		this.org = org.getBounds();
		this.dest = dest.getBounds();
	}
}
