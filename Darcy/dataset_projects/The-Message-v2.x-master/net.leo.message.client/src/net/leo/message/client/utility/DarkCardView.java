package net.leo.message.client.utility;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import net.leo.message.client.element.GameCard;

/**
 * A label showing a card which can be darken.
 * @author Leo Zeng
 */
@SuppressWarnings("serial")
public class DarkCardView extends BasicCardView {

	private static final Color DARK = new Color(0f, 0f, 0f,  0.7f);
	private boolean darken = false;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (darken) {
			Insets inset = getInsets();
			g.fillRect(inset.left, inset.top, getWidth() - (inset.left + inset.right), getHeight() - (inset.top + inset.bottom));
		}
	}

	/**
	 * Queries if this card is darken.
	 * @return true if this card is darken
	 */
	public boolean isDarken() {
		return darken;
	}

	/**
	 * Sets if this card should be darken.
	 * @param darken true to darken this card
	 */
	public void setDarken(boolean darken) {
		if (this.darken == darken) {
			return;
		}
		this.darken = darken;
		Container parent = getParent();
		if (parent == null) {
			repaint();
			return;
		}
		Rectangle bounds = getBounds();
		parent.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	/**
	 * Constructs a label.
	 * @param card card to be shown
	 * @throws NullPointerException if card is null
	 */
	public DarkCardView(GameCard card) {
		super(card);
		setForeground(DARK);
	}
}
