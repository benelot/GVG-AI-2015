package agents.misc;

import bladeRunner.Agent;
import ontology.Types;
import core.game.StateObservation;

public class GameClassifier {

	/**
	 * Number of test steps we run in the game classifier.
	 */
	public static final int testingSteps = 80;

	/**
	 * The game classification categories.
	 *
	 */
	public enum GameType {
		MOVING, STATIC, NOT_DETERMINED
	}

	/**
	 * Our chosen game category
	 */
	static GameType gameType;

	public static GameType determineGameType(StateObservation so) {
		gameType = GameType.NOT_DETERMINED;

		if (hasMovement(so, testingSteps)) {
			gameType = GameType.MOVING;
		} else {
			gameType = GameType.STATIC;
		}

		if (gameType == GameType.MOVING) {
			if (Agent.isVerbose) {
				System.out.println("CLASSIFIER::Movement detected");
			}
		} else if (gameType == GameType.STATIC) {
			PersistentStorage.MCTS_DEPTH_RUN += 20;
			if (Agent.isVerbose) {
				System.out
						.println("CLASSIFIER::Static game");
			}
		}
		return gameType;

	}

	public static GameType getGameType() {
		return gameType;
	}

	// // Stochasticity 1
	// // Main problems: Some movement does not happend during the first 10
	// // steps.
	// // Advance a bit to check if stochastic
	// StateObservation testState1 = so.copy();
	// StateObservation testState2 = so.copy();
	// for (int ii = 0; ii < 10; ii++) {
	// testState1.advance(Types.ACTIONS.ACTION_NIL);
	// testState2.advance(Types.ACTIONS.ACTION_NIL);
	//
	// // I believe the advance method is more costly than the equiv
	// // method.
	// if (!testState1.equiv(testState2)) {
	// gameType = GameType.STOCHASTIC;
	// break;
	// }
	// }
	// gameType = GameType.DETERMINISTIC;

	// // Stochasticity 2
	// // Main problems: Some moving objects are not NPCs.
	// // Checks if there are Non player characters
	// StateObservation testState2 = so.copy();
	// for (int ii = 0; ii < 10; ii++) {
	// testState2.advance(Types.ACTIONS.ACTION_NIL);
	//
	// // I believe the advance method is more costly than the equiv
	// // method.
	// if (testState2.getNPCPositions() != null
	// && testState2.getNPCPositions().length > 0) {
	// gameType = GameType.STOCHASTIC;
	// break;
	// }
	// }
	// gameType = GameType.DETERMINISTIC;

	
	/**
	 * Test if the game has movement in it.
	 * 
	 * @param so
	 *            The state observation.
	 * @param testingSteps
	 *            The number of testing steps we advance the so.
	 * @return If the game has movement or not.
	 */
	public static boolean hasMovement(StateObservation so, int testingSteps) {
		// get initial hash
		int initialHash = ObservationTools.getHash(so);

		// second hash
		int advancedHash = 0;
		if (Agent.isVerbose) {
			System.out.println("Initial hash: " + initialHash);
		}

		// check if hash changes as we advance the forward model
		for (int k = 0; k < testingSteps; k++) {

			// advance the forward model
			so.advance(Types.ACTIONS.ACTION_NIL);

			// get the hash and compare
			advancedHash = ObservationTools.getHash(so);
			if (initialHash != advancedHash) {
				if (Agent.isVerbose) {
					System.out.println("Hash difference after " + k + " steps. (" + initialHash + " vs. " + advancedHash + ")");
				}
				return true;
			}
		}
		return false;

	}

}
