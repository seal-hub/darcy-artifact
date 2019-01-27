package net.leo.message.client.utility;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * A layout that display components from top to bottom. There is a space between each components and between a component and the border.
 * @author Leo Zeng
 */
public class VerticalBoxLayout implements LayoutManager {

	private int nComp = 0;
	private int max;
	private int padding;

	@Override
	public void addLayoutComponent(String name, Component comp) {
		if (nComp == max) {
			throw new IllegalStateException("Full");
		}
		nComp++;
	}

	@Override
	public void layoutContainer(Container parent) {
		int boxHeight = parent.getHeight();

		int x = padding;
		int width = parent.getWidth() - padding * 2;
		int height = (boxHeight - padding * (max + 1)) / max;
		int y = padding;

		for (Component c : parent.getComponents()) {
			c.setBounds(x, y, width, height);
			y += padding + height;
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		int height = 0;
		int width = 0;
		Dimension size;
		for (Component c : parent.getComponents()) {
			size = c.getMinimumSize();
			height += size.height;
			width = Math.max(width, size.width);
		}
		height += padding * (max + 1);
		return new Dimension(width, height);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		int height = 0;
		int width = 0;
		Dimension size;
		for (Component c : parent.getComponents()) {
			size = c.getPreferredSize();
			height += size.height;
			width = Math.max(width, size.width);
		}
		height += padding * (max + 1);
		return new Dimension(width, height);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		nComp--;
	}

	/**
	 * To get the maximum count of components.
	 */
	public int getMaxComponentCount() {
		return max;
	}

	/**
	 * To get the padding in px.
	 */
	public int getPadding() {
		return padding;
	}

	/**
	 * To set the maximum count of components.
	 * @throws IllegalArgumentException if max is lower than 1
	 */
	public void setMaxComponentCount(int max) {
		if (max < 1) {
			throw new IllegalArgumentException();
		}
		this.max = max;
	}

	/**
	 * To set the padding in px.
	 */
	public void setPadding(int padding) {
		if (padding < 0) {
			throw new IllegalArgumentException();
		}
		this.padding = padding;
	}

	/**
	 * Construct a layout manager with particular padding and the maximum count of components.
	 * @param padding the padding in px
	 * @param max     the maximum count of components
	 * @throws IllegalArgumentException if max is lower than 1
	 */
	public VerticalBoxLayout(int padding, int max) {
		setPadding(padding);
		setMaxComponentCount(max);
	}
}
