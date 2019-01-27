package net.leo.message.client.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import net.leo.message.client.domain.GameView;

/**
 * A layer that shows all dialog. All dialog components are put at center horizontally and vertically bt its preferred size.
 * @author Leo Zeng
 */
public class DialogLayer extends JComponent {

	public static final String INTELLIGENCE_VIEW = "INTELLIGENCE VIEW";

	private class DialogLayout implements LayoutManager {

		private Component ivd;

		@Override
		public void addLayoutComponent(String name, Component comp) {
			if (name.equals(INTELLIGENCE_VIEW)) {
				if (ivd != null) {
					remove(ivd);
				}
				ivd = comp;
			}
		}

		@Override
		public void layoutContainer(Container parent) {
			Component[] comps = parent.getComponents();
			Dimension size;
			int x, y;


			final Dimension mySize = parent.getSize();
			Dimension cardSize = locator.getCardSize();
			int x0 = locator.iLeft1().x;
			int x1 = locator.pRight1().x - locator.getMargin();
			int boxWidth = (x1 - x0);
			for (Component comp : comps) {
				if (comp instanceof CardDialog) {
					((CardDialog) comp).setPreferredCardSize(cardSize);
					size = comp.getPreferredSize();
					x = (mySize.width - size.width) / 2;
					y = (mySize.height - size.height) / 2;
					comp.setBounds(x, y, size.width, size.height);
				}
				else if (comp instanceof CardBoxDialog) {
					((CardBoxDialog) comp).setPreferredBoxSize(cardSize);
					size = comp.getPreferredSize();
					y = (mySize.height - size.height) / 2;
					comp.setBounds(x0, y, boxWidth, size.height);
				}
				else {
					size = comp.getPreferredSize();
					x = (mySize.width - size.width) / 2;
					y = (mySize.height - size.height) / 2;
					comp.setBounds(x, y, size.width, size.height);
				}
			}
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(0, 0);
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(1200, 900);
		}

		@Override
		public void removeLayoutComponent(Component comp) {
			if (comp == ivd) {
				ivd = null;
			}
		}
	}

	private final GameView.Locator locator;

	/**
	 * Constructs a dialog layer.
	 * @param locator locator
	 */
	public DialogLayer(GameView.Locator locator) {
		super();
		this.locator = locator;
		setLayout(new DialogLayout());
	}
}
