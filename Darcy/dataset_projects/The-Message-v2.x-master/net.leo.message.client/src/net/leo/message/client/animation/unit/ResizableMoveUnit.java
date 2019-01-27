package net.leo.message.client.animation.unit;

import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import static net.leo.message.client.utility.CalculationUtility.ratio;

public class ResizableMoveUnit extends MoveUnit {

	protected final Rectangle org, dest;

	@Override
	protected Rectangle getClip() {
		return ratio(org, dest, getProgressRate());
	}

	public ResizableMoveUnit(Image image, Rectangle org, Rectangle dest, int nFrame, Component comp) {
		super(image, nFrame, comp);
		this.org = org.getBounds();
		this.dest = dest.getBounds();
	}
}