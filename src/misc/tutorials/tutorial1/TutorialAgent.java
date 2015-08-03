package misc.tutorials.tutorial1;

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
	public TutorialAgent(StateObservation gameState, ElapsedCpuTimer elapsedTimer) {
		randomGenerator = new Random();
	}

	/**
	 * Act function. Called every game step, it must return an action in 40 ms
	 * maximum.
	 */
	public ACTIONS act(StateObservation gameState, ElapsedCpuTimer elapsedTimer) {

		// Get the available actions in this game.
		ArrayList<ACTIONS> available_actions = gameState.getAvailableActions();

		// CHANGE THESE LINES.
		//   It should choose one of any of the actions available in "available_actions," not just left or right.
		//   The best solution would be to:
		//     -Find out how many available_actions there are
		//     -Generate a random number up to that size
		//     -Choose the action that corresponds to that number
		// ---------------------------
		ACTIONS action = ACTIONS.ACTION_RIGHT;
		int rand_num = randomGenerator.nextInt(2);
		if (rand_num == 0){
			action = ACTIONS.ACTION_LEFT;
		}
		// ---------------------------

		// Return the action.
		return action;
	}
}
