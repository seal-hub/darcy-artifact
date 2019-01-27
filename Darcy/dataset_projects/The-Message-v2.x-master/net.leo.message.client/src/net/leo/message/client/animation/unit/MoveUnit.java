package net.leo.message.client.animation.unit;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import static net.leo.message.client.animation.set.Animation.UPDATE;

public abstract class MoveUnit extends AnimationUnit {

	public static int calculateFrameCount(Point from, Point to, int speed) {
		double dis = from.distance(to);
		double time = dis * 1000 / speed; // in millis
		return (int) (time / UPDATE);
	}

	@Override
	public final void paint(Graphics g) {
		Rectangle r = getClip();
		g.drawImage(image, r.x, r.y, r.width, r.height, null);
	}

	public MoveUnit(Image image, int nFrame, Component comp) {
		super(image, nFrame, comp);
	}
}


