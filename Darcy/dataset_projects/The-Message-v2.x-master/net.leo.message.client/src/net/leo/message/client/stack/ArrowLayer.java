package net.leo.message.client.stack;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import static net.leo.message.client.stack.ArrowLayer.Arrow.COLOR_ARROW;
import static net.leo.message.client.stack.ArrowLayer.Arrow.STROKE;
import net.leo.message.client.utility.ArrowTarget;
import static net.leo.message.client.utility.CalculationUtility.round;

@SuppressWarnings("serial")
public class ArrowLayer extends JComponent {

	public static class Arrow {

		public static final float ARROW_THICKNESS = 2f;
		public static final double ARROW_LENGTH = 25d, ARROW_ANGLE = PI / 5, ARROW_SPACE = 20d;
		public static final Color COLOR_ARROW = Color.YELLOW;
		public static final Stroke STROKE = new BasicStroke(ARROW_THICKNESS, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);

		public static Shape getArrow(Point2D.Double p, double length, double angle, double dir) {
			double half = angle / 2;
			double angle1 = dir + PI - half;
			double angle2 = dir + PI + half;
			GeneralPath shape = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
			shape.moveTo(p.x + cos(angle1) * length, p.y + sin(angle1) * length);
			shape.lineTo(p.x, p.y);
			shape.lineTo(p.x + cos(angle2) * length, p.y + sin(angle2) * length);
			return shape;
		}

		public static List<Shape> calculateShapes(Point from, Point to) {
			if (from == null || to == null) {
				throw new NullPointerException();
			}
			final double distance = from.distance(to);
			final int nArrow = round(distance / ARROW_SPACE);
			final double dx = (double) (to.x - from.x) / nArrow;
			final double dy = (double) (to.y - from.y) / nArrow;
			final double dir = to.x >= from.x ? atan((double) (to.y - from.y) / (to.x - from.x)) : atan((double) (to.y - from.y) / (to.x - from.x)) + PI;

			double x = from.x, y = from.y;
			Shape shape;
			List<Shape> list = new ArrayList<>(nArrow);
			for (int i = 0 ; i < nArrow ; i++) {
				x += dx;
				y += dy;
				shape = getArrow(new Point2D.Double(x, y), ARROW_LENGTH, ARROW_ANGLE, dir);
				list.add(shape);
			}
			return list;
		}

		private Arrow() { // No constructor
		}
	}

	private final List<Shape> list = new LinkedList<>();

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
		g2d.setStroke(STROKE);
		for (Shape s : list) {
			g2d.draw(s);
		}
	}

	public void clear() {
		if (!list.isEmpty()) {
			List<Shape> copy = new ArrayList<>(list);
			list.clear();
			for (Shape s : copy) {
				repaint(STROKE.createStrokedShape(s).getBounds());
			}
		}
	}

	public void show(ArrowTarget org, ArrowTarget dest) {
		if (org == null || dest == null) {
			throw new NullPointerException();
		}
		clear();
		Point orgPoint = SwingUtilities.convertPoint((Component) org, org.getTargetCenterPoint(), this);
		Point destPoint = SwingUtilities.convertPoint((Component) dest, dest.getTargetCenterPoint(), this);
		list.addAll(Arrow.calculateShapes(orgPoint, destPoint));
		for (Shape s : list) {
			repaint(STROKE.createStrokedShape(s).getBounds());
		}
	}

	ArrowLayer() {
		super();
		setForeground(COLOR_ARROW);
	}
}
