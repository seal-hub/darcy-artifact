package net.leo.message.client.skill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import net.leo.message.base.bridge.command.data.SkillData;
import net.leo.message.client.utility.Hint;
import net.leo.message.client.utility.HintLayer;
import net.leo.message.client.utility.VerticalBoxLayout;

public class SkillBox extends JComponent {

	private Map<String, SkillButton> map = null;
	private HintLayer hintLayer;

	private SkillBoxListener listener;


	public SkillBox(HintLayer hintLayer) {
		super();
		if (hintLayer == null) {
			throw new NullPointerException();
		}

		this.hintLayer = hintLayer;
		setLayout(new VerticalBoxLayout(8, 4));
	}

	public void init(List<SkillData> skillInfo) {
		if (map != null) {
			throw new IllegalStateException();
		}
		if (skillInfo == null) {
			throw new NullPointerException();
		}
		map = new HashMap<>(skillInfo.size());

		skillInfo.forEach(info -> {
			//Set hint
			Hint hint = new Hint();
			hint.writeSkill(info);

			//Add button
			SkillButton button = new SkillButton(info.name, info.passive, hint.createImage(), hintLayer);
			map.put(info.name, button);
			add(button);
		});
	}

	public void prompt(Set<String> skills) {
		if (map == null) {
			return;
		}

		if (skills == null) {
			map.values().forEach(button -> {
				button.setActivated(false);
				button.listener = null;
			});
			return;
		}

		map.values().forEach(button -> {
			if (skills.contains(button.getSkillName())) {
				button.setActivated(true);
				button.listener = name -> {
					if (listener != null) {
						listener.onSkillClicked(name);
					}
				};
			}
			else {
				button.setActivated(false);
				button.listener = null;
			}
		});
	}

	public void setListener(SkillBoxListener listener) {
		this.listener = listener;
	}

	@Override
	public String toString() {
		String str = getClass() + " [";
		if (map == null) {
			str += "未註冊]";
		}
		else {
			str += map.toString() + "]";
		}
		return str;
	}
}
