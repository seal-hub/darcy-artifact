package net.leo.message.client.utility;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.JLayeredPane;
import javax.swing.event.MouseInputListener;
import net.leo.message.base.lang.Card;
import net.leo.message.client.element.GameCard;
import static net.leo.message.client.utility.CalculationUtility.round;

/**
 * <p>A box that shows cards. When calculating locations of cards, this box does not put insets into consideration. Hence put this box in a bordered panel instead.</p> <p>All
 * subclasses should not do the task of add or remove cards nor the lock or unlock action. What they have to do is to calculate locations of cards and perform reactions to mouse
 * events.</p>
 * @param <T> type of card
 * @author Leo Zeng
 */
public abstract class AbstractCardBox<T extends Component & CardView> extends JLayeredPane implements CardBox<T>, MouseInputListener, ComponentListener {

	private final List<T> list;
	private boolean listening = false;
	private boolean lock = false;

	/**
	 * Constructs a box.
	 */
	public AbstractCardBox() {
		this(8);
	}

	/**
	 * Constructs a box.
	 * @param initialCapacity the count of cards that may be put into this box
	 * @throws IllegalArgumentException if the specified initial capacity is negative or 0
	 */
	public AbstractCardBox(int initialCapacity) {
		if (initialCapacity <= 0) {
			throw new IllegalArgumentException();
		}
		this.list = new ArrayList<>(initialCapacity);
		addComponentListener(this);
	}

	private void addLast(T card) {
		if (card == null) {
			throw new NullPointerException();
		}
		card.setSize(getCardSize());
		list.add(card);
		add(card, 0);
	}

	private void checkMouseListener() {
		if (!lock) {
			if (!listening && activateListener()) {
				addMouseListener(this);
				addMouseMotionListener(this);
				listening = true;
			}
			else if (listening && !activateListener()) {
				removeMouseListener(this);
				removeMouseMotionListener(this);
				listening = false;
			}
		}
	}

	private boolean removeLast(Card card) {
		if (card == null) {
			throw new NullPointerException();
		}
		int index = IntStream.range(0, list.size()).filter(i -> list.get(i).getGameCard().getElement() == card).reduce((a, b) -> b).orElse(-1);
		if (index == -1) {
			return false;
		}
		remove(list.remove(index));
		return true;
	}

	private boolean removeLast(T card) {
		if (card == null) {
			throw new NullPointerException();
		}
		int index = list.lastIndexOf(card);
		if (index == -1) {
			return false;
		}
		remove(list.remove(index));
		return true;
	}

	private void resetAll() {
		if (isShowing()) {
			Point mouse = MouseInfo.getPointerInfo().getLocation();
			Point p = getLocationOnScreen();
			mouse.x -= p.x;
			mouse.y -= p.y;
			Dimension size = getSize();
			if (mouse.x >= 0 && mouse.x < size.width && mouse.y >= 0 && mouse.y < size.height) {
				setFocus(getIndex(mouse));
			}
			else {
				setFocus(-1);
			}
		}
		recalculate();
		int count = count();
		for (int i = 0 ; i < count ; i++) {
			list.get(i).setLocation(getCardLocation(i));
		}
	}

	/**
	 * Queries if mouse listener should be activated or shut down.
	 * @return true to activate mouse listener, or false to shut it down
	 */
	protected abstract boolean activateListener();

	/**
	 * Gets the location of a card by its index
	 * @param index index of card
	 * @return location of card
	 */
	protected abstract Point getCardLocation(int index);

	/**
	 * Gets the space between card and card.
	 * @return margin
	 */
	protected int getCardMargin() {
		return round(getHeight() * 0.06);
	}

	/**
	 * Gets the size of each card
	 * @return size of cards
	 */
	protected Dimension getCardSize() {
		int height = getHeight();
		int width = round(height * GameCard.RATIO_WIDTH_HEIGHT);
		return new Dimension(width, height);
	}

	/**
	 * Gets the corresponding index of card by mouse location
	 * @param mousePoint location of mouse
	 * @return corresponding index of card by mouse location, or -1 if no card is at such location
	 */
	protected abstract int getIndex(Point mousePoint);

	/**
	 * Gets the range (the difference between x coordinates of two neighboring card).
	 * @return range
	 */
	protected int getPreferredRange() {
		return round(getSize().height * 0.40);
	}

	/**
	 * This method is called when mouse exits this box.
	 */
	protected void onMouseExited() {
	}

	/**
	 * This method is called when mouse moves on this box.
	 * @param index index of card the mouse is currently at
	 */
	protected void onMouseMoved(int index) {
	}

	/**
	 * This method is called when mouse pressed.
	 * @param index index of card pressed
	 */
	protected void onMousePressed(int index) {
	}

	/**
	 * This method is called right before any card is relocated.
	 */
	protected abstract void recalculate();

	/**
	 * Relocates a card.
	 * @param index index of card relocated
	 */
	protected final void relocate(int index) {
		list.get(index).setLocation(getCardLocation(index));
	}

	/**
	 * Relocates all cards.
	 * @param focus index of card where the mouse is
	 */
	protected abstract void relocateAll(int focus);

	/**
	 * This method is called when mouse moves to a card.
	 * @param focus index of card where the mouse is
	 */
	protected abstract void setFocus(int focus);

	@Override
	public final void clear() {
		if (list.isEmpty()) {
			return;
		}
		synchronized (this) {
			list.clear();
			removeAll();
			repaint();
			checkMouseListener();
			recalculate();
		}
	}

	@Override
	public final void componentHidden(ComponentEvent e) {
	}

	@Override
	public final void componentMoved(ComponentEvent e) {
	}

	@Override
	public final void componentResized(ComponentEvent e) {
		recalculate();
		Dimension cardSize = getCardSize();
		list.forEach(c -> c.setSize(cardSize));
		resetAll();
		checkMouseListener();
	}

	@Override
	public final void componentShown(ComponentEvent e) {
	}

	@Override
	public final int count() {
		return list.size();
	}

	@Override
	public final T get(int index) {
		return list.get(index);
	}

	@Override
	public final List<T> getAll() {
		return new ArrayList<>(list);
	}

	@Override
	public final int indexOf(T card) {
		return list.indexOf(card);
	}

	@Override
	public final boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public final boolean isLocked() {
		return lock;
	}

	@Override
	public final void lock() {
		if (!lock) {
			synchronized (this) {
				removeMouseListener(this);
				removeMouseMotionListener(this);
				listening = false;
				relocateAll(-1);
				lock = true;
			}
		}
	}

	@Override
	public final void mouseClicked(MouseEvent e) {
	}

	@Override
	public final void mouseDragged(MouseEvent e) {
	}

	@Override
	public final void mouseEntered(MouseEvent e) {
	}

	@Override
	public final void mouseExited(MouseEvent e) {
		relocateAll(-1);
		onMouseExited();
	}

	@Override
	public final void mouseMoved(MouseEvent e) {
		int index = getIndex(e.getPoint());
		relocateAll(index);
		onMouseMoved(getIndex(e.getPoint()));
	}

	@Override
	public final void mousePressed(MouseEvent e) {
		onMousePressed(getIndex(e.getPoint()));
	}

	@Override
	public final void mouseReleased(MouseEvent e) {
	}

	@Override
	public synchronized final void offer(T card) {
		addLast(card);
		recalculate();
		resetAll();
		checkMouseListener();
	}

	@Override
	public synchronized final void offerAll(Collection<? extends T> cards) {
		if (cards.isEmpty()) {
			return;
		}
		synchronized (this) {
			for (T c : cards) {
				addLast(c);
			}
			recalculate();
			resetAll();
			checkMouseListener();
		}
	}

	@Override
	public synchronized final void poll(T card) {
		if (removeLast(card)) {
			if (!lock) {
				recalculate();
				resetAll();
			}
			checkMouseListener();
			repaint();
		}
	}

	@Override
	public synchronized final void poll(int index) {
		remove(list.remove(index));
		if (!lock) {
			recalculate();
			resetAll();
		}
		checkMouseListener();
		repaint();
	}

	@Override
	public synchronized final void pollAll(Collection<? extends T> cards) {
		if (cards.isEmpty()) {
			return;
		}
		boolean changed = false;
		synchronized (this) {
			for (T c : cards) {
				changed |= removeLast(c);
			}
			if (changed) {
				if (!lock) {
					recalculate();
					resetAll();
				}
				checkMouseListener();
				revalidate();
				repaint();
			}
		}
	}

	@Override
	public synchronized final void remove(Card card) {
		if (removeLast(card)) {
			if (!lock) {
				recalculate();
				resetAll();
			}
			checkMouseListener();
			repaint();
		}
	}

	@Override
	public synchronized final void removeAll(Collection<Card> cards) {
		if (cards.isEmpty()) {
			return;
		}
		boolean changed = false;
		synchronized (this) {
			for (Card c : cards) {
				changed |= removeLast(c);
			}
			if (changed) {
				if (!lock) {
					recalculate();
					resetAll();
				}
				checkMouseListener();
				revalidate();
				repaint();
			}
		}
	}

	@Override
	public final void sort() {
		Collections.sort(list);
		for (T card : list) {
			add(card, 0);
		}
		resetAll();
	}

	@Override
	public final void sort(Comparator<T> comparator) {
		list.sort(comparator);
		for (T card : list) {
			add(card, 0);
		}
		resetAll();
	}

	@Override
	public synchronized final void unlock() {
		if (lock) {
			synchronized (this) {
				lock = false;
				checkMouseListener();
				resetAll();
			}
		}
	}
}
