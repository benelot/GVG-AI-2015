package projects.RankingTest;

import misc.PlayRunConfig;
import misc.GameRunner;
import misc.RunConfig;

public class RankingTest {

	public static void main(String[] args) throws Exception {

		String customSampleController = tutorials.tutorial1.TutorialAgent.class
				.getCanonicalName();

		RunConfig config = new RunConfig();
		config.addGameLevel(RunConfig.GamesTraining2014.PORTALS, 1);
		config.addGameLevel(RunConfig.GamesTraining2014.PORTALS, 2);
		config.addGameLevel(RunConfig.GamesTraining2014.FROGS,
				new int[] { 2, 3 });
		config.setRepetitions(2);
		config.setController(customSampleController);
		config.setSaveActions(true);

		// #############
		// UNCOMMENT THE APPROPRIATE PARTS TO...

		// #############
		// == Run the agent without visual feedback (faster for evaluation)
		// GameRunner.runGames(config);

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
