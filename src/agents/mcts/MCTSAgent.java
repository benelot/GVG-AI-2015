package agents.mcts;

import agents.GameAgent;
import agents.misc.PersistentStorage;
import core.game.Observation;
import core.game.StateObservation;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

import java.util.ArrayList;
import java.util.Random;

import ontology.Types;
import ontology.Types.ACTIONS;

public class MCTSAgent extends GameAgent {
	/**
	 * Root of the tree.
	 */
	public MCTSNode m_root;

	/**
	 * Random generator.
	 */
	public Random m_rnd;
	
	public static Vector2d startingPos;

	public int nodeQty;
	

	// additional actions names
	public final int ADD_NEW_ROOT_NODE = -1;
	public final int KEEP_COMPLETE_OLD_TREE = -2;

	public int oldAction = KEEP_COMPLETE_OLD_TREE;

	/**
	 * Creates the MCTS player with a sampleRandom generator object.
	 * 
	 * @param a_rnd
	 *            sampleRandom generator object.
	 */
	public MCTSAgent(StateObservation so, ElapsedCpuTimer elapsedTimer,
			Random a_rnd) {
		m_rnd = a_rnd;
		m_root = new MCTSNode(a_rnd);
		init(so);
		run(elapsedTimer);

		nodeQty = 0;
	}

	/**
	 * Inits the tree with the new observation state in the root.
	 * 
	 * @param a_gameState
	 *            current state of the game.
	 */
	public void initNew(StateObservation a_gameState) {
		// Set the game observation to a newly root node.
		m_root = new MCTSNode(m_rnd);
		m_root.state = a_gameState;
		startingPos = a_gameState.getAvatarPosition();

	}

	public void init(StateObservation a_gameState) {
		// Set the game observation to a newly root node.
		m_root = new MCTSNode(m_rnd);
		m_root.state = a_gameState;
		startingPos = a_gameState.getAvatarPosition();

	}

	public void initWithOldTree(StateObservation a_gameState, int action) {
		/*
		 * Here we create a new root-tree for the next search query based on the
		 * old tree. The old tree gets cut at the chosen action. The depth of
		 * that tree is not changed such that it grows throughout the game.
		 * Therefore, the maximal MCTS_Depth also grows
		 */

		startingPos = a_gameState.getAvatarPosition();
		if (action == KEEP_COMPLETE_OLD_TREE) {
			//int oldDepth = m_root.m_depth;
			// This means we want to keep the old tree and just update the state
			m_root.state = a_gameState;
			//m_root.m_depth = oldDepth;
		} else {
			if (action == ADD_NEW_ROOT_NODE) {
				m_root = new MCTSNode(m_rnd);
				m_root.state = a_gameState;
				
			} else {
				// cut old tree
				m_root = m_root.children[action];
				m_root.state = a_gameState;
				m_root.parent = null;
				// m_root.startingRew = a_gameState.getGameScore();

				// adapting the tree depth is not really feasible within the
				// time constraints
				// m_root.correctDepth();
			}
		}
	}

	/**
	 * Runs MCTS to decide the action to take. It does not reset the tree.
	 * 
	 * @param elapsedTimer
	 *            Timer when the action returned is due.
	 * @return the action to execute in the game.
	 */
	public int run(ElapsedCpuTimer elapsedTimer) {
		// Do the search within the available time.
		m_root.mctsSearch(elapsedTimer);

		// Determine the best action to take and return it.
		// int action = m_root.mostVisitedAction();
		
		int action = m_root.bestAction();
		if(m_root.state.getGameTick() < 100){
			for (int i = 1; i<= m_root.children.length ; i++ ){
				if(m_root.children[i-1] != null){
					System.out.print("  val"+i+": "+ m_root.children[i-1].totValue);
				}
			}

			System.out.println();}
//		 System.out.println(" RewOfSys :" + m_root.value(m_root.state));
		 System.out.println(" action :" + action);
		// if (action >= 0){
		// System.out.println(" isdeath? :" +
		// m_root.children[action].isLoseState());
		// System.out.println(" isdeadend? :" +
		// m_root.children[action].isDeadEnd(2, true));
		// }
		// if (action >= 0)
		// System.out.println(" mroot :" + m_root.nVisits +
		// " visits and the choosen child has "+
		// m_root.children[action].nVisits);

		// System.out.println("Number of nodes: " + m_root.countNodes());

		return action;
	}

	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		// Heuristic: change the reward in the exploration reward map of the
		// visited current position
		Vector2d avatarPos = stateObs.getAvatarPosition();
		startingPos = avatarPos;
		
		// increment reward at all unvisited positions and decrement at
		// current position
		PersistentStorage.rewMap.incrementAll(0.001);
		PersistentStorage.rewMap.setRewardAtWorldPosition(avatarPos, -0.3);
		PersistentStorage.rewMap.decrementAtPos(avatarPos, -0.01);

		// rewMap.print();

		/*
		 * Heuristic: Punish the exploration area where enemies have been // and
		 * reward it if the enemies are attractive
		 * 
		 * @note TODO: This could also be done for resources and one could
		 * reward the surroundings (with a reward gradient, see IDEA below)
		 * 
		 * @note TODO: Or maybe it would be better not to reward the positions
		 * where the npcs were, but always the position and surroundings, where
		 * they currently are which would require a lot of time (I tried it for
		 * all Sprites, but it was too inefficient)
		 * 
		 * @note Heuristic (IDEA): Perhaps increase reward towards positions
		 * that have a resource. -> some sort of diffusion model with the
		 * resources as positive sources and the enemies as negative sources to
		 * create a reward gradient.
		 */

		boolean rewardNPCs = false;
		if (rewardNPCs) {
			ArrayList<Observation>[] npcPositions = null;
			npcPositions = stateObs.getNPCPositions();
			double npcAttractionValue = 0;
			if (npcPositions != null) {
				for (ArrayList<Observation> npcs : npcPositions) {
					for (int i = 0; i < npcs.size(); i++) {
						Vector2d npcPos = npcs.get(i).position;

						npcAttractionValue = PersistentStorage.iTypeAttractivity
								.putIfAbsent(npcs.get(i));

						if (Math.abs(PersistentStorage.rewMap
								.getRewardAtWorldPosition(npcPos)) < 1) {
							PersistentStorage.rewMap
									.incrementRewardAtWorldPosition(npcPos,
											npcAttractionValue * 0.02);
						}
					}
				}
			}
		}

		boolean rewardResources = true;
		if (rewardResources) {
			ArrayList<Observation>[] resPositions = null;
			resPositions = stateObs.getNPCPositions();
			double resAttractionValue = 0;
			if (resPositions != null) {
				for (ArrayList<Observation> ress : resPositions) {
					for (int i = 0; i < ress.size(); i++) {
						Vector2d resPos = ress.get(i).position;

						resAttractionValue = PersistentStorage.iTypeAttractivity
								.putIfAbsent(ress.get(i));

						if (Math.abs(PersistentStorage.rewMap
								.getRewardAtWorldPosition(resPos)) < 1) {
							PersistentStorage.rewMap
									.incrementRewardAtWorldPosition(resPos,
											resAttractionValue * 0.02);
						}
					}
				}
			}
		}

		boolean useOldTree = true;
		if (useOldTree) {
			// Sets a new tree with the children[oldAction] as the root
			initWithOldTree(stateObs, oldAction);
		} else {
			// Set the state observation object as the new root of the tree.
			// (Forgets the whole tree)
			init(stateObs);
		}

		PersistentStorage.startingReward = stateObs.getGameScore();

		PersistentStorage.numberOfBlockedMovables = MCTSNode
				.trapHeuristic(stateObs);

		// Determine the action using MCTS...
		int action = run(elapsedTimer);
		// if (stateObs.getGameTick() % 2 == 0) {
		// action = KEEP_COMPLETE_TREE;
		// }
		if (action > ADD_NEW_ROOT_NODE && useOldTree) {
			PersistentStorage.MCTS_DEPTH_RUN += 1;
		}

		/*
		 * there is a problem when the tree is so small that the chosen children
		 * don't have any grand children, in this case Danny's isDeadEnd method
		 * will give back a true based on the "fear_unknown" input. Therefore, I
		 * treat this waiting as a thinking step, where we expand the old tree
		 * instead of creating a complete new tree that also leads to the same
		 * problem -> the guy is stuck.
		 */

		if (action == ADD_NEW_ROOT_NODE)
			action = KEEP_COMPLETE_OLD_TREE;

		oldAction = action;

		// ... and return it.
		if (action == KEEP_COMPLETE_OLD_TREE || action == ADD_NEW_ROOT_NODE) {
			return Types.ACTIONS.ACTION_NIL;
		} else {
			return PersistentStorage.actions[action];
		}

	}

	public void clearMemory() {
		// TODO Implement what to do when we run out of memory.

	}

}