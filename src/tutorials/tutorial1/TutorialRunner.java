package tutorials.tutorial1;

import java.util.Random;

import misc.RunConfig;
import misc.GameLevelPair;
import core.ArcadeMachine;

public class TutorialRunner {

	public static void main(String[] args) throws Exception {

		// Available controllers from the :
		String sampleRandomController = "controllers.sampleRandom.Agent"; //String sampleRandomController = "tutorials.tutorial1.TutorialAgent";
		String sampleOneStepController = "controllers.sampleonesteplookahead.Agent";
		String sampleMCTSController = "controllers.sampleMCTS.Agent";
		String sampleOLMCTSController = "controllers.sampleOLMCTS.Agent";
		String sampleGAController = "controllers.sampleGA.Agent";

		String customSampleController = tutorials.tutorial1.TutorialAgent.class.getCanonicalName();

		RunConfig config = new RunConfig();
		config.addGameLevel(RunConfig.GamesTraining2014.SOKOBAN, 1);
		// config.addGameLevel(RunConfig.GamesTraining2014.PORTALS, 2);
		// config.addGameLevel(RunConfig.GamesTraining2014.FROGS,
		//		new int[] { 2, 3 });

		config.setRepetitions(2);
		config.setController(customSampleController);
		config.setSaveActions(true);

		runGamesVisually(config);

		// runGames(config);

		// replayGame("actions_game_portals_lvl_1_r0_20150306191441.txt");
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
