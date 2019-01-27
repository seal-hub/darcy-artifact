package net.leo.message.server.character;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.command.animation.unsync.TimeSetCommand;
import net.leo.message.base.bridge.command.data.CharData;
import net.leo.message.base.bridge.command.data.SkillData;
import net.leo.message.base.bridge.command.select.CharSelectionCommand;
import net.leo.message.base.bridge.command.select.PsSkillSelctionCommand;
import net.leo.message.base.bridge.command.select.Selection;
import net.leo.message.base.bridge.reply.BasicReply;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.bridge.reply.NullReply;
import net.leo.message.base.bridge.reply.Reply;
import net.leo.message.base.lang.Character;
import net.leo.message.base.lang.Sex;
import net.leo.message.server.conversation.Examiner;
import net.leo.message.server.conversation.MultiMessenger;
import net.leo.message.server.conversation.SingleMessenger;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.skill.Skill;

/**
 * A character card.
 * @author Parabola
 */
public abstract class CharacterCard {

	/**
	 * Gets an character card instance by a given basic character info
	 * @param character basic character info
	 * @return a character card instance
	 */
	public static CharacterCard getInstance(Character character) {
		switch (character) {
		case C01:
			return new C01();
		case C02:
			return new C02();
		case C03:
			return new C03();
		case C04:
			return new C04();
		case C08:
			return new C08();
		case C09:
			return new C09();
		case C10:
			return new C10();
		case C11:
			return new C11();
		case C14:
			return new C14();
		case C15:
			return new C15();
		case C16:
			return new C16();
		case C17:
			return new C17();
		case C19:
			return new C19();
		case C20:
			return new C20();
		case C22:
			return new C22();
		case C23:
			return new C23();
		case C24:
			return new C24();
		case C25:
			return new C25();
		default:
			return new CTest(); //TODO
		}
	}

	/**
	 * True indicating that this character card is opened and all players know it.
	 */
	protected boolean publicity;
	/**
	 * The basic character info.
	 */
	public final Character character;

	/**
	 * Consturcts a character card
	 * @param character basic character info
	 * @param initOpen  true indicating that this character card should be public to all players when a game starts
	 */
	protected CharacterCard(Character character, boolean initOpen) {
		this.character = character;
		this.publicity = initOpen;
	}

	/**
	 * Invokes <strong>one</strong> skill
	 * @param candidates candidates of skills
	 * @param game       game surroundings
	 * @param invoker    the player who owns this character card
	 * @param e          game event
	 * @return a skill which is invoked, or null if none is invoked
	 */
	private Skill invoke(Map<Skill, Integer> candidates, Game game, Player invoker, GameEvent e) {
		//Gets skills that are invokable
		Set<Skill> invokable = candidates.keySet()
		                                 .stream()
		                                 .filter(s -> s.isInvokable(game, invoker, e))
		                                 .collect(toSet());
		if (invokable.isEmpty()) {
			return null;
		}

		//Sends command
		int msgId = Selection.randomMessageId();
		int time = invokable.size() * 3000 + 9000;
		Map<String, Action> actions = new HashMap<>(invokable.size());
		invokable.forEach(s -> actions.put(s.getName(), s.prepare(game, invoker, e)));
		Skill dfInvo = invokable.stream()
		                        .filter(s -> s.isMadantory())
		                        .findFirst()
		                        .orElse(null);
		Reply dfReply = dfInvo == null ? new NullReply(msgId) : new BasicReply<>(msgId, dfInvo);
		PsSkillSelctionCommand cmd = new PsSkillSelctionCommand(time, actions, dfInvo == null ? null : dfInvo.getName(), msgId);
		Command cmdEx = new TimeSetCommand(time, "請等待玩家 " + invoker.getSeat() + " 操作。", invoker.getSeat());
		MultiMessenger.commandOneAndOthers(game.getPlayers(), invoker, cmd, cmdEx);

		//Listens to reply
		SingleMessenger msg = new SingleMessenger(msgId, invoker, time, reply -> Examiner.checkSelection(reply, cmd));
		Reply reply = msg.reply(dfReply);
		Command timeCmd = new TimeSetCommand(-1, null, invoker.getSeat());
		MultiMessenger.commandAll(game.getPlayers(), timeCmd);

		//Client gave up
		if (reply instanceof NullReply) {
			return null;
		}

		//Invokes skill
		Decision<?> val = ((BasicReply<Decision<?>>) reply).value;
		String skillName = (String) val.contributes.get(0);
		Skill invoked = candidates.keySet()
		                          .stream()
		                          .filter(s -> s.getName().equals(skillName))
		                          .findFirst()
		                          .get();
		invoked.invoke(game, invoker, val.nextDecision, e);

		return invoked;
	}

	/**
	 * Gets a character data of this character card.
	 * @return character data
	 * @see CharData
	 * @see CharSelectionCommand
	 */
	public final CharData getCharacterData() {
		CharData cd = new CharData(character);
		cd.setMission(missionDescription());
		getSkills().forEach(s -> {
			cd.addSkillInfo(new SkillData(s.getName(), s.isRed(), s.description()));
		});
		return cd;
	}

	/**
	 * Gets the character name of this card
	 * @return name of character
	 */
	public final String getName() {
		return character.getName();
	}

	/**
	 * Gets the sex of character regardless that this card is covoered or not.
	 * @return sex of character
	 * @see Sex
	 */
	public final Sex getSex() {
		return character.getSex();
	}

	/**
	 * Gets all ths skills of this character.
	 * @return skills of character
	 */
	public abstract List<Skill> getSkills();

	/**
	 * Informs an game event to this character and invokes the following-up actions.
	 * @param game  game surrondings
	 * @param me    player who owns this character card
	 * @param event game event
	 */
	public final void inform(Game game, Player me, GameEvent event) {
		//Gets the count of invocation of each skill, in order of action
		List<Skill> skills = getSkills();
		Map<Skill, Integer> invocations = new HashMap<>(skills.size());
		skills.forEach(s -> {
			int nInvo = s.inform(game, me, event);
			if (nInvo != 0) {
				invocations.put(s, nInvo);
			}
		});

		//Invokes skills
		Skill invoked;
		while (!invocations.isEmpty() && (invoked = invoke(invocations, game, me, event)) != null) {
			int i = invocations.get(invoked) - 1;
			if (i == 0) {
				invocations.remove(invoked);
			}
			else {
				invocations.put(invoked, i);
			}
		}
	}

	/**
	 * Queries if this character card is opened.
	 * @return true if this character card is opened
	 */
	public final boolean isPublic() {
		return publicity;
	}

	/**
	 * Gets the description of mission of this character.
	 * @return description of mission
	 */
	public abstract String missionDescription();

	/**
	 * Sets the publicity of this character card.
	 * @param publicity true indicating that this character card should be displayed to all the other players
	 */
	public final void setPublicity(boolean publicity) {
		this.publicity = publicity;
	}
}
