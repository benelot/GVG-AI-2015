package misc;

import java.util.Random;

import misc.RunConfig;
import misc.GameLevelPair;
import core.ArcadeMachine;

public class GameRunner {
	
	// Available controllers from the Samples:
	public static String sampleRandomController = "controllers.sampleRandom.Agent";
	public static String sampleOneStepController = "controllers.sampleonesteplookahead.Agent";
	public static String sampleMCTSController = "controllers.sampleMCTS.Agent";
	public static String sampleOLMCTSController = "controllers.sampleOLMCTS.Agent";
	public static String sampleGAController = "controllers.sampleGA.Agent";

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
