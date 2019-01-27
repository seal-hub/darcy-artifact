package net.leo.message.client.utility;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import static java.lang.Math.min;
import static net.leo.message.client.utility.CalculationUtility.round;

/**
 * This container can used to show a set of cards. Cards are located from the left of the box and are always folded.
 * @author Leo Zeng
 */
@SuppressWarnings("serial")
public class FoldBox<T extends Component & CardView> extends AbstractCardBox<T> {

	private double range = 0;
	private int focus = -1;

	@Override
	protected boolean activateListener() {
		return count() > 1;
	}

	@Override
	protected Point getCardLocation(int index) {
		int x;
		if (focus == -1 || index <= focus) {
			x = round(index * range);
		}
		else {
			x = round((index - 1) * range) + getCardSize().width + getCardMargin();
		}
		return new Point(x, 0);
	}

	@Override
	protected int getIndex(Point mousePoint) {
		final int count = count();
		if (count == 0) {
			return -1;
		}

		int lastX = round(range * (count - 1));
		if (mousePoint.x >= lastX) {
			return mousePoint.x < lastX + getCardSize().width ? count - 1 : -1;
		}

		return min((int) (mousePoint.x / range), count - 1);
	}

	/**
	 * Gets the actual range.
	 * @return range
	 */
	protected double getRange() {
		return range;
	}

	/**
	 * Gets the predict range of cards when this box contains a given count of cards.
	 * @param count an assumed count of cards in this box
	 * @return predict range
	 */
	protected double getRangeOnCount(int count) {
		if (count < 2) {
			return getPreferredRange();
		}
		double idealRange = (double) (getWidth() - getCardSize().width) / (count - 1);
		return min(idealRange, getPreferredRange());
	}

	@Override
	protected void recalculate() {
		range = getRangeOnCount(count());
	}

	@Override
	protected void relocateAll(int nextFocus) {
		if (nextFocus == focus) {
			return;
		}
		int prevFocus = focus;
		focus = nextFocus;
		final int count = count();

		if (prevFocus == -1) {
			for (int i = nextFocus + 1 ; i < count ; i++) {
				relocate(i);
			}
			return;
		}
		if (nextFocus == -1) {
			for (int i = prevFocus + 1 ; i < count ; i++) {
				relocate(i);
			}
			return;
		}

		int left = min(nextFocus, Math.min(prevFocus, count - 1)), right = Math.max(nextFocus, Math.min(prevFocus, count - 1));
		for (int i = left + 1 ; i <= right ; i++) {
			relocate(i);
		}
	}

	@Override
	protected void setFocus(int focus) {
		this.focus = focus;
	}

	@Override
	public Rectangle getNextBounds() {
		int x = round(range * count());
		return new Rectangle(new Point(x, 0), getCardSize());
	}


	/**
	 * Constructs a box that shows cards.
	 */
	public FoldBox() {
		super();
	}

	/**
	 * Constructs a box that shows cards.
	 * @param initialCapacity the count of cards that may be put into this box
	 * @throws IllegalArgumentException if the specified initial capacity is negative or 0
	 */
	public FoldBox(int initialCapacity) {
		super(initialCapacity);
	}
}
