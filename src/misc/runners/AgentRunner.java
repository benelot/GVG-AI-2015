package misc.runners;

public class AgentRunner {

	public static void main(String[] args) throws Exception {

		String controller = bladeRunner.Agent.class
				.getCanonicalName();

		RunConfig config = new RunConfig();
		
		// GamesTraining2014
		config.addGameLevel(RunConfig.GamesTraining2014.ALIENS,new int[] {1,2,3,4} );
//		config.addGameLevel(RunConfig.GamesTraining2014.BOULDERDASH,new int[] {1,2,3,4} );
//		config.addGameLevel(RunConfig.GamesTraining2014.BUTTERFLIES,new int[] {1,2,3,4} );
//		config.addGameLevel(RunConfig.GamesTraining2014.CHASE,new int[] {1,2,3,4} );
//		config.addGameLevel(RunConfig.GamesTraining2014.FROGS,new int[] {1,2,3,4} );
		config.addGameLevel(RunConfig.GamesTraining2014.MISSILECOMMAND,new int[] {0,1,2,3,4} );
//		config.addGameLevel(RunConfig.GamesTraining2014.PORTALS,new int[] {1,2,3,4} );
//		config.addGameLevel(RunConfig.GamesTraining2014.SOKOBAN,new int[] {1,2,3,4} );
//		config.addGameLevel(RunConfig.GamesTraining2014.SURVIVEZOMBIES,new int[] {1,2,3,4} );
//		config.addGameLevel(RunConfig.GamesTraining2014.ZELDA,new int[] {1,2,3,4} );
		
		
		// GamesValidation2014
//		config.addGameLevel(RunConfig.GamesValidation2014.CAMELRACE, new int[] {1,2,3,4});
//		config.addGameLevel(RunConfig.GamesValidation2014.DIGDUG, new int[] {1,2,3,4});
//		config.addGameLevel(RunConfig.GamesValidation2014.EGGOMANIA, new int[] {1,2,3,4});
//		config.addGameLevel(RunConfig.GamesValidation2014.FIRECASTER, new int[] {1,2,3,4});
//		config.addGameLevel(RunConfig.GamesValidation2014.FIRESTORMS, new int[] {1,2,3,4});
//		config.addGameLevel(RunConfig.GamesValidation2014.INFECTION, new int[] {1,2,3,4});
//		config.addGameLevel(RunConfig.GamesValidation2014.OVERLOAD, new int[] {1,2,3,4});
//		config.addGameLevel(RunConfig.GamesValidation2014.PACMAN, new int[] {0,1,2,3,4});
//		config.addGameLevel(RunConfig.GamesValidation2014.SEAQUEST, new int[] {1,2,3,4});
//		config.addGameLevel(RunConfig.GamesValidation2014.WHACKAMOLE, new int[] {1,2,3,4});
		
		// GamesTraining2015
//	    config.addGameLevel(RunConfig.GamesTraining2015.BAIT ,new int[] {1,2,3,4} ); //can do 1,2,4
//		config.addGameLevel(RunConfig.GamesTraining2015.BOLOADVENTURES ,new int[] {1,2,3,4} ); //bug
//		config.addGameLevel(RunConfig.GamesTraining2015.BRAINMAN ,new int[] {1,4} ); // can do 1,4
//		config.addGameLevel(RunConfig.GamesTraining2015.CHIPSCHALLENGE ,new int[] {1,2,3,4} );
//		config.addGameLevel(RunConfig.GamesTraining2015.MODALITY ,new int[] {1,2,3,4} ); //can do 2,3
//		config.addGameLevel(RunConfig.GamesTraining2015.PAINTER ,new int[] {1,2,3,4} );
//		config.addGameLevel(RunConfig.GamesTraining2015.REALPORTALS ,new int[] {1,2,3,4} );
//		config.addGameLevel(RunConfig.GamesTraining2015.REALSOKOBAN ,new int[] {1,2,3,4} ); //can do 3,4
		
//		config.addGameLevel(RunConfig.GamesTraining2015.THECITADEL ,new int[] {1,2,3,4} );
//		config.addGameLevel(RunConfig.GamesTraining2015.ZENPUZZLE ,new int[] {0,1,2,3,4} );
//		config.addGameLevel(RunConfig.GamesValidationGECCO2015.PLANTS, new int[] {0,1,2,3,4});
		
		config.setRepetitions(1);
		config.setController(controller);
//		config.setSaveActions(false);
		config.setCalculateStatistics(true);

		// #############
		// UNCOMMENT THE APPROPRIATE PARTS TO...

		// #############
		// == Run the agent without visual feedback (faster for evaluation)
//		 GameRunner.runGames(config);

		// #############
		// == Run the agent visually
		 GameRunner.runGamesVisually(config);

		// ############
		// == Run against some of our benchmarks (Uncomment one of the next
		// three lines and at least the lowest two.)
		// RunConfig runconfig = RunConfig.getRunAllEasyLevelsRunConfig();
		// RunConfig runconfig = RunConfig.getRunAllHardLevelsRunConfig();
//		 RunConfig runconfig = RunConfig.getRunAllGameLevelsRunConfig();
//		 runconfig.setController(controller);
//		 runconfig.setRepetitions(5);
//		 runconfig.setCalculateStatistics(true);
//		 GameRunner.runGames(runconfig);

		// #############
		// == Replay a game
//		 GameRunner.replayGame("actions_game_portals_lvl_1_r0_20150306191441.txt");

		// ########################
		// == Play the games yourself
		// PlayAllGamesConfig is just a preconfigured RunConfig that contains
		// all games and is set to be played by a human
		// Controls: (Up,Down,Left,Right,Space)
//		 GameRunner.playGamesYourself(config);
	}
}
