package misc;

public class PlayRunConfig extends RunConfig {
	public PlayRunConfig() {
		
		String[] games = {
		/**
		 * CIG 2014 Training Set Games
		 *
		 */
			 GamesTraining2014.ALIENS,
			 GamesTraining2014.BOULDERDASH,
			 GamesTraining2014.BUTTERFLIES,
			 GamesTraining2014.CHASE,
			 GamesTraining2014.FROGS,
			 GamesTraining2014.MISSILECOMMAND,
			 GamesTraining2014.PORTALS,
			 GamesTraining2014.SOKOBAN,
			 GamesTraining2014.SURVIVEZOMBIES,
			 GamesTraining2014.ZELDA,


		/**
		 * CIG 2014 Validation Set Games
		 *
		 */
			 GamesValidation2014.CAMELRACE,
			 GamesValidation2014.DIGDUG,
			 GamesValidation2014.FIRESTORMS,
			 GamesValidation2014.INFECTION,
			 GamesValidation2014.FIRECASTER,
			 GamesValidation2014.OVERLOAD,
			 GamesValidation2014.PACMAN,
			 GamesValidation2014.SEAQUEST,
			 GamesValidation2014.WHACKAMOLE,
			 GamesValidation2014.EGGOMANIA,


		/**
		 * CIG 2015 New Training Set Games
		 *
		 */
			 GamesTraining2015.BAIT,
			 GamesTraining2015.BOLOADVENTURES,
			 GamesTraining2015.BRAINMAN,
			 GamesTraining2015.CHIPSCHALLENGE,
			 GamesTraining2015.MODALITY,
			 GamesTraining2015.PAINTER,
			 GamesTraining2015.REALPORTALS,
			 GamesTraining2015.REALSOKOBAN,
			 GamesTraining2015.THECITADEL,
			 GamesTraining2015.ZENPUZZLE,


		/**
		 * EXTRA GAMES
		 *
		 */
			 GamesExtra.SOLARFOX,
			 GamesExtra.BOMBUZAL
		};
		
		
		for (int i = 0; i < games.length; i++) {
			try {
				addGameLevel(games[i], new int[]{0,1,2,3,4});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//play every game only once. Can be changed from outside.
		setRepetitions(1);
		

	}

}
