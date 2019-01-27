package net.leo.message.client.utility;

import java.awt.AlphaComposite;
import java.awt.Color;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import net.leo.message.base.bridge.command.data.SkillData;

public class Hint extends JTextPane {

	private static final Color BLUE_SKILL_TITLE = new Color(99, 156, 243);
	private static final int PREFERRED_HINT_WIDTH = 360;
	private static final int MARGIN = 8;
	private static final int LINE_SPACE = 4;

	private boolean firstLine = true;
	protected Style red, blue, reg, nextLine;

	private void writeBlueSkill(String skillName, String skillDescription) throws BadLocationException {
		StyledDocument doc = getStyledDocument();
		doc.insertString(doc.getLength(), skillName, blue);
		nextLine();
		doc.insertString(doc.getLength(), skillDescription, reg);
	}

	private void writeRedSkill(String skillName, String skillDescription) throws BadLocationException {
		StyledDocument doc = getStyledDocument();
		doc.insertString(doc.getLength(), skillName, red);
		nextLine();
		doc.insertString(doc.getLength(), skillDescription, reg);
	}

	protected void checkFirstLine() throws BadLocationException {
		if (firstLine) {
			firstLine = false;
		}
		else {
			nextParagraph();
		}
	}

	protected Style getRegStyle() {
		return reg;
	}

	protected void nextLine() throws BadLocationException {
		StyledDocument doc = getStyledDocument();
		doc.insertString(doc.getLength(), "\n\n", nextLine);
	}

	protected void nextParagraph() throws BadLocationException {
		StyledDocument doc = getStyledDocument();
		doc.insertString(doc.getLength(), "\n\n", reg);
	}

	protected void registerStyles() {
		StyledDocument doc = getStyledDocument();
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		nextLine = doc.addStyle("next", def);
		StyleConstants.setFontSize(nextLine, LINE_SPACE);

		reg = doc.addStyle("regular", def);
		StyleConstants.setFontSize(reg, 16);
		StyleConstants.setForeground(reg, WHITE);
		StyleConstants.setFontFamily(reg, "華康新特明體");
		StyleConstants.setAlignment(reg, StyleConstants.ALIGN_JUSTIFIED);

		red = doc.addStyle("red", reg);
		StyleConstants.setForeground(red, RED);
		StyleConstants.setBold(red, true);

		blue = doc.addStyle("blue", reg);
		StyleConstants.setForeground(blue, BLUE_SKILL_TITLE);
		StyleConstants.setBold(blue, true);
	}

	public Image createImage() {
		setSize(PREFERRED_HINT_WIDTH, Short.MAX_VALUE);
		Dimension size = getPreferredSize();
		size.width = PREFERRED_HINT_WIDTH;
		BufferedImage bi = new BufferedImage(size.width + MARGIN * 2, size.height + MARGIN * 2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) bi.getGraphics();
		Composite defaultComposite = g.getComposite();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setComposite(AlphaComposite.SrcOver.derive(0.8f));
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, size.width + MARGIN * 2, size.height + MARGIN * 2);
		g.setComposite(defaultComposite);
		g.setTransform(AffineTransform.getTranslateInstance(MARGIN, MARGIN));
		paint(g);
		g.dispose();
		return bi;
	}

	public void writeSkill(SkillData info) {
		try {
			checkFirstLine();
			if (info.passive) {
				writeBlueSkill(info.name, info.description);
			}
			else {
				writeRedSkill(info.name, info.description);
			}
		} catch (BadLocationException e) {
			throw new IllegalArgumentException(e);
		}
	}


	public Hint() {
		registerStyles();
		setOpaque(false);
		setEditable(false);
		setHighlighter(null);
	}
}

