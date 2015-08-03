package agents.mcts;

import core.game.StateObservation;
import tools.ElapsedCpuTimer;

import java.util.Random;

/**
 * Created with IntelliJ IDEA. User: Diego Date: 07/11/13 Time: 17:13
 */
public class SingleMCTSPlayer {
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
	public SingleMCTSPlayer(Random a_rnd) {
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

		if ( action == -2){
			// keep the complete old tree
			m_root.state = a_gameState;
		}
		else{
			if ( action == -1){
				m_root = new SingleTreeNode(m_rnd);
				m_root.state = a_gameState;
			}
			else{
				// cut old tree
				m_root = m_root.children[action];
				m_root.state = a_gameState;
				m_root.parent = null;
				//m_root.startingRew = a_gameState.getGameScore();

				// adapting the tree depth is not really feasible within the time constraints
				//m_root.correctDepth();
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
		//int action = m_root.mostVisitedAction();

		int action = m_root.bestAction(true);

//				for (int i = 1; i<= m_root.children.length ; i++ ){
//					if(m_root.children[i-1] != null){
//						System.out.print("  val"+i+": "+ m_root.children[i-1].totValue);}
//				}
//				System.out.println();
//				System.out.println(" RewOfSys :" + m_root.value(m_root.state));
//				System.out.println(" action :" + action);
//				if (action >= 0){
//					System.out.println(" isdeath? :" + m_root.children[action].isLoseState());
//					System.out.println(" isdeadend? :" + m_root.children[action].isDeadEnd(2, true));
//				}
//		if (action >= 0)
//			System.out.println(" mroot :" + m_root.nVisits + " visits and the choosen child has "+ m_root.children[action].nVisits);
			
			//System.out.println("Number of nodes: " + m_root.countNodes());
		
		return action;
	}

}