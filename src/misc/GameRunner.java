package misc;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import benchmarking.GameStats;
import misc.RunConfig;
import misc.GameLevelPair;
import core.ArcadeMachine;
import core.game.StateObservation;
import ontology.Types;

public class GameRunner {

	public static Random random = new Random();

	// Available controllers from the Samples:
	public static String sampleRandomController = "controllers.sampleRandom.Agent";
	public static String sampleOneStepController = "controllers.sampleonesteplookahead.Agent";
	public static String sampleMCTSController = "controllers.sampleMCTS.Agent";
	public static String sampleOLMCTSController = "controllers.sampleOLMCTS.Agent";
	public static String sampleGAController = "controllers.sampleGA.Agent";

	public static StateObservation lastStateObservation;

	public static StateObservation getLastStateObservation() {
		return lastStateObservation;
	}

	public static void setLastStateObservation(
			StateObservation lastStateObservation) {
		GameRunner.lastStateObservation = lastStateObservation;
	}

	public static Map<String, GameStats> gameStatistics = new TreeMap<String, GameStats>();
	public static int win;
	public static double score;
	public static double time;

	/**
	 * Run the configured games with the configured controller and show the game
	 * visually.
	 * 
	 * @param config
	 *            The run configuration containing the game details.
	 */
	public static void runGamesVisually(RunConfig config) {
		// for each game
		for (GameLevelPair<String, String[]> gameLevelPair : config
				.getGameLevels()) {

			// for each level of the game
			for (String level : gameLevelPair.level) {
				// for each repetion of the game level
				for (int repetition = 0; repetition < config.getRepetitions(); repetition++) {

					// create the file name in case it is needed
					String actionsFile = "actions_game_" + gameLevelPair.game
							+ "_lvl_" + level + "_r" + repetition + "_"
							+ RunConfig.getTimestampNow() + ".txt";

					// run one game with the config
					ArcadeMachine.runOneGame(RunConfig
							.getGamePath(gameLevelPair.game), RunConfig
							.getGameLevelPath(gameLevelPair.game, level), true,
							config.getController(),
							(config.isSaveActions()) ? actionsFile : null,
							random.nextInt());
					if (config.isCalculateStatistics()) {
						processGameStatistics(RunConfig
								.getGamePath(gameLevelPair.game));
					}
					System.gc(); // free memory where possible
				}
			}
		}
		writeGameStatistics();
	}

	/**
	 * Run the configured games with the configured controller without visual
	 * feedback.
	 * 
	 * @param config
	 *            The run configuration containing the game details.
	 */
	public static void runGames(RunConfig config) {

		if (config.isCalculateStatistics()) {
			// for each game
			for (GameLevelPair<String, String[]> gameLevelPair : config
					.getGameLevels()) {

				// for each level of the game
				for (String level : gameLevelPair.level) {
					// for each repetion of the game level
					for (int repetition = 0; repetition < config
							.getRepetitions(); repetition++) {

						// create the file name in case it is needed
						String actionsFile = "actions_game_"
								+ gameLevelPair.game + "_lvl_" + level + "_r"
								+ repetition + "_"
								+ RunConfig.getTimestampNow() + ".txt";

						// run one game with the config
						ArcadeMachine.runOneGame(RunConfig
								.getGamePath(gameLevelPair.game), RunConfig
								.getGameLevelPath(gameLevelPair.game, level),
								false, config.getController(), (config
										.isSaveActions()) ? actionsFile : null,
								random.nextInt());
						processGameStatistics(RunConfig
								.getGamePath(gameLevelPair.game));
					}
				}
			}
			writeGameStatistics();
		} else {
			for (GameLevelPair<String, String[]> gameLevelPair : config
					.getGameLevels()) {

				// run all games with the appropriate levels and repetition
				ArcadeMachine.runGames(RunConfig
						.getGamePath(gameLevelPair.game), RunConfig
						.getGameLevelPaths(gameLevelPair.game,
								gameLevelPair.level), config.getRepetitions(),
						config.getController(), config
								.getRecordingPathsForGame(gameLevelPair.game,
										gameLevelPair.level));
			}
		}
	}

	/**
	 * Run the configured games and play them yourself
	 * 
	 * @param config
	 *            The run configuration containing the game details.
	 */
	public static void playGamesYourself(RunConfig config) {
		// for all games
		for (GameLevelPair<String, String[]> gameLevelPair : config
				.getGameLevels()) {
			// for all levels of the game
			for (String level : gameLevelPair.level) {
				// for all repetitions of the game levels
				for (int repetition = 0; repetition < config.getRepetitions(); repetition++) {

					// create the file name in case it is needed
					String actionsFile = "actions_game_" + gameLevelPair.game
							+ "_lvl_" + level + "_r" + repetition + "_"
							+ RunConfig.getTimestampNow() + ".txt";

					// run one game with the config yourself
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
		// split the level information from the game path
		String[] gameLevelInformation = readActionsFile.split("_");

		// replay the game level
		ArcadeMachine
				.replayGame(RunConfig.getGamePath(gameLevelInformation[2]),
						RunConfig.getGameLevelPath(gameLevelInformation[2],
								gameLevelInformation[4]), true, readActionsFile);
	}

	public static void setGameStatistics(boolean win, double score, double time) {
		GameRunner.win = (win) ? 1 : 0;
		GameRunner.score = score;
		GameRunner.time = time;
	}

	public static void processGameStatistics(String gamePath) {
		setGameStatistics(ArcadeMachine.lastWinner == Types.WINNER.PLAYER_WINS,
				ArcadeMachine.lastScore, ArcadeMachine.lastTime);
		GameStats gameStats = gameStatistics.get(gamePath);
		if (gameStats != null) {
			gameStats.winRatio = ((gameStats.winRatio * gameStats.sampleSize) + win)
					/ (gameStats.sampleSize + 1);
			gameStats.avgScore = ((gameStats.avgScore * gameStats.sampleSize) + score)
					/ (gameStats.sampleSize + 1);
			gameStats.avgTime = ((gameStats.avgTime * gameStats.sampleSize) + time)
					/ (gameStats.sampleSize + 1);
			gameStats.sampleSize++;
		} else {
			gameStatistics.put(gamePath, new GameStats(win, score, time));
		}
	}

	public static void writeGameStatistics() {
		for (String gamePath : gameStatistics.keySet()) {
			GameStats gameStat = gameStatistics.get(gamePath);
			System.out.println("Statistics for: " + gamePath);
			System.out.println("---------------------------------");
			gameStat.print();
		}
	}

}
