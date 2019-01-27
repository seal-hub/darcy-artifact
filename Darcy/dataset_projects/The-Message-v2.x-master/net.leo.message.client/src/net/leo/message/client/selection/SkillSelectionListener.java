package net.leo.message.client.selection;

import net.leo.message.base.bridge.reply.Decision;

public interface SkillSelectionListener {

	void onCompleted(String skill, Decision<?> decision);
}
