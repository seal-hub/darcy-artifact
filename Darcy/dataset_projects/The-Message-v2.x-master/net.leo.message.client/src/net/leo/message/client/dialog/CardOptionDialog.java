package net.leo.message.client.dialog;

@FunctionalInterface
public interface CardOptionDialog extends Dialog {

	void setListener(CardSelectionListener listener);
}
