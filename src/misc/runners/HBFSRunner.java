package misc.runners;

import agents.hbfs.HBFSAgent;

public class HBFSRunner {

	public static void main(String[] args) throws Exception {
		String customSampleController = agents.hbfs.HBFSAgent.class
				.getCanonicalName();
		RunConfig config = new RunConfig();

		
		// Puzzle Style Games: * work with HBFS, heuristic parameters wT = -3; wL = -2; 
		// - Check out modality, level 3, for some gripping game play!
		
		// - In some games the forward model does not seem to work properly. E.g. in BOLOADVENTURES (level 1), 
		//   an initial move to the left is not reflected in the updated StateObservation (see comments in HBFSAgent.initializeBfs(StateObservation so))
		
//		config.addGameLevel(RunConfig.GamesTraining2015.BOLOADVENTURES , new int[] {0,1,2,3,4}); // failure, too hard
//		config.addGameLevel(RunConfig.GamesTraining2015.CHIPSCHALLENGE ,new int[] {0,1,2,3,4}); //* except 4
//		config.addGameLevel(RunConfig.GamesTraining2015.BRAINMAN, new int[] {0,1,2,3,4}); //* except 0, 2, 3
//		config.addGameLevel(RunConfig.GamesTraining2015.REALSOKOBAN, new int[] {0,1,2,3,4}); // * except 1, 2
//		config.addGameLevel(RunConfig.GamesTraining2015.BAIT, new int[] {0,1,2,3,4}); //* except 3
//		config.addGameLevel(RunConfig.GamesTraining2014.SOKOBAN, new int[] {0,1,2,3,4}); //*
		config.addGameLevel(RunConfig.GamesTraining2015.MODALITY, new int[] {0,1,2,3,4}); //*
//		config.addGameLevel(RunConfig.GamesValidationGECCO2015.ESCAPE, new int[] {0,1,2,3,4}); //*
//		config.addGameLevel(RunConfig.GamesValidationGECCO2015.LABYRINTH, new int[] {0,1,2,3,4}); //*
				
		//config.addGameLevel(RunConfig.GamesTraining2015.PAINTER, new int[] {1,2,3,4}); // failure. using the load score seems to be a bad idea in this game.
		// config.addGameLevel(RunConfig.GamesTraining2015.THECITADEL, new int[] {1,2,3,4}); // failure. requires serious planning.
		// // config.addGameLevel(RunConfig.GamesTraining2015.REALPORTALS ,2 ); // failure. special handling of avatar generated observations (avatar can make portals) might be required.
		// // config.addGameLevel(RunConfig.GamesTraining2015.ZENPUZZLE ,1 ); // failure. using the load score might be a bad idea in this game.
				
		config.setRepetitions(1);
		config.setController(customSampleController);
		config.setSaveActions(false);
		config.setCalculateStatistics(true);

		// #############
		// UNCOMMENT THE APPROPRIATE PARTS TO...

		// #############
		// == Run the agent without visual feedback (faster for evaluation)
		// GameRunner.runGames(config);

		// #############
		// == Run the agent visually
		GameRunner.runGamesVisually(config);

		// ############
		// == Run against some of our benchmarks (Uncomment one of the next
		// three lines and the lowest two.)
		// RunConfig runconfig = RunConfig.getRunAllEasyLevelsRunConfig();
		// RunConfig runconfig = RunConfig.getRunAllHardLevelsRunConfig();
		// RunConfig runconfig = RunConfig.getRunAllGameLevelsRunConfig();
		// runconfig.setController(customSampleController);
		// GameRunner.runGames(runconfig);

		// #############
		// == Replay a game
		// GameRunner.replayGame("actions_game_portals_lvl_1_r0_20150306191441.txt");

		// ########################
		// == Play the games yourself
		// PlayAllGamesConfig is just a preconfigured RunConfig that contains
		// all games and is set to be played by a human
		// Controls: (Up,Down,Left,Right,Space)
		// GameRunner.playGamesYourself(RunConfig.getPlayAllGamesConfig());
		
		HBFSAgent.saveHashlist();
		HBFSAgent.displayHashingDiagnostics();
		
	}
}
