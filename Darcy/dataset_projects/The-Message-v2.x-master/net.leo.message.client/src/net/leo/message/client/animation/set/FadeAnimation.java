package net.leo.message.client.animation.set;

import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Queue;
import net.leo.message.client.animation.order.AnimationOrder;
import net.leo.message.client.animation.order.BasicAnimationOrder;
import net.leo.message.client.animation.unit.FadeUnit;

/**
 * An animation of letting an image fade.
 * @author Leo Zeng
 */
public class FadeAnimation implements Animation {

	private final AnimationOrder order;

	@Override
	public void offerToQueue(Queue<? super AnimationOrder> queue) {
		queue.offer(order);
	}

	/**
	 * Constructs an animation of fading an image.
	 * @param image  image to be faded
	 * @param bounds the place where the animation plays
	 * @param fadeIn true representing a fade-in animation, or false fade-out animation
	 * @param nFrame count of frames of this animation
	 * @param delay  delay of this animation
	 * @param comp   component which paints this animation
	 *               @throws NullPointerException  if comp is null
	 * @throws IllegalStateException if nFrame is not positive or delay is negative
	 */
	public FadeAnimation(Image image, Rectangle bounds, boolean fadeIn, int nFrame, int delay, Component comp) {
		order = new BasicAnimationOrder(new FadeUnit(image, bounds, fadeIn, nFrame, comp), delay);
	}
}
