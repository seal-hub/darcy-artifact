package net.leo.message.client.utility;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JComponent;
import net.leo.message.client.element.GameCard;

/**
 * A label that shows a card. This label can fit in any size. This is a value-based label.
 * @author Leo Zeng
 */
@SuppressWarnings("serial")
public class BasicCardView extends JComponent implements CardView {

	protected final GameCard card;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Insets inset = getInsets();
		Rectangle clip = new Rectangle(inset.left, inset.top, getWidth() - (inset.left + inset.right), getHeight() - (inset.top + inset.bottom));
		g.drawImage(card.getImage(clip.getSize()), clip.x, clip.y, clip.width, clip.height, null);
	}

	@Override
	public GameCard getGameCard() {
		return card;
	}

	@Override
	public String toString() {
		return getClass() + "[" + card.name() + "]";
	}

	/**
	 * Construct a card label that shows a card.
	 * @param card the card to be shown
	 * @throws NullPointerException if card is null
	 */
	public BasicCardView(GameCard card) {
		super();
		this.card = card;
	}
}
