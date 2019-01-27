package net.leo.message.base.lang;

import java.io.Serializable;

public enum Identity implements Serializable {

	RED_TEAM,
	BLUE_TEAM,
	PASSER_BY;

	public String getName() {
		switch (this) {
		case RED_TEAM:
			return "潛伏戰線";
		case BLUE_TEAM:
			return "軍情處";
		case PASSER_BY:
			return "打醬油的";
		}
		throw new InternalError();
	}

	public String getSimpleName() {
		switch (this) {
		case RED_TEAM:
			return "潛伏";
		case BLUE_TEAM:
			return "軍情";
		case PASSER_BY:
			return "醬油";
		}
		throw new InternalError();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + name() + "]";
	}
}
