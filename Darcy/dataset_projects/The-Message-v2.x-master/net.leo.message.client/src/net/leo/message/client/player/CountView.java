package net.leo.message.client.player;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardColor;
import net.leo.message.client.animation.executor.ShineGear;
import net.leo.message.client.animation.executor.ShinePainter;
import net.leo.message.client.dialog.DialogLayer;
import net.leo.message.client.dialog.IntelViewDialog;
import net.leo.message.client.element.GameCard;
import static net.leo.message.client.utility.CalculationUtility.round;

/**
 * @author Leo Zeng
 */
@SuppressWarnings("serial")
public class CountView extends JLabel implements Comparable<CountView>, MouseListener {

	private static final double GRADIENT_TOP = 0.05;

	/**
	 * Builder of count views.
	 */
	public static class Builder {

		//private static final Color BG_RED = Color.RED, BG_BLUE = Color.BLUE, BG_BLACK = Color.BLACK, BG_HAND = Color.GREEN;
		private static final Color BLUE_TOP = new Color(0x00, 0x7d, 0xc1);
		private static final Color BLUE_BOTTOM = new Color(0x00, 0x61, 0xa7);
		private static final Color BLUE_BORDER = new Color(0x12, 0x4d, 0x77);
		private static final Color RED_TOP = new Color(0xd0, 0x45, 0x1b);
		private static final Color RED_BOTTOM = new Color(0xbc, 0x33, 0x15);
		private static final Color RED_BORDER = new Color(0x94, 0x29, 0x11);
		private static final Color BLACK_TOP = new Color(0x75, 0x75, 0x75);
		private static final Color BLACK_BOTTOM = new Color(0x3d, 0x3d, 0x3d);
		private static final Color BLACK_BORDER = new Color(0x1f, 0x1f, 0x1f);
		private static final Color HAND_TOP = new Color(0x00, 0x8f, 0x40);
		private static final Color HAND_BOTTOM = new Color(0x07, 0x45, 0x26);
		private static final Color HAND_BORDER = new Color(0x08, 0x61, 0x43);

		private final ShineGear gear;
		private final DialogLayer layer;

		/**
		 * Constructs a builder.
		 * @param gear  shine gear
		 * @param layer dialog layer
		 */
		public Builder(ShineGear gear, DialogLayer layer) {
			if (gear == null || layer == null) {
				throw new NullPointerException();
			}
			this.gear = gear;
			this.layer = layer;
		}

		/**
		 * Creates a count view.
		 * @param type color of count view
		 * @return a count view
		 * @throws IllegalArgumentException if type is illegal
		 */
		public CountView build(CardColor type) {
			CountView instance;
			if (type == null) {
				instance = new CountView();
				instance.top = HAND_TOP;
				instance.bottom = HAND_BOTTOM;
				instance.setBorder(BorderFactory.createLineBorder(HAND_BORDER, 1));
				instance.hand = true;
			}
			else {
				switch (type) {
				case RED:
					instance = new CountView();
					instance.top = RED_TOP;
					instance.bottom = RED_BOTTOM;
					instance.setBorder(BorderFactory.createLineBorder(RED_BORDER, 1));
					break;
				case BLUE:
					instance = new CountView();
					instance.top = BLUE_TOP;
					instance.bottom = BLUE_BOTTOM;
					instance.setBorder(BorderFactory.createLineBorder(BLUE_BORDER, 1));
					break;
				case BLACK:
					instance = new CountView();
					instance.top = BLACK_TOP;
					instance.bottom = BLACK_BOTTOM;
					instance.setBorder(BorderFactory.createLineBorder(BLACK_BORDER, 1));
					break;
				default:
					throw new InternalError();
				}
			}
			instance.painter = gear.createPainter(instance);
			instance.layer = layer;
			return instance;
		}
	}

	private boolean hand = false;
	private boolean mouseOn = false;
	private Color top, bottom;
	private ShinePainter painter;
	private DialogLayer layer;
	private List<Card> intels = new ArrayList<>(3);

	private CountView() {
		super("0", CENTER);
		setOpaque(false);
		setForeground(Color.WHITE);
		addMouseListener(this);
	}

	private Rectangle getBoundsFitCard() {
		Insets inset = getInsets();
		int x = inset.left;
		int y = inset.top;
		int height = getHeight() - (inset.top + inset.bottom);
		int width = getWidth() - (inset.left + inset.right);

		final int cardWidth = round(height * GameCard.RATIO_WIDTH_HEIGHT);
		//plus 1 to round x value
		x += (width - cardWidth) / 2 + 1;
		width = cardWidth;
		return new Rectangle(x, y, width, height);
	}

	@Override
	protected final void paintComponent(Graphics g) {
		super.paintComponent(g);
		painter.paintAnimation(g);
	}

	@Override
	public final int compareTo(CountView o) {
		int v = getOwner().compareTo(o.getOwner());
		if (v != 0) {
			return v;
		}
		return getX() - o.getX();
	}

	/**
	 * To get the bounds of this label relative to a component.
	 */
	public final Rectangle getBoundsFitCardRelativeTo(Component destination) {
		Rectangle bounds = getBoundsFitCard();
		Point abs = SwingUtilities.convertPoint(this, bounds.getLocation(), destination);
		bounds.setLocation(abs);
		return bounds;
	}

	/**
	 * To get the owner of this container.
	 */
	public PlayerView getOwner() {
		Container container = getParent();
		if (container instanceof PlayerView) {
			return (PlayerView) container;
		}
		else {
			return null;
		}
	}

	@Override
	public final Dimension getPreferredSize() {
		return new Dimension(128, 128);
	}

	/**
	 * Sets the increment of count of this label. Negative number represents decreasing.
	 * @param increment increment of count
	 */
	public final void increase(int increment) {
		if (increment == 0) {
			return;
		}
		int value = Integer.parseInt(getText());
		value += increment;
		setText(Integer.toString(value));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (hand || Integer.parseInt(getText()) <= 0) {
			return;
		}
		IntelViewDialog d = new IntelViewDialog(intels, "情報檢視");
		layer.add(d, DialogLayer.INTELLIGENCE_VIEW);
		layer.revalidate();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseOn = true;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseOn = false;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * Offers an inelligence into this count view.
	 * @param card intelligence
	 */
	public void offer(Card card) {
		if (card == null) {
			throw new NullPointerException();
		}
		intels.add(card);
	}

	/**
	 * Offers a plenty of intelligences into this count view.
	 * @param cards intelligences
	 */
	public void offerall(List<Card> cards) {
		if (cards.contains(null)) {
			throw new NullPointerException();
		}
		intels.addAll(cards);
	}

	@Override
	public void paint(Graphics g) {
		Color top = mouseOn ? this.bottom : this.top;
		Color bottom = mouseOn ? this.top : this.bottom;
		Insets inset = getInsets();
		int height = getHeight() - (inset.top + inset.bottom);
		int y0 = (int) (inset.top + height * GRADIENT_TOP);
		int y1 = inset.top + height;

		GradientPaint paint = new GradientPaint(0, y0, top, 0, y1, bottom);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(paint);
		g2d.fillRect(inset.left, inset.top, getWidth() - (inset.left + inset.right), height);

		super.paint(g);
	}

	/**
	 * Removes an intelligence from this count view.
	 * @param card intelligence
	 */
	public void poll(Card card) {
		if (card == null) {
			throw new NullPointerException();
		}
		intels.remove(card);
	}

	/**
	 * Rmoeves some intelligences from this count view.
	 * @param cards intelligences
	 */
	public void pollAll(List<Card> cards) {
		if (cards.contains(null)) {
			throw new NullPointerException();
		}
		intels.removeAll(cards);
	}

	@Override
	public String toString() {
		PlayerView owner = getOwner();
		if (owner == null) {
			return getClass() + ", " + getBackground();
		}
		return getClass() + "[" + getOwner().seat + ", " + getBackground() + "]";
	}
}

