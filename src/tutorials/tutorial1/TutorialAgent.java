package tutorials.tutorial1;

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
		ArrayList<ACTIONS> available_actions = gameState.getAvailableActions();
		ACTIONS action = available_actions.get(randomGenerator.nextInt(available_actions.size()));
		return action;
	}
}
