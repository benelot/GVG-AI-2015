package projects.MCTS;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import misc.GameRunner;
import ontology.Types;
import tools.ElapsedCpuTimer;

import java.util.ArrayList;
import java.util.Random;

import tools.Vector2d;

import java.awt.Dimension;

/**
 * @Created with IntelliJ IDEA.
 * @User: ssamot
 * @Date: 14/11/13
 * @Time: 21:45
 * @detail This is a Java port from Tom Schaul's VGDL -
 *         https://github.com/schaul/py-vgdl
 */
public class Agent extends AbstractPlayer {

	public static int NUM_ACTIONS;
	public static int ROLLOUT_DEPTH = 0;
	// running and fixed MCTS_DEPTH, first increments to counter the increment
	// of the depth of the cut trees. The later stays fixed
	public static int MCTS_DEPTH_RUN;
	public static int MCTS_DEPTH_FIX = 3;
	public static int MCTS_AVOID_DEATH_DEPTH = 2;	
	public static double K = Math.sqrt(2);
	public static Types.ACTIONS[] actions;

	// an exploration reward map that is laid over the game-world to reward
	// places that haven't been visited lately
	public static double[][] addRewMap;
	public static int rewMapResolution = 20;

	// keeps track of the reward at the start of the MCTS search
	public static double startingReward;

	public int oldAction;

	/**
	 * Random generator for the agent.
	 */
	private SingleMCTSPlayer mctsPlayer;

	/**
	 * Public constructor with state observation and time due.
	 * 
	 * @param so
	 *            state observation of the current game.
	 * @param elapsedTimer
	 *            Timer for the controller creation.
	 */
	public Agent(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		// Get the actions in a static array.
		ArrayList<Types.ACTIONS> act = so.getAvailableActions();
		actions = new Types.ACTIONS[act.size()];
		for (int i = 0; i < actions.length; ++i) {
			actions[i] = act.get(i);
		}
		NUM_ACTIONS = actions.length;

		// Create the player.
		mctsPlayer = new SingleMCTSPlayer(new Random());

		// init exploration reward map
		addRewMap = new double[rewMapResolution][rewMapResolution];
		for (int i = 0; i < rewMapResolution; i++) {
			for (int j = 0; j < rewMapResolution; j++) {
				Agent.addRewMap[i][j] = 1;
			}
		}
		// fix the MCTS_DEPTH to the starting DEPTH
		MCTS_DEPTH_RUN = MCTS_DEPTH_FIX;
		oldAction = -1;
		startingReward = 0;

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
		
//		this line writes the game stats to the GameRunner if the game is over
		GameRunner.setLastStateObservation(stateObs);
//		if(stateObs.isGameOver()){
//			GameRunner.setGameStatistics((stateObs.getGameWinner() == Types.WINNER.PLAYER_WINS), stateObs.getGameScore(), stateObs.getGameTick());
//		}

		// ArrayList<Observation> obs[] =
		// stateObs.getFromAvatarSpritesPositions();
		// ArrayList<Observation> grid[][] = stateObs.getObservationGrid();

		// Heuristic: change the reward in the exploration reward map of the
		// visited current position
		Vector2d pos = stateObs.getAvatarPosition();
		Dimension dim = stateObs.getWorldDimension();
		int intposX = (int) Math.round(pos.x / dim.getWidth()
				* (rewMapResolution - 1));
		int intposY = (int) Math.round(pos.y / dim.getHeight()
				* (rewMapResolution - 1));
		// addRewMap[intposX][intposY] /= 2;
		// if(addRewMap[intposX][intposY] < 0.01){
		// addRewMap[intposX][intposY] = -0.1;}

		// increment reward at all unvisited positions and decrement at current
		// position
		for (int i = 0; i < rewMapResolution; i++) {
			for (int j = 0; j < rewMapResolution; j++) {
				if (addRewMap[i][j] < 1) {
					addRewMap[i][j] += 0.001;
				}
			}
		}
		if (intposX >= 0 && intposY >= 0 && intposX < Agent.rewMapResolution
				&& intposY < Agent.rewMapResolution)
			addRewMap[intposX][intposY] = 0;
		// System.out.println("Current Position, x: "+intposX +"  y : "+ intposY
		// + "  with val " + addRewMap[intposX][intposY] );

		// Heuristic: Punish the exploration area where enemies have been
		ArrayList<Observation>[] npcPositions = null;
		npcPositions = stateObs.getNPCPositions();
		if (npcPositions != null) {
			for (ArrayList<Observation> npcs : npcPositions) {
				if (npcs.size() > 0) {
					Vector2d npcPos = npcs.get(0).position;

					//TODO: I received an ArrayIndexOutOfBoundsException: -2 in seaquest_lvl1 game
					//Do you see why or do we have to simply cap it? Either intposEnemyX or intposEnemyY was
					//-2 and that was not in the bounds of the addRewMap.
					int intposEnemyX = (int) Math.round(npcPos.x
							/ dim.getWidth() * (rewMapResolution - 1));
					int intposEnemyY = (int) Math.round(npcPos.y
							/ dim.getHeight() * (rewMapResolution - 1));
					if (addRewMap[intposEnemyX][intposEnemyY] > 0.02) {
						addRewMap[intposEnemyX][intposEnemyY] -= 0.02;
					}
				}
			}
		}
		// Heuristic (IDEA): Perhaps increase reward towards positions that have
		// a resource. -> some sort of diffusion model with the resources as
		// positive
		// sources and the enemies as negative sources to create a reward
		// gradient.

		int useOldTree = 1;
		if (useOldTree == 1) {
			// Sets a new tree with the children[oldAction] as the root
			mctsPlayer.initWithOldTree(stateObs, oldAction);
		} else {
			// Set the state observation object as the new root of the tree.
			// (Forgets the whole tree)
			mctsPlayer.init(stateObs);
		}

		startingReward = stateObs.getGameScore();
		// Determine the action using MCTS...
		int action = mctsPlayer.run(elapsedTimer);
		if (stateObs.getGameTick() % 2 == 0) {
			// action = -2;
		}
		if (action > -2) {
			MCTS_DEPTH_RUN += useOldTree;
		}

		oldAction = action;

		//... and return it.
		if(action == -2 || action ==-1){
			return Types.ACTIONS.ACTION_NIL;
		} else {
			return actions[action];
		}
	}

}
