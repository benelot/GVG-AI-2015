package projects.HBFS;

import misc.GameRunner;
import misc.RunConfig;

public class HBFSRunner {

	public static void main(String[] args) throws Exception {
		String customSampleController = projects.HBFS.HBFSAgent.class
				.getCanonicalName();
		RunConfig config = new RunConfig();

		
		// Puzzle Style Games: * work with HBFS, heuristic parameters wT = -3; wL = -2; 
		// - Check out modality, level 3, for some gripping game play!
		
		// - When running two different games in a row, errors can occur. The controller gets reset, so it is currently unclear why this happens. 
		//   As a workaround Run only blocks of the same game only.
		// - In some games the forward model does not seem to work properly. E.g. in BOLOADVENTURES (level 1), 
		//   an initial move to the left is not reflected in the updated StateObservation (see comments in HBFSAgent.initializeBfs(StateObservation so))
		
		// // config.addGameLevel(RunConfig.GamesTraining2015.REALPORTALS ,2 ); // failure. special handling of avatar generated observations (avatar can make portals) might be required.
		// // config.addGameLevel(RunConfig.GamesTraining2015.ZENPUZZLE ,1 ); // failure. using the load score might be a bad idea in this game.
		// // config.addGameLevel(RunConfig.GamesTraining2015.BOLOADVENTURES ,1 ); //? potential bug: forward model doesn't update avatar position
		
		// config.addGameLevel(RunConfig.GamesTraining2015.BRAINMAN, 1); //*
		// // config.addGameLevel(RunConfig.GamesTraining2015.BRAINMAN, 2); // failure
		// // config.addGameLevel(RunConfig.GamesTraining2015.BRAINMAN, 3); // failure
		// config.addGameLevel(RunConfig.GamesTraining2015.BRAINMAN, 4); //*
		// // config.addGameLevel(RunConfig.GamesTraining2015.CHIPSCHALLENGE ,1); //? potential bug: forward model doesn't update avatar position
		
		// config.addGameLevel(RunConfig.GamesTraining2015.BAIT, 1); //*
		// config.addGameLevel(RunConfig.GamesTraining2015.BAIT, 2); //*
		// //config.addGameLevel(RunConfig.GamesTraining2015.BAIT, 3); // failure, pipe too short
		// config.addGameLevel(RunConfig.GamesTraining2015.BAIT, 4);  // *
		
		// // config.addGameLevel(RunConfig.GamesTraining2015.REALSOKOBAN ,1 ); // failure, seems as if load score does not reflect box-push-in interactions
		// // config.addGameLevel(RunConfig.GamesTraining2015.REALSOKOBAN ,2 ); // failure
		// config.addGameLevel(RunConfig.GamesTraining2015.REALSOKOBAN ,3 ); //*
		// config.addGameLevel(RunConfig.GamesTraining2015.REALSOKOBAN ,4 ); //*
		
		// config.addGameLevel(RunConfig.GamesTraining2014.SOKOBAN, 1); //*
		// config.addGameLevel(RunConfig.GamesTraining2014.SOKOBAN, 2); //*
		// config.addGameLevel(RunConfig.GamesTraining2014.SOKOBAN, 3); //*
		// config.addGameLevel(RunConfig.GamesTraining2014.SOKOBAN, 4); //*

		// config.addGameLevel(RunConfig.GamesTraining2015.MODALITY, 1); //? potential bug: forward model doesn't update avatar position
		// config.addGameLevel(RunConfig.GamesTraining2015.MODALITY, 2); //* potential bug: winning state is not recognized
		config.addGameLevel(RunConfig.GamesTraining2015.MODALITY, 3); //*
		// config.addGameLevel(RunConfig.GamesTraining2015.MODALITY, 4); //? potential bug: forward model doesn't update avatar position

		// config.addGameLevel(RunConfig.GamesTraining2015.PAINTER, 1); // failure. using the load score seems to be a bad idea in this game.
		// config.addGameLevel(RunConfig.GamesTraining2015.THECITADEL, 1); // failure. requires serious planning.

		
		// config.addGameLevel(RunConfig.GamesTraining2014.BOULDERDASH, 1); // has npcs
		// config.addGameLevel(RunConfig.GamesValidation2014.PACMAN,new int[] {
		// 1 });

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
	}
}
