package tutorials.tutorial1solution;

import java.util.ArrayList;
import java.util.Random;

import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;
import core.game.StateObservation;
import core.player.AbstractPlayer;

public class TutorialAgent extends AbstractPlayer {

	/**
	 * A random number generator.
	 */
	protected Random randomGenerator;

	/**
	 * Constructor. Your "Setup function". Will be run once to initialize your
	 * controller resources. It must return in 1 second maximum.
	 * 
	 * @param gameState
	 *            The state of the game.
	 * @param elapsedTimer
	 *            The time that has elapsed already.
	 */
	public TutorialAgent(StateObservation gameState,
			ElapsedCpuTimer elapsedTimer) {
		randomGenerator = new Random();
	}

	/**
	 * Act function. Called every game step, it must return an action in 40 ms
	 * maximum.
	 */
	public ACTIONS act(StateObservation gameState, ElapsedCpuTimer elapsedTimer) {
		ArrayList<ACTIONS> available_actions = gameState.getAvailableActions();
		ACTIONS action = available_actions.get(randomGenerator
				.nextInt(available_actions.size()));
		// ### Another solution would be to get the randomGenerator to produce
		// ### integers in the range of available actions:
		//
		// int actionIndex = randomGenerator.nextInt(available_actions.size());
		//
		// ### And then create ifs and else ifs or a switch statement to look
		// for
		// ### each number and set the appropriate action to the action
		// variable.
		// ### Something like:
		// if(actionIndex == 0)
		// {
		// action = available_actions.get(0);
		// }
		// if(actionIndex == 1)
		// {
		// action = available_action.get(1);
		// }
		// ### and so on until you covered all the actions in the correct
		// ### QUESTION: Why does it not work to write the number to action
		// ### mapping explicitly?
		// ### Answer: The index you generate does not always correspond to
		// ### the same action, therefore the number to action mapping would be
		// ### wrong. In the available actions array list are the currently
		// ### applicable actions listed. It might be that you currently can not
		// ### go left because something is blocking you out or the game does
		// ### not offer game items, so you never need ACTIONS.ACTION_USE.
		// ### Therefore you must choose an action from the available_actions
		// ### array list.
		// ### Quick lesson: An ArrayList is a data structure that is like an
		// ### array (you can use indices to get array items) and like a list
		// ### (you can use add(item)/remove(item) to add/remove the item
		// ### to/from the list.
		return action;
	}
}
