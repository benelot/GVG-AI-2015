package bladeRunner;

import java.util.ArrayList;
import java.util.Random;

import agents.gameClassifier.GameClassifier;
import agents.gameClassifier.GameClassifier.GameType;
import agents.hbfs.HBFSAgent;
import agents.mcts.MCTSAgent;
import agents.persistentStorage.ITypeAttractivity;
import agents.persistentStorage.PersistentStorage;
import agents.persistentStorage.RewardMap;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

public class Agent extends AbstractPlayer {

	public enum AgentType {
		MCTS, BFS, MIXED
	}

	public static boolean isVerbose = true;
	public AgentType forcedAgentType = AgentType.MIXED;

	/**
	 * Random generator for the agent.
	 */
	private MCTSAgent mctsAgent;
	private HBFSAgent bfAgent;

	/**
	 * Public constructor with state observation and time due.
	 * 
	 * @param so
	 *            state observation of the current game.
	 * @param elapsedTimer
	 *            Timer for the controller creation.
	 */
	public Agent(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		
		//#################
		//PERSISTENT STORAGE
		// Get the actions in a static array.
		ArrayList<Types.ACTIONS> act = so.getAvailableActions();
		PersistentStorage.actions = new Types.ACTIONS[act.size()];
		for (int i = 0; i < PersistentStorage.actions.length; ++i) {
			PersistentStorage.actions[i] = act.get(i);
		}

		// initialize exploration reward map with 1
		PersistentStorage.rewMap = new RewardMap(so, 1);
		
		// initialize ItypeAttracivity object for starting situation
		PersistentStorage.iTypeAttractivity = new ITypeAttractivity(so);
		
		//Classify game
		GameClassifier.determineGameType(so);

		// use time that is left to build a tree or do BFS
		if ((GameClassifier.getGameType() == GameType.STOCHASTIC || forcedAgentType == AgentType.MCTS)
				&& forcedAgentType != AgentType.BFS) {
			// Create the player.
			mctsAgent = new MCTSAgent(new Random());
			mctsAgent.init(so);
			mctsAgent.run(elapsedTimer);
		} else {
			bfAgent = new HBFSAgent(so, elapsedTimer);
		}

	}

	/**
	 * Picks an action. This function is called every game step to request an
	 * action from the player.
	 * 
	 * @param stateObs
	 *            Observation of the current state.
	 * @param elapsedTimer
	 *            Timer when the action returned is due.
	 * @return An action for the current state
	 */
	public Types.ACTIONS act(StateObservation stateObs,
			ElapsedCpuTimer elapsedTimer) {
		Types.ACTIONS action = Types.ACTIONS.ACTION_NIL;
		if (forcedAgentType == AgentType.BFS
				|| (forcedAgentType == AgentType.MIXED && !(GameClassifier.getGameType() == GameType.STOCHASTIC))) {
			action = bfAgent.act(stateObs, elapsedTimer);
		} else {
			action = mctsAgent.act(stateObs, elapsedTimer);
		} // end if isstochastic
		return action;
	}
}
