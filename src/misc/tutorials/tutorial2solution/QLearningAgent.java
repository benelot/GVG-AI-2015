package misc.tutorials.tutorial2solution;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import ontology.Types;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;
import core.game.StateObservation;
import core.player.AbstractPlayer;

public class QLearningAgent extends AbstractPlayer {

	protected Random randomGenerator;
	public static ACTIONS[] actions;
	public static double egreedyEpsilon = 0.05; // epsilon for epsilon-greedy
												// action-selection
	public static double gamma = 0.8; // discount factor for future rewards
	public static double alpha = 0.4; // learning rate
	private static final double HUGE_POSITIVE = 10.0; // reward for reaching the
														// goal
	private static final double DEFAULT_REWARD = -0.1; // reward for taking one
														// step in the maze

	public static int blockSize;
	public static int nBlocksX;
	public static int nBlocksY;
	public static int nActions;
	public static double[][][] qValues;

	public static JFrame visPlot;
	public static Container visPlotPane;
	public static Visualization visPlotVis;
	public static int visBlockSize = 100;

	public QLearningAgent(StateObservation gameState,
			ElapsedCpuTimer elapsedTimer) {
		randomGenerator = new Random();

		if (qValues == null) {
			// we are running the first episode and thus need to initialize
			// everything.
			// note that everything that is declared static is common to all
			// instances of the agent.

			// read the actions that are available in the game into a static
			// array.
			ArrayList<ACTIONS> act = gameState.getAvailableActions();
			actions = new ACTIONS[act.size()];
			for (int i = 0; i < actions.length; ++i) {
				actions[i] = act.get(i);
			}

			// get game dimensions and avatar starting position.
			blockSize = gameState.getBlockSize();
			nBlocksX = gameState.getWorldDimension().width / blockSize;
			nBlocksY = gameState.getWorldDimension().height / blockSize;
			nActions = actions.length;

			// initialize qValues to zero
			qValues = new double[nBlocksX][nBlocksY][nActions];

			// initialize visualization frame
			visPlot = new JFrame();
			visPlot.setSize(nBlocksX * visBlockSize, nBlocksY * visBlockSize);
			visPlot.setVisible(true);
			visPlotPane = visPlot.getContentPane();
			visPlotVis = new Visualization(qValues, visBlockSize);
			visPlotPane.add(visPlotVis);
		}

		visPlotVis.values = qValues;
		visPlotVis.repaint();
	}

	private double getScore(StateObservation gameState) {
		boolean gameOver = gameState.isGameOver();
		Types.WINNER win = gameState.getGameWinner();
		double score = gameState.getGameScore();

		if (gameOver && win == Types.WINNER.PLAYER_WINS)
			return score + HUGE_POSITIVE;

		return score;
	}

	private double getReward(StateObservation gameState, int actIdx) {
		// use forward model to obtain action-outcome. this is a little hack for
		// now, as we
		// have not found a hook to obtain the action-outcome without utilizing
		// the forward model
		StateObservation nextState = gameState.copy();
		nextState.advance(actions[actIdx]);

		// if we encounter invalid actions on the way, we set their respective
		// q-values to negative infinity
		if (nextState.getAvatarPosition().x == gameState.getAvatarPosition().x
				&& nextState.getAvatarPosition().y == gameState
						.getAvatarPosition().y) {
			return Double.NEGATIVE_INFINITY;
		}

		// if action was valid, but we did not reach the goal, yield default
		// reward.
		double reward = getScore(nextState) - getScore(gameState);
		if (reward == 0.0) {
			reward = DEFAULT_REWARD;
		}
		return reward;
	}

	private int getBestActionIdx(int stateX, int stateY) {
		// pick the best action according to Q-value.
		int maxIdx = 0;
		for (int i = 1; i < nActions; i++) {
			if (qValues[stateX][stateY][i] > qValues[stateX][stateY][maxIdx]) {
				maxIdx = i;
			} else if (qValues[stateX][stateY][i] == qValues[stateX][stateY][maxIdx]
					&& randomGenerator.nextInt(2) == 1) {
				// randomly pick the new idx half the time when its qvalue is
				// equal to the known max so far
				maxIdx = i;
			}
		}
		return maxIdx;
	}

	private double getBestQValue(int stateX, int stateY) {
		// get best Q value for state.
		double maxVal = Double.NEGATIVE_INFINITY;
		for (int i = 1; i < nActions; i++) {
			if (qValues[stateX][stateY][i] > maxVal) {
				maxVal = qValues[stateX][stateY][i];
			}
		}
		return maxVal;
	}

	private double getQPrediction(int stateX, int stateY, int actIdx) {
		int nextX = stateX;
		int nextY = stateY;
		if (actIdx == 0) { // left
			nextX = nextX - 1;
		} else if (actIdx == 1) { // right
			nextX = nextX + 1;
		} else if (actIdx == 2) { // down
			nextY = nextY + 1;
		} else { // up
			nextY = nextY - 1;
		}

		return gamma * getBestQValue(nextX, nextY);
	}

	public ACTIONS act(StateObservation gameState, ElapsedCpuTimer elapsedTimer) {

		int stateX = (int) gameState.getAvatarPosition().x / blockSize;
		int stateY = (int) gameState.getAvatarPosition().y / blockSize;

		// epsilon-greedy action-selection
		int actIdx = 0;
		if (randomGenerator.nextDouble() < egreedyEpsilon) {
			// choose randomly
			actIdx = randomGenerator.nextInt(actions.length);
			while (qValues[stateX][stateY][actIdx] == Double.NEGATIVE_INFINITY) {
				actIdx = randomGenerator.nextInt(actions.length);
			}
		} else {
			// pick best action
			actIdx = getBestActionIdx(stateX, stateY);
		}

		// update Q value
		double reward = getReward(gameState, actIdx);
		double oldVal = qValues[stateX][stateY][actIdx];
		double nextQVal = getQPrediction(stateX, stateY, actIdx);
		qValues[stateX][stateY][actIdx] = (1 - alpha) * oldVal + alpha
				* (reward + nextQVal);

		// System.out.println("ACTION: (" + stateY + "," + stateX + ") to (" +
		// nextY + "," + nextX + "), update: " + oldVal + "->" + newVal);
		return actions[actIdx];
	}

}
