package net.leo.message.client.selection;

import java.awt.Rectangle;
import java.util.List;
import java.util.Objects;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.bridge.command.action.OrderedCardrAction;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.client.dialog.DialogLayer;
import net.leo.message.client.dialog.OrderedCheckDialog;
import net.leo.message.client.domain.GameView;

public class OrderedCardActionController extends ActionController {

	private final OrderedCardrAction action;

	protected OrderedCardActionController(OrderedCardrAction action, GameView gameView, BasicSelectionListener listener) {
		super(gameView, listener);
		this.action = Objects.requireNonNull(action);
	}

	private void check() {
		DialogLayer layer = gameView.getDialogLayer();
		OrderedCheckDialog dialog = new OrderedCheckDialog(action.inst, action.candidates, action.inst2, action.min, action.max, action.enforcement);
		dialog.setListener((source, list) -> {
			gameView.getInstruction().inactivate();
			gameView.getInstruction().setListener(null);
			Rectangle bounds = dialog.getBounds();
			layer.remove(dialog);
			layer.repaint(bounds);

			if (list == null) {
				listener.onCompleted(null);
				return;
			}
			List<Card> conts = list.stream().map(c->c.getElement()).collect(toList());

			if (action.nextAction == null) {
				listener.onCompleted(new Decision<>(conts, null));
				return;
			}

			ActionController.execute(gameView, (nextDecision -> {

				//All actions are canceled
				if (nextDecision == null) {
					listener.onCompleted(null);
					return;
				}

				//All actions are completed
				listener.onCompleted(new Decision<>(conts, nextDecision));
			}), action.nextAction);
		});

		layer.add(dialog);
		layer.validate();
	}


	@Override
	protected void run() {
		check();
	}
}
