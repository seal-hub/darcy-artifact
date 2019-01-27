package net.leo.message.client.animation.executor;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Queue;
import net.leo.message.client.animation.order.AnimationOrder;
import net.leo.message.client.animation.set.Animation;
import static net.leo.message.client.animation.set.Animation.UPDATE;
import static net.leo.message.client.utility.CalculationUtility.round;

/**
 * An animation painter that paints animation image frame by frame on the component. It contains no timer and it should be invoked by a {@link AnimationGear}. No constructors are provided,
 * and the only way is to call {@link AnimationGear#createPainter()}.
 */
public class AnimationPainter implements Painter {

	private final Queue<AnimationOrder> queue = new LinkedList<>();
	private final AnimationGear gear;

	private int max;
	private int current = 0;
	private AnimationOrder order;
	private boolean pauseState = false;

	/**
	 * Adda an animation into animation queue. If this painter is painting another animation, interrupted the painting.
	 * @param animation animation to be added
	 */
	public void addAnimation(Animation animation) {
		if (gear.isRunning()) {
			gear.stop();
		}
		animation.offerToQueue(queue);
	}

	@Override
	public synchronized void clear() {
		if (order != null) {
			order.hide();
			order = null;
		}
		queue.clear();
		pauseState = false;
	}

	@Override
	public synchronized boolean nextFrame() {
		//Check if this is the first animation, or should play next animation, or the pause time has up
		if (order == null || current >= max) {

			//Pause time up, so go run current animation
			if (pauseState) {
				pauseState = false;
				max = order.getFrameCount();
			}

			//Change to next animation, check if should pause
			else {

				//Clear old animation
				if (order != null) {
					order.hide();
				}

				//Check if there is next animation, or otherwise stop
				order = queue.poll();
				if (order == null) {
					return false;
				}

				//Check if should pause
				int nPauseFrame = round((double) order.getDelay() / UPDATE);
				if (nPauseFrame == 0) {
					max = order.getFrameCount();
				}
				else {
					order.show();
					max = nPauseFrame;
					pauseState = true;
				}
			}

			current = 0;
		}

		//If is not pause stage, play animation
		if (!pauseState) {
			order.nextFrame();
		}

		current++;
		return true;
	}

	@Override
	public boolean paintAnimation(Graphics g) {
		if (order != null) {
			order.paintFrame(g);
			return true;
		}
		return false;
	}

	AnimationPainter(AnimationGear gear) {
		if (gear == null) {
			throw new NullPointerException();
		}
		this.gear = gear;
	}
}