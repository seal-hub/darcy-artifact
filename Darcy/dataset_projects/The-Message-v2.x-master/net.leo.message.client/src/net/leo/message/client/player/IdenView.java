package net.leo.message.client.player;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import net.leo.message.client.element.IdenCard;

public class IdenView extends JComponent {

	private IdenCard card = IdenCard.COVERED_CARD;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Insets inset = getInsets();
		Rectangle bounds = new Rectangle(inset.left, inset.top, getWidth() - (inset.left + inset.right), getHeight() - (inset.top + inset.bottom));
		g.drawImage(card.getImage(bounds.getSize()), bounds.x, bounds.y, bounds.width, bounds.height, null);
	}



	public IdenView() {
		super();
		setBorder(BorderFactory.createLoweredBevelBorder());
	}
}
