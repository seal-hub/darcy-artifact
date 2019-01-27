package net.leo.message.client.utility;

import static java.awt.Color.GREEN;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import net.leo.message.base.bridge.command.data.CharData;
import net.leo.message.base.lang.Sex;
import net.leo.message.client.element.CharCard;

public class CharHint extends Hint {

	/**
	 * Gets the hint by given info
	 * @param info character info, or null indicating a covered character
	 * @return charater hint, or null indicating no hint
	 */
	public static CharHint toCharHint(CharData info) {
		if (info == null) {
			return null;
		}

		//Create hint
		CharHint hint;
		CharCard character = CharCard.getInstance(info.character);
		if (info.character.getSex() == Sex.FEMALE) {
			hint = new CharHint(character, true);
		}
		else if (info.character.getSex() == Sex.MALE) {
			hint = new CharHint(character, false);
		}
		else {
			hint = new CharHint(character);
		}

		//Write skills into hint
		info.skillData.forEach(s -> hint.writeSkill(s));

		//Write mission into hint (香水 has no mission)
		if (info.mission != null) {
			hint.writeMission(info.mission);
		}

		return hint;
	}

	private final CharCard character;
	private Style name, miss;

	public CharHint(CharCard character, boolean sex) {
		this(character, sex ? "女" : "男");
	}

	public CharHint(CharCard character) {
		this(character, null);
	}


	private CharHint(CharCard character, String sex) {
		super();
		if (character == null) {
			throw new NullPointerException();
		}
		this.character = character;
		writeCharacter(sex);
	}

	@Override
	protected void registerStyles() {
		super.registerStyles();
		StyledDocument doc = getStyledDocument();

		name = doc.addStyle("name", getRegStyle());
		StyleConstants.setFontSize(name, 22);
		StyleConstants.setBold(name, true);

		miss = doc.addStyle("miss", reg);
		StyleConstants.setForeground(miss, GREEN);
		StyleConstants.setBold(miss, true);
	}

	protected void writeCharacter(String sex) {
		StyledDocument doc = getStyledDocument();
		try {
			checkFirstLine();
			doc.insertString(doc.getLength(), character.getName(), name);
			if (sex != null) {
				doc.insertString(doc.getLength(), "（" + sex + "）", reg);
			}
		}
		catch (BadLocationException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public CharCard getCharacter() {
		return character;
	}

	public void writeMission(String description) {
		StyledDocument doc = getStyledDocument();
		try {
			checkFirstLine();
			doc.insertString(doc.getLength(), "機密任務", miss);
			nextLine();
			doc.insertString(doc.getLength(), description, reg);
		}
		catch (BadLocationException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
