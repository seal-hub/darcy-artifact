package net.leo.message.client.animation.computer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import javax.swing.SwingUtilities;
import net.leo.message.client.animation.executor.AnimationLayer;
import net.leo.message.client.player.CountView;
import net.leo.message.client.player.PlayerView;
import static net.leo.message.client.utility.CalculationUtility.ensureInRange;
import static net.leo.message.client.utility.CalculationUtility.round;

/**
 * A helper class that calculates and sets cards' bounds for animations.
 * @author Leo Zeng
 */
public final class BoundsUtility {

	private static final Comparator<Descendable> DESC_CMP = (o1, o2) -> {
		int v = o1.getDestination().compareTo(o2.getDestination());
		if (v != 0) {
			return v;
		}
		return o1.getCard().compareTo(o2.getCard());
	};
	private static final Comparator<Risable> RS_CMP = (o1, o2) -> {
		int v = o1.getOrigin().getOwner().compareTo(o2.getOrigin().getOwner());
		if (v != 0) {
			return v;
		}
		return o1.getCard().compareTo(o2.getCard());
	};

	/**
	 * Locates bounds of rising cards.
	 * @param cards cards to be located
	 * @param comp  component playing card risen concerning animation
	 */
	public static void locateRiseViews(List<? extends Risable> cards, AnimationLayer comp) {
		BoundsUtility util = new BoundsUtility(comp);
		// sort
		cards.sort(RS_CMP);
		// classify
		Map<PlayerView, List<? extends Risable>> map = classify(cards);
		// relocate each card
		for (Map.Entry<PlayerView, List<? extends Risable>> entry : map.entrySet()) {
			util.locateRiseView(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Locates bounds of descending cards.
	 * @param cards cards to be descended
	 * @param comp  component playing card descended concerning animation.
	 */
	public static void locateDropViews(List<? extends Dropable> cards, AnimationLayer comp) {
		BoundsUtility util = new BoundsUtility(comp);
		// sort
		cards.sort(RS_CMP);
		// classify
		Map<PlayerView, List<? extends Dropable>> map = classify(cards);
		// relocate each card
		for (Map.Entry<PlayerView, List<? extends Dropable>> entry : map.entrySet()) {
			util.locateDropView(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Locates bounds of descending cards.
	 * @param cards cards to be descended
	 * @param comp  component playing card descended concerning animation.
	 */
	public static void locateDescendViews(List<? extends Descendable> cards, AnimationLayer comp) {
		BoundsUtility util = new BoundsUtility(comp);
		cards.sort(DESC_CMP);
		util.locateCDescendView(cards);
	}


	private final int compWidth, compHeight, margin, defaultRange;
	private final Dimension topSize;
	private final Component destComp;

	private static <T extends Risable> Map<PlayerView, List<? extends T>> classify(List<? extends T> list) {
		// get all origin players
		Set<PlayerView> allPlayers = list.stream().map(r -> r.getOrigin().getOwner()).collect(toSet());

		// iterate all players and add them to map
		Map<PlayerView, List<? extends T>> map = new HashMap<>(allPlayers.size());
		List<? extends T> sublist;
		for (PlayerView v : allPlayers) {
			sublist = list.stream().filter(r -> r.getOrigin().getOwner() == v).collect(toList());
			map.put(v, sublist);
		}

		return map;
	}

	private int getY(PlayerView v) {
		CountView cv = v.getCountView(null);
		Insets inset = cv.getInsets();
		int cvCenterY = inset.top + (cv.getHeight() - inset.top + inset.bottom) / 2;
		Point absCenter = SwingUtilities.convertPoint(cv, 0, cvCenterY, destComp);
		int idealY = absCenter.y - topSize.height / 2;
		return ensureInRange(idealY, margin, compHeight - margin - topSize.height);
	}

	private void locateCDescendView(List<? extends Descendable> cards) {
		// calculate y coordinate
		final int y = (compHeight - topSize.height) / 2;

		// check if only one card
		final int count = cards.size();
		if (count == 1) {
			// if only one card, no need to calculate
			cards.get(0).setTopBounds(new Rectangle((compWidth - topSize.width) / 2, y, topSize.width, topSize.height));
			return;
		}

		// calculate range
		final float range;
		float idealRange = (float) (compWidth - margin * 2 - topSize.width) / (count - 1);
		range = Math.min(defaultRange, idealRange);

		// calculate total width
		final int totalWidth = round((count - 1) * range) + topSize.width;
		// calculate x coordinate of first card
		final int x0 = (compWidth - totalWidth) / 2;

		// set coordinate of each card
		float x = x0;
		for (Descendable d : cards) {
			d.setTopBounds(new Rectangle(round(x), y, topSize.width, topSize.height));
			x += range;
		}
	}

	private void locateDropView(PlayerView v, List<? extends Dropable> list) {
		// calculate the y coordinate of each card
		final int y = getY(v);

		// calculate range and total width
		final float range;
		final int totalWidth;
		final int count = list.size();
		if (count == 1) {
			// if only one card, no need to calculate
			totalWidth = topSize.width;
			range = -1; // range is not needed
		}
		else {
			final int idealWidth = defaultRange * (count - 1) + topSize.width;
			// width too large
			int maxWidth = compWidth - margin * 2;
			if (idealWidth > maxWidth) {
				totalWidth = maxWidth;
				range = (float) (totalWidth - topSize.width) / (count - 1);
			}
			// ideal width
			else {
				totalWidth = idealWidth;
				range = defaultRange;
			}
		}

		// calculate the x coordinate of the first card
		final int x0;
		final int minX = margin;
		final int maxX = compWidth - margin - totalWidth;
		Point playerPoint = SwingUtilities.convertPoint(v, v.getTargetCenterPoint(), destComp);
		x0 = ensureInRange(playerPoint.x - totalWidth / 2, minX, maxX);

		// set coordinate of each card
		float x = x0;
		for (Dropable r : list) {
			r.setTopBounds(new Rectangle(round(x), y, topSize.width, topSize.height));
			r.setRange(round(x + range) - round(x));
			x += range;
		}
		// adjust the range of the last card to the while card
		list.get(count - 1).setRange(topSize.width);
	}

	private void locateRiseView(PlayerView v, List<? extends Risable> list) {
		// calculate the y coordinate of each card
		final int y = getY(v);

		// calculate range and total width
		final float range;
		final int totalWidth;
		final int count = list.size();
		if (count == 1) {
			// if only one card, no need to calculate
			totalWidth = topSize.width;
			range = -1; // range is not needed
		}
		else {
			final int idealWidth = defaultRange * (count - 1) + topSize.width;
			// width too large
			int maxWidth = compWidth - margin * 2;
			if (idealWidth > maxWidth) {
				totalWidth = maxWidth;
				range = (float) (totalWidth - topSize.width) / (count - 1);
			}
			// ideal width
			else {
				totalWidth = idealWidth;
				range = defaultRange;
			}
		}

		// calculate the x coordinate of the first card
		final int x0;
		final int minX = margin;
		final int maxX = compWidth - margin - totalWidth;
		Point playerPoint = SwingUtilities.convertPoint(v, v.getTargetCenterPoint(), destComp);
		x0 = ensureInRange(playerPoint.x - totalWidth / 2, minX, maxX);

		// set coordinate of each card
		float x = x0;
		for (Risable r : list) {
			r.setTopBounds(new Rectangle(round(x), y, topSize.width, topSize.height));
			x += range;
		}
	}

	private BoundsUtility(AnimationLayer comp) {
		compWidth = comp.getWidth();
		compHeight = comp.getHeight();
		margin = compWidth / 100;
		defaultRange = compWidth / 20;
		topSize = comp.getRisenCardSize();
		destComp = comp;
	}
}
