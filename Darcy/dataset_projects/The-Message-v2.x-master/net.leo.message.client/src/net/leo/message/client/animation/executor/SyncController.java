package net.leo.message.client.animation.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class SyncController {

	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	private static BlockingQueue<Object> QUEUE = new LinkedBlockingQueue<>(1);

	public static void await() throws InterruptedException {
		QUEUE.take();
	}

	public static void inform() {
		try {
			QUEUE.put(new Object());
		} catch (InterruptedException e) {
			throw new InternalError(e);
		}
	}

	private SyncController() {
	}
}
