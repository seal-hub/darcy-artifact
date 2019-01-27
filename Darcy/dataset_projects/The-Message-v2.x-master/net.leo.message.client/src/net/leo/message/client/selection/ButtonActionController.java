package net.leo.message.client.selection;

import java.awt.Component;
import java.util.List;
import net.leo.message.base.bridge.command.action.ButtonAction;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.client.domain.GameView;
import net.leo.message.client.utility.ButtonListener;

class ButtonActionController extends ActionController {

	private class CustomListener implements ButtonListener {

		@Override
		public void onButtonClicked(Component source, int buttonIndex) {
			//Stop selection and remove listener
			gameView.getInstruction().inactivate();
			gameView.getInstruction().setListener(null);

			//Submit, and no next action
			if (action.nextAction == null) {
				listener.onCompleted(new Decision<>(List.of(buttonIndex), null));
				return;
			}

			//Submit, and run next action
			ActionController.execute(gameView, (nextDecision -> {

				//All actions are canceled
				if (nextDecision == null) {
					listener.onCompleted(null);
					return;
				}

				//All actions are completed
				listener.onCompleted(new Decision<>(List.of(buttonIndex), nextDecision));
			}), action.nextAction);
		}
	}

	protected ButtonAction action;

	ButtonActionController(ButtonAction action, GameView gameView, BasicSelectionListener listener) {
		super(gameView, listener);
		this.action = action;
	}

	@Override
	protected void run() {
		gameView.getInstruction().activate(action.inst, action.candidates);
		gameView.getInstruction().setListener(new CustomListener());
	}
}
