package net.leo.message.client.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.leo.message.base.lang.Card;
import net.leo.message.client.element.GameCard;
import net.leo.message.client.utility.BasicCardView;
import net.leo.message.client.utility.DisplayBox;

public class IntelViewDialog extends JPanel implements CardBoxDialog, Dialog {

	private static final int MARGIN = 6;
	private DisplayBox<BasicCardView> box = new DisplayBox<>();
	private JButton close = new JButton("關閉");

	public IntelViewDialog(List<Card> cards, String title) {
		super(new BorderLayout());
		if (title == null) {
			throw new NullPointerException();
		}
		box.offerAll(cards.stream().map(c -> new BasicCardView(GameCard.getInstance(c))).collect(toList()));
		createGUI(title);
	}

	private void createGUI(String title) {
		JPanel center = new JPanel(new BorderLayout());
		center.add(box);
		center.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN),
		                                                    BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title),
		                                                                                       BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN))));

		close.addActionListener(e -> {
			Container parent = this.getParent();
			if (parent != null) {
				Rectangle bounds = getBounds();
				parent.remove(this);
				parent.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
			}
		});
		JPanel south = new JPanel();
		south.add(close);

		add(center, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);
	}

	@Override
	public void setPreferredBoxSize(Dimension size) {
		box.setPreferredSize(size);
	}
}
