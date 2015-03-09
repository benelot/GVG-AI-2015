package projects.project1;

import java.util.ArrayList;
import java.util.Random;

import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;
import core.game.StateObservation;
import core.player.AbstractPlayer;

public class Agent extends AbstractPlayer {

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
	public Agent(StateObservation gameState, ElapsedCpuTimer elapsedTimer) {
		randomGenerator = new Random();
	}

	/**
	 * Act function. Called every game step, it must return an action in 40 ms
	 * maximum.
	 */
	public ACTIONS act(StateObservation gameState, ElapsedCpuTimer elapsedTimer) {

		// Get the available actions in this game.
		ArrayList<ACTIONS> actions = gameState.getAvailableActions();

		// Determine an index randomly and get the action to return.
		int index = randomGenerator.nextInt(actions.size());
		ACTIONS action = actions.get(index);

		// Return the action.
		return action;
	}
}
