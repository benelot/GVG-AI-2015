package agents.mcts;

import java.util.ArrayList;
import java.util.Random;

import agents.misc.PersistentStorage;
import bladeRunner.Agent;
import core.game.Observation;
import core.game.StateObservation;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Utils;
import tools.Vector2d;

public class MCTSNode {
	public enum StateType {
		UNCACHED, LOSE, NORMAL, WIN
	}

	public static Random m_rnd;

	// strong rewards
	private static final double HUGE_NEGATIVE_REWARD = -Double.MAX_VALUE;
	private static final double HUGE_POSITIVE_REWARD = Double.MAX_VALUE;

	public static double fear_of_unknown = 0.9;
	public static double epsilon = 1e-6;
	public static double egreedyEpsilon = 0.05;
	public StateObservation state;
	public MCTSNode parent;
	public MCTSNode[] children;
	public double totValue;
	public int nVisits;

	public int m_depth;
	private static double[] lastBounds = new double[] { 0, 1 };
	private static double[] curBounds = new double[] { 0, 1 };
	public StateType stateType = StateType.UNCACHED;

	// keeps track of the reward at the start of the MCTS search
	// public double startingRew;

	public MCTSNode(Random rnd) {
		this(null, null, rnd);
	}

	public MCTSNode(StateObservation state, MCTSNode parent, Random rnd) {
		this.state = state;
		this.parent = parent;
		MCTSNode.m_rnd = rnd;
		children = new MCTSNode[PersistentStorage.actions.length];
		totValue = 0.0;
		if (parent != null) {
			m_depth = parent.m_depth + 1;
		} else {
			m_depth = 0;
		}

	}

	public MCTSNode(StateObservation state, MCTSNode parent) {
		this.state = state;
		this.parent = parent;
		children = new MCTSNode[PersistentStorage.actions.length];
		totValue = 0.0;
		if (parent != null) {
			m_depth = parent.m_depth + 1;
		} else {
			m_depth = 0;
		}

	}

	public int countNodes() {
		int n = 1;
		for (MCTSNode child : children) {
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
			MCTSNode selected = treePolicy();

			// rollout subsequent steps randomly
			double delta = selected.rollOut();

			// feedback the reward form rollout along the "selected" branch of
			// the tree
			backUp(selected, delta);
			// backUpBest(selected, delta);

			remaining = elapsedTimer.remainingTimeMillis();
		}
	}

	public MCTSNode treePolicy() {

		MCTSNode cur = this;
		while (!cur.state.isGameOver()
				&& cur.m_depth < PersistentStorage.MCTS_DEPTH_RUN) {
			if (cur.notFullyExpanded()) {
				// expand with random actions of the unused actions.
				return cur.expand();

			} else {
				MCTSNode next = cur.uct();
				// SingleTreeNode next = cur.egreedy();
				cur = next;
			}
		}

		return cur;
	}

	public MCTSNode expand() {

		int bestAction = 0;
		double bestValue = -1; // select a never used action
		for (int i = 0; i < children.length; i++) {
			double x = MCTSNode.m_rnd.nextDouble();
			if (x > bestValue && children[i] == null) {
				bestAction = i;
				bestValue = x;
			}
		}
		StateObservation nextState = state.copy();
		nextState.advance(PersistentStorage.actions[bestAction]);

		// build children for the newly tried action
		MCTSNode tn = new MCTSNode(nextState, this);
		children[bestAction] = tn;
		return tn;

	}

	public MCTSNode uct() {

		// TODO: Cleanup these parts if not used
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

		MCTSNode selectedNode = null;
		int selected = -1;
		double bestValue = -Double.MAX_VALUE;
		for (int i = 0; i < children.length; i++) {
			double hvVal = children[i].totValue;
			double childValue = hvVal
					/ (children[i].nVisits + MCTSNode.epsilon);

			// reward + UCT-exploration term. Not clear to me if this is useful
			// for the size of the tree that we have within our time constraints
			double uctValue = childValue
					+ PersistentStorage.K
					* Math.sqrt(Math.log(nVisits + 1)
							/ (children[i].nVisits + MCTSNode.epsilon))
					+ MCTSNode.m_rnd.nextDouble() * MCTSNode.epsilon;

			// small sampleRandom numbers: break ties in unexpanded nodes
			if (uctValue > bestValue && !children[i].isLoseState()) {
				selected = i;
				bestValue = uctValue;
			}
		}
		if (selected == -1 || children[selected].isLoseState()) {
			if (Agent.isVerbose) {
				System.out
						.println("MCTS::##### Oh crap.  Death awaits with choice "
								+ selected + ".");
			}
			selected = 0;
		}
		if (selected != -1) {
			// if we do the uct step it might be worthwhile also to update the
			// state believe, this way we create a real rollout from a newly
			// sampled state-pathway and not just from the very first one.

			selectedNode = children[selected];
			StateObservation nextState = state.copy();
			nextState.advance(PersistentStorage.actions[selected]);
			selectedNode.state = nextState;

		}
		if (selectedNode == null) {
			throw new RuntimeException("Warning! returning null: " + bestValue
					+ " : " + children.length);
		}
		return selectedNode;
	}

	public MCTSNode egreedy() {

		MCTSNode selected = null;

		if (MCTSNode.m_rnd.nextDouble() < egreedyEpsilon) {
			// Choose randomly
			int selectedIdx = MCTSNode.m_rnd.nextInt(children.length);
			selected = children[selectedIdx];

		} else {
			// pick the best Q.
			double bestValue = -Double.MAX_VALUE;
			for (MCTSNode child : children) {
				double hvVal = child.totValue + MCTSNode.m_rnd.nextDouble()
						* MCTSNode.epsilon;

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
			previousScore = rollerState.getGameScore();
			int action = MCTSNode.m_rnd
					.nextInt(PersistentStorage.actions.length);
			rollerState.advance(PersistentStorage.actions[action]);
			PersistentStorage.iTypeAttractivity.updateAttraction(rollerState,
					previousScore);
			thisDepth++;
		}

		// get current position and reward at that position
		double explRew = PersistentStorage.rewMap
				.getRewardAtWorldPosition(rollerState.getAvatarPosition());

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
			normDelta = (value(rollerState) - PersistentStorage.startingReward)
					+ additionalRew;
		}
		int useTrappedHeuristics = 1;
		if (useTrappedHeuristics == 1) {
			normDelta += 0.1f * (PersistentStorage.numberOfBlockedMovables - trapHeuristic(rollerState));
		}
		return normDelta;
	}

	public double value(StateObservation a_gameState) {

		boolean gameOver = a_gameState.isGameOver();
		Types.WINNER win = a_gameState.getGameWinner();
		double rawScore = a_gameState.getGameScore();

		if (gameOver && win == Types.WINNER.PLAYER_LOSES) {
			// return -2;
			return HUGE_NEGATIVE_REWARD;
		}

		if (gameOver && win == Types.WINNER.PLAYER_WINS) {
			return HUGE_POSITIVE_REWARD;
		}

		return rawScore;
	}

	public boolean finishRollout(StateObservation rollerState, int depth) {
		if (depth >= PersistentStorage.ROLLOUT_DEPTH) // rollout end condition
														// occurs
			// "ROLLOUT_DEPTH" after the
			// MCTS/expand is finished
			return true;

		if (rollerState.isGameOver()) // end of game
			return true;

		return false;
	}

	public void backUp(MCTSNode node, double result) {
		// add the rewards and visits the the chosen branch of the tree
		MCTSNode n = node;
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

	public void backUpBest(MCTSNode node, double result) {
		// add the rewards and visits the the chosen branch of the tree
		MCTSNode n = node;
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

				if (children[i].nVisits + MCTSNode.m_rnd.nextDouble() * epsilon > bestValue) {
					bestValue = children[i].nVisits;
					selected = i;
				}
			}
		}

		if (selected == -1) {
			if (Agent.isVerbose) {
				System.out.println("MCTS::There are no visited actions!");
			}
			selected = 0;
		} else if (allEqual) {
			// If all are equal, we opt to choose for the one with the best Q.
			selected = bestAction();
		}
		selected = bestAction();
		return selected;
	}

	public int bestAction() {
		int selected = -1;
		double bestValue = -Double.MAX_VALUE;

		for (int i = 0; i < children.length; i++) {

			// previous implementation lead to the tendency to choose the later
			// actions thats why the avatar
			// ended up in the top right corner in most cases.
			if (children[i] != null) {
				// we divide the reward by the number of times that we actually
				// tried that child ( the sqrt is there just for fun ;) )
				double disturbedChildRew = (children[i].totValue + (MCTSNode.m_rnd
						.nextDouble() - 0.5) * epsilon)
						/ Math.sqrt(children[i].nVisits);
				if (disturbedChildRew > bestValue
						&& !children[i].isDeadEnd(2)) {
					bestValue = disturbedChildRew;
					// bestValue = children[i].totValue;
					selected = i;
				}
			}
		}

		if (selected == -1) {
			if (Agent.isVerbose) {
				System.out.println("MCTS::Best action is no action!");
			}
		}
		return selected;
	}

	public boolean isDeadEnd(int max_depth) {
		MCTSNode cur = this;
		boolean allDeaths = true;

		// Base case
		if (max_depth == 0 || this.isLoseState()
				|| this.stateType == StateType.WIN) {
			return this.isLoseState();
		} else {
			for (int i = 0; allDeaths && i < cur.children.length; i++) {
				if (cur.children[i] != null) {
					allDeaths = allDeaths
							&& cur.children[i].isDeadEnd(max_depth - 1);
				} else {
					if (MCTSNode.m_rnd.nextDouble() > fear_of_unknown) {
						// Well, there's an unknown path, and we're not worried
						// - so let's guess it isn't a dead end!
						return false;
					}
				}
			}
			// Let the callers know if there is only death this way
			return allDeaths;
		}
	}

	public boolean isLoseState() {
		if (this.stateType == StateType.UNCACHED) {
			boolean gameOver = this.state.isGameOver();
			Types.WINNER win = this.state.getGameWinner();
			if (gameOver && win == Types.WINNER.PLAYER_LOSES) {
				this.stateType = StateType.LOSE;
				return true;
			} else {
				if (win == Types.WINNER.PLAYER_WINS) {
					this.stateType = StateType.WIN;
				} else {
					this.stateType = StateType.NORMAL;
				}
				return false;
			}
		} else {
			return this.stateType == StateType.LOSE;
		}
	}

	public boolean notFullyExpanded() {
		for (MCTSNode tn : children) {
			if (tn == null) {
				return true;
			}
		}
		return false;
	}

	public static double trapHeuristic(StateObservation a_gameState) {

		// return the number of movable objects that are apparently blocked, at
		// least for 1 move
		ArrayList<Observation>[] movePos = null;
		movePos = a_gameState.getMovablePositions();
		int isTrapped = 0;
		int isCompletelyFree = 0;
		double blockSquare = a_gameState.getBlockSize()
				* a_gameState.getBlockSize();
		if (movePos != null) {
			for (int j = 0; j < movePos.length; j++) {
				for (int i = 0; i < movePos[j].size(); i++) {
					Vector2d mPPos = movePos[j].get(i).position;

					ArrayList<Observation>[] trappedByImmovables = a_gameState
							.getImmovablePositions(mPPos);

					ArrayList<Observation>[] trappedByMovables = a_gameState
							.getMovablePositions(mPPos);

					if (trappedByImmovables != null
							&& trappedByImmovables.length > 0) {
						// if surrounded by 3 objects, its trapped
						if (trappedByImmovables[0].size() >= 3) {
							if ((trappedByImmovables[0].get(0).sqDist - blockSquare) < 1
									&& (trappedByImmovables[0].get(1).sqDist - blockSquare) < 1
									&& (trappedByImmovables[0].get(2).sqDist - blockSquare) < 1) {
								isTrapped++;
							}
						}
						// if surrounded by a corner its trapped
						if (trappedByImmovables[0].size() >= 2) {
							if ((trappedByImmovables[0].get(0).sqDist - blockSquare) < 1
									&& (trappedByImmovables[0].get(1).sqDist - blockSquare) < 1
									&& Math.abs((trappedByImmovables[0].get(1).position.x - trappedByImmovables[0]
											.get(0).position.x)
											* (trappedByImmovables[0].get(1).position.y - trappedByImmovables[0]
													.get(0).position.y)) > 1) {
								isTrapped++;
							} else {
								// if surrounded by two immovable objects and a
								// movable object its trapped
								if (trappedByMovables != null
										&& trappedByMovables.length > 0) {
									if (trappedByMovables[0].size() > 1) {
										if ((trappedByImmovables[0].get(0).sqDist - blockSquare) < 1
												&& (trappedByImmovables[0]
														.get(1).sqDist - blockSquare) < 1
												&& (trappedByMovables[0].get(1).sqDist - blockSquare) < 1) {
											isTrapped++;
										}
									}
								}
							}
						}
					}

					if (trappedByImmovables != null
							&& trappedByImmovables.length > 0
							&& trappedByMovables != null
							&& trappedByMovables.length > 0) {
						// reward movable objects that are not surrounded by
						// anything
						if (trappedByImmovables[0].size() > 0) {
							if (trappedByMovables[0].size() > 1) {
								if ((trappedByImmovables[0].get(0).sqDist - blockSquare) > 1
										&& (trappedByMovables[0].get(1).sqDist - blockSquare) > 1) {
									isCompletelyFree++;
								}
							} else {
								if (trappedByImmovables[0].get(0).sqDist
										- blockSquare > 1) {
									isCompletelyFree++;
								}

							}
						}
					}
				}
			}
		}
		// reward the completely free objects not as much as the trapped ones.
		return isTrapped - isCompletelyFree / 2;
	}

	public void correctDepth() {
		// should correct (subtract 1 of) the depth of the whole tree. Needed
		// after cut, but seems to be to slow
		MCTSNode root = this;
		root.m_depth -= 1;
		for (int i = 0; i < root.children.length; i++) {
			if (root.children[i] != null) {
				// search for ALL children being null!
				root.children[i].correctDepth();
			}
		}
	}

}