package net.leo.message.base.bridge.command.action;

import java.io.Serializable;
import net.leo.message.base.bridge.reply.Decision;

public interface Action extends Serializable {

	Decision<?> getDefaultDecision();

	boolean isDecisionLegal(Decision<?> decision);

	boolean isMadantory();

	Action nextAction();
}
