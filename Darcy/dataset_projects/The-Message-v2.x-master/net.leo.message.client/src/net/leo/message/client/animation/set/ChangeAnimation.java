package net.leo.message.client.animation.set;

import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Queue;
import net.leo.message.client.animation.order.AnimationOrder;
import net.leo.message.client.animation.order.BasicAnimationOrder;
import net.leo.message.client.animation.unit.AnimationUnit;
import net.leo.message.client.animation.unit.ChangeUnit;

/**
 * A image changing animation.
 * @author Leo Zeng
 */
public class ChangeAnimation implements Animation {

	private final AnimationOrder anim;

	@Override
	public void offerToQueue(Queue<? super AnimationOrder> queue) {
		queue.offer(anim);
	}

	/**
	 * Constructs a image changing animation
	 * @param prev   original image
	 * @param next   new image
	 * @param bounds place where animation plays
	 * @param nFrame count of frames of this animation
	 * @param delay  delay time of this animation
	 * @param comp   component which draws this animation
	 * @throws NullPointerException     if any of prev, next, bounds or comp is null
	 * @throws IllegalArgumentException if nFrame is not positive or delay is negative
	 */
	public ChangeAnimation(Image prev, Image next, Rectangle bounds, int nFrame, int delay, Component comp) {
		AnimationUnit unit = new ChangeUnit(prev, next, bounds, nFrame, comp);
		anim = new BasicAnimationOrder(unit, delay);
	}
}
