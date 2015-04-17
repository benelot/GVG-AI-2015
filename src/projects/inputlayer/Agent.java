package projects.inputlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;
import tools.Vector2d;
import core.game.Observation;
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
		// get game dimensions and Avatar starting position.
		// blockSize = gameState.getBlockSize();
		// nBlocksX = gameState.getWorldDimension().width / blockSize;
		// nBlocksY = gameState.getWorldDimension().height / blockSize;

		// initialize map to zero
		grid = gameState.getObservationGrid();
		nBlocksX = grid[0].length;
		nBlocksY = grid.length;
		map = new String[nBlocksY][nBlocksX];
	}

	/**
	 * Act function. Called every game step, it must return an action in 40 ms
	 * maximum.
	 */
	public ACTIONS act(StateObservation gameState, ElapsedCpuTimer elapsedTimer) {

		// draw map
		// Vector2d avatarPosition = gameState.getAvatarPosition();
		grid = gameState.getObservationGrid();

		// From http://www.gvgai.net/forwardModel.php

		// My concerns:
		// -It seems that an object can be in multiple blocks at the same time,
		// so probably we need to switch to the coordinates instead of the
		// block indices.
		// -The distinction of objects is not yet very good. For instance the
		// goal is also considered an imovable object and therefore is the same
		// as a wall, that should be fixed.

		// These methods might be of use to you as well
		// ArrayList<Observation>[] immovablePositions =
		// gameState.getImmovablePositions();
		// ArrayList<Observation>[] movablePositions =
		// gameState.getMovablePositions();
		// ArrayList<Observation>[] npcPositions = gameState.getNPCPositions();
		// ArrayList<Observation>[] portalPositions =
		// gameState.getPortalsPositions();
		// ArrayList<Observation>[] resourcesPositions =
		// gameState.getResourcesPositions();
		// HashMap<Integer, Integer> avatarRessources =
		// gameState.getAvatarResources();
		//
		for (int i = 0; i < nBlocksX; i++) {
			for (int j = 0; j < nBlocksY; j++) {
				ArrayList<Observation> observationOfPosition = grid[j][i];
				for (int k = 0; k < observationOfPosition.size(); k++) {
					switch (observationOfPosition.get(k).category) {
					case 0:
						map[j][i] = "A";
					case 1:
						map[j][i] = "1";
						break;
					case 2:
						map[j][i] = "P";
						break;
					case 3:
						map[j][i] = "3";
						break;
					case 4:
						map[j][i] = "I";
						break;
					default:
						map[j][i] = String
								.valueOf(observationOfPosition.get(k).category);
						break;
					}
				}
			}
		}

		for (int i = 0; i < nBlocksX; i++) {
			for (int j = 0; j < nBlocksY; j++) {
				if (map[j][i] != null) {
					System.out.print(map[j][i]);
				} else {
					System.out.print(" ");
				}
				map[j][i] = null;

			}
			System.out.println();
		}

		System.out.println();
		System.out.println();

		// Get the available actions in this game.
		ArrayList<ACTIONS> actions = gameState.getAvailableActions();

		// Determine an index randomly and get the action to return.
		int index = randomGenerator.nextInt(actions.size());
		ACTIONS action = actions.get(index);

		// Return the action.
		return action;
	}

	ArrayList<Observation>[][] grid;

	public int blockSize;
	public int nBlocksX;
	public int nBlocksY;
	public String[][] map;

}
