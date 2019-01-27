package net.leo.message.client.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.leo.message.base.lang.Card;
import net.leo.message.client.element.GameCard;
import net.leo.message.client.utility.BasicCardView;
import net.leo.message.client.utility.FoldBox;
import net.leo.message.client.utility.Timable;
import net.leo.message.client.utility.TimeRoll;

/**
 * A dialog that allows client to net.leo.message.client.select some cards, and order of cards are put into consideration.
 * @author Leo Zeng
 */
public class OrderedCheckDialog extends JPanel implements CardOptionDialog, CardBoxDialog, Timable {

	private static final int MARGIN = 6;

	private class OptionFoldBox extends FoldBox<BasicCardView> {

		public OptionFoldBox(int initialCapacity) {
			super(initialCapacity);
		}

		@Override
		protected boolean activateListener() {
			return count() != 0;
		}

		@Override
		protected void onMousePressed(int index) {
			super.onMousePressed(index);
			if (index != -1) {
				onCardClicked(get(index), this);
			}
		}
	}

	/**
	 * True if this selection is mandatory.
	 */
	protected final boolean enforcement;
	/**
	 * Minimum count of cards should be selected.
	 */
	protected final int min;
	/**
	 * Maximum count of cards can be selected.
	 */
	protected final int max;
	/**
	 * Reset button.
	 */
	protected final JButton reset = new JButton("清除");
	/**
	 * Submit button.
	 */
	protected final JButton submit = new JButton("確定");
	/**
	 * Cancel button.
	 */
	protected final JButton cancel = new JButton("取消");

	protected CardSelectionListener listener;
	private TimeRoll timeRoll;
	private OptionFoldBox candidates;
	private OptionFoldBox contributes;

	/**
	 * Constructs a card net.leo.message.client.select dialog.
	 * @param candidates  candidates
	 * @param title       title of this dialog
	 * @param inst        instructions of this dialog
	 * @param min         minimum count of cards should be selected
	 * @param max         maximum count of cards can be selected
	 * @param enforcement true if this selection is mandatory
	 */
	public OrderedCheckDialog(String title, List<? extends Card> candidates, String inst, int min, int max, boolean enforcement) {
		super(new BorderLayout());
		if (candidates.isEmpty()) {
			throw new IllegalArgumentException();
		}
		if (min < 0 || max < 0 || min > max || (min > candidates.size() && enforcement)) {
			throw new IllegalArgumentException();
		}
		this.enforcement = enforcement;
		this.min = min;
		this.max = max;
		this.candidates = new OptionFoldBox(candidates.size());
		this.contributes = new OptionFoldBox(candidates.size());
		createGUI(candidates, title, inst);
	}

	private void createGUI(List<? extends Card> cards, String title, String description) {
		//Adds cards into box
		List<BasicCardView> list = cards.stream().map(gc -> new BasicCardView(GameCard.getInstance(gc))).collect(toList());
		candidates.offerAll(list);
		candidates.sort();

		//Sets buttons
		submit.setEnabled(false);
		submit.addActionListener(e -> {
			if (listener != null) {
				listener.onCardSubmit(this, contributes.getAll().stream().map(c -> c.getGameCard()).collect(toList()));
			}
		});
		if (enforcement) {
			cancel.setEnabled(false);
		}
		else {
			cancel.addActionListener(e -> {
				if (listener != null) {
					listener.onCardSubmit(this, null);
				}
			});
		}
		reset.setEnabled(false);
		reset.addActionListener(e -> {
			candidates.offerAll(contributes.getAll());
			contributes.clear();
			candidates.sort();
			reset.setEnabled(false);
			submit.setEnabled(false);
		});

		//Sets time roll
		timeRoll = new TimeRoll(true);
		timeRoll.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

		//Assembles this dialog
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(submit);
		buttonsPanel.add(cancel);
		buttonsPanel.add(reset);
		JPanel candPanel = new JPanel(new BorderLayout());
		candPanel.add(candidates);
		candPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN),
		                                                       BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title),
		                                                                                          BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN))));
		JPanel contPanel = new JPanel(new BorderLayout());
		contPanel.add(contributes);
		contPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, MARGIN, MARGIN, MARGIN),
		                                                       BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(description),
		                                                                                          BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN))));
		JPanel south = new JPanel(new BorderLayout());
		south.add(buttonsPanel, BorderLayout.EAST);
		south.add(timeRoll, BorderLayout.CENTER);
		south.setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN * 3, MARGIN, MARGIN * 3));
		add(candPanel, BorderLayout.NORTH);
		add(contPanel, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);
	}

	private void onCardClicked(BasicCardView source, OptionFoldBox container) {
		//Move from unselected to selected
		if (container == candidates) {
			int cCount = contributes.count();
			if (cCount >= max) {
				return;
			}
			candidates.poll(source);
			contributes.offer(source);

			//Check buttons state
			reset.setEnabled(true);
			if (contributes.count() >= min) {
				submit.setEnabled(true);
			}
		}

		//Move from selected to unselected
		else {
			contributes.poll(source);
			candidates.offer(source);
			candidates.sort();

			//Check buttons state
			if (contributes.isEmpty()) {
				reset.setEnabled(false);
				submit.setEnabled(false);
			}
			else if (contributes.count() < min) {
				submit.setEnabled(false);
			}
		}
	}

	@Override
	public void setListener(CardSelectionListener listener) {
		this.listener = listener;
	}

	@Override
	public void setPreferredBoxSize(Dimension size) {
		candidates.setPreferredSize(size);
		contributes.setPreferredSize(size);
	}

	@Override
	public void update(double rate) {
		timeRoll.update(rate);
	}
}
