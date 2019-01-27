package net.leo.message.client.selection;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import net.leo.message.base.bridge.command.action.IntelAction;
import net.leo.message.base.bridge.command.action.IntelLocation;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.client.dialog.CardSelectionListener;
import net.leo.message.client.dialog.CheckDialog;
import net.leo.message.client.domain.GameView;
import net.leo.message.client.element.GameCard;
import net.leo.message.client.player.PlayerBoxListener;
import net.leo.message.client.player.PlayerView;
import net.leo.message.client.utility.ButtonListener;

class IntelActionController extends ActionController {

	private class CustomeListener implements PlayerBoxListener, CardSelectionListener, ButtonListener {

		//Intelligence that are slected
		private List<IntelLocation> conts = new ArrayList<>(action.max);
		//Players that can be selected
		private Set<Integer> pCands;
		//The current player who is being selected
		private PlayerView ima = null;
		//Count of players who have been selected
		private int nPlayer = 1;
		//The dialog currently on the screen
		private Component dialog = null;

		public CustomeListener() {
			pCands = action.candidates.stream().map(i -> i.location).collect(toSet());
		}

		private void playerSelectingMode() {
			ima = null;

			//Set listeners
			gameView.getInstruction().activate("選擇第 " + nPlayer + " 位玩家。", conts.size() >= action.min, !action.enforcement);
			gameView.getPlayerBox().runSelection(pCands);
			gameView.getPlayerBox().setListener(this);
			gameView.getInstruction().setListener(this);
		}

		private void removeDialog() {
			Rectangle bounds = dialog.getBounds();
			gameView.getDialogLayer().remove(dialog);
			gameView.validate();
			gameView.repaint(bounds);
			dialog = null;
		}

		@Override
		public void onButtonClicked(Component source, int buttonIndex) {
			//Remove listeners
			gameView.getInstruction().inactivate();
			gameView.getPlayerBox().stopSelection();
			gameView.getPlayerBox().setListener(null);
			gameView.getInstruction().setListener(null);

			//Cancel
			if (buttonIndex == 1) {
				listener.onCompleted(null);
				return;
			}

			//Actions completed
			if (action.nextAction == null) {
				listener.onCompleted(new Decision<>(conts, null));
				return;
			}

			//Run next action
			ActionController.execute(gameView, (nextDecision -> {

				//All actions canceled
				if (nextDecision == null) {
					listener.onCompleted(null);
					return;
				}

				//Completed
				listener.onCompleted(new Decision<>(conts, nextDecision));
			}), action.nextAction);
		}

		@Override
		public void onCardSubmit(Component source, List<GameCard> intels) {
			//If client does select intelligence, do this tasks
			if (intels != null && !intels.isEmpty()) {

				//Add contributes
				List<IntelLocation> newConts = intels.stream().map(c -> new IntelLocation(ima.seat, c.getElement())).collect(toList());
				conts.addAll(newConts);

				//If max, send message or do next step
				int nCont = conts.size();
				if (nCont == action.max) {
					removeDialog();
					onButtonClicked(gameView.getInstruction(), 0);
					return;
				}

				//Removes this player from candidates
				pCands.remove(ima.seat);
				if (pCands.isEmpty()) {
					removeDialog();
					onButtonClicked(gameView, 0);
					return;
				}

				//Count of players selected +1
				nPlayer++;
			}

			//Go back to player selecting mode
			removeDialog();
			playerSelectingMode();
		}

		@Override
		public void onPlayerToggled(PlayerView player, boolean status) {
			//Diselect this player view
			player.setSelected(false);
			ima = player;

			//Create dialog
			List<Card> intelCanddidates = action.candidates.stream().filter(i -> i.location == player.seat).map(i -> i.card).collect(toList());
			int max = action.max - conts.size();
			CheckDialog cd = new CheckDialog(action.inst, intelCanddidates, 0, max, false);
			cd.setListener(this);
			dialog = cd;

			//Inactivate instruction
			gameView.resetAll();
			gameView.getInstruction().inactivate();

			//Add dialog
			gameView.getDialogLayer().add(cd);
			gameView.validate();
		}
	}

	protected IntelAction action;

	IntelActionController(IntelAction action, GameView gameView, BasicSelectionListener listener) {
		super(gameView, listener);
		this.action = action;
	}

	@Override
	protected void run() {
		CustomeListener cl = new CustomeListener();
		cl.playerSelectingMode();
	}
}
