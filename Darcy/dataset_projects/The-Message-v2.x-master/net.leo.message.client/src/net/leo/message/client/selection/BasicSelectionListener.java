package net.leo.message.client.selection;

import net.leo.message.base.bridge.reply.Decision;

@FunctionalInterface
public interface BasicSelectionListener {

	void onCompleted(Decision<?> decision);
}
