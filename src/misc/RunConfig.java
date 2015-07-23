package misc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RunConfig {

	public RunConfig() {
		gameLevels = new ArrayList<GameLevelPair<String, String[]>>();
	}

	/**
	 * The path of the games.
	 */
	private final static String gamesPath = "examples/gridphysics/";

	/**
	 * CIG 2014 Training Set Games
	 *
	 */
	public interface GamesTraining2014 {
		String ALIENS = "aliens";
		String BOULDERDASH = "boulderdash";
		String BUTTERFLIES = "butterflies";
		String CHASE = "chase";
		String FROGS = "frogs";
		String MISSILECOMMAND = "missilecommand";
		String PORTALS = "portals";
		String SOKOBAN = "sokoban";
		String SURVIVEZOMBIES = "survivezombies";
		String ZELDA = "zelda";
	};

	/**
	 * CIG 2014 Validation Set Games
	 *
	 */
	public interface GamesValidation2014 {
		String CAMELRACE = "camelRace";
		String DIGDUG = "digdug";
		String FIRESTORMS = "firestorms";
		String INFECTION = "infection";
		String FIRECASTER = "firecaster";
		String OVERLOAD = "overload";
		String PACMAN = "pacman";
		String SEAQUEST = "seaquest";
		String WHACKAMOLE = "whackamole";
		String EGGOMANIA = "eggomania";
	};

	/**
	 * CIG 2015 New Training Set Games
	 *
	 */
	public interface GamesTraining2015 {
		String BAIT = "bait";
		String BOLOADVENTURES = "boloadventures";
		String BRAINMAN = "brainman";
		String CHIPSCHALLENGE = "chipschallenge";
		String MODALITY = "modality";
		String PAINTER = "painter";
		String REALPORTALS = "realportals";
		String REALSOKOBAN = "realsokoban";
		String THECITADEL = "thecitadel";
		String ZENPUZZLE = "zenpuzzle";
	};

	/**
	 * EXTRA GAMES
	 *
	 */
	public interface GamesExtra {
		String SOLARFOX = "solarfox";
		String BOMBUZAL = "bombuzal";
	};

	/**
	 * Choose a new game and level to evaluate the controller against.
	 * 
	 * @param game
	 *            A game name from RunConfig.Games*
	 * @param level
	 *            A level number between 0 and 4.
	 * @throws Exception
	 *             If the level is not between 0 and 4.
	 */
	public void addGameLevel(String game, int level) throws Exception {
		ArrayList<String> validLevels = new ArrayList<String>();
		if (level > 4) {
			throw new Exception("Level must be between 0 and 4");
		} else {
			validLevels.add(String.valueOf(level));
		}

		gameLevels.add(new GameLevelPair<String, String[]>(game, validLevels
				.toArray(new String[validLevels.size()])));

	}

	/**
	 * Choose a new game and multiple levels to evaluate the controller against.
	 * 
	 * @param game
	 *            A game name from RunConfig.Games*
	 * @param level
	 *            Level numbers between 0 and 4 (e.g. new int[]{2,3})
	 * @throws Exception
	 *             If the level is not between 0 and 4.
	 */
	public void addGameLevel(String game, int[] levels) throws Exception {
		for (int level : levels) {
			addGameLevel(game, level);
		}
	}

	/**
	 * Remove the last configured added game level.
	 */
	public void removeLastGameLevel() {
		if (gameLevels.size() > 0) {
			gameLevels.remove(gameLevels.size() - 1);
		}
	}
	
	/**
	 * Clear all configured game levels to add new ones.
	 */
	public void clearGameLevels()
	{
		gameLevels.clear();
	}

	/**
	 * Get all the stored game level pairs.
	 * 
	 * @return
	 */
	public ArrayList<GameLevelPair<String, String[]>> getGameLevels() {
		return gameLevels;
	}

	/**
	 * Create the game path out of a game name.
	 * 
	 * @param game
	 *            The name of the game.
	 * @return The path of the game.
	 */
	public static String getGamePath(String game) {
		String gamePath;
		gamePath = gamesPath + game + ".txt";

		return gamePath;
	}

	/**
	 * Create the level paths out of the game name and the level numbers.
	 * 
	 * @param game
	 *            The name of the game.
	 * @param levels
	 *            The selected levels.
	 * @return The paths of the levels.
	 */
	public static String[] getGameLevelPaths(String game, String[] levels) {
		String[] levelPaths = new String[levels.length];
		for (int i = 0; i < levels.length; i++) {
			levelPaths[i] = getGameLevelPath(game, levels[i]);
		}
		return levelPaths;
	}

	/**
	 * Create a level path out of the game name and a level number.
	 * 
	 * @param game
	 *            The name of the game.
	 * @param levels
	 *            The selected levels.
	 * @return The paths of the levels.
	 */
	public static String getGameLevelPath(String game, String level) {
		return gamesPath + game + "_lvl" + level + ".txt";
	}

	/**
	 * Create paths for action recordings for a certain game, levels and number
	 * of repetitions. Creates a unique path for a recorded game using the game
	 * name, the level number, the repetition number and a time stamp.
	 * 
	 * @param game
	 *            The name of the game.
	 * @param levels
	 *            The levels.
	 * @return The paths for the action recordings.
	 */
	public String[] getRecordingPathsForGame(String game, String[] levels) {
		String[] actionFiles = new String[levels.length * repetitions];
		if (saveActions) {
			int actionIdx = 0;
			for (String level : levels) {
				for (int repetition = 0; repetition < repetitions; repetition++) {
					actionFiles[actionIdx++] = "actions_game_" + game + "_lvl_"
							+ level + "_r" + repetition + "_"
							+ getTimestampNow() + ".txt";
				}
			}
		}
		return actionFiles;
	}

	/**
	 * Get the timestamp string of the current time and date.
	 * 
	 * @return
	 */
	public static String getTimestampNow() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHmmss");
		return sdf.format(date);
	}

	private ArrayList<GameLevelPair<String, String[]>> gameLevels;

	/**
	 * The number of repetitions we run for each game.
	 */
	private int repetitions;

	/**
	 * The controller to test.
	 */
	private String controller;

	/**
	 * Whether we save the actions of the controller to file or not.
	 */
	private boolean saveActions;

	public boolean isSaveActions() {
		return saveActions;
	}

	public void setSaveActions(boolean saveActions) {
		this.saveActions = saveActions;
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public int getRepetitions() {
		return repetitions;
	}

	public void setRepetitions(int repetitions) {
		this.repetitions = repetitions;
	}
	
	public static PlayAllGamesRunConfig getPlayAllGamesConfig(){
		return new PlayAllGamesRunConfig();
	}
}
