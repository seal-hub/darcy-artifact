package net.leo.message.client.animation.set;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import static java.util.stream.Collectors.toList;
import net.leo.message.client.element.GameCard;
import net.leo.message.client.animation.order.AnimationOrder;
import net.leo.message.client.animation.order.MultiAnimationOrder;
import net.leo.message.client.animation.unit.AnimationUnit;
import net.leo.message.client.animation.unit.ResizableMoveUnit;
import net.leo.message.client.animation.computer.BoundsUtility;
import net.leo.message.client.animation.computer.Descendable;
import net.leo.message.client.animation.computer.ShineUtility;
import net.leo.message.client.animation.executor.AnimationLayer;
import net.leo.message.client.player.CountView;

/**
 * An animation of drawing card.
 * @author Leo Zeng
 */
public class DrawAnimation implements Animation {

	private static class Item implements Descendable {

		private final GameCard card;
		private final CountView dest;
		private Rectangle org = null;

		@Override
		public GameCard getCard() {
			return card;
		}

		@Override
		public CountView getDestination() {
			return dest;
		}

		@Override
		public void setTopBounds(Rectangle top) {
			this.org = top;
		}

		private Item(GameCard card, CountView dest) {
			if (card == null || dest == null) {
				throw new NullPointerException();
			}
			this.card = card;
			this.dest = dest;
		}
	}

	private final List<Item> list = new ArrayList<>();
	private final int nFrame, delay;
	private final AnimationLayer comp;

	private List<AnimationUnit> buildUnits() {
		return list.stream().map(i -> new ResizableMoveUnit(i.card.getImage(i.org.getSize()), i.org, i.dest.getBoundsFitCardRelativeTo(comp), nFrame, comp)).collect(toList());
	}

	/**
	 * Adds a card into this animation.
	 * @param org  origin place of this card
	 * @param card card drawn
	 * @throws NullPointerException if any of argument is null
	 */
	public void addAnimationItem(CountView org, GameCard card) {
		list.add(new Item(card, org));
	}

	/**
	 * Gets a process of letting specific count label shine.
	 * @return a Runnable object of letting specific count label shine
	 */
	public Runnable getShineRunnable() {
		return ShineUtility.getShineInRunnable(list, comp.getShineGear());
	}

	/**
	 * Puts a series of animation order into a given queue.
	 * @param queue queue to put animation orders
	 * @throws NullPointerException  if queue is null
	 * @throws IllegalStateException if no card is added
	 */
	@Override
	public void offerToQueue(Queue<? super AnimationOrder> queue) {
		if (list.isEmpty()) {
			throw new IllegalStateException();
		}
		BoundsUtility.locateDescendViews(list, comp);
		List<AnimationUnit> units = buildUnits();
		AnimationOrder mc = new MultiAnimationOrder(units, delay);
		queue.offer(mc);
	}

	/**
	 * Constructs a drawing card animation
	 * @param nFrame count of frame of this animation
	 * @param delay  delay of this animation
	 * @param comp   component which paints this animation
	 * @throws NullPointerException  if comp is null
	 * @throws IllegalStateException if nFrame is not positive or delay is negative
	 */
	public DrawAnimation(int nFrame, int delay, AnimationLayer comp) {
		if (nFrame < 0 || delay < 0) {
			throw new IllegalArgumentException();
		}
		if (comp == null) {
			throw new NullPointerException();
		}
		this.delay = delay;
		this.nFrame = nFrame;
		this.comp = comp;
	}
}
