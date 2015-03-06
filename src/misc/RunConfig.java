package misc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RunConfig {

	public RunConfig() {
		gameLevels = new ArrayList<GameLevelPair<String, String[]>>();
	}

	// Available games:
	private final static String gamesPath = "examples/gridphysics/";

	// CIG 2014 Training Set Games
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

	// CIG 2014 Validation Set Games
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

	// CIG 2015 New Training Set Games
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

	// EXTRA GAMES:
	public interface GamesExtra {
		String SOLARFOX = "solarfox";
		String BOMBUZAL = "bombuzal";
	};

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

	public void addGameLevel(String game, int[] levels) throws Exception {
		for (int level : levels) {
			addGameLevel(game, level);
		}

	}

	public ArrayList<GameLevelPair<String, String[]>> getGameLevels() {
		return gameLevels;
	}

	public static String getGamePath(String game) {
		String gamePath;
		gamePath = gamesPath + game + ".txt";

		return gamePath;
	}

	public static String[] getGameLevelPaths(String game, String[] levels) {
		String[] levelPaths = new String[levels.length];
		for (int i = 0; i < levels.length; i++) {
			levelPaths[i] = getGameLevelPath(game, levels[i]);
		}
		return levelPaths;
	}

	public static String getGameLevelPath(String game, String level) {
		return gamesPath + game + "_lvl" + level + ".txt";
	}

	public String[] getRecordingsForGame(String game, String[] levels) {
		String[] actionFiles = new String[levels.length * repetitions];
		if (saveActions) {
			int actionIdx = 0;
			for (String level : levels) {
				for (int repetition = 0; repetition < repetitions; repetition++) {
					actionFiles[actionIdx++] = "actions_game_"
							+ game + "_lvl_" + level + "_r"
							+ repetition + "_" + getTimestampNow() + ".txt";
				}
			}
		}
		return actionFiles;
	}

	public static String getTimestampNow() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHmmss");
		return sdf.format(date);
	}

	private ArrayList<GameLevelPair<String, String[]>> gameLevels;

	private int repetitions;
	private String controller;

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

	// 1. This starts a game, in a level, played by a human.
	// ArcadeMachine.playOneGame(game, level1, recordActionsFile, seed);

	// 2. This plays a game in a level by the controller.
	// ArcadeMachine.runOneGame(game, level1, visuals, sampleMCTSController,
	// recordActionsFile, seed);
	// ArcadeMachine.runOneGame(game, level1, visuals, tester,
	// recordActionsFile, seed);

	// 3. This replays a game from an action file previously recorded
	// String readActionsFile = "actionsFile_aliens_lvl0.txt"; //This example is
	// for
	// ArcadeMachine.replayGame(game, level1, visuals, readActionsFile);

	// 4. This plays a single game, in N levels, M times :
	// String level2 = gamesPath + games[gameIdx] + "_lvl" + 1 +".txt";
	// int M = 3;
	// ArcadeMachine.runGames(game, new String[]{level1, level2}, M,
	// sampleMCTSController, null);

	// 5. This plays N games, in the first L levels, M times each. Actions to
	// file optional (set saveActions to true).
	/*
	 * int N = 10, L = 5, M = 2; boolean saveActions = false; String[] levels =
	 * new String[L]; String[] actionFiles = new String[L*M]; for(int i = 0; i <
	 * N; ++i) { int actionIdx = 0; game = gamesPath + games[i] + ".txt";
	 * for(int j = 0; j < L; ++j){ levels[j] = gamesPath + games[i] + "_lvl" + j
	 * +".txt"; if(saveActions) for(int k = 0; k < M; ++k)
	 * actionFiles[actionIdx++] = "actions_game_" + i + "_level_" + j + "_" + k
	 * + ".txt"; } ArcadeMachine.runGames(game, levels, M, sampleMCTSController,
	 * saveActions? actionFiles:null); }
	 */
}
