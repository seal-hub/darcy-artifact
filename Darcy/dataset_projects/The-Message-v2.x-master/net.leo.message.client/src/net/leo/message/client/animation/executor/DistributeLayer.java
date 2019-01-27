package net.leo.message.client.animation.executor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import static java.lang.Math.min;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import javax.swing.JLayeredPane;
import net.leo.message.base.lang.Card;
import static net.leo.message.client.animation.set.Animation.UPDATE;
import net.leo.message.client.animation.set.IntelligenceReceiveAnimation;
import net.leo.message.client.domain.GameView;
import net.leo.message.client.element.GameCard;
import static net.leo.message.client.element.GameCard.COVERED_CARD;
import net.leo.message.client.player.CountView;

/**
 * A layer that plays function card {@link Card#DISTRIBUTE} animation.
 * @author Leo Zeng
 */
public class DistributeLayer extends JLayeredPane {

	private static final int MARGIN = 30;
	private static final int PADDING = 10;
	private static final Color TABLE = new Color(0f, 1f, 0f, 0.1f);

	/**
	 * The count of frame of flip stage.
	 */
	public static final int N_FLIP = 600 / UPDATE;
	/**
	 * The count of frame of descend stage.
	 */
	public static final int N_DESCEND = 300 / UPDATE;

	private final GameView.Locator locator;
	private final AnimationGear animationGear = new AnimationGear(1);
	private final AnimationPainter painter = animationGear.createPainter();
	private final LinkedList<Integer> rands = new LinkedList<>();

	private int total;
	private boolean running;
	private final ShineGear shineGear;

	/**
	 * Constructs a layer playing distribute animation.
	 * @param locator locator
	 */
	public DistributeLayer(GameView.Locator locator, ShineGear shineGear) {
		super();
		if (shineGear == null || locator == null) {
			throw new NullPointerException();
		}
		this.locator = locator;
		this.shineGear = shineGear;
		setForeground(TABLE);
	}

	private Rectangle getCardBounds(int index) {
		Rectangle table = getTableBounds();
		Dimension card = getCardSize();
		int row = index / 5;
		int column = index % 5;
		int x = table.x + MARGIN + column * (card.width + PADDING);
		int y = table.y + MARGIN + row * (card.height + PADDING);
		return new Rectangle(new Point(x, y), card);
	}

	private Dimension getCardSize() {
		return locator.getCardSize();
	}

	private Rectangle getTableBounds() {
		int nRow = (total + 4) / 5;
		int nColumn = min(5, total);
		Dimension card = getCardSize();
		int width = nColumn * card.width + (nColumn - 1) * PADDING + 2 * MARGIN;
		int height = nRow * card.height + (nRow - 1) * PADDING + 2 * MARGIN;
		int x = (getWidth() - width) / 2;
		int y = (getHeight() - height) / 2;
		return new Rectangle(x, y, width, height);
	}

	private void paintTable(Graphics g) {
		Rectangle table = getTableBounds();
		g.fillRoundRect(table.x, table.y, table.width, table.height, PADDING, PADDING);

		Rectangle card;
		for (Integer index : rands) {
			card = getCardBounds(index);
			g.drawImage(COVERED_CARD.getImage(card.getSize()), card.x, card.y, card.width, card.height, null);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (running) {
			paintTable(g);
		}
		painter.paintAnimation(g);
	}

	/**
	 * Clears the table.
	 */
	public void clear() {
		if (!running) {
			throw new IllegalStateException();
		}
		running = false;
		rands.clear();
		repaint(getTableBounds());
	}

	/**
	 * Plays an distribute animation.
	 * @param card card distributed
	 * @param dest destination
	 * @throws IllegalGearStateException if another animation is playing
	 * @throws IllegalStateException     if it is not distribute time right now or there is no remaining cards
	 */
	public void distribute(GameCard card, CountView dest) {
		if (animationGear.isRunning()) {
			throw new IllegalGearStateException();
		}
		if (!running || rands.isEmpty()) {
			throw new IllegalStateException();
		}
		int index = rands.removeFirst();
		IntelligenceReceiveAnimation anim = new IntelligenceReceiveAnimation(COVERED_CARD, getCardBounds(index), this);
		anim.flipTo(card, getCardBounds(index), N_FLIP, 0);
		anim.descendTo(dest, N_DESCEND, 0);
		painter.addAnimation(anim);
		animationGear.run(true, () -> {
			dest.offer(card.getElement());
			shineGear.run(new ShineInfo(dest, 1));
		});
	}

	/**
	 * Shows the table.
	 * @param nCard count of cards on the table
	 */
	public void prepare(int nCard) {
		if (running) {
			throw new IllegalStateException();
		}
		if (nCard <= 0) {
			throw new IllegalArgumentException();
		}
		running = true;
		total = nCard;
		List<Integer> integers = IntStream.range(0, nCard).boxed().collect(toList());
		Collections.shuffle(integers);
		rands.addAll(integers);
		repaint(getTableBounds());
	}
}
