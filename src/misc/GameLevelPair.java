package misc;

/**
 * A data structure to store the name of the game and the selected levels of that
 * game.
 * 
 * @author Benjamin Ellenberger
 *
 * @param <FIRST>
 * @param <SECOND>
 */
public class GameLevelPair<FIRST, SECOND> implements
		Comparable<GameLevelPair<FIRST, SECOND>> {

	public final FIRST game;
	public final SECOND level;

	GameLevelPair(FIRST first, SECOND second) {
		this.game = first;
		this.level = second;
	}

	public static <FIRST, SECOND> GameLevelPair<FIRST, SECOND> of(FIRST first,
			SECOND second) {
		return new GameLevelPair<FIRST, SECOND>(first, second);
	}

	@Override
	public int compareTo(GameLevelPair<FIRST, SECOND> o) {
		int cmp = compare(game, o.game);
		return cmp == 0 ? compare(level, o.level) : cmp;
	}

	// todo move this to a helper class.
	@SuppressWarnings("unchecked")
	private static int compare(Object o1, Object o2) {
		return o1 == null ? o2 == null ? 0 : -1 : o2 == null ? +1
				: ((Comparable<Object>) o1).compareTo(o2);
	}

	@Override
	public int hashCode() {
		return 31 * hashcode(game) + hashcode(level);
	}

	// todo move this to a helper class.
	private static int hashcode(Object o) {
		return o == null ? 0 : o.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GameLevelPair))
			return false;
		if (this == obj)
			return true;
		return equal(game, ((GameLevelPair<?, ?>) obj).game)
				&& equal(level, ((GameLevelPair<?, ?>) obj).level);
	}

	// todo move this to a helper class.
	private boolean equal(Object o1, Object o2) {
		return o1 == null ? o2 == null : (o1 == o2 || o1.equals(o2));
	}

	@Override
	public String toString() {
		return "(" + game + ", " + level + ')';
	}
}
