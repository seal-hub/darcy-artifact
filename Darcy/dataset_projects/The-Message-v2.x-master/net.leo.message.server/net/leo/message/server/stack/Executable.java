package net.leo.message.server.stack;

import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

/**
 * An executable item that should be put into {@link Stack}.
 * @author Parabola
 */
public interface Executable {

	/**
	 * Diposes this item because this item is disabled.
	 * @param game game surroundings
	 */
	void dispose(Game game);

	/**
	 * Executes this item.
	 * @param game     game surroundings
	 * @return an integer indicating the external effect
	 */
	int execute(Game game);

	/**
	 * Queries if an another exeutable card item would be disabled because of the exectuion of this.
	 * @return true indicating that such another item would be disabled
	 */
	boolean invalidate(ExecutableCard rest);

	/**
	 * Queries if this item is effective and would be executed when the stack runs.
	 * @return true indicating that this item is effective and would be executed when the stack runs; otherwise this woudl be disposed.
	 */
	boolean isEffective();

	/**
	 * Sets whether this item is effective.
	 * @param effective true to set to effective
	 */
	void setEffective(boolean effective);
}
