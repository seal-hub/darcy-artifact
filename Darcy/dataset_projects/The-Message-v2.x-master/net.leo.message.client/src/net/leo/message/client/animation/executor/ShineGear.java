package net.leo.message.client.animation.executor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;
import static net.leo.message.client.animation.set.Animation.UPDATE;
import net.leo.message.client.player.CountView;

/**
 * A shine gear that pushes shine animations.
 */
public class ShineGear implements ActionListener {

	/**
	 * Count of frames of a shine animation.
	 */
	public static final int N_SHINE = 2500 / UPDATE;

	protected final Map<CountView, ShinePainter> painters;
	protected volatile boolean running = false;
	protected final Timer timer = new Timer(UPDATE, this);

	private void nextFrame() {
		boolean painted = false;
		for (ShinePainter painter : painters.values()) {
			painted |= painter.nextFrame();
		}
		if (!painted) {
			timer.stop();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		nextFrame();
	}

	/**
	 * Constructs a painter own by this gear and given component.
	 * @param comp the component which would owns this painter
	 * @return a painter
	 */
	public ShinePainter createPainter(CountView comp) {
		if (comp == null) {
			throw new NullPointerException();
		}
		ShinePainter painter = painters.get(comp);
		if (painter == null) {
			painter = new ShinePainter(comp);
			painters.put(comp, painter);
		}
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
	 * Invokes shine animations. If this gear is already running, restart it.
	 * @param info an information telling which count views should shine and the increments
	 * @throws IllegalArgumentException if info contains a count view that doest not belong to the painter of this gear
	 */
	public synchronized void run(ShineInfo info) {
		if (!painters.keySet().containsAll(info.map.keySet())) {
			throw new IllegalArgumentException();
		}
		running = true;

		info.map.keySet().forEach(c -> painters.get(c).reset(N_SHINE));
		info.changeText();
		if (running) {
			timer.start();
		}
	}

	/**
	 * Constructs a shine gear. A number of painters this gear will create should be given, which helps save memories.
	 * @param nPainter count of painters this gear will create
	 * @throws IllegalArgumentException if nPainter is not positive
	 */
	public ShineGear(int nPainter) {
		if (nPainter <= 0) {
			throw new IllegalArgumentException();
		}
		painters = new HashMap<>(nPainter);
	}
}
