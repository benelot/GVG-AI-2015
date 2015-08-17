package misc.runners;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import benchmarking.RunAllEasyHBFSLevelsRunConfig;
import benchmarking.RunAllEasyLevelsRunConfig;
import benchmarking.RunAllEasyMCTSLevelsRunConfig;
import benchmarking.RunAllGameLevelsRunConfig;
import benchmarking.RunAllHardLevelsRunConfig;
import benchmarking.RunAllHardMCTSLevelsRunConfig;
import benchmarking.RunAllMCTSLevelsRunConfig;

public class RunConfig {

	public RunConfig() {
		gameLevels = new ArrayList<GameLevelPair<String, String[]>>();
	}

	/**
	 * The path of the games.
	 */
	private final static String gamesPath = "examples/gridphysics/";

	/**
	 * CIG 2014 Training Set Games. These are the 10 games from the training set
	 * 1. This set was used as (the only) training set during the CIG 2014
	 * competition.
	 *
	 */
	public interface GamesTraining2014 {
		/**
		 * In this game you control a ship at the bottom of the screen shooting
		 * aliens that come from space. You better kill them all before they
		 * reach you!
		 */
		String ALIENS = "aliens";

		/**
		 * Your objective here is to move your player through a cave, collecting
		 * diamonds, before finding the exit. Beware of enemies that hide
		 * underground!
		 */
		String BOULDERDASH = "boulderdash";

		/**
		 * You are a happy butterfly hunter. This is how you live your life, and
		 * you like it. So be careful, you don't want them to become extinct!
		 */
		String BUTTERFLIES = "butterflies";

		/**
		 * You like to chase goats. And kill them. However, they usually don't
		 * like you to do it, so try not to get caught doing that!
		 */
		String CHASE = "chase";

		/**
		 * Why did the frog cross the road? Because there is a river at the
		 * other side. What would you cross the river as well? Because your home
		 * is there, and it's cosy.
		 */
		String FROGS = "frogs";

		/**
		 * Some missiles are being shot to cities in your country, you better
		 * destroy them before they reach them!
		 */
		String MISSILECOMMAND = "missilecommand";

		/**
		 * You control an avatar that needs to find the exit of a maze, but
		 * moving around is not so simple. Find the correct doors that take you
		 * to the exit!
		 */
		String PORTALS = "portals";

		/**
		 * In this puzzle you must push the boxes in the maze to make them fall
		 * through some holes. Be sure you push them properly!
		 */
		String SOKOBAN = "sokoban";

		/**
		 * How long can you survive before you become their main course for
		 * dinner? Hint: zombies don't like honey (didn't you know that?).
		 */
		String SURVIVEZOMBIES = "survivezombies";

		/**
		 * Get your way out of the dungeon infested with enemies. Remember to
		 * find the key that opens the door that leads you to freedom!
		 */
		String ZELDA = "zelda";
	};

	/**
	 * CIG 2014 Validation Set Games. These are the 10 games from the 2015
	 * training set 2. This set was used as validation game set for the CIG 2014
	 * competition.
	 *
	 */
	public interface GamesValidation2014 {
		/**
		 * The avatar must get to the finish line before any other camel does.
		 */
		String CAMELRACE = "camelRace";

		/**
		 * The avatar must collect all gems and gold coins in the cave, digging
		 * its way through it. There are also enemies in the level that kill the
		 * player on collision with him. Also, the player can shoot boulders by
		 * pressing USE two consecutive time steps, which kill enemies.
		 */
		String DIGDUG = "digdug";

		/**
		 * The avatar must find its way to the exit while avoiding the flames in
		 * the level, spawned by some portals from hell. The avatar can collect
		 * water in its way. One unit of water saves the avatar from one hit of
		 * a flame, but the game will be lost if flames touch the avatar and he
		 * has no water.
		 */
		String FIRESTORMS = "firestorms";

		/**
		 * The avatar can get infected by colliding with some bugs scattered
		 * around the level, or other animals that are infected (orange). The
		 * goal is to infect all healthy animals (green). Blue sprites are
		 * medics that cure infected animals and the avatar, but don't worry,
		 * they can be killed with your mighty sword.
		 */
		String INFECTION = "infection";

		/**
		 * The avatar must find its way to the exit by burning wooden boxes
		 * down. In order to be able to shoot, the avatar needs to collect
		 * ammunition (mana) scattered around the level. Flames spread, being
		 * able to destroy more than one box, but they can also hit the avatar.
		 * The avatar has health, that decreases when a flame touches him. If
		 * health goes down to 0, the player loses.
		 */
		String FIRECASTER = "firecaster";

		/**
		 * The avatar must reach the exit with a determined number of coins, but
		 * if the amount of collected coins is higher than a (different) number,
		 * the avatar is trapped when traversing marsh and the game finishes. In
		 * that case, the avatar may kill marsh sprites with the sword, if he
		 * collects it first.
		 */
		String OVERLOAD = "overload";

		/**
		 * The avatar must clear the maze by eating all pellets and power pills.
		 * There are ghosts that kill the player if he hasn't eaten a power pill
		 * when colliding (otherwise, the avatar kills the ghost). There are
		 * also fruit pieces that must be collected.
		 */
		String PACMAN = "pacman";

		/**
		 * The player controls a submarine that must avoid being killed by
		 * animals and rescue divers taking them to the surface. Also, the
		 * submarine must return to the surface regularly to collect more
		 * oxygen, or the avatar would lose. Submarine capacity is for 4 divers,
		 * and it can shoot torpedoes to the animals.
		 */
		String SEAQUEST = "seaquest";

		/**
		 * The avatar must collect moles that pop out of holes. There is also a
		 * cat in the level doing the same. If the cat collides with the player,
		 * this one loses the game.
		 */
		String WHACKAMOLE = "whackamole";

		/**
		 * There is a chicken at the top of the level throwing eggs down. The
		 * avatar must move from left to right to avoid eggs breaking on the
		 * floor. Only when the avatar has collected enough eggs, he can shoot
		 * at the chicken to win the game. If a single egg is broken, the player
		 * loses the game.
		 */
		String EGGOMANIA = "eggomania";
	};

	/**
	 * CIG 2015 Training Set Games. These games are all PUZZLES (= No NPCs).
	 *
	 */
	public interface GamesTraining2015 {
		/**
		 * The objective of this game is to reach the goal, collecting a key
		 * first. The player can push boxes around to open paths. There are
		 * holes in the ground that kill the player, but they can be filled with
		 * boxes (and both hole and box disappear). The player can also collect
		 * mushrooms that give points.
		 */
		String BAIT = "bait";

		/**
		 * The objective is to reach the goal. The avatar can push boxes (1 cell
		 * at a time) and boulders (roll until it hits another obstacle) around.
		 * Boulders can be used to fill holes that obstruct the player movement,
		 * and both boulders and boxes can be used to block lasers that kill the
		 * avatar.
		 */
		String BOLOADVENTURES = "boloadventures";

		/**
		 * Push keys into doors to open them, until the avatar reaches the exit.
		 * Many gems around are available for collection.
		 */
		String BRAINMAN = "brainman";

		/**
		 * The avatar must reach the exit in a maze where there are different
		 * types of doors: one door can only be open when a determined number of
		 * chips is collected. Other doors, of four different colours, can only
		 * be open if a determined resource (of the matching) colour has been
		 * collected. Finally, some surfaces (water, fire) can only be traversed
		 * (or the avatar dies) if specific boots (water boots, fire boots) have
		 * been picked up.
		 */
		String CHIPSCHALLENGE = "chipschallenge";

		/**
		 * The goal is to push a crate into a hole. The avatar can walk over two
		 * types of surfaces, and there is a unique point where the player can
		 * move from one to the other surface. The box can cross surfaces
		 * freely.
		 */
		String MODALITY = "modality";

		/**
		 * The avatar must paint in blue the whole traversable area (initially
		 * painted in grey). Each grid cell abandoned by the avatar switches
		 * colour from grey to blue, and vice-versa. Some positions can not be
		 * painted in any colour.
		 */
		String PAINTER = "painter";

		/**
		 * The avatar must find the exit of the level. It is armed with a gun
		 * that allows to open portals in the walls of the level. Two types of
		 * portals can be opened (entrance or exit portals), and the avatar must
		 * change its weapon in order to open one type or another. The avatar
		 * can travel through this portals, as well as shooting through them or
		 * pushing rocks. There can also be a key that opens locks in the way to
		 * the exit.
		 */
		String REALPORTALS = "realportals";

		/**
		 * The avatar must push boxes so they cover all holes in the level.
		 * Boxes that are on top of holes can still be moved (this is different
		 * from "Sokoban", from Training Set 1, where a box in a hole cannot be
		 * moved anymore). This forces that each box must be placed in a
		 * different target.
		 */
		String REALSOKOBAN = "realsokoban";

		/**
		 * The objective is to get to the exit of the level. Boxes can be pushed
		 * in rows, but boulders only one by one. Boxes and boulders fill
		 * different types of holes.
		 */
		String THECITADEL = "thecitadel";

		/**
		 * The player must paint completely a central area of the map. The
		 * avatar must be outside this area when everything is painted, and he
		 * cannot traverse those cells already painted.
		 */
		String ZENPUZZLE = "zenpuzzle";
	};

	/**
	 * GECCO 2015 Validation Set Games
	 *
	 */
	public interface GamesValidationGECCO2015 {

		/**
		 * Similar to Boulderdash, but the enemies can dig as well and chase the
		 * player. They also drop an additional gem every time a gem is
		 * consumed.
		 */
		String BOULDERCHASE = "boulderchase";

		/**
		 * The objective of the game is to reach the exit door to win. The
		 * avatar cannot step on ponds of water, but can jump over them using
		 * catapults. Each catapult can be used only once.
		 */
		String CATAPULTS = "catapults";

		/**
		 * The objective is to leave the level through the exit door, pushing
		 * away boxes that are in the way. This boxes can be destroyed by
		 * pushing them into holes, but those holes also kill the player if the
		 * avatar falls into them.
		 */
		String ESCAPE = "escape";

		/**
		 * Back to the bottom of the sea, you must shoot dangerous beasts (fish)
		 * to collect the resources they drop. At some point, a shark will
		 * appear that you cannot kill by shooting at him, but only touching him
		 * when you have collected a determined number of resources. Killing the
		 * shark gives considerably more points than the other animals, and the
		 * game ends when the player dies (defeat) or the time runs out
		 * (victory).
		 */
		String JAWS = "jaws";

		/**
		 * Simple game where the objective is to find the exit of the maze.
		 * Beware of some traps in the way, though.
		 */
		String LABYRINTH = "labyrinth";

		/**
		 * Lemmings are spawned from one door and try to get to the exit of the
		 * level. The problem is that there are many obstacles on the way, and
		 * the avatar must destroy these so the lemmings can reach the exit.
		 * There are traps as well that kill the lemmings if they fall into
		 * them. Score is given for every lemming that reaches the exit, but
		 * subtracted from every piece of wall destroyed (hence the game rewards
		 * those diggers that do less work - more optimally).
		 */
		String LEMMINGS = "lemmings";

		/**
		 * Emulating the Plants vs. Zombies game, this game consists of setting
		 * a farm full of plants that shoots peas to incoming zombies. These
		 * zombies can also shoot 'stuff' to kill the plants. The game ends if
		 * the time runs out with a victory, but you'd lose if at least a zombie
		 * reaches the defensive end.
		 */
		String PLANTS = "plants";

		/**
		 * Hamburguers and hotdogs are attacking your teeth. You must shoot them
		 * (ammo: fluoride, of course) in order to save at least one tooth (to
		 * keep eating more hamburguers and hotdogs). Damaged teeth can be
		 * repaired by the avatar upon contact. When all food items are
		 * destroyed, you win. If all your teeth are gone, you lose.
		 */
		String PLAQUEATTACK = "plaqueattack";

		/**
		 * In this game, the objective is to find and escape the maze through
		 * the final door. In between, you'll find monsters that can be killed
		 * with a sword that you can pick up, doors can be opens with
		 * collectible keys and gems and gold are available to be looted. There
		 * is also a market where you can exchange coins for health.
		 */
		String ROGUELIKE = "roguelike";

		/**
		 * This game can be ended whenever you want, using the USE action to win
		 * the game. You get a point for every move you are able to make. After
		 * moving, a non-traversable sprite is added in your previous location.
		 * There is also an enemy doing the same as you, who kills you in
		 * contact.
		 */
		String SURROUND = "surround";
	};

	/**
	 * EXTRA GAMES
	 *
	 */
	public interface GamesExtra {
		String SOLARFOX = "solarfox";
		String BOMBUZAL = "bombuzal";
	};

	public interface DeterministicGames {

		/**
		 * Your objective here is to move your player through a cave, collecting
		 * diamonds, before finding the exit. Beware of enemies that hide
		 * underground!
		 */
		String BOULDERDASH = "boulderdash";

		/**
		 * You control an avatar that needs to find the exit of a maze, but
		 * moving around is not so simple. Find the correct doors that take you
		 * to the exit!
		 */
		String PORTALS = "portals";

		/**
		 * In this puzzle you must push the boxes in the maze to make them fall
		 * through some holes. Be sure you push them properly!
		 */
		String SOKOBAN = "sokoban";

		/**
		 * Get your way out of the dungeon infested with enemies. Remember to
		 * find the key that opens the door that leads you to freedom!
		 */
		String ZELDA = "zelda";

		/**
		 * CIG 2014 Validation Set Games. These are the 10 games from the 2015
		 * training set 2. This set was used as validation game set for the CIG
		 * 2014 competition.
		 *
		 */
		/**
		 * The avatar must get to the finish line before any other camel does.
		 */
		String CAMELRACE = "camelRace";

		/**
		 * The avatar must collect all gems and gold coins in the cave, digging
		 * its way through it. There are also enemies in the level that kill the
		 * player on collision with him. Also, the player can shoot boulders by
		 * pressing USE two consecutive time steps, which kill enemies.
		 */
		String DIGDUG = "digdug";

		/**
		 * The avatar must find its way to the exit while avoiding the flames in
		 * the level, spawned by some portals from hell. The avatar can collect
		 * water in its way. One unit of water saves the avatar from one hit of
		 * a flame, but the game will be lost if flames touch the avatar and he
		 * has no water.
		 */
		String FIRESTORMS = "firestorms";

		/**
		 * The avatar must find its way to the exit by burning wooden boxes
		 * down. In order to be able to shoot, the avatar needs to collect
		 * ammunition (mana) scattered around the level. Flames spread, being
		 * able to destroy more than one box, but they can also hit the avatar.
		 * The avatar has health, that decreases when a flame touches him. If
		 * health goes down to 0, the player loses.
		 */
		String FIRECASTER = "firecaster";

		/**
		 * The avatar must reach the exit with a determined number of coins, but
		 * if the amount of collected coins is higher than a (different) number,
		 * the avatar is trapped when traversing marsh and the game finishes. In
		 * that case, the avatar may kill marsh sprites with the sword, if he
		 * collects it first.
		 */
		String OVERLOAD = "overload";

		/**
		 * CIG 2015 Training Set Games. These games are all PUZZLES (= No NPCs).
		 *
		 */
		/**
		 * The objective of this game is to reach the goal, collecting a key
		 * first. The player can push boxes around to open paths. There are
		 * holes in the ground that kill the player, but they can be filled with
		 * boxes (and both hole and box disappear). The player can also collect
		 * mushrooms that give points.
		 */
		String BAIT = "bait";

		/**
		 * The objective is to reach the goal. The avatar can push boxes (1 cell
		 * at a time) and boulders (roll until it hits another obstacle) around.
		 * Boulders can be used to fill holes that obstruct the player movement,
		 * and both boulders and boxes can be used to block lasers that kill the
		 * avatar.
		 */
		String BOLOADVENTURES = "boloadventures";

		/**
		 * Push keys into doors to open them, until the avatar reaches the exit.
		 * Many gems around are available for collection.
		 */
		String BRAINMAN = "brainman";

		/**
		 * The avatar must reach the exit in a maze where there are different
		 * types of doors: one door can only be open when a determined number of
		 * chips is collected. Other doors, of four different colours, can only
		 * be open if a determined resource (of the matching) colour has been
		 * collected. Finally, some surfaces (water, fire) can only be traversed
		 * (or the avatar dies) if specific boots (water boots, fire boots) have
		 * been picked up.
		 */
		String CHIPSCHALLENGE = "chipschallenge";

		/**
		 * The goal is to push a crate into a hole. The avatar can walk over two
		 * types of surfaces, and there is a unique point where the player can
		 * move from one to the other surface. The box can cross surfaces
		 * freely.
		 */
		String MODALITY = "modality";

		/**
		 * The avatar must paint in blue the whole traversable area (initially
		 * painted in grey). Each grid cell abandoned by the avatar switches
		 * colour from grey to blue, and vice-versa. Some positions can not be
		 * painted in any colour.
		 */
		String PAINTER = "painter";

		/**
		 * The avatar must find the exit of the level. It is armed with a gun
		 * that allows to open portals in the walls of the level. Two types of
		 * portals can be opened (entrance or exit portals), and the avatar must
		 * change its weapon in order to open one type or another. The avatar
		 * can travel through this portals, as well as shooting through them or
		 * pushing rocks. There can also be a key that opens locks in the way to
		 * the exit.
		 */
		String REALPORTALS = "realportals";

		/**
		 * The avatar must push boxes so they cover all holes in the level.
		 * Boxes that are on top of holes can still be moved (this is different
		 * from "Sokoban", from Training Set 1, where a box in a hole cannot be
		 * moved anymore). This forces that each box must be placed in a
		 * different target.
		 */
		String REALSOKOBAN = "realsokoban";

		/**
		 * The objective is to get to the exit of the level. Boxes can be pushed
		 * in rows, but boulders only one by one. Boxes and boulders fill
		 * different types of holes.
		 */
		String THECITADEL = "thecitadel";

		/**
		 * The player must paint completely a central area of the map. The
		 * avatar must be outside this area when everything is painted, and he
		 * cannot traverse those cells already painted.
		 */
		String ZENPUZZLE = "zenpuzzle";

		/**
		 * GECCO 2015 Validation Set Games
		 *
		 */

		/**
		 * The objective of the game is to reach the exit door to win. The
		 * avatar cannot step on ponds of water, but can jump over them using
		 * catapults. Each catapult can be used only once.
		 */
		String CATAPULTS = "catapults";

		/**
		 * The objective is to leave the level through the exit door, pushing
		 * away boxes that are in the way. This boxes can be destroyed by
		 * pushing them into holes, but those holes also kill the player if the
		 * avatar falls into them.
		 */
		String ESCAPE = "escape";

		/**
		 * Simple game where the objective is to find the exit of the maze.
		 * Beware of some traps in the way, though.
		 */
		String LABYRINTH = "labyrinth";

		/**
		 * Lemmings are spawned from one door and try to get to the exit of the
		 * level. The problem is that there are many obstacles on the way, and
		 * the avatar must destroy these so the lemmings can reach the exit.
		 * There are traps as well that kill the lemmings if they fall into
		 * them. Score is given for every lemming that reaches the exit, but
		 * subtracted from every piece of wall destroyed (hence the game rewards
		 * those diggers that do less work - more optimally).
		 */
		String LEMMINGS = "lemmings";

		/**
		 * In this game, the objective is to find and escape the maze through
		 * the final door. In between, you'll find monsters that can be killed
		 * with a sword that you can pick up, doors can be opens with
		 * collectible keys and gems and gold are available to be looted. There
		 * is also a market where you can exchange coins for health.
		 */
		String ROGUELIKE = "roguelike";

		/**
		 * This game can be ended whenever you want, using the USE action to win
		 * the game. You get a point for every move you are able to make. After
		 * moving, a non-traversable sprite is added in your previous location.
		 * There is also an enemy doing the same as you, who kills you in
		 * contact.
		 */
		String SURROUND = "surround";

		/**
		 * EXTRA GAMES
		 *
		 */
		String SOLARFOX = "solarfox";
		String BOMBUZAL = "bombuzal";

	}

	public interface StochasticGames {

		/**
		 * In this game you control a ship at the bottom of the screen shooting
		 * aliens that come from space. You better kill them all before they
		 * reach you!
		 */
		String ALIENS = "aliens";

		/**
		 * Your objective here is to move your player through a cave, collecting
		 * diamonds, before finding the exit. Beware of enemies that hide
		 * underground!
		 */
		String BOULDERDASH = "boulderdash";

		/**
		 * You are a happy butterfly hunter. This is how you live your life, and
		 * you like it. So be careful, you don't want them to become extinct!
		 */
		String BUTTERFLIES = "butterflies";

		/**
		 * You like to chase goats. And kill them. However, they usually don't
		 * like you to do it, so try not to get caught doing that!
		 */
		String CHASE = "chase";

		/**
		 * Why did the frog cross the road? Because there is a river at the
		 * other side. What would you cross the river as well? Because your home
		 * is there, and it's cosy.
		 */
		String FROGS = "frogs";

		/**
		 * Some missiles are being shot to cities in your country, you better
		 * destroy them before they reach them!
		 */
		String MISSILECOMMAND = "missilecommand";

		/**
		 * How long can you survive before you become their main course for
		 * dinner? Hint: zombies don't like honey (didn't you know that?).
		 */
		String SURVIVEZOMBIES = "survivezombies";

		/**
		 * Get your way out of the dungeon infested with enemies. Remember to
		 * find the key that opens the door that leads you to freedom!
		 */
		String ZELDA = "zelda";

		/**
		 * CIG 2014 Validation Set Games. These are the 10 games from the 2015
		 * training set 2. This set was used as validation game set for the CIG
		 * 2014 competition.
		 *
		 */

		/**
		 * The avatar must collect all gems and gold coins in the cave, digging
		 * its way through it. There are also enemies in the level that kill the
		 * player on collision with him. Also, the player can shoot boulders by
		 * pressing USE two consecutive time steps, which kill enemies.
		 */
		String DIGDUG = "digdug";

		/**
		 * The avatar must find its way to the exit while avoiding the flames in
		 * the level, spawned by some portals from hell. The avatar can collect
		 * water in its way. One unit of water saves the avatar from one hit of
		 * a flame, but the game will be lost if flames touch the avatar and he
		 * has no water.
		 */
		String FIRESTORMS = "firestorms";

		/**
		 * The avatar can get infected by colliding with some bugs scattered
		 * around the level, or other animals that are infected (orange). The
		 * goal is to infect all healthy animals (green). Blue sprites are
		 * medics that cure infected animals and the avatar, but don't worry,
		 * they can be killed with your mighty sword.
		 */
		String INFECTION = "infection";

		/**
		 * The avatar must reach the exit with a determined number of coins, but
		 * if the amount of collected coins is higher than a (different) number,
		 * the avatar is trapped when traversing marsh and the game finishes. In
		 * that case, the avatar may kill marsh sprites with the sword, if he
		 * collects it first.
		 */
		String OVERLOAD = "overload";

		/**
		 * The avatar must clear the maze by eating all pellets and power pills.
		 * There are ghosts that kill the player if he hasn't eaten a power pill
		 * when colliding (otherwise, the avatar kills the ghost). There are
		 * also fruit pieces that must be collected.
		 */
		String PACMAN = "pacman";

		/**
		 * The player controls a submarine that must avoid being killed by
		 * animals and rescue divers taking them to the surface. Also, the
		 * submarine must return to the surface regularly to collect more
		 * oxygen, or the avatar would lose. Submarine capacity is for 4 divers,
		 * and it can shoot torpedoes to the animals.
		 */
		String SEAQUEST = "seaquest";

		/**
		 * The avatar must collect moles that pop out of holes. There is also a
		 * cat in the level doing the same. If the cat collides with the player,
		 * this one loses the game.
		 */
		String WHACKAMOLE = "whackamole";

		/**
		 * There is a chicken at the top of the level throwing eggs down. The
		 * avatar must move from left to right to avoid eggs breaking on the
		 * floor. Only when the avatar has collected enough eggs, he can shoot
		 * at the chicken to win the game. If a single egg is broken, the player
		 * loses the game.
		 */
		String EGGOMANIA = "eggomania";

		/**
		 * CIG 2015 Training Set Games. These games are all PUZZLES (= No NPCs).
		 *
		 */
		
		/**
		 * GECCO 2015 Validation Set Games
		 *
		 */
		/**
		 * Similar to Boulderdash, but the enemies can dig as well and chase the
		 * player. They also drop an additional gem every time a gem is
		 * consumed.
		 */
		String BOULDERCHASE = "boulderchase";

		/**
		 * Back to the bottom of the sea, you must shoot dangerous beasts (fish)
		 * to collect the resources they drop. At some point, a shark will
		 * appear that you cannot kill by shooting at him, but only touching him
		 * when you have collected a determined number of resources. Killing the
		 * shark gives considerably more points than the other animals, and the
		 * game ends when the player dies (defeat) or the time runs out
		 * (victory).
		 */
		String JAWS = "jaws";

		/**
		 * Lemmings are spawned from one door and try to get to the exit of the
		 * level. The problem is that there are many obstacles on the way, and
		 * the avatar must destroy these so the lemmings can reach the exit.
		 * There are traps as well that kill the lemmings if they fall into
		 * them. Score is given for every lemming that reaches the exit, but
		 * subtracted from every piece of wall destroyed (hence the game rewards
		 * those diggers that do less work - more optimally).
		 */
		String LEMMINGS = "lemmings";

		/**
		 * Emulating the Plants vs. Zombies game, this game consists of setting
		 * a farm full of plants that shoots peas to incoming zombies. These
		 * zombies can also shoot 'stuff' to kill the plants. The game ends if
		 * the time runs out with a victory, but you'd lose if at least a zombie
		 * reaches the defensive end.
		 */
		String PLANTS = "plants";

		/**
		 * Hamburguers and hotdogs are attacking your teeth. You must shoot them
		 * (ammo: fluoride, of course) in order to save at least one tooth (to
		 * keep eating more hamburguers and hotdogs). Damaged teeth can be
		 * repaired by the avatar upon contact. When all food items are
		 * destroyed, you win. If all your teeth are gone, you lose.
		 */
		String PLAQUEATTACK = "plaqueattack";

		/**
		 * In this game, the objective is to find and escape the maze through
		 * the final door. In between, you'll find monsters that can be killed
		 * with a sword that you can pick up, doors can be opens with
		 * collectible keys and gems and gold are available to be looted. There
		 * is also a market where you can exchange coins for health.
		 */
		String ROGUELIKE = "roguelike";

		/**
		 * This game can be ended whenever you want, using the USE action to win
		 * the game. You get a point for every move you are able to make. After
		 * moving, a non-traversable sprite is added in your previous location.
		 * There is also an enemy doing the same as you, who kills you in
		 * contact.
		 */
		String SURROUND = "surround";

		/**
		 * EXTRA GAMES
		 *
		 */
		String SOLARFOX = "solarfox";
		String BOMBUZAL = "bombuzal";

	}

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
	 * @param value
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
	public void clearGameLevels() {
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
	private int repetitions = 1;

	/**
	 * The controller to test.
	 */
	private String controller;

	/**
	 * Whether we save the actions of the controller to file or not.
	 */
	private boolean saveActions = false;

	/**
	 * Whether we calculate statistics of the controller or not.
	 */
	private boolean calculateStatistics = false;

	public boolean isCalculateStatistics() {
		return calculateStatistics;
	}

	public void setCalculateStatistics(boolean calculateStatistics) {
		this.calculateStatistics = calculateStatistics;
	}

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

	// benchmarks
	public static PlayAllGamesRunConfig getPlayAllGamesRunConfig() {
		return new PlayAllGamesRunConfig();
	}

	public static RunAllEasyLevelsRunConfig getEasyLevelsRunConfig() {
		return new RunAllEasyLevelsRunConfig();
	}

	public static RunAllGameLevelsRunConfig getAllLevelsRunConfig() {
		return new RunAllGameLevelsRunConfig();
	}

	public static RunAllHardLevelsRunConfig getHardLevelsRunConfig() {
		return new RunAllHardLevelsRunConfig();
	}
	
	public static RunAllEasyHBFSLevelsRunConfig getEasyHBFSLevelsRunConfig() {
		return new RunAllEasyHBFSLevelsRunConfig();
	}

	public static RunAllGameLevelsRunConfig getHBFSLevelsRunConfig() {
		return new RunAllGameLevelsRunConfig();
	}

	public static RunAllHardLevelsRunConfig getHardHBFSLevelsRunConfig() {
		return new RunAllHardLevelsRunConfig();
	}
	
	public static RunAllEasyMCTSLevelsRunConfig getEasyMCTSLevelsRunConfig() {
		return new RunAllEasyMCTSLevelsRunConfig();
	}

	public static RunAllMCTSLevelsRunConfig getMCTSLevelsRunConfig() {
		return new RunAllMCTSLevelsRunConfig();
	}

	public static RunAllHardMCTSLevelsRunConfig getHardMCTSLevelsRunConfig() {
		return new RunAllHardMCTSLevelsRunConfig();
	}
}
