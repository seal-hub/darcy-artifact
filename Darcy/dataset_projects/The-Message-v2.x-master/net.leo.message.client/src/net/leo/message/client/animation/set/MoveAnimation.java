package net.leo.message.client.animation.set;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Queue;
import net.leo.message.client.animation.order.AnimationOrder;
import net.leo.message.client.animation.order.BasicAnimationOrder;
import net.leo.message.client.animation.unit.AnimationUnit;
import net.leo.message.client.animation.unit.BasicMoveUnit;
import net.leo.message.client.animation.unit.ResizableMoveUnit;

public class MoveAnimation implements Animation {


	private final AnimationOrder anim;

	@Override
	public void offerToQueue(Queue<? super AnimationOrder> queue) {
		queue.offer(anim);
	}

	public MoveAnimation(Image image, Rectangle org, Rectangle dest, int nFrame, int delay, Component comp) {
		AnimationUnit unit;
		if (org.getSize().equals(dest.getSize())) {
			unit = new BasicMoveUnit(image, org.getLocation(), dest.getLocation(), org.getSize(), nFrame, comp);
		}
		else {
			unit = new ResizableMoveUnit(image, org, dest, nFrame, comp);
		}
		anim = new BasicAnimationOrder(unit, delay);
	}

	public MoveAnimation(Image image, Rectangle org, Point dest, int nFrame, int delay, Component comp) {
		AnimationUnit unit = new BasicMoveUnit(image, org.getLocation(), dest, org.getSize(), nFrame, comp);
		anim = new BasicAnimationOrder(unit, delay);
	}

	public MoveAnimation(Image image, Point org, Point dest, Dimension size, int nFrame, int delay, Component comp) {
		AnimationUnit unit = new BasicMoveUnit(image, org, dest, size, nFrame, comp);
		anim = new BasicAnimationOrder(unit, delay);
	}
}
