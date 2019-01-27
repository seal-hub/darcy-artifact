package net.leo.message.client.selection;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.bridge.command.action.PlayerAction;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.client.domain.GameView;
import net.leo.message.client.player.PlayerBoxListener;
import net.leo.message.client.player.PlayerView;
import net.leo.message.client.utility.ButtonListener;

class PlayerActionController extends ActionController {

	private class CustomListener implements PlayerBoxListener, ButtonListener {

		private List<PlayerView> contributes = new ArrayList<>(action.max);

		@Override
		public void onButtonClicked(Component source, int buttonIndex) {
			gameView.getInstruction().inactivate();
			gameView.getPlayerBox().stopSelection();
			gameView.setGameListener(null);

			if (buttonIndex == 1) {
				listener.onCompleted(null);
				return;
			}

			if (action.nextAction == null) {
				listener.onCompleted(new Decision<>(contributes.stream().map(p -> p.seat).collect(toList()), null));
				return;
			}

			ActionController.execute(gameView, (nextDecision -> {
				if (nextDecision == null) {
					listener.onCompleted(null);
				}
				else {
					listener.onCompleted(new Decision<>(contributes, nextDecision));
				}
			}), action.nextAction);
		}

		@Override
		public void onPlayerToggled(PlayerView player, boolean status) {
			int size = contributes.size();
			if (status) {
				if (size == action.max) {
					contributes.remove(--size).setSelected(false);
				}
				contributes.add(player);
				if (++size >= action.min) {
					gameView.getInstruction().getSubmit().setEnabled(true);
				}
			}
			else {
				contributes.remove(player);
				if (--size < action.min) {
					gameView.getInstruction().getSubmit().setEnabled(false);
				}
			}
		}
	}

	protected PlayerAction action;

	PlayerActionController(PlayerAction action, GameView gameView, BasicSelectionListener listener) {
		super(gameView, listener);
		this.action = action;
	}

	@Override
	protected void run() {
		gameView.getInstruction().activate(action.inst, action.min == 0, !action.enforcement);
		gameView.getPlayerBox().runSelection(new HashSet<>(action.candidates));

		CustomListener cl = new CustomListener();
		gameView.getPlayerBox().setListener(cl);
		gameView.getInstruction().setListener(cl);
	}
}
