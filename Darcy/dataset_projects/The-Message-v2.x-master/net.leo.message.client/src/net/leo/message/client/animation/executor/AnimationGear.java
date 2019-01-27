package net.leo.message.client.animation.executor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import static net.leo.message.client.animation.set.Animation.UPDATE;

/**
 * A animation gear that push the animation to go frame by frame. It contains a swing timer and is thread-safe.
 * @author Leo Zeng
 */
public class AnimationGear implements ActionListener {

	protected final List<AnimationPainter> painters;
	protected volatile boolean running = false;
	protected final Timer timer = new Timer(UPDATE, this);
	protected boolean inform;
	protected Runnable doRun;

	private void nextFrame() {
		boolean painted = false;
		for (AnimationPainter painter : painters) {
			painted |= painter.nextFrame();
		}
		if (!painted) {
			timer.stop();
			complete();
		}
	}

	private void complete() {
		if (doRun != null) {
			doRun.run();
			doRun = null;
		}

		running = false;
		if (inform) {
			SyncController.inform();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		nextFrame();
	}

	/**
	 * Constructs and gets a painter own by this gear.
	 * @return a painter
	 */
	public AnimationPainter createPainter() {
		AnimationPainter painter = new AnimationPainter(this);
		painters.add(painter);
		return painter;
	}

	/**
	 * Queries if this gear is running.
	 * @return true if this gear is running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Invokes animations put in each painters. If this gear has been running, shut it down and perform the finishing procedure, then run.
	 * @param inform gear will call {@link SyncController#inform()} if this is set to true
	 * @param doRun  finishing procedure, or null to perform nothing
	 */
	public synchronized void run(boolean inform, Runnable doRun) {
		if (running) {
			stop();
		}
		running = true;
		this.inform = inform;
		this.doRun = doRun;
		timer.start();
	}

	/**
	 * Stops this gear immediately and runs the finishing procedure.
	 */
	public synchronized void stop() {
		if (running) {
			timer.stop();
		}

		painters.forEach(p -> p.clear());

		if (doRun != null) {
			doRun.run();
			doRun = null;
		}

		running = false;
		if (running) {
			running = false;
			if (inform) {
				SyncController.inform();
			}
		}
	}

	/**
	 * Creates an animation gear. User should give a number which represents the total count of painters it will create. This will help save the memories.
	 * @param nPainter count of painters this gear will create
	 * @throws IllegalArgumentException if nPainter is not positive
	 */
	public AnimationGear(int nPainter) {
		if (nPainter <= 0) {
			throw new IllegalArgumentException();
		}
		painters = new ArrayList<>(nPainter);
	}
}
