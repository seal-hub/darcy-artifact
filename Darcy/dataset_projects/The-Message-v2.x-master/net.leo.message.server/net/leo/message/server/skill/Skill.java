package net.leo.message.server.skill;

import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.command.animation.sync.SkillInvokedCommand;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.server.conversation.MultiMessenger;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.UsageStartEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;
import net.leo.message.server.stack.ExecutableCard;

/**
 * An interface rerpresenting all the skills, including "mark", "coversion", "initiative" and "passive" skills.<p>
 * Running order: {@link #inform(Game, Player, GameEvent)} -> {@link #isSurroundingIdeal(Game, Player, GameEvent)} -> {@link #prepare(Game, Player, GameEvent)} -> (skill
 * invocation animation) -> {@link #invoke(Game, Player, Decision, GameEvent)}.
 * @author Parabola
 * @see InsertedSkill
 * @see ExecutableSkill
 * @see MarkSkill
 * @see ConversionSkill
 */
public interface Skill {

	/**
	 * A character card opening skill.
	 */
	int OPENING = 1;
	/**
	 * A character card covering skill.
	 */
	int COVERING = 2;
	/**
	 * An ordinary skill.
	 */
	int ORDINARY = 0;

	/**
	 * Gets the description (instruction) of this skill.
	 * @return description
	 */
	String description();

	/**
	 * Gets the name of this skill.
	 * @return name of skill
	 */
	String getName();

	/**
	 * Gets the target player or target card of this skill.
	 * @return target of this skill; null indicating no target
	 */
	Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e);

	/**
	 * Queries if this skill is invokable when an event happens. Whether the character card is opened is <strong>NOT</strong> put into consideration.
	 * @param game the game where event happened
	 * @param me   the player who owns this skill
	 * @param e    a game event
	 * @return a number indicating the launching count
	 */
	int inform(Game game, Player me, GameEvent e);

	/**
	 * Invokes this skill. This method is called after the skill invocation animation is shown. Character-card-flipped animation is <strong>DOES</strong> be put into
	 * consideration.
	 * @param game     the game where this skill is about to launch
	 * @param me       the player who owns this skill
	 * @param decision a decision made by the client, or null if no decsion is required
	 */
	default void invoke(Game game, Player me, Decision<?> decision, GameEvent e) {
		int mode = mode();
		if (mode == OPENING) {
			game.operate().charOpened(me);
		}

		//Determines integer code of target
		Target target = getTarget(game, me, decision, new UsageStartEvent(game.getStage()));
		int iTar;
		if (target instanceof Player) {
			iTar = ((Player) target).getSeat();
		}
		else if (target instanceof ExecutableCard) {
			iTar = game.getStack().getIndexOfFuncCard((ExecutableCard) target);
		}
		else {
			iTar = -1;
		}

		//Sends invocation command
		Command invoCmd = new SkillInvokedCommand(me.getCharacter().character, getName(), me.getSeat(), iTar);
		MultiMessenger.commandAll(game.getPlayers(), invoCmd);

		if (mode == COVERING) {
			game.operate().charCovered(me);
		}
		run(game, me, decision, e);
	}

	/**
	 * Queries if this skill is invokable when an event happens. Whether the character card is opened <strong>DOES</strong> be put into consideration. Subclasses of this
	 * interface should not	overrite this method; instead overrite {@link #isSurroundingIdeal(Game, Player, GameEvent)}.
	 * @param game the game where event happened
	 * @param me   the player who owns this skill
	 * @return a number indicating the launching count
	 */
	default boolean isInvokable(Game game, Player me, GameEvent e) {
		int mode = mode();
		if (mode == OPENING) {
			return !me.getCharacter().isPublic() && isSurroundingIdeal(game, me, e);
		}
		return me.getCharacter().isPublic() && isSurroundingIdeal(game, me, e);
	}

	/**
	 * Queries if this skill is madantory.
	 * @return true if this skill is mandantory
	 */
	boolean isMadantory();

	/**
	 * Queries if this skill is a "Red Skill".
	 * @return true if this skill is a "Red Skill"; otherwise a "Blue Skill"
	 */
	boolean isRed();

	/**
	 * Queries if the surroundings is idael so that this skill can be invoked. Whether the character card is opened is <strong>NOT</strong> put into consideration. Subclasses of
	 * this interface should overwrits this method but not {@link #isInvokable(Game, Player, GameEvent)}.
	 * @param game the game surroundings
	 * @param me   the player who owns this skill
	 * @return true if this skill can be invoked
	 */
	boolean isSurroundingIdeal(Game game, Player me, GameEvent e);

	/**
	 * Queries if this skill is a character-card-opening/covering skill or simply an ordinary one.
	 * @return an integer indicating the mode of this skill
	 */
	int mode();

	/**
	 * Gets the correspoding action and lets the player prepare to invoke this skill. This method is called just before the skill invocation animation is played. If the client
	 * replies a valid decision, the server would soon show the animation and call {@link #run(Game, Player, Decision, GameEvent)}.
	 * @param game the game where this skill exists
	 * @param me   the player who owns this skill
	 * @return an action indicating how to operate this skill, or null if no action is involved
	 */
	Action prepare(Game game, Player me, GameEvent e);

	/**
	 * Invokes this skill. This method is called after the skill invocation animation is shown. Character-card-flipped animation is <strong>NOT</strong> put into consideration.
	 * @param game     the game where this skill is about to launch
	 * @param me       the player who owns this skill
	 * @param decision a decision made by the client, or null if no decsion is required
	 */
	void run(Game game, Player me, Decision<?> decision, GameEvent e);
}
