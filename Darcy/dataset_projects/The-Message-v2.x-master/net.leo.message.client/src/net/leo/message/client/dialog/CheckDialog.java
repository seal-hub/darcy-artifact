package net.leo.message.client.dialog;

import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.swing.JButton;
import net.leo.message.base.lang.Card;
import net.leo.message.client.utility.OptionCardView;

/**
 * A dialog letting client to net.leo.message.client.select several cards. Cards' order are not put into consideration.
 * @author Leo Zeng
 */
public class CheckDialog extends AbstractOptionDialog {

	/**
	 * Minimum count of cards should be selected.
	 */
	protected final int min;
	/**
	 * Maximum count of cards can be selected.
	 */
	protected final int max;
	/**
	 * Cards that are selected.
	 */
	protected final LinkedList<OptionCardView> contributes;
	/**
	 * Reset button.
	 */
	protected final JButton reset = new JButton("清除");

	/**
	 * Constructs a cards net.leo.message.client.select dialog.
	 * @param candidates  candidates
	 * @param title       title of this dialog
	 * @param min         minimum count of cards should be selected, only 0 or positive numbers are permitted and must be less than count of candidates
	 * @param max         maximum count of cards can be selected, not less then min and must less than count of candidates
	 * @param enforcement true if this selection is mandatory
	 * @throws IllegalArgumentException if min or max is illegal, or no candidates
	 */
	public CheckDialog(String title, List<? extends Card> candidates, int min, int max, boolean enforcement) {
		super(title, candidates, enforcement);
		if (min < 0 || max < 0 || min > max || (min > candidates.size() && enforcement)) {
			throw new IllegalArgumentException();
		}
		this.min = min;
		this.max = max;
		this.contributes = new LinkedList<>();

		//Set and add reset button into button container
		reset.addActionListener(e -> {
			contributes.forEach(c -> c.setSelected(false));
			contributes.clear();
		});
		reset.setEnabled(false);
		btnPanel.add(reset);
	}

	@Override
	protected void onCancelCliked() {
		if (listener != null) {
			listener.onCardSubmit(this, null);
		}
	}

	@Override
	protected void onCardClicked(OptionCardView source, boolean status) {
		if (status) {
			if (contributes.size() == max) {
				contributes.removeLast().setSelected(false);
			}
			contributes.addLast(source);
			reset.setEnabled(true);
			if (contributes.size() >= min) {
				submit.setEnabled(true);
			}
		}
		else {
			contributes.remove(source);
			if (contributes.isEmpty()) {
				reset.setEnabled(false);
			}
			if (contributes.size() < min) {
				submit.setEnabled(false);
			}
		}
	}

	@Override
	protected void onSubmitClicked() {
		if (listener != null) {
			listener.onCardSubmit(this, contributes.stream().map(c -> c.getGameCard()).collect(toList()));
		}
	}
}
