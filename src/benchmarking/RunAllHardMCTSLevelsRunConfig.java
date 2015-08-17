package benchmarking;

import misc.runners.RunConfig;

public class RunAllHardMCTSLevelsRunConfig extends RunConfig {
	public RunAllHardMCTSLevelsRunConfig() {
		
		String[] games = {
		/**
		 * The stochastic games
		 *
		 */
				StochasticGames.ALIENS,
				StochasticGames.BOMBUZAL,
				StochasticGames.BOULDERCHASE,
				StochasticGames.BOULDERDASH,
				StochasticGames.BUTTERFLIES,
				StochasticGames.CHASE,
				StochasticGames.DIGDUG,
				StochasticGames.EGGOMANIA,
				StochasticGames.FIRESTORMS,
				StochasticGames.FROGS,
				StochasticGames.INFECTION,
				StochasticGames.JAWS,
				StochasticGames.LEMMINGS,
				StochasticGames.MISSILECOMMAND,
				StochasticGames.OVERLOAD,
				StochasticGames.PACMAN,
				StochasticGames.PLANTS,
				StochasticGames.PLAQUEATTACK,
				StochasticGames.ROGUELIKE,
				StochasticGames.SEAQUEST,
				StochasticGames.SOLARFOX,
				StochasticGames.SURROUND,
				StochasticGames.SURVIVEZOMBIES,
				StochasticGames.WHACKAMOLE,
				StochasticGames.ZELDA
			
		};
		
		
		for (int i = 0; i < games.length; i++) {
			try {
				addGameLevel(games[i], new int[]{4});
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
