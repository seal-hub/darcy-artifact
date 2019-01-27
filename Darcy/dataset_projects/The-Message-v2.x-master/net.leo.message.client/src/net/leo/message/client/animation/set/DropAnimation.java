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
import net.leo.message.client.animation.unit.FadeUnit;
import net.leo.message.client.animation.unit.ResizableMoveUnit;
import net.leo.message.client.animation.computer.BoundsUtility;
import net.leo.message.client.animation.computer.Dropable;
import net.leo.message.client.animation.computer.ShineUtility;
import net.leo.message.client.animation.executor.AnimationLayer;
import net.leo.message.client.player.CountView;

/**
 * An animation of card dropped.
 * @author Leo Zeng
 */
public class DropAnimation implements Animation {

	private static class Item implements Dropable {

		private final CountView org;
		private Rectangle top;
		private final GameCard card;
		private int range = -1;

		@Override
		public GameCard getCard() {
			return card;
		}

		@Override
		public CountView getOrigin() {
			return org;
		}

		@Override
		public void setRange(int range) {
			this.range = range;
		}

		@Override
		public void setTopBounds(Rectangle top) {
			this.top = top;
		}

		public Item(CountView org, GameCard card) {
			if (org == null || card == null) {
				throw new NullPointerException();
			}
			this.org = org;
			this.card = card;
		}
	}

	private final int delay1, delay2, nFrame1, nFrame2;
	private final AnimationLayer comp;
	private final List<Item> list = new ArrayList<>();

	private List<AnimationUnit> buildFadeUnits() {
		return list.stream().map(i -> new FadeUnit(i.card.getImage(i.top.getSize()), i.top, false, i.range, nFrame2, comp)).collect(toList());
	}

	private List<AnimationUnit> buildRiseUnits() {
		return list.stream().map(i -> new ResizableMoveUnit(i.card.getImage(i.top.getSize()), i.org.getBoundsFitCardRelativeTo(comp), i.top, nFrame1, comp)).collect(toList());
	}

	/**
	 * Adds a card into this animation.
	 * @param org  origin place of this card
	 * @param card card faded
	 * @throws NullPointerException if any of argument is null
	 */
	public void addAnimationItem(CountView org, GameCard card) {
		list.add(new Item(org, card));
	}

	/**
	 * Gets a process of letting specific count label shine.
	 * @return a Runnable object of letting specific count label shine
	 */
	public Runnable getShineOutRunnable() {
		return ShineUtility.getShineOutRunnable(list, comp.getShineGear());
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
		BoundsUtility.locateDropViews(list, comp);
		AnimationOrder rises = new MultiAnimationOrder(buildRiseUnits(), delay1);
		AnimationOrder fades = new MultiAnimationOrder(buildFadeUnits(), delay2);
		queue.offer(rises);
		queue.offer(fades);
	}

	/**
	 * Constructs a card dropped animation.
	 * @param nRiseFrame count of frames of rise animation
	 * @param nFadeFrame count of frames of fade animation
	 * @param delay      delay of this animation
	 * @param pause      pause time between rise and fade animation
	 * @param comp       component which draw sthis animation
	 * @throws NullPointerException     if comp is null
	 * @throws IllegalArgumentException if any of nFrame is not positive or any of time is negative
	 */
	public DropAnimation(int nRiseFrame, int nFadeFrame, int delay, int pause, AnimationLayer comp) {
		if (nRiseFrame < 0 || nFadeFrame < 0 || delay < 0 || pause < 0) {
			throw new IllegalStateException();
		}
		if (comp == null) {
			throw new NullPointerException();
		}
		this.delay1 = delay;
		this.delay2 = pause;
		this.nFrame1 = nRiseFrame;
		this.nFrame2 = nFadeFrame;
		this.comp = comp;
	}
}
