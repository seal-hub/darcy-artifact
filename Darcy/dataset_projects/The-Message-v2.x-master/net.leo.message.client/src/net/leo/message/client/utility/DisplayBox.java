package net.leo.message.client.utility;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import static java.lang.Math.min;
import static net.leo.message.client.utility.CalculationUtility.round;

/**
 * A box that shows cards. If the card is not full of box, they are put in the center of the box, or otherwise they are folded and are put from the left to right margin.
 * @param <T> type of card
 * @author Leo Zeng
 */
public class DisplayBox<T extends Component & CardView> extends AbstractCardBox<T> {

	private double range = 0d;
	private int focus = -1;

	@Override
	protected boolean activateListener() {
		return isFoldOnCount(count());
	}

	@Override
	protected Point getCardLocation(int index) {
		int x;
		if (range == 0d) {
			x = getXOnExtended(index, count());
		}
		else {
			if (focus == -1 || index <= focus || !isFoldOnCount(count())) {
				x = round(index * range);
			}
			else {
				x = round((index - 1) * range) + getCardSize().width + getCardMargin();
			}
		}
		return new Point(x, 0);
	}

	/**
	 * Gets the sum of width of cards, inclusive of margin and margin is equals to preferred margin. Assumes that cards are not folded and this box contains <tt>count</tt> cards.
	 * @param count an assumed count of cards in this box
	 * @return sum of width
	 */
	protected int getExtendedWidthOnCount(int count) {
		if (count == 0) {
			return 0;
		}
		return getCardSize().width * count + getCardMargin() * (count - 1);
	}

	@Override
	protected int getIndex(Point mousePoint) {
		final int count = count();
		if (count == 0) {
			return -1;
		}

		double range = getRange();
		if (range == 0d) {
			int mrg = getCardMargin();
			int cardWidth = getCardSize().width;
			mousePoint.x -= ((getWidth() - getExtendedWidthOnCount(count)) / 2);
			if (mousePoint.x < 0) {
				return -1;
			}
			int prer = cardWidth + mrg;
			int xx = mousePoint.x % prer;
			if (xx >= cardWidth) {
				return -1;
			}
			int index = mousePoint.x / prer;
			return index < count ? index : -1;
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
		if (isExtendedOnCount(count)) {
			return 0d;
		}
		else {
			return (double) (getWidth() - getCardSize().width) / (count - 1);
		}
	}

	/**
	 * Gets the x coordinate of a card when cards are not folded. Assumes that this box contains <tt>count</tt> card.
	 * @param index index of a card whose x coordinate is queried
	 * @param count an assumed count of cards in this box
	 * @return x coordinate
	 */
	protected int getXOnExtended(int index, int count) {
		final int mrg = getCardMargin();
		final int cardWidth = getCardSize().width;
		int left = (getWidth() - getExtendedWidthOnCount(count)) / 2;
		return left + (mrg + cardWidth) * index;
	}

	/**
	 * Queries if cards are not folded when this box contains <tt>count</tt> card.
	 * @param count an assumed count of cards in this box
	 * @return true if cards are not folded.
	 */
	protected boolean isExtendedOnCount(int count) {
		if (count < 2) {
			return true;
		}
		return getWidth() >= getExtendedWidthOnCount(count);
	}

	/**
	 * Queries if cards are folded when this box contains <tt>count</tt> card.
	 * @param count an assumed count of cards in this box
	 * @return true if cards are folded or margin is less than the preferred one.
	 */
	protected final boolean isFoldOnCount(int count) {
		return getWidth() < getCardSize().width * count;
	}

	@Override
	protected void recalculate() {
		range = getRangeOnCount(count());
	}

	@Override
	protected void relocateAll(int nextFocus) {
		if (focus == nextFocus) {
			return;
		}

		this.focus = nextFocus;
		int count = count();
		int init = isFoldOnCount(count) ? 1 : 0;
		for (int i = init ; i < count ; i++) {
			relocate(i);
		}
	}

	@Override
	protected void setFocus(int focus) {
		this.focus = focus;
	}

	@Override
	public Rectangle getNextBounds() {
		int x, y;

		int newCount = count() + 1;
		double range = getRangeOnCount(newCount);
		if (range == 0d) {
			x = getXOnExtended(newCount - 1, newCount);
		}
		else {
			x = getWidth() - getCardSize().width;
		}

		y = 0;
		return new Rectangle(new Point(x, y), getCardSize());
	}


	/**
	 * Constructs a box that shows cards.
	 * @param initialCapacity the count of cards that may be put into this box
	 * @throws IllegalArgumentException if the specified initial capacity is negative or 0
	 */
	public DisplayBox(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Constructs a box that shows cards.
	 */
	public DisplayBox() {
		super();
	}
}
