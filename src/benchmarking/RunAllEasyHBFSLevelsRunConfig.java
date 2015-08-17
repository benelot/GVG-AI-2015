package benchmarking;

import misc.runners.RunConfig;

public class RunAllEasyHBFSLevelsRunConfig extends RunConfig {
	public RunAllEasyHBFSLevelsRunConfig() {
		
		String[] games = {
		/**
		 * Deterministic games
		 *
		 */
				DeterministicGames.BAIT,
				DeterministicGames.BOLOADVENTURES,
				DeterministicGames.BOMBUZAL,
				DeterministicGames.BOULDERDASH,
				DeterministicGames.BRAINMAN,
				DeterministicGames.CAMELRACE,
				DeterministicGames.CATAPULTS,
				DeterministicGames.CHIPSCHALLENGE,
				DeterministicGames.DIGDUG,
				DeterministicGames.ESCAPE,
				DeterministicGames.FIRECASTER,
				DeterministicGames.FIRESTORMS,
				DeterministicGames.LABYRINTH,
				DeterministicGames.LEMMINGS,
				DeterministicGames.MODALITY,
				DeterministicGames.OVERLOAD,
				DeterministicGames.PAINTER,
				DeterministicGames.PORTALS,
				DeterministicGames.REALPORTALS,
				DeterministicGames.REALSOKOBAN,
				DeterministicGames.ROGUELIKE,
				DeterministicGames.SOKOBAN,
				DeterministicGames.SOLARFOX,
				DeterministicGames.SURROUND,
				DeterministicGames.THECITADEL,
				DeterministicGames.ZELDA
		};
		
		
		for (int i = 0; i < games.length; i++) {
			try {
				addGameLevel(games[i], new int[]{0});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//play every game only once. Can be changed from outside.
		setRepetitions(1);
		
		//get statistics for the benchmark
		setCalculateStatistics(true);
	}

}
