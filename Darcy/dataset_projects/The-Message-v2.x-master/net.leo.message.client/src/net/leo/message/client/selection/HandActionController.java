package net.leo.message.client.selection;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.bridge.command.action.HandAction;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.client.domain.GameView;
import net.leo.message.client.hand.HandBoxListener;
import net.leo.message.client.hand.HandView;
import net.leo.message.client.utility.ButtonListener;

class HandActionController extends ActionController {

	private class CustomListener implements HandBoxListener, ButtonListener {

		//Hand cards that are selected
		private List<HandView> conts = new ArrayList<>(action.max);

		@Override
		public void onButtonClicked(Component source, int buttonIndex) {
			//Remove listener
			gameView.getInstruction().inactivate();
			gameView.getHandBox().stopSelection();
			gameView.getHandBox().setListener(null);
			gameView.getInstruction().setListener(null);

			//Cancel
			if (buttonIndex == 1) {
				listener.onCompleted(null);
				return;
			}

			//Submit, and no next action
			List<Card> cardConts = conts.stream().map(h -> h.getGameCard().getElement()).collect(toList());
			if (action.nextAction == null) {
				listener.onCompleted(new Decision<>(cardConts, null));
				return;
			}

			//Submit, and run next action
			ActionController.execute(gameView, (nextDecision) -> {

				//All actions are canceled
				if (nextDecision == null) {
					listener.onCompleted(null);
					return;
				}

				//All actions are completed
				listener.onCompleted(new Decision<>(cardConts, nextDecision));
			}, action.nextAction);
		}

		@Override
		public void onHandToggled(HandView hand, boolean status) {
			int size = conts.size();
			if (status) {
				if (size == action.max) {
					conts.remove(--size).setSelected(false);
				}
				conts.add(hand);
				if (++size >= action.min) {
					gameView.getInstruction().getSubmit().setEnabled(true);
				}
			}
			else {
				conts.remove(hand);
				if (--size < action.min) {
					gameView.getInstruction().getSubmit().setEnabled(false);
				}
			}
		}
	}

	protected HandAction action;

	HandActionController(HandAction action, GameView gameView, BasicSelectionListener listener) {
		super(gameView, listener);
		this.action = action;
	}

	@Override
	protected void run() {
		gameView.getInstruction().activate(action.inst, action.min == 0, !action.enforcement);
		gameView.getHandBox().runSelection(new HashSet<>(action.candidates));

		//Add listeners
		CustomListener hl = new CustomListener();
		gameView.getHandBox().setListener(hl);
		gameView.getInstruction().setListener(hl);
	}
}
