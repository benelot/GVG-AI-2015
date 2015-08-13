package agents.misc.pathplanning;

import java.util.ArrayList;
import java.util.Stack;

import agents.misc.PersistentStorage;
import bladeRunner.Agent;
import ontology.Types;
import ontology.Types.ACTIONS;

/**
 * Node Class Computes hashcodes and heuristic.
 * 
 * @see {@link PathPlanner} for details.
 */
public class PathPlannerNode implements Comparable<PathPlannerNode> {

	public Types.ACTIONS causingAction;
	public PathPlannerNode parent;
	public int depth;

	public int x, y;

	private double score = -1;
	private int hash = -1;

	public double distanceFromStart = 0;
	public double fullDistance = 0;

	// Default constructor
	public PathPlannerNode() {

	}

	public PathPlannerNode(int depth, int x, int y) {
		super();
		this.depth = 0;
		this.x = x;
		this.y = y;

	}

	public PathPlannerNode(Types.ACTIONS causingAction, PathPlannerNode parent, int depth) {
		super();
		this.causingAction = causingAction;
		this.parent = parent;
		this.depth = depth;
		switch (causingAction) {
		case ACTION_DOWN:
			this.x = parent.x;
			this.y = parent.y + 1;
			break;
		case ACTION_ESCAPE:
			this.x = parent.x;
			this.y = parent.y;
			break;
		case ACTION_LEFT:
			this.x = parent.x - 1;
			this.y = parent.y;
			break;
		case ACTION_NIL:
			this.x = parent.x;
			this.y = parent.y;
			break;
		case ACTION_RIGHT:
			this.x = parent.x + 1;
			this.y = parent.y;
			break;
		case ACTION_UP:
			this.x = parent.x;
			this.y = parent.y - 1;
			break;
		case ACTION_USE:
			this.x = parent.x;
			this.y = parent.y;
			break;
		default:
			break;

		}
	}

	ArrayList<PathPlannerNode> getNeighbors() {
		ArrayList<PathPlannerNode> neighbors = new ArrayList<>();

		if (!PersistentStorage.adjacencyMap.isObstacle(x + 1, y)) {
			neighbors.add(new PathPlannerNode(Types.ACTIONS.ACTION_RIGHT, this, depth + 1));
		}

		if (!PersistentStorage.adjacencyMap.isObstacle(x, y + 1)) {
			neighbors.add(new PathPlannerNode(Types.ACTIONS.ACTION_DOWN, this, depth + 1));

		}

		if (!PersistentStorage.adjacencyMap.isObstacle(x - 1, y)) {
			neighbors.add(new PathPlannerNode(Types.ACTIONS.ACTION_LEFT, this, depth + 1));
		}

		if (!PersistentStorage.adjacencyMap.isObstacle(x, y - 1)) {
			neighbors.add(new PathPlannerNode(Types.ACTIONS.ACTION_UP, this, depth + 1));
		}
		return neighbors;
	}

	// Overloaded constructor
	public PathPlannerNode(PathPlannerNode other) {
		this.x = other.x;
		this.y = other.y;
		this.causingAction = other.causingAction;
		this.depth = other.depth;
		this.hash = other.hash;
		this.score = other.score;
	}

	@Override
	public boolean equals(Object obj) {
		PathPlanner.equalCalls++;
		if (Agent.isVerbose) {
			// System.out.print(".");
		}
		if (hashCode() != obj.hashCode())
			return false;

		if (PathPlanner.TRACK_HASHING)
			PathPlanner.hashesEqual++;

		return true;
	}

	double getScore() {
		return 0;

	}

	public ArrayList<Types.ACTIONS> getActionSequence() {
		ArrayList<Types.ACTIONS> seq = new ArrayList<Types.ACTIONS>();
		PathPlannerNode current = this;
		while (true) {
			if (current.causingAction != null) {
				seq.add(current.causingAction);
			}
			if (current.parent != null) {
				current = current.parent;
			} else {
				break;
			}
		}
		return seq;
	}

	public void displayActionSequence() {
		ArrayList<ACTIONS> s = getActionSequence();
		if (Agent.isVerbose) {
			System.out.print("Actions: ");
		}
		for (Types.ACTIONS a : s) {
			if (Agent.isVerbose) {
				System.out.print(a + ";");
			}
		}
		if (Agent.isVerbose) {
			System.out.println();
		}
	}

	// Override the CompareTo function for the HashMap usage
	@Override
	public int hashCode() {
		return this.x + 34245 * this.y;
	}

	@Override
	public int compareTo(PathPlannerNode o) {
		return (hashCode() == o.hashCode()) ? 1 : 0;
	}
}