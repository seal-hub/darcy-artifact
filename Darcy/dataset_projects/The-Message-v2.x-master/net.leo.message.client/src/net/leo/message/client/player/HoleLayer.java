package net.leo.message.client.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Objects;
import java.util.Set;
import javax.swing.JComponent;
import net.leo.message.client.utility.CalculationUtility;

public class HoleLayer extends JComponent implements ComponentListener {

	private static final Color BLACK = new Color(0f, 0f, 0f, 0.85f);
	private Set<Integer> holes;
	private final PlayerBox controller;
	private Image image;

	HoleLayer(PlayerBox controller) {
		this.controller = controller;
		setForeground(BLACK);
	}

	private void createImage() {
		if (holes == null) {
			image = null;
			return;
		}
		image = CalculationUtility.drawHoles(BLACK, getSize(), holes.stream().map(h -> controller.get(h).getBounds()).toArray(l -> new Rectangle[holes.size()]));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image == null) {
			return;
		}

		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	@Override
	public void componentResized(ComponentEvent e) {
		createImage();
	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	public void setHoles(Set<Integer> holes) {
		if (Objects.equals(this.holes, holes)) {
			return;
		}
		this.holes = holes;
		createImage();
		repaint();
	}
}
