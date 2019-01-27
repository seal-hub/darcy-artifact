package net.leo.message.client.animation.executor;

import java.awt.Component;
import java.awt.Graphics;

public interface Painter {

	/**
	 * Clears the animation on the component and dispose this painter. AnimationGear will not push this painter anymore until new animations are added into the queue of this painter.
	 */
	void clear();

	/**
	 * Invokes the next frame of animation.
	 * @return true if animation is running
	 */
	boolean nextFrame();

	/**
	 * Paints an animation frame by given graphics. This should be called in the method {@link Component#paint(Graphics)}.
	 * @param g graphics object
	 * @return true if current animation frame is painted by graphics.
	 */
	boolean paintAnimation(Graphics g);
}
