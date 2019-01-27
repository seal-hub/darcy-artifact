package net.leo.message.client.animation.set;

import java.util.Queue;
import net.leo.message.client.animation.order.AnimationOrder;

/**
 * @author Lao Zeng
 */
@FunctionalInterface
public interface Animation {

	/**
	 * Frame per Second.
	 */
	int FPS = 60;
	/**
	 * Cycle time a timer updates, in millis
	 */
	int UPDATE = 1000 / FPS;

	/**
	 * Puts a series of animation order into a given queue.
	 * @param queue queue to put animation orders
	 * @throws NullPointerException if queue is null
	 */
	void offerToQueue(Queue<? super AnimationOrder> queue);
}
