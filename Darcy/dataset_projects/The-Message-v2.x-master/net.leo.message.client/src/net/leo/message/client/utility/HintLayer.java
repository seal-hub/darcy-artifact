package net.leo.message.client.utility;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JLayeredPane;

/**
 * A layer that shows hints.
 * @author Leo Zeng
 */
public class HintLayer extends JLayeredPane {

	private Image hint;
	private Rectangle clip;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (hint != null) {
			g.drawImage(hint, clip.x, clip.y, clip.width, clip.height, null);
		}
	}

	/**
	 * Removes hints on this layer.
	 */
	public void hideHint() {
		if (hint != null) {
			hint = null;
			repaint(clip);
			clip = null;
		}
	}

	/**
	 * Shows a hint image on this layer
	 * @param location location of hint
	 * @param hint     hint image
	 */
	public void showHint(Point location, Image hint) {
		if (hint == null || location == null) {
			throw new NullPointerException();
		}

		//No paint if both location and hint are same
		boolean same = this.hint == hint;
		if (same && clip != null && location.equals(clip.getLocation())) {
			return;
		}

		//Set clip
		if (clip == null) {
			clip = new Rectangle();
		}
		else {
			//Repaint at old bounds
			repaint(clip);
		}
		clip.setLocation(location.x - hint.getWidth(null), location.y - hint.getHeight(null));
		if (!same) {
			//Resize if this is a new image
			this.hint = hint;
			clip.setSize(hint.getWidth(null), hint.getHeight(null));
		}

		//Adjust image location if out of screen
		if (clip.x < 0) {
			clip.x = location.x;
		}
		if (clip.y < 0) {
			clip.y = location.y;
		}

		//Repaint at new bounds
		repaint(clip);
	}

	/**
	 * Constructs a hint layer.
	 */
	public HintLayer() {
		super();
	}
}
