package net.leo.message.client.selection;

import java.util.List;
import java.util.Map;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.client.domain.GameListener;
import net.leo.message.client.domain.GameView;
import net.leo.message.client.hand.HandView;
import net.leo.message.client.player.PlayerView;
import net.leo.message.client.stack.FuncCardView;

public class PsActionController {

	private class MutilSkillListener implements GameListener {

		String skCon;

		private void invokeNext() {
			Action action = skillCandidates.get(skCon);

			if (action == null) {
				listener.onCompleted(skCon, new Decision<>(List.of(skCon), null));
				return;
			}

			ActionController.execute(gameView, (decision) -> {
				if (decision == null) {
					listener.onCompleted(skCon, null);
					return;
				}

				listener.onCompleted(skCon, new Decision<>(List.of(skCon), decision));
			}, action);
		}

		@Override
		public void onCompleted(GameView source, boolean completed) {
			if (!completed) {
				listener.onCompleted(skCon, null);
				return;
			}
			invokeNext();
		}

		@Override
		public void onFuncToggled(GameView source, FuncCardView func, int index, boolean status) {
		}

		@Override
		public void onHandToggled(GameView source, HandView hand, boolean status) {
		}

		@Override
		public void onPlayerToggled(GameView source, PlayerView player, boolean status) {
		}

		@Override
		public void onSkillSelected(GameView source, String skillName) {
			skCon = skillName;
			gameView.getInstruction().activate("發動 " + skillName + " ？", true, true);
		}
	}

	private class SingleSkillListener implements GameListener {

		private String candidate;
		private Action action;

		public SingleSkillListener(String candidate, Action action) {
			this.candidate = candidate;
			this.action = action;
		}

		private void invokeNext() {
			if (action == null) {
				listener.onCompleted(candidate, new Decision<>(List.of(candidate), null));
				return;
			}

			ActionController.execute(gameView, (decision) -> {
				if (decision == null) {
					listener.onCompleted(candidate, null);
					return;
				}

				listener.onCompleted(candidate, new Decision<>(List.of(candidate), decision));
			}, action);
		}

		@Override
		public void onCompleted(GameView source, boolean completed) {
			if (!completed) {
				listener.onCompleted(candidate, null);
				return;
			}
			invokeNext();
		}

		@Override
		public void onFuncToggled(GameView source, FuncCardView func, int index, boolean status) {

		}

		@Override
		public void onHandToggled(GameView source, HandView hand, boolean status) {

		}

		@Override
		public void onPlayerToggled(GameView source, PlayerView player, boolean status) {

		}

		@Override
		public void onSkillSelected(GameView source, String skillName) {
			invokeNext();
		}
	}

	private Map<String, ? extends Action> skillCandidates;
	private GameView gameView;
	private SkillSelectionListener listener;
	private boolean enforcement;

	public PsActionController(Map<String, ? extends Action> skillCandidatesm, boolean enforcement, GameView gameView) {
		if (gameView == null || skillCandidatesm == null) {
			throw new NullPointerException();
		}
		this.skillCandidates = skillCandidatesm;
		this.gameView = gameView;
		this.enforcement = enforcement;
	}

	public void run() {
		gameView.getSkillBox().prompt(skillCandidates.keySet());
		if (skillCandidates.size() == 1) {
			String name = skillCandidates.keySet().iterator().next();
			gameView.getInstruction().activate("是否發動 " + name + " ？", true, !enforcement);
			gameView.setGameListener(new SingleSkillListener(name, skillCandidates.get(name)));
		}
		else {
			gameView.getInstruction().activate("選擇要發動的技能。", false, !enforcement);
			gameView.setGameListener(new MutilSkillListener());
		}
	}

	public void setListener(SkillSelectionListener listener) {
		this.listener = listener;
	}
}
