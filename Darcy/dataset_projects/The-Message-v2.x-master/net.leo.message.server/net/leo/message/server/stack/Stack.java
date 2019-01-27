package net.leo.message.server.stack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import net.leo.message.base.bridge.command.animation.unsync.StackExecutedCommand;
import net.leo.message.server.conversation.MultiMessenger;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;

public class Stack {

	/**
	 * An intelligence is returned.
	 */
	public static int INTELLIGENCE_RETURNED = 1;

	/**
	 * Something is executed, so a new usage should start
	 */
	public static int ONE_MORE_USAGE = 0;

	/**
	 * Nothing is exeucted.
	 */
	public static int PASS = -1;

	private final Game game;
	private final LinkedList<Executable> list = new LinkedList<>();

	/**
	 * Constructs a stack.
	 * @param game game surroundings
	 */
	public Stack(Game game) {
		if (game == null) {
			throw new NullPointerException();
		}
		this.game = game;
	}

	/**
	 * Adds an executable item into this stack
	 * @param exe executable item to be added
	 * @return an updated info telling which executable items are disabled
	 */
	public List<Integer> add(Executable exe) {
		list.add(Objects.requireNonNull(exe));
		return disable();
	}

	/**
	 * Executes all the execttable items in this stack.
	 * @return an interger indicates an eternal effect of this execution
	 */
	public int execute() {
		if (list.isEmpty()) {
			return PASS;
		}

		//Notifies clients to lock stack boxes
		List<Player> players = game.getPlayers();
		MultiMessenger.commandAll(players, StackExecutedCommand.LOCK);

		Executable exe;
		int result = ONE_MORE_USAGE;
		while ((exe = list.pollLast()) != null) {

			//Determines if this stack unit should be executed.
			if (exe.isEffective()) {
				exe.execute(game);
			}
			else {
				exe.dispose(game);
			}

			if (exe instanceof ExecutableCard) {

				//Puts this cad into deadwood
				game.getDeadwood().add(((ExecutableCard) exe).card);

				//Notifies clients to remove this stack unit from the screen
				MultiMessenger.commandAll(players, StackExecutedCommand.EXE);

				//External effect would be intelligence returned
				if (exe instanceof Return) {
					result = INTELLIGENCE_RETURNED;
				}
			}
		}

		//Notifies clients to unlock stack boxes
		MultiMessenger.commandAll(players, StackExecutedCommand.UNLOCK);

		return result;
	}

	/**
	 * Gets all the executbla items in this stack.
	 * @return a list contains all the exectable items in this stack
	 */
	public List<Executable> getAll() {
		return new ArrayList<>(list);
	}

	/**
	 * Queries which executable items are disabled.
	 * @return an integer list indicating all the disabled items
	 */
	public List<Integer> disable() {
		//Reset conditions
		list.forEach(i -> i.setEffective(true));

		LinkedList<Executable> listCopy = new LinkedList<>(list);
		List<Integer> disabled = new ArrayList<>(list.size());
		Executable last;

		//Determines which items wold be disabled
		while ((last = listCopy.pollLast()) != null) {
			if (!last.isEffective()) {
				continue;
			}

			for (Executable rest : listCopy) {
				rest.setEffective(!(rest instanceof ExecutableCard
						&& rest.isEffective()
						&& last.invalidate((ExecutableCard) rest)));
			}
		}

		//Gernerates the integer list
		int index = 100;
		for (Executable ee : list) {
			if (ee instanceof ExecutableCard) {
				if (!ee.isEffective()) {
					disabled.add(index);
				}
				index++;
			}
		}

		return disabled;
	}

	/**
	 * Gets an executable function card by a given index from this stack.
	 * @param funcCardIndex index of function card
	 * @return an exectuable function card
	 */
	public ExecutableCard getFuncCardByIndex(int funcCardIndex) {
		funcCardIndex -= 100;
		if (funcCardIndex < 0) {
			throw new IllegalArgumentException();
		}
		return (ExecutableCard) list.stream().filter(e -> e instanceof ExecutableCard).skip(funcCardIndex).findFirst().orElseThrow(() -> new IllegalArgumentException());
	}

	/**
	 * Gets the index of an executable function card.
	 * @param func function card
	 * @return index
	 */
	public int getIndexOfFuncCard(ExecutableCard func) {
		Objects.requireNonNull(func);
		int index = 100;
		for (Executable e : list) {
			if (e == func) {
				return index;
			}
			if (e instanceof ExecutableCard) {
				index++;
			}
		}
		return -1;
	}

	/**
	 * Queires if there is not any exeutable items in this stack.
	 * @return true if this stack is empty
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}
}
