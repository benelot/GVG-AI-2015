package tutorials.tutorial2;

import java.util.Random;

import misc.RunConfig;
import misc.GameLevelPair;
import core.ArcadeMachine;


/*
 * Possible Games:
 * ALIENS, BOULDERDASH, BUTTERFLIES, CHASE, FROGS, MISSILECOMMAND,
 * PORTALS, SOKOBAN, SURVIVEZOMBIES, ZELDA
 */

public class Tutorial2Runner {

	public static void main(String[] args) throws Exception {

		RunConfig config = new RunConfig();
		config.addGameLevel("qlearnMaze", 0);

		
		config.setController(tutorials.tutorial2.QLearningAgent.class.getCanonicalName());
		config.setSaveActions(true);
		
		// train 100 times
		config.setRepetitions(300);
		runGames(config);
		// play game visually one
		config.setRepetitions(1);
		runGamesVisually(config);
	}

	/**
	 * Run the configured games with the configured controller and show the game
	 * visually.
	 * 
	 * @param config
	 *            The run configuration containing the game details.
	 */
	public static void runGamesVisually(RunConfig config) {
		for (GameLevelPair<String, String[]> gameLevelPair : config
				.getGameLevels()) {
			for (String level : gameLevelPair.level) {
				for (int repetition = 0; repetition < config.getRepetitions(); repetition++) {
					String actionsFile = "actions_game_" + gameLevelPair.game
							+ "_lvl_" + level + "_r" + repetition + "_"
							+ RunConfig.getTimestampNow() + ".txt";
					ArcadeMachine.runOneGame(RunConfig
							.getGamePath(gameLevelPair.game), RunConfig
							.getGameLevelPath(gameLevelPair.game, level), true,
							config.getController(),
							(config.isSaveActions()) ? actionsFile : null,
							new Random().nextInt());
				}
			}
		}

	}

	/**
	 * Run the configured games with the configured controller without visual
	 * feedback.
	 * 
	 * @param config
	 *            The run configuration containing the game details.
	 */
	public static void runGames(RunConfig config) {
		for (GameLevelPair<String, String[]> gameLevelPair : config
				.getGameLevels()) {
			ArcadeMachine.runGames(RunConfig.getGamePath(gameLevelPair.game),
					RunConfig.getGameLevelPaths(gameLevelPair.game,
							gameLevelPair.level), config.getRepetitions(),
					config.getController(), config.getRecordingPathsForGame(
							gameLevelPair.game, gameLevelPair.level));
		}
	}

	/**
	 * Run the configured games and play them yourself
	 * 
	 * @param config
	 *            The run configuration containing the game details.
	 */
	public static void playGamesYourself(RunConfig config) {
		for (GameLevelPair<String, String[]> gameLevelPair : config
				.getGameLevels()) {
			for (String level : gameLevelPair.level) {
				for (int repetition = 0; repetition < config.getRepetitions(); repetition++) {
					String actionsFile = "actions_game_" + gameLevelPair.game
							+ "_lvl_" + level + "_r" + repetition + "_"
							+ RunConfig.getTimestampNow() + ".txt";
					ArcadeMachine.playOneGame(RunConfig
							.getGamePath(gameLevelPair.game), RunConfig
							.getGameLevelPath(gameLevelPair.game, level),
							(config.isSaveActions()) ? actionsFile : null,
							new Random().nextInt());
				}
			}

		}
	}

	/**
	 * Replay a recorded game
	 * 
	 * @param readActionsFile
	 *            The file name of the recorded game.
	 */
	public static void replayGame(String readActionsFile) {
		String[] split = readActionsFile.split("_");
		ArcadeMachine.replayGame(RunConfig.getGamePath(split[2]),
				RunConfig.getGameLevelPath(split[2], split[4]), true,
				readActionsFile);
	}

}
