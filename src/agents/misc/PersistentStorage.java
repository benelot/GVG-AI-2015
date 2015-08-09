package agents.misc;

import ontology.Types.ACTIONS;

public class PersistentStorage {

	// HashMap of iTypeAttractivity for start situation
	// TODO: Maybe create List of AttractivityMaps for different game situations
	// (e.g. avatar has found sword/has eaten mushroom/has a lot of honey)
	public static ITypeAttractivity iTypeAttractivity;
	// an exploration reward map that is laid over the game-world to reward
	// places that haven't been visited lately
	public static RewardMap rewMap;
	// keeps track of the reward at the start of the MCTS search
	public static double startingReward = 0;
	public static double numberOfBlockedMovables = 0;
	public static ACTIONS[] actions;
	public static double K = Math.sqrt(2);
	public static int MCTS_AVOID_DEATH_DEPTH = 2;
	// fix the MCTS_DEPTH to the starting DEPTH
	public static int MCTS_DEPTH_RUN = PersistentStorage.MCTS_DEPTH_FIX;
	// running and fixed MCTS_DEPTH, first increments to counter the increment
	// of the depth of the cut trees. The later stays fixed
	public static int MCTS_DEPTH_FIX = 3;
	// ## Parameters
	public static int ROLLOUT_DEPTH = 0;

}
