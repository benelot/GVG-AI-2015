package projects.MCTS;

import core.game.StateObservation;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Utils;

import java.util.Random;

public class SingleTreeNode {
	public enum StateType{
	    UNCACHED, LOSE, NORMAL, WIN
	}
	
	private static final double HUGE_NEGATIVE = -10000000.0;
	private static final double HUGE_POSITIVE = 10000000.0;
	public static double epsilon = 1e-6;
	public static double egreedyEpsilon = 0.05;
	public StateObservation state;
	public SingleTreeNode parent;
	public SingleTreeNode[] children;
	public double totValue;
	public int nVisits;
	public static Random m_rnd;
	public int m_depth;
	private static double[] lastBounds = new double[] { 0, 1 };
	private static double[] curBounds = new double[] { 0, 1 };
	public StateType state_type = StateType.UNCACHED;

	// keeps track of the reward at the start of the MCTS search
	// public double startingRew;

	public SingleTreeNode(Random rnd) {
		this(null, null, rnd);
	}

	public SingleTreeNode(StateObservation state, SingleTreeNode parent,
			Random rnd) {
		this.state = state;
		this.parent = parent;
		SingleTreeNode.m_rnd = rnd;
		children = new SingleTreeNode[Agent.NUM_ACTIONS];
		totValue = 0.0;
		if (parent != null) {
			m_depth = parent.m_depth + 1;
		} else {
			m_depth = 0;
		}

	}

	public int countNodes() {
		int n = 1;
		for (SingleTreeNode child : children) {
			if (child != null) {
				n += child.countNodes();
			}
		}
		return n;
	}

	public void mctsSearch(ElapsedCpuTimer elapsedTimer) {

		lastBounds[0] = curBounds[0];
		lastBounds[1] = curBounds[1];

		long remaining = elapsedTimer.remainingTimeMillis();

		while (remaining > 10) {
		
			// form tree with MCTS
			SingleTreeNode selected = treePolicy();

			// rollout subsequent steps randomly
			double delta = selected.rollOut();

			// feedback the reward form rollout along the "selected" branch of
			// the tree
			backUp(selected, delta);
			// backUpBest(selected, delta);

			remaining = elapsedTimer.remainingTimeMillis();
			// numIters++;
		}
		// System.out.println("-- " + numIters + " --");
	}

	public SingleTreeNode treePolicy() {

		SingleTreeNode cur = this;
		while (!cur.state.isGameOver() && cur.m_depth < Agent.MCTS_DEPTH_RUN) {
			if (cur.notFullyExpanded()) {
				// expand with random actions of the unused actions.
				return cur.expand();

			} else {
				SingleTreeNode next = cur.uct();
				// SingleTreeNode next = cur.egreedy();
				cur = next;
			}
		}

		return cur;
	}

	public SingleTreeNode expand() {

		int bestAction = 0;
		double bestValue = -1; // select a never used action
		for (int i = 0; i < children.length; i++) {
			double x = m_rnd.nextDouble();
			if (x > bestValue && children[i] == null) {
				bestAction = i;
				bestValue = x;
			}
		}
		StateObservation nextState = state.copy();
		nextState.advance(Agent.actions[bestAction]);

		// build children for the newly tried action
		SingleTreeNode tn = new SingleTreeNode(nextState, this,
				SingleTreeNode.m_rnd);
		children[bestAction] = tn;
		return tn;

	}

	public SingleTreeNode uct() {

		// SingleTreeNode selected = null;
		// double bestValue = -Double.MAX_VALUE;
		// for (SingleTreeNode child : this.children) {
		// double hvVal = child.totValue;
		// double childValue = hvVal / (child.nVisits + this.epsilon);
		//
		// // reward + UCT-exploration term. Not clear to me if this is useful
		// for the size of the tree that we have within our time constraints .
		// double uctValue = childValue
		// + Agent.K
		// * Math.sqrt(Math.log(this.nVisits + 1)
		// / (child.nVisits + this.epsilon))
		// + this.m_rnd.nextDouble() * this.epsilon;
		//
		// // small sampleRandom numbers: break ties in unexpanded nodes
		// if (uctValue > bestValue) {
		// selected = child;
		// bestValue = uctValue;
		// }
		// }
		//
		// if (selected == null) {
		// throw new RuntimeException("Warning! returning null: " + bestValue
		// + " : " + this.children.length);
		// }
		// return selected;

		SingleTreeNode selectedNode = null;
		int selected = -1;
		double bestValue = -Double.MAX_VALUE;
		for (int i = 0; i < this.children.length; i++) {
			double hvVal = this.children[i].totValue;
			double childValue = hvVal
					/ (this.children[i].nVisits + SingleTreeNode.epsilon);

			// reward + UCT-exploration term. Not clear to me if this is useful
			// for the size of the tree that we have within our time constraints
			// .
			double uctValue = childValue
					+ Agent.K
					* Math.sqrt(Math.log(this.nVisits + 1)
							/ (this.children[i].nVisits + SingleTreeNode.epsilon))
					+ SingleTreeNode.m_rnd.nextDouble()
					* SingleTreeNode.epsilon;

			// small sampleRandom numbers: break ties in unexpanded nodes
			if (uctValue > bestValue && !this.children[i].isLoseState()){
				selected = i;
				bestValue = uctValue;
			}
		}
		if(selected == -1 || this.children[selected].isLoseState()){
			System.out.println("##### Oh crap.  Death awaits with choice " + selected + ".");
			selected = 0;
		} 
		if (selected != -1) {
			// if we do the uct step it might be worthwhile also to update the
			// state believe,
			// this way we create a real rollout from a newly sampled
			// state-pathway and not just from the very first one.
			selectedNode = this.children[selected];
			StateObservation nextState = state.copy();
			nextState.advance(Agent.actions[selected]);
			selectedNode.state = nextState;
		}
		if (selectedNode == null) {
			throw new RuntimeException("Warning! returning null: " + bestValue
					+ " : " + this.children.length);
		}
		return selectedNode;
	}

	public SingleTreeNode egreedy() {

		SingleTreeNode selected = null;

		if (m_rnd.nextDouble() < egreedyEpsilon) {
			// Choose randomly
			int selectedIdx = m_rnd.nextInt(children.length);
			selected = this.children[selectedIdx];

		} else {
			// pick the best Q.
			double bestValue = -Double.MAX_VALUE;
			for (SingleTreeNode child : this.children) {
				double hvVal = child.totValue
						+ SingleTreeNode.m_rnd.nextDouble()
						* SingleTreeNode.epsilon;

				// small sampleRandom numbers: break ties in unexpanded nodes
				if (hvVal > bestValue) {
					selected = child;
					bestValue = hvVal;
				}
			}

		}

		if (selected == null) {
			throw new RuntimeException("Warning! returning null: "
					+ this.children.length);
		}

		return selected;
	}

	public double rollOut() {
		StateObservation rollerState = state.copy();

		// int thisDepth = this.m_depth;
		int thisDepth = 0; // here we guarantee "ROLLOUT_DEPTH" more rollout
							// after MCTS/expand is finished
		double previousScore;
		// rollout with random actions for "ROLLOUT_DEPTH" times
		while (!finishRollout(rollerState, thisDepth)) {
			previousScore=rollerState.getGameScore();		
			int action = m_rnd.nextInt(Agent.NUM_ACTIONS);
			rollerState.advance(Agent.actions[action]);
			Agent.iTypeAttractivity.updateAttraction(rollerState, previousScore);
			thisDepth++;
		}

		
		// get current position and  reward at that position
		double explRew = Agent.rewMap.getRewardAtWorldPosition(rollerState.getAvatarPosition());
	

		// use a fraction of "explRew" as an additional reward (Not given by the
		// Gamestats)
		double additionalRew = explRew / 2;

		// in the old implementation the absolut normalized reward was feed
		// back, This means that whenever there was no additional
		// reward, did the system feed back a reward of R/R = 1. If there was an
		// additional reward "r>0" then (R+r)/R > 1 was fed back.
		// For children that were tried differently oft is this kind of
		// comparison later on certainly unfair. Thats why one can consider
		// sending back the relative reward.
		int useRelativeRewar = 1;
		double normDelta = 0;
		if (useRelativeRewar == 0) {

			double delta = value(rollerState) + additionalRew;
			if (delta < curBounds[0])
				curBounds[0] = delta;
			if (delta > curBounds[1])
				curBounds[1] = delta;

			normDelta = Utils.normalise(delta, lastBounds[0], lastBounds[1]);
		} else {
			// get the relative reward
			normDelta = (value(rollerState) - Agent.startingReward)
					+ additionalRew;
		}

		return normDelta;
	}

	public double value(StateObservation a_gameState) {

		boolean gameOver = a_gameState.isGameOver();
		Types.WINNER win = a_gameState.getGameWinner();
		double rawScore = a_gameState.getGameScore();

		if (gameOver && win == Types.WINNER.PLAYER_LOSES) {
			// return -2;
			return HUGE_NEGATIVE;
		}

		if (gameOver && win == Types.WINNER.PLAYER_WINS) {
			return HUGE_POSITIVE;
		}

		return rawScore;
	}

	public boolean finishRollout(StateObservation rollerState, int depth) {
		if (depth >= Agent.ROLLOUT_DEPTH) // rollout end condition occurs
											// "ROLLOUT_DEPTH" after the
											// MCTS/expand is finished
			return true;

		if (rollerState.isGameOver()) // end of game
			return true;

		return false;
	}

	public void backUp(SingleTreeNode node, double result) {
		// add the rewards and visits the the chosen branch of the tree
		SingleTreeNode n = node;
		while (n != null) {
			n.nVisits++;
			n.totValue += result;
			n = n.parent;

			// a little hack to compare deaths which are close by and those that
			// are far away
			if (result < 0)
				result /= 2;
		}
	}

	public void backUpBest(SingleTreeNode node, double result) {
		// add the rewards and visits the the chosen branch of the tree
		SingleTreeNode n = node;
		while (n != null) {
			n.nVisits++;
			if (n.totValue < result) {
				n.totValue = result;
			}
			n = n.parent;
		}
	}

	public int mostVisitedAction() {
		int selected = -1;
		double bestValue = -Double.MAX_VALUE;
		boolean allEqual = true;
		double first = -1;

		for (int i = 0; i < children.length; i++) {

			if (children[i] != null) {
				if (first == -1)
					first = children[i].nVisits;
				else if (first != children[i].nVisits) {
					allEqual = false;
				}

				if (children[i].nVisits + m_rnd.nextDouble() * epsilon > bestValue) {
					bestValue = children[i].nVisits;
					selected = i;
				}
			}
		}

		if (selected == -1) {
			System.out.println("Unexpected selection!");
			selected = 0;
		} else if (allEqual) {
			// If all are equal, we opt to choose for the one with the best Q.
			selected = bestAction(false);
		}
		selected = bestAction(false);
		return selected;
	}

	public int bestAction(boolean fear_unknown) {
		int selected = -1;
		double bestValue = -Double.MAX_VALUE;

		for (int i = 0; i < children.length; i++) {

			// previous implementation lead to the tendency to choose the later
			// actions thats why the avatar
			// ended up in the top right corner in most cases.
			if (children[i] != null) {
				// we divide the reward by the number of times that we actually
				// tried that child ( the sqrt is there just for fun ;) )
				double disturbedChildRew = (children[i].totValue + (m_rnd
						.nextDouble() - 0.5) * epsilon)
						/ Math.sqrt(children[i].nVisits);
				if (disturbedChildRew > bestValue && !children[i].isDeadEnd(2, fear_unknown)) {
					bestValue = disturbedChildRew;
					// bestValue = children[i].totValue;
					selected = i;
				}
			}
		}

		if (selected == -1 ) {
			System.out.println("Unexpected selection!");
		}
		return selected;
	}

	public boolean isDeadEnd(int max_depth, boolean fear_unknown){
		SingleTreeNode cur = this;
		boolean allDeaths = true;
		
		// Base case
		if (max_depth == 0 || this.isLoseState() || this.state_type == StateType.WIN){
			return this.isLoseState();
		}		
		else {
			for (int i=0; allDeaths && i< cur.children.length; i++){
				if( cur.children[i] != null ){
					allDeaths = allDeaths && cur.children[i].isDeadEnd(max_depth - 1, fear_unknown);
				}
				else{
					if(!fear_unknown){
						// Well, there's an unknown path, and we're not worried - so let's guess it isn't a dead end!
						return false;
					}
				}
			}
			// Let the callers know if there is only death this way
			return allDeaths;			
		}
	}
	
	public boolean isLoseState(){
		if (this.state_type == StateType.UNCACHED){
			boolean gameOver = this.state.isGameOver();
			Types.WINNER win = this.state.getGameWinner();		
			if (gameOver && win == Types.WINNER.PLAYER_LOSES){
				this.state_type = StateType.LOSE;
				return true;
			}
			else {
				if(win == Types.WINNER.PLAYER_WINS){
					this.state_type = StateType.WIN;
				}
				else{
					this.state_type = StateType.NORMAL;
				}				
				return false;
			}
		}
		else {
			return this.state_type == StateType.LOSE;
		}
	}

	public boolean notFullyExpanded() {
		for (SingleTreeNode tn : children) {
			if (tn == null) {
				return true;
			}
		}
		return false;
	}

	public void correctDepth() {
		// should correct (subtract 1 of) the depth of the whole tree. Needed
		// after cut, but seems to be to slow
		//TODO: Is old_depth going to be used in the future?
		// int old_depth = this.m_depth;
		SingleTreeNode root = this;
		root.m_depth -= 1;
		for (int i = 0; i < root.children.length; i++) {
			if (root.children[i] != null) {
				// search for ALL children being null!
				root.children[i].correctDepth();
			}
		}
	}

}