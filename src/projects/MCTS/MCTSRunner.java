package projects.MCTS;

import java.util.ArrayList;
import java.util.Random;

import misc.RunConfig;
import misc.GameLevelPair;
import core.ArcadeMachine;

public class MCTSRunner {

	public static void main(String[] args) throws Exception {

		// Available controllers from the :
		String sampleRandomController = "controllers.sampleRandom.Agent";
		String sampleOneStepController = "controllers.sampleonesteplookahead.Agent";
		String sampleMCTSController = "controllers.sampleMCTS.Agent";
		String sampleOLMCTSController = "controllers.sampleOLMCTS.Agent";
		String sampleGAController = "controllers.sampleGA.Agent";

		String customSampleController = projects.MCTS.Agent.class
				.getCanonicalName();

		RunConfig config = new RunConfig();
		config.addGameLevel(RunConfig.GamesTraining2014.MISSILECOMMAND, 1);
		config.addGameLevel(RunConfig.GamesTraining2014.BOULDERDASH, 2);
//		config.addGameLevel(RunConfig.GamesValidation2014.PACMAN,new int[] { 1 });

		config.setRepetitions(1);
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
		
		ArrayList<Double> scores = new ArrayList<>();
		ArrayList<Long> times = new ArrayList<>();
		
		for (GameLevelPair<String, String[]> gameLevelPair : config
				.getGameLevels()) {
			
			long startTime = 0;
			long duration = 0;
			
			double averageScore = 0;
			double score;
			
			long averageTime = 0;
			
			for (String level : gameLevelPair.level) {
				for (int repetition = 0; repetition < config.getRepetitions(); repetition++) {
					String actionsFile = "actions_game_" + gameLevelPair.game
							+ "_lvl_" + level + "_r" + repetition + "_"
							+ RunConfig.getTimestampNow() + ".txt";
					
					startTime = System.nanoTime();
					score = ArcadeMachine.runOneGame(RunConfig
							.getGamePath(gameLevelPair.game), RunConfig
							.getGameLevelPath(gameLevelPair.game, level), true,
							config.getController(),
							(config.isSaveActions()) ? actionsFile : null,
							new Random().nextInt());
					duration = (long) ((System.nanoTime() - startTime)/1e6);
					averageScore = (averageScore*(repetition+1)+score)/(repetition+2);
					averageTime = (averageTime*(repetition+1)+duration)/(repetition+2);
					
				}
			}
			
			scores.add(averageScore);
			times.add(averageTime);

		}
		
		double cumulativeAvgScore = 0;
		double cumulativeAvgTime = 0;
		for(int i = 0; i < scores.size();i++){
			cumulativeAvgScore = (cumulativeAvgScore*(i+1)+scores.get(i))/(i+2);
			cumulativeAvgTime = (cumulativeAvgTime*(i+1)+times.get(i))/(i+2);
		}
		
		System.out.println("Avg Score:"+ cumulativeAvgScore);
		System.out.println("Avg Time: "+cumulativeAvgTime);
		System.out.println("Score/Time Ratio:" + cumulativeAvgScore/cumulativeAvgTime);

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
	 * Run the configured games with the configured controller and don't show the game.
	 * 
	 * @param config
	 *            The run configuration containing the game details.
	 */
	public static void runGamesWithScore(RunConfig config) {
		
		ArrayList<Double> scores = new ArrayList<>();
		ArrayList<Long> times = new ArrayList<>();
		
		for (GameLevelPair<String, String[]> gameLevelPair : config
				.getGameLevels()) {
			
			long startTime = 0;
			long duration = 0;
			
			double averageScore = 0;
			double score;
			
			long averageTime = 0;
			
			for (String level : gameLevelPair.level) {
				for (int repetition = 0; repetition < config.getRepetitions(); repetition++) {
					String actionsFile = "actions_game_" + gameLevelPair.game
							+ "_lvl_" + level + "_r" + repetition + "_"
							+ RunConfig.getTimestampNow() + ".txt";
					
					startTime = System.nanoTime();
					score = ArcadeMachine.runOneGame(RunConfig
							.getGamePath(gameLevelPair.game), RunConfig
							.getGameLevelPath(gameLevelPair.game, level), false,
							config.getController(),
							(config.isSaveActions()) ? actionsFile : null,
							new Random().nextInt());
					duration = System.nanoTime() - startTime;
					averageScore = (averageScore*(repetition+1)+score)/(repetition+2);
					averageTime = (averageTime*(repetition+1)+duration)/(repetition+2);
					
				}
			}
			
			scores.add(averageScore);
			times.add(averageTime);

		}
		
		double cumulativeAvgScore = 0;
		double cumulativeAvgTime = 0;
		for(int i = 0; i < scores.size();i++){
			cumulativeAvgScore = (cumulativeAvgScore*(i+1)+scores.get(i))/(i+2);
			cumulativeAvgTime = (cumulativeAvgTime*(i+1)+times.get(i))/(i+2);
		}
		
		System.out.println("Avg Score:"+ cumulativeAvgScore);
		System.out.println("Avg Time: "+cumulativeAvgTime);
		System.out.println("Score/Time Ratio:" + cumulativeAvgScore/cumulativeAvgTime);

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

	public static void evaluateGame() {
	}

}
