package net.leo.message.base.bridge.reply;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Decision<T> implements Serializable {

	public final Decision<?> nextDecision;
	public final List<T> contributes;

	public Decision(List<T> contributes, Decision<?> nextDecision) {
		this.contributes = new ArrayList<>(contributes);
		this.nextDecision = nextDecision;
	}

	public Decision(T contributes, Decision<?> nextDecision) {
		this.contributes = List.of(contributes);
		this.nextDecision = nextDecision;
	}

	private String description() {
		if (nextDecision != null) {
			return "Contributes: " + contributes.toString() + " -> " + nextDecision.description();
		}
		return "Contributes: " + contributes.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Decision) {
			Decision oDecision = (Decision) o;
			return Objects.equals(nextDecision, oDecision.nextDecision) && contributes.equals(oDecision.contributes);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(contributes, nextDecision);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + description() + "]";
	}
}
