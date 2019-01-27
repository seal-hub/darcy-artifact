package net.leo.message.client.animation.unit;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import static net.leo.message.client.utility.CalculationUtility.ratio;

public class BasicMoveUnit extends MoveUnit {

	protected final Dimension size;
	protected final Point org, dest;

	@Override
	protected Rectangle getClip() {
		return new Rectangle(ratio(org, dest, getProgressRate()), size);
	}

	public BasicMoveUnit(Image image, Point org, Point dest, Dimension size, int nFrame, Component comp) {
		super(image, nFrame, comp);
		this.org = org.getLocation();
		this.dest = dest.getLocation();
		this.size = size.getSize();
	}
}
