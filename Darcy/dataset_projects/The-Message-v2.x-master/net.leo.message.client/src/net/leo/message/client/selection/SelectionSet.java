package net.leo.message.client.selection;

import java.util.Set;

public interface SelectionSet<T> {

	void runSelection(Set<T> candidates);

	void stopSelection();
}
