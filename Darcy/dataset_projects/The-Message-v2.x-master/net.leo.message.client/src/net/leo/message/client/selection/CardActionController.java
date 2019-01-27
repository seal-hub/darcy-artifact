package net.leo.message.client.selection;

import java.awt.Rectangle;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.bridge.command.action.CardAction;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.client.dialog.CheckDialog;
import net.leo.message.client.dialog.DialogLayer;
import net.leo.message.client.dialog.RadioDialog;
import net.leo.message.client.domain.GameView;

public class CardActionController extends ActionController {

	private final CardAction action;

	protected CardActionController(CardAction action, GameView gameView, BasicSelectionListener listener) {
		super(gameView, listener);
		this.action = Objects.requireNonNull(action);
	}

	private void check() {
		DialogLayer layer = gameView.getDialogLayer();
		CheckDialog dialog = new CheckDialog(action.inst, action.candidates, action.min, action.max, action.enforcement);
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
			Card cont = list.get(0).getElement();

			if (action.nextAction == null) {
				listener.onCompleted(new Decision<>(cont, null));
				return;
			}

			ActionController.execute(gameView, (nextDecision -> {

				//All actions are canceled
				if (nextDecision == null) {
					listener.onCompleted(null);
					return;
				}

				//All actions are completed
				listener.onCompleted(new Decision<>(cont, nextDecision));
			}), action.nextAction);
		});

		layer.add(dialog);
		layer.validate();
	}

	private void radio() {
		DialogLayer layer = gameView.getDialogLayer();
		RadioDialog dialog = new RadioDialog(action.inst, action.candidates, action.enforcement);
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
		int min = action.min;
		int max = action.max;
		if (min == 1 && max == 1) {
			radio();
		}
		else {
			check();
		}
	}
}
