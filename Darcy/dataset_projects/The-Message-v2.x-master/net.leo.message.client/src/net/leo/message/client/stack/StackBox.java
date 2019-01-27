package net.leo.message.client.stack;

import java.util.Set;
import java.util.stream.IntStream;
import net.leo.message.client.player.PlayerView;
import net.leo.message.client.selection.SelectionSet;
import net.leo.message.client.utility.ArrowTarget;
import net.leo.message.client.utility.DisplayBox;

/**
 * A box that stores used function cards.
 * @author Leo Zeng
 */
public class StackBox extends DisplayBox<FuncCardView> implements SelectionSet<Integer> {

	private ArrowLayer arrowLayer;
	private StackBoxListener listener = null;
	private Set<Integer> candidates;

	public StackBox() {
		super();
		arrowLayer = new ArrowLayer();
	}

	@Override
	protected boolean activateListener() {
		return count() != 0;
	}

	@Override
	protected void onMouseExited() {
		super.onMouseExited();
		arrowLayer.clear();
	}

	@Override
	protected void onMouseMoved(int index) {
		super.onMouseMoved(index);
		if (index == -1) {
			arrowLayer.clear();
			return;
		}

		//Determin origin and destination
		ArrowTarget origin, dest;
		FuncCardView card = get(index);
		PlayerView user = card.getUser();
		ArrowTarget target = card.getTarget();
		if (target == null || user == target) {
			origin = card;
			dest = user;
		}
		else {
			origin = user;
			dest = target;
		}
		arrowLayer.show(origin, dest);
	}

	@Override
	protected void onMousePressed(int index) {
		super.onMousePressed(index);

		if (candidates != null && candidates.contains(index + 100)) {
			FuncCardView fv = get(index);
			fv.setSelected(!fv.isSelected());
			if (listener != null) {
				listener.onFuncToggled(this, fv, index + 100, fv.isSelected());
			}
		}
	}

	public void disabled(Set<Integer> diss) {
		IntStream.range(0, count()).forEach(i -> {
			get(i).setDarken(diss.contains(i + 100));
		});
	}

	/**
	 * Gets the arrow layer of this box.
	 * @return arrowl layer
	 */
	public ArrowLayer getArrowLayer() {
		return arrowLayer;
	}

	public void pollLast() {
		if (isEmpty()) {
			throw new IllegalStateException();
		}
		poll(count() - 1);
		validate();
		repaint();
	}

	@Override
	public void runSelection(Set<Integer> candidates) {
		//Count from 100
		int i = 100;
		for (FuncCardView fv : getAll()) {
			fv.setDarken(!candidates.contains(i++));
		}
		this.candidates = candidates;
	}

	/**
	 * Sets listener of this box
	 * @param listener listener
	 */
	public void setListener(StackBoxListener listener) {
		this.listener = listener;
	}

	@Override
	public void stopSelection() {
		getAll().forEach(view -> {
			view.setDarken(false);
			view.setSelected(false);
		});
		this.candidates = null;
	}
}
