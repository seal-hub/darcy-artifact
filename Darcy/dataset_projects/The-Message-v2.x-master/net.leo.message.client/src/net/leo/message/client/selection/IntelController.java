package net.leo.message.client.selection;

import java.awt.Component;
import java.util.List;
import java.util.Map;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.client.domain.GameView;
import net.leo.message.client.hand.HandBoxListener;
import net.leo.message.client.hand.HandView;
import net.leo.message.client.skill.SkillBoxListener;
import net.leo.message.client.utility.ButtonListener;

public class IntelController {

	private class CustomListener implements SkillBoxListener, ButtonListener, HandBoxListener {

		private HandView hvCon;
		private String skCon;

		private void end() {
			gameView.getInstruction().inactivate();
			gameView.getHandBox().stopSelection();
			gameView.getHandBox().setListener(null);
			gameView.getSkillBox().setListener(null);
			gameView.getSkillBox().prompt(null);
		}

		@Override
		public void onButtonClicked(Component source, int buttonIndex) {
			end();

			if (buttonIndex == 1) {
				listener.onCompleted(null);
				return;
			}

			if (hvCon != null) {
				listener.onCompleted(new Decision<>(List.of(hvCon.getGameCard().getElement()), null));
			}
			else {
				listener.onCompleted(new Decision<>(List.of(skCon), null));
			}
		}


		@Override
		public void onHandToggled(HandView hand, boolean status) {
			if (hvCon != null) {
				hvCon.setSelected(false);
			}
			else if (skCon != null) {
				skCon = null;
			}
			hvCon = hand;

			//If has next action, immediately run it
			Action action = handCandidates.get(hand.getGameCard().getElement());
			Card card = hand.getGameCard().getElement();
			if (action != null) {
				hand.setSelected(false);
				end();
				ActionController.execute(gameView, (decision) -> {

					//Go back to the beginning
					if (decision == null) {
						run();
						return;
					}

					//Completed
					listener.onCompleted(new Decision<>(List.of(card), decision));
				}, handCandidates.get(card));
				return;
			}

			//Or otherwsie check it
			gameView.getInstruction().activate("確定傳出這張 " + card.getFunction().getName() + " ？", true, true);
		}


		@Override
		public void onSkillClicked(String skillName) {
			Action action = skillCandidates.get(skillName);

			if (hvCon != null) {
				hvCon.setSelected(false);
				hvCon = null;
			}
			skCon = skillName;

			//If no next action, check it
			if (action == null) {
				gameView.getInstruction().activate("發動 " + skillName + " ？", true, true);
				return;
			}

			//Or otherwise run the action immediately
			end();
			ActionController.execute(gameView, (decision) -> {

				//Go back to the beginning
				if (decision == null) {
					run();
					return;
				}

				//Completed
				listener.onCompleted(new Decision<>(List.of(skCon), decision));
			}, skillCandidates.get(skillName));
		}
	}

	private Map<Card, ? extends Action> handCandidates;
	private Map<String, ? extends Action> skillCandidates;
	private GameView gameView;
	private BasicSelectionListener listener;

	public IntelController(Map<Card, ? extends Action> handCandidates, Map<String, ? extends Action> skillCandidatesm, GameView gameView) {
		if (gameView == null || handCandidates == null || skillCandidatesm == null) {
			throw new NullPointerException();
		}
		this.handCandidates = handCandidates;
		this.skillCandidates = skillCandidatesm;
		this.gameView = gameView;
	}

	public void run() {
		gameView.getInstruction().activate("該你傳情報ㄌ，請選擇一張要傳遞的情報。", false, false);
		gameView.getHandBox().runSelection(handCandidates.keySet());
		gameView.getSkillBox().prompt(skillCandidates.keySet());

		CustomListener cl = new CustomListener();
		gameView.getHandBox().setListener(cl);
		gameView.getSkillBox().setListener(cl);
		gameView.getInstruction().setListener(cl);
	}

	public void setListener(BasicSelectionListener listener) {
		this.listener = listener;
	}
}
