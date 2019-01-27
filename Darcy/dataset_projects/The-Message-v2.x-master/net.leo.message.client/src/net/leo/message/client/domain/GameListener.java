package net.leo.message.client.domain;

import net.leo.message.client.hand.HandView;
import net.leo.message.client.player.PlayerView;
import net.leo.message.client.stack.FuncCardView;

public interface GameListener {

	void onCompleted(GameView source, boolean completed);

	void onFuncToggled(GameView source, FuncCardView func, int index, boolean status);

	void onHandToggled(GameView source, HandView hand, boolean status);

	void onPlayerToggled(GameView source, PlayerView player, boolean status);

	void onSkillSelected(GameView source, String skillName);
}
