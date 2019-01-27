package net.leo.message.client.selection;

public interface Selectable {

	int NOT_SELECTION_MODE = -1;
	int NOT_CANDIDATE = 0;
	int SELECTED = 1;
	int UNSELECTED = 2;

	static boolean isSameDisplay(int s1, int s2) {
		if (s1 == s2) {
			return true;
		}
		if ((s1 == UNSELECTED && s2 == NOT_SELECTION_MODE) || (s2 == UNSELECTED && s1 == NOT_SELECTION_MODE)) {
			return true;
		}
		return false;
	}

	boolean isSelected();

	void setSelected(boolean status);
}
