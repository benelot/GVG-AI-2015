package misc;

import java.util.Random;

import misc.RunConfig;
import misc.GameLevelPair;
import core.ArcadeMachine;

public class GameRunner {

	public static Random random = new Random();

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
		//for each game
		for (GameLevelPair<String, String[]> gameLevelPair : config
				.getGameLevels()) {
			
			//for each level of the game
			for (String level : gameLevelPair.level) {
				//for each repetion of the game level
				for (int repetition = 0; repetition < config.getRepetitions(); repetition++) {
					
					//create the file name in case it is needed
					String actionsFile = "actions_game_" + gameLevelPair.game
							+ "_lvl_" + level + "_r" + repetition + "_"
							+ RunConfig.getTimestampNow() + ".txt";
					
					//run one game with the config
					ArcadeMachine.runOneGame(RunConfig
							.getGamePath(gameLevelPair.game), RunConfig
							.getGameLevelPath(gameLevelPair.game, level), true,
							config.getController(),
							(config.isSaveActions()) ? actionsFile : null,
							random.nextInt());
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
			
			//run all games with the appropriate levels and repetition
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
		//for all games
		for (GameLevelPair<String, String[]> gameLevelPair : config
				.getGameLevels()) {
			//for all levels of the game
			for (String level : gameLevelPair.level) {
				//for all repetitions of the game levels
				for (int repetition = 0; repetition < config.getRepetitions(); repetition++) {
					
					//create the file name in case it is needed
					String actionsFile = "actions_game_" + gameLevelPair.game
							+ "_lvl_" + level + "_r" + repetition + "_"
							+ RunConfig.getTimestampNow() + ".txt";
					
					//run one game with the config yourself
					ArcadeMachine.playOneGame(RunConfig
							.getGamePath(gameLevelPair.game), RunConfig
							.getGameLevelPath(gameLevelPair.game, level),
							(config.isSaveActions()) ? actionsFile : null,
							random.nextInt());
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
		//split the level information from the game path
		String[] gameLevelInformation = readActionsFile.split("_");
		
		//replay the game level
		ArcadeMachine.replayGame(RunConfig.getGamePath(gameLevelInformation[2]),
				RunConfig.getGameLevelPath(gameLevelInformation[2], gameLevelInformation[4]), true,
				readActionsFile);
	}

}
