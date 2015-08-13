package agents.misc.pathplanning;

import java.util.ArrayList;
import java.util.PriorityQueue;

import bladeRunner.Agent;
import ontology.Types;

/**
 * An implementation of A*
 * 
 * @author Benjamin Ellenberger
 *
 */
public class PathPlanner {

	public static int INITIAL_PIPE_LENGTH = 200;
	public static int INITIAL_REJECTION_SET_SIZE = 200;
	//
	public static final boolean IS_VERY_VERBOSE = true;
	public static final boolean TRACK_HASHING = false;

	public static int compareCalls = 0;
	public static int equalCalls = 0;
	public static int hashCollisions = 0;
	public static int hashesEqual = 0;
	//
	public static PriorityQueue<PathPlannerNode> pipe = new PriorityQueue<PathPlannerNode>(INITIAL_PIPE_LENGTH);;
	public static ArrayList<PathPlannerNode> visited = new ArrayList<PathPlannerNode>(INITIAL_REJECTION_SET_SIZE);
	public static PathPlannerNode hbfsRoot = null;
	//
	public int stats_rejects = 0;
	public int stats_nonUseful = 0;
	public static int turnAroundSpeed = -1;
	public int pipeEmptyEvents = 0;

	public boolean hasTimedOut = false;

	private static int goalX = 0;
	private static int goalY = 0;
	private static int startX = 0;
	private static int startY = 0;

	public static boolean stopAtStart = false;

	public static boolean startFound = false;

	private static void cleanHbfs() {
		pipe.clear();
		visited.clear();
		hbfsRoot = null;
	}

	public static void updateStart(int startX, int startY) {
		PathPlanner.startX = startX;
		PathPlanner.startY = startY;
	}

	public static void updateGoal(int goalX, int goalY) {
		PathPlanner.goalX = goalX;
		PathPlanner.goalY = goalY;
	}

	public Types.ACTIONS whereToGoNext(int x, int y) {
		for (PathPlannerNode n : visited) {
			if (n.x == x && n.y == y) {
				return n.actionToParent;
			}
		}
		return Types.ACTIONS.ACTION_NIL;
	}

	public static double getDistance(int x, int y) {
		double highestDistance = 0;
		for (PathPlannerNode n : visited) {
			highestDistance = (highestDistance < n.getDistanceFromStart()) ? n.getDistanceFromStart() : highestDistance;
			if (n.x == x && n.y == y) {
				return n.getDistanceFromStart();
			}
		}
		return highestDistance + 1;
	}

	public static ArrayList<Types.ACTIONS> getPath(int x, int y) {
		ArrayList<Types.ACTIONS> path = new ArrayList<>();
		for (PathPlannerNode n : visited) {
			if (n.x == x && n.y == y) {
				path = n.getActionSequence();
			}
		}

		return path;
	}

	private static boolean performHbfs() {

		if (pipe.isEmpty()) {
			return true;
		}

		// get the first Node from non-searched Node list, sorted by lowest
		// distance from our goal as guessed by our heuristic
		PathPlannerNode current = pipe.remove();

		// check if our current Node location is the start node. If it is, we
		// are done.
		if (current.x == startX && current.y == startY) {
			if (Agent.isVerbose) {
				System.out.println();
				System.out.println("PathHBFS::Goal found.");
			}
			startFound = true;
		}
		if (stopAtStart && startFound) {
			return true;
		}

		// move current node to the closed (already searched) list
		visited.add(current);

		// go through all the current node neighbors and calculate if one should
		// be our next step
		for (PathPlannerNode neighbor : current.getNeighbors()) {

			// calculate how long the path is if we chose this neighbor as the
			// next step in the path
			double neighborDistanceFromStart = current.getDistanceFromStart() + 1;
			double totalDistance = neighborDistanceFromStart + euclidianDistance(neighbor, startX, startY);

			// if child node has been evaluated and the newer fullDistance is
			// higher, skip
			int i = visited.indexOf(neighbor);
			if (i != -1) {
				neighbor = visited.get(i);
				if (totalDistance >= neighbor.getTotalDistance()) {
					continue;
				}
			}

			for (PathPlannerNode n : pipe) {
				if (n.equals(neighbor)) {
					neighbor = n;
				}
			}

			// if child node is not in queue or new fullDistance is lower
			if ((!pipe.contains(neighbor)) || (totalDistance < neighbor.getTotalDistance())) {

				neighbor.parent = current;
				neighbor.setDistanceFromStart(neighborDistanceFromStart);
				neighbor.setTotalDistance(totalDistance);

				if (!pipe.contains(neighbor)) {
					pipe.add(neighbor);
					if (Agent.isVerbose) {
						System.out.print(".");
					}
				}

			}
		}

		if (Agent.isVerbose) {
			System.out.print("|");
		}
		return false;

	}

	public static void displayPathState() {
		displayPathState(null);
	}

	public static void displayPathState(PathPlannerNode node) {
		if (node == null) {
			node = pipe.peek();
		}
		if (node == null) {
			if (Agent.isVerbose) {
				System.out.println("PathHBFS::#Pipe Empty");
				System.out.format("PathHBFS::Pipe:%5d|R.Set:%5d|LongestDistance:%3.2f|Speed:%3d", pipe.size(), visited.size(),
						getDistance(0, 0)-1, turnAroundSpeed);
			}
			return;
		}
		if (Agent.isVerbose) {
			System.out.println();
			System.out.format("PathHBFS::Pipe:%5d|R.Set:%5d|Depth:%3d|TotDistance:%3.2f|Speed:%3d", pipe.size(), visited.size(),
					node.depth, node.getTotalDistance(), turnAroundSpeed);
		}
	}

	/**
	 * Picks an action. This function is called every game step to request an
	 * action from the player.
	 * 
	 * @param so
	 *            Observation of the current state.
	 * @param elapsedTimer
	 *            Timer when the action returned is due.
	 * @return An action for the current state
	 */
	public static void updateWays() {
		cleanHbfs();

		hbfsRoot = new PathPlannerNode(0, goalX, goalY);
		hbfsRoot.setDistanceFromStart(0);
		hbfsRoot.setTotalDistance(euclidianDistance(hbfsRoot, startX, startY));

		pipe.add(hbfsRoot);

		boolean hasTerminated = false;
		turnAroundSpeed = 0;
		while (!hasTerminated) {
			hasTerminated = performHbfs();
			turnAroundSpeed += 1;
		}
		System.out.println();
		displayPathState();
		System.out.println();
	}

	/**
	 * Euclidean cost between state a and state b
	 */
	@SuppressWarnings("unused")
	private static double euclidianDistance(PathPlannerNode a, PathPlannerNode b) {
		float x = a.x - b.x;
		float y = a.y - b.y;
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Euclidean cost between state a and and a position
	 */
	private static double euclidianDistance(PathPlannerNode a, int goalX, int goalY) {
		float x = a.x - goalX;
		float y = a.y - goalY;
		return Math.sqrt(x * x + y * y);
	}
}
