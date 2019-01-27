package net.leo.message.client.utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import static net.leo.message.client.animation.set.Animation.UPDATE;

/**
 * A timer that controls time rolls or whatever timable.
 * @author Leo Zeng
 */
public class Timer implements ActionListener {

	private final Set<Timable> rolls = new HashSet<>(10);
	private final javax.swing.Timer timer = new javax.swing.Timer(UPDATE, this);

	private volatile boolean running = false;
	private Runnable timeUp;
	private int nFrame;
	private int currentFrame;

	/**
	 * Constructs a timer
	 */
	public Timer() {
	}

	private void nextFrame() {
		if (--currentFrame <= 0) {
			timer.stop();
			for (Timable roll : rolls) {
				roll.update(0d);
			}
			if (timeUp != null) {
				timeUp.run();
				timeUp = null;
			}
			rolls.clear();
			running = false;
			return;
		}
		double rate = (double) currentFrame / nFrame;
		for (Timable roll : rolls) {
			roll.update(rate);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		nextFrame();
	}

	/**
	 * Lets this timer controls a timable object. If this timer is running, shut down this timer, and perform the finishing procedure, then add this timable.
	 * @param timable object which is to controlled by this timer
	 */
	public void add(Timable timable) {
		if (timable == null) {
			throw new NullPointerException();
		}
		synchronized (this) {
			if (running) {
				stop(true);
			}
			rolls.add(timable);
		}
	}

	/**
	 * Lets this timer controls a plenty of timers. If this timer is running, shut down this timer, and perform the finishing procedure, then add timables.
	 * @param timables objects which are to controlled by this timer
	 * @throws NullPointerException if this set contains null
	 */
	public void addAll(Set<? extends Timable> timables) {
		if (timables.contains(null)) {
			throw new NullPointerException();
		}
		synchronized (this) {
			if (running) {
				stop(true);
			}
			this.rolls.addAll(timables);
		}
	}

	/**
	 * Removes a timable object from this timer. If this timer is running, only this timable will be shut down. If this is the only object of this timer, the whole timer stops and
	 * not performs finishing procedure.
	 * @param timable object removed
	 */
	public void remove(Timable timable) {
		if (timable == null) {
			throw new NullPointerException();
		}
		synchronized (this) {
			rolls.remove(timable);
		}
		if (rolls.isEmpty()) {
			timer.stop();
			timeUp = null;
			running = false;
		}
		timable.update(0);
	}

	/**
	 * Removes timable objects from this timer. If this timer is running, only these timables will be shut down. If these are the only objects of this timer, the whole timer stops
	 * and not performs finishing procedure.
	 * @param timables objects removed
	 * @throws NullPointerException if this set contains null
	 */
	public void removeAll(Set<? extends Timable> timables) {
		if (timables.contains(null)) {
			throw new NullPointerException();
		}
		synchronized (this) {
			this.rolls.removeAll(timables);
		}

		if (this.rolls.isEmpty()) {
			timer.stop();
			timeUp = null;
			running = false;
		}
		timables.forEach(r -> r.update(0));
	}

	/**
	 * Starts this timer. If this timer has been running, stops this timer and runs the finishing procedure; then restarts it.
	 * @param time               time limit
	 * @param finishingProcedure finishing procedure, or null to do nothing
	 */
	public void run(int time, Runnable finishingProcedure) {
		if (rolls.isEmpty()) {
			throw new IllegalStateException();
		}
		if (running) {
			stop(true);
		}
		if (time <= 0) {
			throw new IllegalArgumentException();
		}
		running = true;

		this.timeUp = finishingProcedure;
		this.nFrame = time / UPDATE;
		this.currentFrame = nFrame;
		timer.start();
	}

	/**
	 * Stops this timer.
	 * @param runFinish true to perform finishing procedure after this timer is stopped, or false to give up this procedure
	 */
	public void stop(boolean runFinish) {
		if (!running) {
			return;
		}
		timer.stop();
		synchronized (this) {
			rolls.forEach(r -> r.update(0d));
			rolls.clear();
		}
		if (runFinish && timeUp != null) {
			timeUp.run();
		}
		timeUp = null;
		running = false;
	}
}
