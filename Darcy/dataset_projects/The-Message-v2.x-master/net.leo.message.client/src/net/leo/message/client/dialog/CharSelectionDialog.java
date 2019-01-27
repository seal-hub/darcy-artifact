package net.leo.message.client.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import net.leo.message.client.utility.CharHint;
import net.leo.message.client.utility.Hint;
import net.leo.message.client.utility.HintLayer;
import net.leo.message.client.utility.HorizontalBoxLayout;
import net.leo.message.client.utility.Timable;
import net.leo.message.client.utility.TimeRoll;

/**
 * A view that shows some characters for client to net.leo.message.client.select.
 * @author Leo Zeng
 */
public class CharSelectionDialog extends JPanel implements Dialog, Timable {

	private static final int MARGIN = 8;

	private static final Dimension PREFERRED_CHARACTER_IMAGE_SIZE = new Dimension(200, 283);
	private static final Color DARK = new Color(0f, 0f, 0f, 0.5f);

	private class CharacterView extends JComponent implements MouseInputListener {

		private CharHint hint;
		private Image hintImage;
		private Image characterImage;
		private boolean selected = false;

		public CharacterView(CharHint hint) {
			super();
			this.hint = hint;
			createHintImage(hint);
			createCharacterImage();
			setForeground(DARK);
			addMouseListener(this);
			addMouseMotionListener(this);
		}

		private void createCharacterImage() {
			try {
				characterImage = ImageIO.read(CharSelectionDialog.class.getResource("/image/skill/character/" + hint.getCharacter().getName() + ".png"));
			}
			catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}

		private void createHintImage(Hint hint) {
			hintImage = hint.createImage();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Insets inset = getInsets();
			int height = getHeight() - (inset.top + inset.bottom);
			int width = getWidth() - (inset.left + inset.right);
			g.drawImage(characterImage, inset.left, inset.top, width, height, null);
			if (!selected) {
				g.fillRect(inset.left, inset.top, width, height);
			}
		}

		@Override
		public Dimension getPreferredSize() {
			Insets inset = getInsets();
			int width = PREFERRED_CHARACTER_IMAGE_SIZE.width + inset.left + inset.right;
			int height = PREFERRED_CHARACTER_IMAGE_SIZE.height + inset.top + inset.bottom;
			return new Dimension(width, height);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			selected = true;
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			selected = false;
			hintLayer.hideHint();
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Point p = SwingUtilities.convertPoint(CharacterView.this, e.getPoint(), hintLayer);
			hintLayer.showHint(p, hintImage);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (listener != null) {
				listener.onCompleted(this, hint.getCharacter());
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}

	private TimeRoll timeRoll;
	private HintLayer hintLayer;
	private JPanel rollPanel = new JPanel(new BorderLayout());
	private CharDialogListener listener;

	/**
	 * Constructs a character net.leo.message.client.select dialog.
	 * @param hintLayer the hint layer that shows the hint of characters' skills
	 * @param hints     hints of characters' skills
	 * @throws IllegalArgumentException if no hint is passed
	 */
	public CharSelectionDialog(HintLayer hintLayer, List<CharHint> hints) {
		super(new BorderLayout());
		if (hintLayer == null) {
			throw new NullPointerException();
		}
		if (hints.size() == 0) {
			throw new IllegalArgumentException();
		}
		this.hintLayer = hintLayer;
		createGUI(hints);
	}

	private void createGUI(List<CharHint> hints) {
		//Create character views
		JPanel viewsPanel = new JPanel();
		viewsPanel.setLayout(new HorizontalBoxLayout(MARGIN, hints.size()));
		hints.forEach(hint -> {
			viewsPanel.add(new CharacterView(hint));
		});
		viewsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("選擇角色"), BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN)));
		add(viewsPanel, BorderLayout.CENTER);

		//Create time roll
		timeRoll = new TimeRoll(true);
		timeRoll.setSize(0, 18);
		rollPanel.add(timeRoll);
		rollPanel.setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN / 2, 0, MARGIN / 2));

		//Add components
		add(rollPanel, BorderLayout.SOUTH);
		setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
	}

	/**
	 * Sets a character selected listener.
	 * @param listener listener
	 */
	public void setListener(CharDialogListener listener) {
		this.listener = listener;
	}

	@Override
	public void update(double rate) {
		timeRoll.update(rate);
	}
}
