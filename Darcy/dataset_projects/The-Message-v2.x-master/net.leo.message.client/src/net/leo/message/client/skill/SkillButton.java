package net.leo.message.client.skill;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;
import net.leo.message.client.utility.HintLayer;

class SkillButton extends JPanel {

	private static final float G_TOP = 0.05f;
	private static final float G_BOTTOM = 1.00f;
	private static final int THICKNESS = 2;

	private static final Color IN_ON_TOP = new Color(0x06, 0x88, 0xfa);
	private static final Color IN_ON_BOTTOM = new Color(0x79, 0xbb, 0xff);
	private static final Color IN_OUT_TOP = new Color(0x2d, 0xab, 0xf9);
	private static final Color IN_OUT_BOTTOM = new Color(0x06, 0x88, 0xfa);

	private static final Color PS_ON_TOP = new Color(0xe4, 0x68, 0x5d);
	private static final Color PS_ON_BOTTOM = new Color(0xfc, 0x8d, 0x83);
	private static final Color PS_OUT_TOP = new Color(0xfc, 0x8d, 0x83);
	private static final Color PS_OUT_BOTTOM = new Color(0xe4, 0x68, 0x5d);

	private static final Color DISABLED_TOP = new Color(0xed, 0xed, 0xed);
	private static final Color DISABLED_BOTTOM = new Color(0xdf, 0xdf, 0xdf);

	private static final Border IN_BORDER = new LineBorder(new Color(0x33, 0x7b, 0xc4), THICKNESS);
	private static final Border PS_BORDER = new LineBorder(new Color(0xd8, 0x35, 0x26), THICKNESS);
	private static final Border DISABLED_BORDER = new LineBorder(new Color(0xdc, 0xdc, 0xdc), THICKNESS);

	private static final Color ABLED = Color.WHITE;
	private static final Color DISABLED = Color.GRAY;


	private static final int OUT = 1;
	private static final int ON = 2;
	private static final int PRESS = 3;


	private class Button extends JLabel implements ComponentListener, MouseInputListener {

		public Button(String text) {
			super(text, CENTER);
			addComponentListener(this);
			addMouseListener(this);
			addMouseMotionListener(this);
			setBorder(DISABLED_BORDER);
			setForeground(DISABLED);
		}

		@Override
		public void componentHidden(ComponentEvent e) {
		}

		@Override
		public void componentMoved(ComponentEvent e) {
		}

		@Override
		public void componentResized(ComponentEvent e) {
		}

		@Override
		public void componentShown(ComponentEvent e) {
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(200, 50);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (listener != null) {
				listener.onButtonClicked(getSkillName());
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (activated) {
				mode = ON;
				repaint();
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			hintLayer.hideHint();

			if (activated) {
				mode = OUT;
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			hintLayer.showHint(SwingUtilities.convertPoint(this, e.getPoint(), hintLayer), hint);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (activated) {
				mode = PRESS;
				super.revalidate();
				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (activated) {
				mode = ON;
				super.revalidate();
				repaint();
			}
		}

		@Override
		public void paint(Graphics g) {
			float g0, g1;
			Color top, bottom;
			if (!activated) {
				top = DISABLED_TOP;
				bottom = DISABLED_BOTTOM;
			}
			else if (mode == ON) {
				top = passive ? PS_ON_TOP : IN_ON_TOP;
				bottom = passive ? PS_ON_BOTTOM : IN_ON_BOTTOM;
			}
			else {
				top = passive ? PS_OUT_TOP : IN_OUT_TOP;
				bottom = passive ? PS_OUT_BOTTOM : IN_OUT_BOTTOM;
			}

			int height = getHeight();
			g0 = height * G_TOP;
			g1 = height * G_BOTTOM;
			GradientPaint paint = new GradientPaint(0, g0, top, 0, g1, bottom);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setPaint(paint);
			g2d.fillRect(0, 0, getWidth(), height);

			super.paint(g);
		}
	}

	private class SkillButtonLayoutManager implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {

		}

		@Override
		public void layoutContainer(Container parent) {
			int space = getSpace();
			int y = mode == PRESS ? space : 0;
			int height = getHeight() - space;
			inner.setBounds(0, y, getWidth(), height);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			Dimension d = inner.getMinimumSize();
			d.height += getSpace();
			return d;
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			Dimension d = inner.getPreferredSize();
			d.height += getSpace();
			return d;
		}

		@Override
		public void removeLayoutComponent(Component comp) {

		}
	}


	private boolean passive;
	private Image hint;
	private HintLayer hintLayer;
	private Button inner;
	private String name;
	private int mode = OUT;
	private boolean activated = false;
	SkillButtonListener listener = null;


	SkillButton(String skill, boolean passive, Image hint, HintLayer hintLayer) {
		super();
		if (hint == null || hintLayer == null) {
			throw new NullPointerException();
		}
		this.name = skill;
		this.inner = new Button(skill);
		this.passive = passive;
		this.hint = hint;
		this.hintLayer = hintLayer;
		setLayout(new SkillButtonLayoutManager());
		add(inner);
	}


	private int getSpace() {
		return getHeight() / 10;
	}

	String getSkillName() {
		return name;
	}

	void setActivated(boolean activated) {
		if (this.activated != activated) {
			this.activated = activated;
			if (activated) {
				inner.setBorder(passive ? PS_BORDER : IN_BORDER);
				inner.setForeground(ABLED);
			}
			else {
				inner.setBorder(DISABLED_BORDER);
				inner.setForeground(DISABLED);
			}
			repaint();
		}
	}

	@Override
	public String toString() {
		return getClass() + "[" + name + "]";
	}
}
