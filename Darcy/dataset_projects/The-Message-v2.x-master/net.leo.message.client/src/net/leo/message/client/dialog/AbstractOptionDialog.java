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
import net.leo.message.client.utility.OptionBox;
import net.leo.message.client.utility.OptionCardView;
import net.leo.message.client.utility.Timable;
import net.leo.message.client.utility.TimeRoll;

/**
 * A module of card net.leo.message.client.select dialog.
 * @author Leo Zeng
 */
public abstract class AbstractOptionDialog extends JPanel implements CardOptionDialog, CardBoxDialog, Timable {

	private static final int MARGIN = 6;

	private OptionBox candidates;
	private TimeRoll timeRoll;

	/**
	 * The container which contains buttons of this dialog.
	 */
	protected final JPanel btnPanel = new JPanel();
	/**
	 * Submit button. It is disabled initially.
	 */
	protected final JButton submit = new JButton("確定");
	/**
	 * Cancel button.
	 */
	protected final JButton cancel = new JButton("取消");
	/**
	 * True if this selection is mandatory.
	 */
	protected final boolean enforcement;
	/**
	 * Listener of this diaog.
	 */
	protected CardSelectionListener listener = null;

	/**
	 * Construct a model of card net.leo.message.client.select dialog.
	 * @param title       title of this dialog
	 * @param candidates  candidates
	 * @param enforcement true if this selection is mandatory
	 * @throws IllegalArgumentException if no candidates
	 * @throws NullPointerException     if title or timer is null
	 */
	public AbstractOptionDialog(String title, List<? extends Card> candidates, boolean enforcement) {
		super(new BorderLayout());
		if (title == null) {
			throw new NullPointerException();
		}
		if (candidates.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.enforcement = enforcement;
		createGUI(candidates, title);
	}


	private void createGUI(List<? extends Card> cards, String title) {
		//Create option box and add listener
		candidates = new OptionBox();
		candidates.setListener((source, status) -> {
			onCardClicked(source, status);
		});


		//Add cards into dialog card list
		List<OptionCardView> list = cards.stream().map(gc -> new OptionCardView(GameCard.getInstance(gc))).collect(toList());
		candidates.offerAll(list);
		candidates.sort();

		//Set buttons and button listener
		submit.setEnabled(false);
		submit.addActionListener(e -> {
			onSubmitClicked();
		});
		if (enforcement) {
			cancel.setEnabled(false);
		}
		else {
			cancel.addActionListener(e -> {
				onCancelCliked();
			});
		}

		//Set time roll
		timeRoll = new TimeRoll(true);
		timeRoll.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

		//Add components
		btnPanel.add(submit);
		btnPanel.add(cancel);
		JPanel south = new JPanel(new BorderLayout());
		south.add(btnPanel, BorderLayout.EAST);
		south.add(timeRoll, BorderLayout.CENTER);
		south.setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN * 3, MARGIN, MARGIN * 3));
		JPanel boxPanel = new JPanel(new BorderLayout());
		boxPanel.add(candidates);
		boxPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN),
		                                                      BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title),
		                                                                                         BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN))));
		add(boxPanel, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);
	}


	protected abstract void onCancelCliked();

	protected abstract void onCardClicked(OptionCardView source, boolean status);

	protected abstract void onSubmitClicked();

	@Override
	public void setListener(CardSelectionListener listener) {
		this.listener = listener;
	}

	@Override
	public void setPreferredBoxSize(Dimension size) {
		candidates.setPreferredSize(size);
	}

	@Override
	public void update(double rate) {
		timeRoll.update(rate);
	}
}
