package net.leo.message.client.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import net.leo.message.client.element.GameCard;
import net.leo.message.client.utility.BasicCardView;
import net.leo.message.client.utility.ButtonListener;
import net.leo.message.client.utility.Timable;
import net.leo.message.client.utility.TimeRoll;

/**
 * A dialog that shows a card and a message. It may contains some buttons.
 * @author Leo Zeng
 */
public class ButtonDialog extends JPanel implements CardDialog, Timable {

	private static final int MARGIN = 6;

	private static class DialogText extends JTextArea {

		@SuppressWarnings("serial")
		private static class NoTextSelectionCaret extends DefaultCaret {

			public NoTextSelectionCaret(JTextComponent textComponent) {
				setBlinkRate(textComponent.getCaret().getBlinkRate());
				textComponent.setHighlighter(null);
			}

			@Override
			public int getMark() {
				return getDot();
			}
		}

		public DialogText(String text) {
			super(text);
			setLineWrap(true);
			setWrapStyleWord(true);
			setEditable(false);
			setOpaque(false);
			setCaret(new NoTextSelectionCaret(this));
		}
	}

	private static class DialogView extends JComponent {

		protected static final int PADDING = 12;
		protected static final Dimension SIZE_BUTTON = new Dimension(169, 30);

		private class DialogLayout implements LayoutManager {

			private int getContentHeight(int width) {
				return Math.max(SIZE_CARD.height, getItemsHeight(width));
			}

			private int getItemsHeight(int width) {
				text.setSize(width, 1000);
				int preferredTextHeight = text.getPreferredSize().height;
				return preferredTextHeight + buttons.size() * (SIZE_BUTTON.height + PADDING);
			}

			private int getPreferredWidth() {
				return SIZE_CARD.width + PADDING + SIZE_BUTTON.width;
			}

			@Override
			public void addLayoutComponent(String name, Component comp) {
			}

			@Override
			public void layoutContainer(Container parent) {
				Insets inset = getInsets();
				int maxWidth = getWidth() - (inset.left + inset.right);
				int maxHeight = getHeight() - (inset.top + inset.bottom);

				final int btnWidth = Math.min(maxWidth - PADDING - SIZE_CARD.width, SIZE_BUTTON.width);

				int x = inset.left;
				int y = inset.top + (maxHeight - SIZE_CARD.height) / 2;
				card.setBounds(new Rectangle(new Point(x, y), SIZE_CARD));

				x = inset.left + SIZE_CARD.width + PADDING;
				int textWidth = getWidth() - inset.right - x;
				int iHeight = getItemsHeight(textWidth);

				if (iHeight < maxHeight) {
					y = inset.top + (maxHeight - iHeight) / 2;
					text.setSize(textWidth, 1000);
					int preh = text.getPreferredSize().height;
					text.setBounds(x, y, textWidth, preh);

					x += (textWidth - btnWidth) / 2;
					y += preh + PADDING;
					for (JButton btn : buttons) {
						btn.setBounds(x, y, btnWidth, SIZE_BUTTON.height);
						y += (SIZE_BUTTON.height + PADDING);
					}
				}
				else {
					y = inset.top;
					text.setSize(textWidth, 0);
					int preh = text.getPreferredSize().height;
					text.setBounds(x, y, textWidth, preh);

					x += (textWidth - btnWidth) / 2;
					y = getHeight() - inset.bottom - SIZE_BUTTON.height;
					int nButton = buttons.size();
					for (int i = nButton - 1 ; i >= 0 ; i--) {
						buttons.get(i).setBounds(x, y, btnWidth, SIZE_BUTTON.height);
						y -= (SIZE_BUTTON.height + PADDING);
					}
				}
			}

			@Override
			public Dimension minimumLayoutSize(Container parent) {
				return preferredLayoutSize(parent);
			}

			@Override
			public Dimension preferredLayoutSize(Container parent) {
				Insets inset = getInsets();
				int w = inset.left + inset.right + getPreferredWidth();
				// when text view's width equals to button's width, it's preferred size
				int h = inset.top + inset.bottom + getContentHeight(SIZE_BUTTON.width);
				return new Dimension(w, h);
			}

			@Override
			public void removeLayoutComponent(Component comp) {
			}
		}

		protected final Dimension SIZE_CARD = new Dimension(108, 144);
		protected List<JButton> buttons = new ArrayList<>();
		protected BasicCardView card;
		protected DialogText text;

		public DialogView(GameCard card, String text) {
			super();
			setLayout(new DialogLayout());
			this.card = new BasicCardView(card);
			this.text = new DialogText(text);
			this.text.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
			add(this.card);
			add(this.text);
		}

		public void addButton(JButton button) {
			if (button == null) {
				throw new NullPointerException();
			}
			buttons.add(button);
			add(button);
		}

		public void setPreferredCardSize(Dimension size) {
			SIZE_CARD.setSize(size);
		}
	}


	private DialogView inner;
	private int nButton = 0;
	private ButtonListener listener;
	private TimeRoll timeRoll;

	/**
	 * Constructs a card dialog.
	 * @param card    card to be shown
	 * @param message message
	 * @param title   title of this dialog
	 * @throws NullPointerException if any argument is null
	 */
	public ButtonDialog(String title, GameCard card, String message) {
		super(new BorderLayout());
		if (title == null || card == null || message == null) {
			throw new NullPointerException();
		}
		createGUI(title, card, message);
	}

	private void createGUI(String title, GameCard card, String message) {
		//Set main view
		inner = new DialogView(card, message);
		inner.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN),
		                                                   BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title),
		                                                                                      BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN))));
		//Set time roll
		timeRoll = new TimeRoll(true);
		timeRoll.setSize(new Dimension(0, 16));

		//Assemble this view
		JPanel south = new JPanel(new BorderLayout());
		south.add(timeRoll, BorderLayout.CENTER);
		south.setBorder(BorderFactory.createEmptyBorder(0, MARGIN, MARGIN, MARGIN));
		add(inner, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);
	}

	/**
	 * Adds a button into this dialog.
	 * @param text    text of button
	 * @param enabled true if this button is enabled
	 * @throws NullPointerException if button is null
	 */
	public void addButton(String text, boolean enabled) {
		JButton button = new JButton(text);
		if (!enabled) {
			button.setEnabled(false);
		}
		final int index = nButton++;
		button.addActionListener(e -> {
			if (listener != null) {
				listener.onButtonClicked(this, index);
			}
		});
		inner.addButton(button);
	}

	/**
	 * Sets a button clicked listener.
	 * @param listener listener
	 */
	public void setListener(ButtonListener listener) {
		this.listener = listener;
	}

	@Override
	public void setPreferredCardSize(Dimension size) {
		inner.setPreferredCardSize(size);
		revalidate();
	}

	@Override
	public void update(double rate) {
		timeRoll.update(rate);
	}
}
