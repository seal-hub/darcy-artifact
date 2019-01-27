package net.leo.message.client.selection;

import java.util.Objects;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.command.action.ButtonAction;
import net.leo.message.base.bridge.command.action.CardAction;
import net.leo.message.base.bridge.command.action.FuncCardAction;
import net.leo.message.base.bridge.command.action.HandAction;
import net.leo.message.base.bridge.command.action.IntelAction;
import net.leo.message.base.bridge.command.action.OrderedCardrAction;
import net.leo.message.base.bridge.command.action.PlayerAction;
import net.leo.message.client.domain.GameView;

public abstract class ActionController {

	public static void execute(GameView gv, BasicSelectionListener listener, Action action) {
		Objects.requireNonNull(action);

		if (action instanceof PlayerAction) {
			new PlayerActionController((PlayerAction) action, gv, listener).run();
		}
		else if (action instanceof HandAction) {
			new HandActionController((HandAction) action, gv, listener).run();
		}
		else if (action instanceof FuncCardAction) {
			new FuncActionController((FuncCardAction) action, gv, listener).run();
		}
		else if (action instanceof IntelAction) {
			new IntelActionController((IntelAction) action, gv, listener).run();
		}
		else if (action instanceof ButtonAction) {
			new ButtonActionController((ButtonAction) action, gv, listener).run();
		}
		else if (action instanceof CardAction) {
			new CardActionController((CardAction) action, gv, listener).run();
		}
		else if (action instanceof OrderedCardActionController) {
			new OrderedCardActionController((OrderedCardrAction) action, gv, listener).run();
		}
		else {
			throw new IllegalArgumentException();
		}
	}

	protected BasicSelectionListener listener;
	protected GameView gameView;

	protected ActionController(GameView gameView, BasicSelectionListener listener) {
		if (gameView == null || listener == null) {
			throw new NullPointerException();
		}
		this.gameView = gameView;
		this.listener = listener;
	}

	protected abstract void run();
}
