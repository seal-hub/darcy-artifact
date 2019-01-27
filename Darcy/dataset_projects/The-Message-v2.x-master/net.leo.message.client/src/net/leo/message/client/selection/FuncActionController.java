package net.leo.message.client.selection;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.bridge.command.action.FuncCardAction;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.client.domain.GameView;
import net.leo.message.client.stack.FuncCardView;
import net.leo.message.client.stack.StackBox;
import net.leo.message.client.stack.StackBoxListener;
import net.leo.message.client.utility.ButtonListener;

class FuncActionController extends ActionController {

	private class CustomListener implements StackBoxListener, ButtonListener {

		//Function cards that are selected
		private List<FuncCardView> conts = new ArrayList<>(action.max);

		@Override
		public void onButtonClicked(Component source, int buttonIndex) {
			//Styop selection and remove listener
			gameView.getInstruction().inactivate();
			gameView.getStackBox().stopSelection();
			gameView.getStackBox().setListener(null);
			gameView.getInstruction().setListener(null);

			//Cancel
			if (buttonIndex == 1) {
				listener.onCompleted(null);
				return;
			}

			//Submit, and no next action
			if (action.nextAction == null) {
				StackBox stackBox = gameView.getStackBox();
				List<Integer> iCont = conts.stream().map(f -> stackBox.indexOf(f) + 100).collect(toList());
				listener.onCompleted(new Decision<>(iCont, null));
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
				listener.onCompleted(new Decision<>(conts, nextDecision));
			}), action.nextAction);
		}

		@Override
		public void onFuncToggled(StackBox source, FuncCardView view, int index, boolean status) {
			int size = conts.size();

			//Card is selected
			if (status) {
				if (size == action.max) {
					conts.get(--size).setSelected(false);
				}
				conts.add(view);
				if (++size >= action.min) {
					gameView.getInstruction().getSubmit().setEnabled(true);
				}
				return;
			}

			//Card is deselected
			conts.remove(view);
			if (--size < action.min) {
				gameView.getInstruction().getSubmit().setEnabled(false);
			}
		}
	}

	protected FuncCardAction action;

	FuncActionController(FuncCardAction action, GameView gameView, BasicSelectionListener listener) {
		super(gameView, listener);
		this.action = action;
	}

	@Override
	protected void run() {
		gameView.getInstruction().activate(action.inst, action.min == 0, !action.enforcement);
		gameView.getStackBox().runSelection(new HashSet<>(action.candidates));

		CustomListener fl = new CustomListener();
		gameView.getStackBox().setListener(fl);
		gameView.getInstruction().setListener(fl);
	}
}
