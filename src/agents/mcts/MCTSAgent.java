package agents.mcts;

import agents.persistentStorage.PersistentStorage;
import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

import java.util.ArrayList;
import java.util.Random;

import ontology.Types;
import ontology.Types.ACTIONS;

/**
 * Created with IntelliJ IDEA. User: Diego Date: 07/11/13 Time: 17:13
 */
public class MCTSAgent extends AbstractPlayer {
	/**
	 * Root of the tree.
	 */
	public SingleTreeNode m_root;

	/**
	 * Random generator.
	 */
	public Random m_rnd;

	public int nodeQty;

	/**
	 * Creates the MCTS player with a sampleRandom generator object.
	 * 
	 * @param a_rnd
	 *            sampleRandom generator object.
	 */
	public MCTSAgent(Random a_rnd) {
		m_rnd = a_rnd;
		m_root = new SingleTreeNode(a_rnd);

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
		m_root = new SingleTreeNode(m_rnd);
		m_root.state = a_gameState;

	}

	public void init(StateObservation a_gameState) {
		// Set the game observation to a newly root node.
		m_root = new SingleTreeNode(m_rnd);
		m_root.state = a_gameState;

	}

	public void initWithOldTree(StateObservation a_gameState, int action) {
		// Here we create a new root-tree for the next search query based
		// on the old tree. The old tree gets cut at the chosen action.
		// The depth of that tree is not changed such that it grows throughout
		// the game. Therefore, the maximal MCTS_DEpth also grows

		if (action == -2) {
			// keep the complete old tree
			m_root.state = a_gameState;
		} else {
			if (action == -1) {
				m_root = new SingleTreeNode(m_rnd);
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

		int action = m_root.bestAction(true);

		// for (int i = 1; i<= m_root.children.length ; i++ ){
		// if(m_root.children[i-1] != null){
		// System.out.print("  val"+i+": "+ m_root.children[i-1].totValue);}
		// }
		// System.out.println();
		// System.out.println(" RewOfSys :" + m_root.value(m_root.state));
		// System.out.println(" action :" + action);
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

		// increment reward at all unvisited positions and decrement at
		// current position
		PersistentStorage.rewMap.incrementAll(0.001);
		PersistentStorage.rewMap.setRewardAtWorldPosition(avatarPos, -0.3);

		// rewMap.print();

		// Heuristic: Punish the exploration area where enemies have been
		// and reward it if the enemies are attractive
		// TODO: This could also be done for resources and one could reward
		// the surroundings (with a reward gradient, see IDEA below)
		// TODO: Or maybe it would be better not to reward the positions
		// where the npcs were, but always the position and surroundings,
		// where they currently are
		// which would require a lot of time (I tried it for all Sprites,
		// but it was too inefficient)
		// Heuristic (IDEA): Perhaps increase reward towards positions that
		// have
		// a resource. -> some sort of diffusion model with the resources as
		// positive sources and the enemies as negative sources to create a
		// reward
		// gradient.

		boolean rewardNPCs = true;
		if (rewardNPCs) {
			ArrayList<Observation>[] npcPositions = null;
			npcPositions = stateObs.getNPCPositions();
			double npcAttractionValue = 0;
			if (npcPositions != null) {
				for (ArrayList<Observation> npcs : npcPositions) {
					if (npcs.size() > 0) {
						Vector2d npcPos = npcs.get(0).position;
						try {
							npcAttractionValue = PersistentStorage.iTypeAttractivity
									.get(npcs.get(0).itype);
						} catch (java.lang.NullPointerException e) {
							PersistentStorage.iTypeAttractivity
									.putNewUniqueItype(npcs.get(0));
							npcAttractionValue = PersistentStorage.iTypeAttractivity
									.get(npcs.get(0).itype);
						}
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

		boolean rewardRecources = true;
		if (rewardRecources) {
			ArrayList<Observation>[] resPositions = null;
			resPositions = stateObs.getNPCPositions();
			double resAttractionValue = 0;
			if (resPositions != null) {
				for (ArrayList<Observation> ress : resPositions) {
					if (ress.size() > 0) {
						Vector2d resPos = ress.get(0).position;
						try {
							resAttractionValue = PersistentStorage.iTypeAttractivity
									.get(ress.get(0).itype);
						} catch (java.lang.NullPointerException e) {
							PersistentStorage.iTypeAttractivity
									.putNewUniqueItype(ress.get(0));
							resAttractionValue = PersistentStorage.iTypeAttractivity
									.get(ress.get(0).itype);
						}
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

		int useOldTree = 1;
		if (useOldTree == 1) {
			// Sets a new tree with the children[oldAction] as the root
			initWithOldTree(stateObs, oldAction);
		} else {
			// Set the state observation object as the new root of the tree.
			// (Forgets the whole tree)
			init(stateObs);
		}

		PersistentStorage.startingReward = stateObs.getGameScore();

		PersistentStorage.numberOfBlockedMovables = SingleTreeNode
				.trapHeuristic(stateObs);

		// Determine the action using MCTS...
		int action = run(elapsedTimer);
		// if (stateObs.getGameTick() % 2 == 0) {
		// action = -2;
		// }
		if (action > -2) {
			PersistentStorage.MCTS_DEPTH_RUN += useOldTree;
		}

		// there is a problem when the tree is so small that the chosen
		// children don't have any grand children,
		// in this case Danny's isDeadEnd method will give back a true based
		// on the "fear_unknown" input. Therefore,
		// I treat this waiting as a thinking step, where we expand the old
		// tree instead of creating a complete
		// new tree that also leads to the same problem -> the guy is stuck.

		if (action == -1)
			action = -2;

		oldAction = action;

		// ... and return it.
		if (action == -2 || action == -1) {
			return Types.ACTIONS.ACTION_NIL;
		} else {
			return PersistentStorage.actions[action];
		}

	}
	
	public int oldAction = -2;

}