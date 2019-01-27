package net.leo.message.client.utility;

import java.awt.Color;
import static java.awt.Color.WHITE;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import net.leo.message.client.element.GameCard;
import net.leo.message.client.selection.Selectable;

public class OptionCardView extends DarkCardView implements Selectable {

	private static final Color GREEN_LIGHT = new Color(0f, 1f, 0f, 0f);

	private boolean status = false;

	public OptionCardView(GameCard card) {
		super(card);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (status) {
			Insets inset = getInsets();
			int realHeight = getHeight() - (inset.top + inset.bottom);
			int y0 = inset.top + (int) (realHeight * 0.52), y1 = getHeight() - inset.bottom;
			Paint grad = new GradientPaint(0, y0, GREEN_LIGHT, 0, y1, WHITE);

			Graphics2D g2d = (Graphics2D) g;
			g2d.setPaint(grad);
			g2d.fillRect(inset.left, y0, getWidth() - (inset.left + inset.right), y1 - y0);
		}
	}

	@Override
	public boolean isSelected() {
		return status;
	}

	@Override
	public void setSelected(boolean status) {
		if (this.status != status) {
			this.status = status;
			repaint(); //TODO determine clip
		}
	}
}
