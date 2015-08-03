package misc.rankingtest;

import misc.runners.GameRunner;
import misc.runners.PlayAllGamesRunConfig;
import misc.runners.RunConfig;

public class RankingTest {

	public static void main(String[] args) throws Exception {

		String customSampleController = misc.tutorials.tutorial1.TutorialAgent.class
				.getCanonicalName();

		RunConfig config = new RunConfig();
		config.addGameLevel(RunConfig.GamesTraining2014.PORTALS, new int[]{0,1,2,3,4});
		config.addGameLevel(RunConfig.GamesTraining2014.PORTALS, 2);
		config.addGameLevel(RunConfig.GamesTraining2014.FROGS,
				new int[] { 2, 3 });
		config.setRepetitions(2);
		config.setController(customSampleController);
		config.setSaveActions(false);

		// #############
		// UNCOMMENT THE APPROPRIATE PARTS TO...

		// #############
		// == Run the agent without visual feedback (faster for evaluation)
		 GameRunner.runGames(config);

		// #############
		// == Run the agent visually
		// GameRunner.runGamesVisually(config);

		// #############
		// == Replay a game
		// GameRunner.replayGame("actions_game_portals_lvl_1_r0_20150306191441.txt");

		// ########################
		// == Play the games yourself
		// PlayAllGamesConfig is just a preconfigured RunConfig that contains
		// all games and is set to be played by a human
		// Controls: (Up,Down,Left,Right,Space)
		// GameRunner.playGamesYourself(RunConfig.getPlayAllGamesConfig());

	}

}
