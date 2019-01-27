package net.leo.message.client.player;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JLabel;
import static net.leo.message.client.utility.CalculationUtility.round;

public class IdView extends JLabel {

	private String id;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Insets inset = getInsets();
		int fontSize = round((getHeight() - inset.top - inset.bottom) * 0.775);
		g.setFont(new Font("Serif", Font.PLAIN, fontSize));
		g.drawString(id, 2, fontSize);
	}

	public IdView(String id) {
		super();
		if (id == null) {
			id = "";
		}
		this.id = id;
	}
}
