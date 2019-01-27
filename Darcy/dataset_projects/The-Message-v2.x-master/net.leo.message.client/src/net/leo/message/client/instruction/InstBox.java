package net.leo.message.client.instruction;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.leo.message.client.utility.ButtonListener;
import net.leo.message.client.utility.Timable;
import net.leo.message.client.utility.TimeRoll;

/**
 * A box that shows instruction text and submit and cancel buttons.
 */
public class InstBox extends JComponent implements Timable {

	private class InstLayoutManager implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void layoutContainer(Container parent) {
			Insets inset = getInsets();
			int height = (getHeight() - (inset.bottom + inset.top)) / 2;
			int width = getWidth() - (inset.left + inset.right);

			iPanel.setBounds(inset.left, inset.top, width, height);
			roll.setBounds(inset.left, inset.top + height + (height - 16) / 2, width, 16);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(0, 0);
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(0, 0);
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}
	}

	private JPanel iPanel;
	private JPanel buttonPanel;
	private JButton submit;
	private JButton cancel;
	private JLabel text;
	private TimeRoll roll;

	private ButtonListener listener;

	/**
	 * Constructs a instruction box
	 */
	public InstBox() {
		super();
		setLayout(new InstLayoutManager());
		createGUI();
		setOpaque(false);
	}

	private void createGUI() {
		//Set button
		text = new JLabel();
		text.setVisible(false);
		submit = new JButton("確定");
		submit.setVisible(false);
		submit.addActionListener(e -> {
			if (listener != null) {
				listener.onButtonClicked(this, 0);
			}
		});
		cancel = new JButton("取消");
		cancel.setVisible(false);
		cancel.addActionListener(e -> {
			if (listener != null) {
				listener.onButtonClicked(this, 1);
			}
		});

		buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.add(text);
		buttonPanel.add(submit);
		buttonPanel.add(cancel);
		iPanel = new JPanel(new GridBagLayout());
		iPanel.setOpaque(false);
		iPanel.add(buttonPanel, new GridBagConstraints());

		//Set roll
		roll = new TimeRoll(false);
		roll.setSize(100, 100);

		//Add components
		add(iPanel);
		add(roll);
	}

	/**
	 * Shows instruction and let buttons be visible.
	 * @param inst          instruction text
	 * @param submitEnabled true to let button submit clickable
	 * @param cancelEnabled truee to let button cancel clickable
	 */
	public void activate(String inst, boolean submitEnabled, boolean cancelEnabled) {
		text.setText(inst);
		submit.setEnabled(submitEnabled);
		cancel.setEnabled(cancelEnabled);

		text.setVisible(true);
		submit.setVisible(true);
		cancel.setVisible(true);
	}

	public void activate(String inst, Collection<? extends String> options) {
		text.setText(inst);
		int i = 0;
		for (String bText : options) {
			final JButton button;
			if (bText.endsWith("X")) {
				bText = bText.substring(0, bText.length() - 1);
				button = new JButton(bText);
				button.setEnabled(false);
			}
			else {
				button = new JButton(bText);
			}

			final int index = i++;
			button.addActionListener(e -> {
				if (listener != null) {
					listener.onButtonClicked(button, index);
				}
			});

			buttonPanel.add(button);
		}
		text.setVisible(true);
		validate();
	}

	public JButton getCancel() {
		return cancel;
	}

	public JButton getSubmit() {
		return submit;
	}

	/**
	 * Removes instruction and buttons.
	 */
	public void inactivate() {
		text.setVisible(false);
		submit.setVisible(false);
		cancel.setVisible(false);

		Component[] comps = buttonPanel.getComponents();
		if (comps.length > 3) {
			Arrays.stream(comps).filter(c -> c != text && c != submit && c != cancel).forEach(c -> buttonPanel.remove(c));
			buttonPanel.validate();
			buttonPanel.repaint();
		}
	}

	/**
	 * Sets the instruction text.
	 * @param text instruction text
	 */
	public void setInstruction(String text) {
		if (text == null) {
			this.text.setVisible(false);
		}
		else {
			this.text.setText(text);
			this.text.setVisible(true);
		}
	}

	/**
	 * Sets the listener of this instruction box.
	 * @param listener listener
	 */
	public void setListener(ButtonListener listener) {
		this.listener = listener;
	}

	@Override
	public void update(double rate) {
		roll.update(rate);
	}
}
