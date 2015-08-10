package agents.misc;

import bladeRunner.Agent;
import ontology.Types;
import core.game.StateObservation;

public class GameClassifier {
	
	public static final int testingSteps = 20;
	
	public enum GameType {
		STOCHASTIC, DETERMINISTIC,NOT_DETERMINED
	}

	static GameType gameType;
	public static GameType determineGameType(StateObservation so){
		gameType = GameType.DETERMINISTIC;

		// // Stochasticity 1
		// // Advance a bit to check if stochastic
		// StateObservation testState1 = so.copy();
		// StateObservation testState2 = so.copy();
		// for (int ii = 0; ii < 10; ii++) {
		// testState1.advance(Types.ACTIONS.ACTION_NIL);
		// testState2.advance(Types.ACTIONS.ACTION_NIL);
		//
		// //I believe the advance method is more costly than the equiv method.
		// if(!testState1.equiv(testState2)){
		// isStochastic = true;
		// break;
		// }
		// }

		// Stochasticity 2
		// Checks if there are Non player characters
		StateObservation testState2 = so.copy();
		for (int ii = 0; ii < 10; ii++) {
			testState2.advance(Types.ACTIONS.ACTION_NIL);

			// I believe the advance method is more costly than the equiv
			// method.
			if (testState2.getNPCPositions() != null
					&& testState2.getNPCPositions().length > 0) {
				gameType = GameType.STOCHASTIC;
				break;
			}
		}

		if (gameType == GameType.STOCHASTIC) {
			if (Agent.isVerbose) {
				System.out.println("CLASSIFIER::Game seems to be stochastic");
			}
		} else {
			PersistentStorage.MCTS_DEPTH_RUN += 20;
			if (Agent.isVerbose) {
				System.out.println("CLASSIFIER::Game seems to be deterministic");
			}
		}
		return gameType;
		
	}
	public static GameType getGameType() {
		return gameType;
	}
	
	public static boolean hasMovement(StateObservation so) {
		int hash0 = ObservationTools.getHash(so);
		for (int k = 0; k < testingSteps; k++) {
			so.advance(Types.ACTIONS.ACTION_NIL);
			int hash1 = ObservationTools.getHash(so);
			if (hash0 != hash1) {
				return true;
			}
		}
		return false;
	}
	
	
}
