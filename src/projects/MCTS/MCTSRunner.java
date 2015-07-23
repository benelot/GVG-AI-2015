package projects.MCTS;

import misc.GameRunner;
import misc.PlayRunConfig;
import misc.RunConfig;

public class MCTSRunner {

	public static void main(String[] args) throws Exception {

		String customSampleController = projects.MCTS.Agent.class
				.getCanonicalName();

		RunConfig config = new RunConfig();

		config.addGameLevel(RunConfig.GamesValidation2014.PACMAN, new int[] {
				0, 1, 2, 3, 4 });
		// config.addGameLevel(RunConfig.GamesValidation2014.PACMAN, new int[] {
		// 1, 2, 3, 4 });
		// config.addGameLevel(RunConfig.GamesValidation2014.PACMAN, new int[] {
		// 1, 2, 3, 4 });
		// //config.addGameLevel(RunConfig.GamesTraining2014.CHASE ,new int[]
		// {1,2,3,4} );
		// //config.addGameLevel(RunConfig.GamesTraining2014.CHASE ,new int[]
		// {1,2,3,4} );
		// config.addGameLevel(RunConfig.GamesTraining2014.CHASE ,new int[]
		// {1,2,3,4} );
		// config.addGameLevel(RunConfig.GamesTraining2014.CHASE,new int[]
		// {1,2,3, 4} );
		// config.addGameLevel(RunConfig.GamesTraining2014.BOULDERDASH,new int[]
		// {1,2,3,4} );
		// config.addGameLevel(RunConfig.GamesTraining2014.FROGS,new int[]
		// {1,2,3,4} );
		// config.addGameLevel(RunConfig.GamesTraining2014.MISSILECOMMAND, new
		// int[] {1,2,3,4} );
		// config.addGameLevel(RunConfig.GamesTraining2014.ZELDA, new int[]
		// {1,2,3,4} );
		// config.addGameLevel(RunConfig.GamesTraining2014.PORTALS,1 );
		// config.addGameLevel(RunConfig.GamesTraining2014.SOKOBAN ,1 );
		// config.addGameLevel(RunConfig.GamesTraining2014.SURVIVEZOMBIES , new
		// int[] {1,2,3,4} );
		// config.addGameLevel(RunConfig.GamesTraining2014.ZELDA ,1 );

		// config.addGameLevel(RunConfig.GamesValidation2014.PACMAN, 1);
		// config.addGameLevel(RunConfig.GamesValidation2014.PACMAN, 2);
		// config.addGameLevel(RunConfig.GamesValidation2014.PACMAN, 3);
		// config.addGameLevel(RunConfig.GamesValidation2014.PACMAN, 4);
		// config.addGameLevel(RunConfig.GamesTraining2014.SURVIVEZOMBIES, 1);
		// config.addGameLevel(RunConfig.GamesTraining2014.SURVIVEZOMBIES, 2);
		// config.addGameLevel(RunConfig.GamesTraining2014.SURVIVEZOMBIES, 3);
		// config.addGameLevel(RunConfig.GamesTraining2014.SURVIVEZOMBIES, 4);
		// config.addGameLevel(RunConfig.GamesTraining2014.ZELDA, 1);
		// config.addGameLevel(RunConfig.GamesTraining2014.CHASE, 1);

		// config.addGameLevel(RunConfig.GamesTraining2015.REALPORTALS ,1 );
		// config.addGameLevel(RunConfig.GamesTraining2015.ZENPUZZLE ,1 );
		// config.addGameLevel(RunConfig.GamesTraining2015.BOLOADVENTURES ,1 );
		// config.addGameLevel(RunConfig.GamesTraining2015.BRAINMAN ,1 );
		// config.addGameLevel(RunConfig.GamesTraining2015.CHIPSCHALLENGE ,1 );
		// config.addGameLevel(RunConfig.GamesTraining2015.BAIT ,1 );
		// config.addGameLevel(RunConfig.GamesTraining2015.REALSOKOBAN ,1 );
		// config.addGameLevel(RunConfig.GamesTraining2014.SOKOBAN, 1);
		// config.addGameLevel(RunConfig.GamesTraining2014.BOULDERDASH, 2);
		// config.addGameLevel(RunConfig.GamesValidation2014.PACMAN,new int[] {
		// 1 });

		config.setRepetitions(1);
		config.setController(customSampleController);
		config.setSaveActions(false);

		// #############
		// UNCOMMENT THE APPROPRIATE PARTS TO...

		// #############
		// == Run the agent without visual feedback (faster for evaluation)
//		GameRunner.runGames(config);

		// #############
		// == Run the agent visually
		 GameRunner.runGamesVisually(config);

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