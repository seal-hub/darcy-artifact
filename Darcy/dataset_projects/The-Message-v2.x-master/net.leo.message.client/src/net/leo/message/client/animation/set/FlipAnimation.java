package net.leo.message.client.animation.set;

import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Queue;
import net.leo.message.client.animation.order.AnimationOrder;
import net.leo.message.client.animation.order.BasicAnimationOrder;
import net.leo.message.client.animation.unit.AnimationUnit;
import net.leo.message.client.animation.unit.BasicFlipUnit;
import net.leo.message.client.animation.unit.MoveFlipUnit;

/**
 * An animation of flipping an image to another one.
 */
public class FlipAnimation implements Animation {

	private final AnimationOrder anim;

	@Override
	public void offerToQueue(Queue<? super AnimationOrder> queue) {
		queue.offer(anim);
	}

	public FlipAnimation(Image prev, Image next, Rectangle bounds, int nFrame, int delay, Component comp) {
		AnimationUnit unit = new BasicFlipUnit(prev, next, bounds, nFrame, comp);
		anim = new BasicAnimationOrder(unit, delay);
	}

	/**
	 * Constructs a flipping and moving animation.
	 * @param prev   origin image
	 * @param next   next image
	 * @param org    origin place
	 * @param dest   destination place
	 * @param nFrame count of frames of this animation
	 * @param delay  delay of this animation
	 * @param comp   component which draws this animation
	 * @throws NullPointerException  if comp is null
	 * @throws IllegalStateException if nFrame is not positive or delay is negative
	 */
	public FlipAnimation(Image prev, Image next, Rectangle org, Rectangle dest, int nFrame, int delay, Component comp) {
		AnimationUnit unit;
		if (org.equals(dest)) {
			unit = new BasicFlipUnit(prev, next, org, nFrame, comp);
		}
		else {
			unit = new MoveFlipUnit(prev, next, org, dest, nFrame, comp);
		}
		anim = new BasicAnimationOrder(unit, delay);
	}
}
