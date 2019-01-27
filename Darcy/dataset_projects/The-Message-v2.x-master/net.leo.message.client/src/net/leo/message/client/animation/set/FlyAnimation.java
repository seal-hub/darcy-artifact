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
import net.leo.message.client.animation.computer.Risable;
import net.leo.message.client.animation.computer.ShineUtility;
import net.leo.message.client.animation.executor.AnimationLayer;
import net.leo.message.client.player.CountView;

/**
 * An animation of cards moved from a place to another.
 */
public class FlyAnimation implements Animation {

	private static class Item implements Risable, Descendable {

		private Rectangle top;
		private final GameCard card;
		private final CountView org, dest;

		@Override
		public GameCard getCard() {
			return card;
		}

		@Override
		public CountView getDestination() {
			return dest;
		}

		@Override
		public CountView getOrigin() {
			return org;
		}

		@Override
		public void setTopBounds(Rectangle top) {
			this.top = top;
		}

		public Item(GameCard card, CountView org, CountView dest) {
			if (card == null || org == null || dest == null) {
				throw new NullPointerException();
			}
			this.card = card;
			this.org = org;
			this.dest = dest;
		}
	}

	private final int delay1, delay2, nFrame1, nFrame2;
	private final AnimationLayer comp;
	private final List<Item> list = new ArrayList<>();

	private List<AnimationUnit> buildDescendUnits() {
		return list.stream().map(i -> new ResizableMoveUnit(i.card.getImage(i.top.getSize()), i.top, i.dest.getBoundsFitCardRelativeTo(comp), nFrame2, comp)).collect(toList());
	}

	private List<AnimationUnit> buildRiseUnits() {
		return list.stream().map(i -> new ResizableMoveUnit(i.card.getImage(i.top.getSize()), i.org.getBoundsFitCardRelativeTo(comp), i.top, nFrame1, comp)).collect(toList());
	}

	public void addAnimationItem(GameCard card, CountView org, CountView dest) {
		list.add(new Item(card, org, dest));
	}

	public Runnable getShineInRunnable() {
		return ShineUtility.getShineInRunnable(list, comp.getShineGear());
	}

	public Runnable getShineOutRunnable() {
		return ShineUtility.getShineOutRunnable(list, comp.getShineGear());
	}

	@Override
	public void offerToQueue(Queue<? super AnimationOrder> queue) {
		if (list.isEmpty()) {
			throw new IllegalStateException();
		}
		BoundsUtility.locateRiseViews(list, comp);
		AnimationOrder rises = new MultiAnimationOrder(buildRiseUnits(), delay1);
		AnimationOrder descends = new MultiAnimationOrder(buildDescendUnits(), delay2);
		queue.offer(rises);
		queue.offer(descends);
	}

	public FlyAnimation(int nRiseFrame, int nDescendFrame, int delay, int pause, AnimationLayer comp) {
		super();
		if (nRiseFrame < 0 || nDescendFrame < 0 || delay < 0 || pause < 0) {
			throw new IllegalArgumentException();
		}
		if (comp == null) {
			throw new NullPointerException();
		}
		this.delay1 = delay;
		this.delay2 = pause;
		this.nFrame1 = nRiseFrame;
		this.nFrame2 = nDescendFrame;
		this.comp = comp;
	}
}
