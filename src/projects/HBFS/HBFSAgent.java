package projects.HBFS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import core.game.Event;
import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import misc.GameRunner;
import ontology.Types;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

// Heuristic Breadth First Search
// 
// - Paths are scored by a heuristic:
//   - It is a weighted sum of 
//   	+ depth
//   	- how many events have been created 
//   	- 1.75^(how many different tile interactions have been seen)
//   	- how did the total number of tiles change (positive for decrease)
//   	(depth has a positive weight, the other 3 weights are negative)
// - Paths with minimal values of the heuristic are considered for expansion
//   (Paths under consideration are stored in the pipe)
//  
// - Loops are prevented by keeping a hash set of visited states (visited)
//   Hash codes for the StateObservation are computed via 
//	 Rotating hash for sequences of small values:
//   http://burtleburtle.net/bob/hash/doobs.html
//   (heuristics and hashing in BFSNode)
//
//   Both pipe and rejection Set are cleared once they reach a limit number of 
//   elements to prevent stalling and eventual out of memory errors
//
//   Puzzle Style Games: * (see HBFSRunner) work with HBFS, heuristic parameters wT = -3; wL = -2;
// - When running two different games in a row, errors can occur. The controller gets reset, so it is currently unclear why this happens. 
//   As a workaround Run only blocks of the same game only.
// - In some games the forward model does not seem to work properly. E.g. in BOLOADVENTURES (level 1), 
//   an initial move to the left is not reflected in the updated StateObservation (see comments in HBFSAgent.initializeBfs(StateObservation so))
//
// @author Sepp Kollmorgen


public class HBFSAgent extends AbstractPlayer {
	
	public static final int STATE_PLANNING = 1;
	public static final int STATE_ACTING = 2;
	public static final int STATE_IDLE = 3;
	public static final int STATE_OTHER = 4;
		
	public static int NUM_ACTIONS;
	public static int INITIALIZATION_REMTIME = 25;
	public static int ACTION_REMTIME = 10;
	public static int INITIALIZATION_ITEMS_PER_ROUND = 1;
	public static int ACTION_ITEMS_PER_ROUND = 1;
	public static int MAX_TICKS = 1800;
	public static int MAX_PIPE_LENGTH = 10000;
	public static int MAX_REJECTION_SET_SIZE = 10000;
	public static int CARRY_OVER_PIPE_LENGTH = 2000;
	
	public static boolean isVerbose = false;
	public static int reportFrequency = 25;
	
	public static Types.ACTIONS[] ACTIONS;

	public static int compareCalls = 0;
	public static int equalCalls = 0;
	public static int rootLoad = -1;
	public static double correspondingScore = Double.NEGATIVE_INFINITY;
	public static double maxScoreDifference = Double.NEGATIVE_INFINITY;
	public static final int prime = 32353; //18097; //4583; 
	public static final int callReportFrequency = 300;
	public static final double wLoad = -2; // -4
	public static final double wPosition = 0;
	public static final double wTileDiversity = -3; // -2
	public static final double wEvents = -0.1;
	public static final double wDepth = 1;
	
	/* Node Class
	   Computes hashcodes and heuristic.
	 */
	public static class HBFSNode implements Comparable<HBFSNode> {

		public StateObservation so;
		public Types.ACTIONS causingAction;
		public HBFSNode parent;
		public int depth;

		private double score = -1;
		private double eventScore = -1;
		private double tileDiversityScore = -1;
		private double loadScore = -1;
		private int totalLoad = -1;
		private int hash = -1;

		public HBFSNode(StateObservation so, Types.ACTIONS causingAction, HBFSNode parent, int depth) {
			super();
			this.so = so;
			this.causingAction = causingAction;
			this.parent = parent;
			this.depth = depth;
		}

		// Computes the heuristic score for this path
		// It is a weighted sum of 
		// + depth
		// - how many events have been created 
		// - 1.75^(how many different tile interactions have been seen)
		// - how did the total number of tiles change (positive for decrease)
		//
		// depth has a positive weight, the other 3 weights are negative
		// paths with minimal values of the heuristic are considered for expansion (see HBFSAgent)
		public double scoreDfsNode(HBFSNode arg0) {

			loadScore = HBFSAgent.rootLoad - arg0.getLoad();
			Set<IntPair> typeIds = new TreeSet<IntPair>();
			eventScore = 0;
			for (Event ev : arg0.so.getEventsHistory()) {
				eventScore += scoreEvent(ev);
				typeIds.add(new IntPair(ev.activeTypeId, ev.passiveTypeId));
			}
			tileDiversityScore = Math.pow(1.75, typeIds.size());

			double positionScore = 0;

			return HBFSAgent.wDepth*arg0.depth + HBFSAgent.wEvents*eventScore + + HBFSAgent.wTileDiversity*tileDiversityScore + HBFSAgent.wPosition*positionScore + HBFSAgent.wLoad*loadScore;
		}

		//  Computes hash code for the StateObservation. Used to organize the list of visited states.
		//	Rotating hash for sequences of small values:
		//  http://burtleburtle.net/bob/hash/doobs.html
		public int getHash() {
			int sequenceLength = so.getWorldDimension().height*so.getWorldDimension().width + 2;
			ArrayList<Observation>[][] grid = so.getObservationGrid();
			totalLoad = 0;
			hash = sequenceLength;
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					for (Observation o : grid[i][j]) {
						hash = (hash<<4)^(hash>>28)^o.itype;
					}
					totalLoad += grid[i][j].size();
				}
			}
			hash = (hash << 4)^(hash>>28)^((int) so.getAvatarPosition().x);
			hash = (hash << 4)^(hash>>28)^((int) so.getAvatarPosition().y);
			hash = hash % HBFSAgent.prime;
			return hash;
		}

		public double getScore() {
			if (score != -1) {
				return score;
			}
			score = scoreDfsNode(this);	
			if (HBFSAgent.maxScoreDifference < this.depth - this.score) {
				HBFSAgent.maxScoreDifference = Math.max(this.depth - this.score, HBFSAgent.maxScoreDifference);
				HBFSAgent.correspondingScore = this.score;
			}	
			return score;
		}

		public double updateScore() {
			score = scoreDfsNode(this);	
			if (HBFSAgent.maxScoreDifference < this.depth - this.score) {
				HBFSAgent.maxScoreDifference = Math.max(this.depth - this.score, HBFSAgent.maxScoreDifference);
				HBFSAgent.correspondingScore = this.score;
			}	
			return score;
		}

		public double getTileDiversityScore() {
			if (tileDiversityScore != -1) {
				return tileDiversityScore;
			}
			getScore();
			return tileDiversityScore;
		}

		public double getEventScore() {
			if (eventScore != -1) {
				return eventScore;
			}
			getScore();
			return eventScore;
		}

		public int getLoad() {
			if (totalLoad != -1) {
				return totalLoad;
			}
			getHash();
			return totalLoad;
		}

		public double getLoadScore() {
			if (loadScore != -1) {
				return loadScore;
			}
			getScore();
			return loadScore;
		}

		public double scoreEvent(Event ev) {
			double rt = 0;
			if (ev.passiveTypeId != 0 && ev.activeTypeId != 0) {
				rt = rt + 1;
			}
			return rt;
		}



		@Override
		public boolean equals(Object obj) {
			if (hashCode() != obj.hashCode())
				return false;
			HBFSAgent.equalCalls++;
			if (HBFSAgent.equalCalls % HBFSAgent.callReportFrequency == 1) System.out.print(".");
			// detailed comparison
			HBFSNode n = (HBFSNode) obj;
			if (!n.so.getAvatarPosition().equals(so.getAvatarPosition())) {
				return false;
			}
			if (!n.so.getAvatarOrientation().equals(so.getAvatarOrientation())) {
				return false;
			}
			if (n.so.getAvatarSpeed() != so.getAvatarSpeed()) {
				return false;
			}

			ArrayList<Observation>[][] grid = so.getObservationGrid();
			ArrayList<Observation>[][] ngrid = n.so.getObservationGrid();

			//int k = 0;
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					if (grid[i][j].size() != ngrid[i][j].size()) {
						return false;
					}
					for (int k = 1; k < grid[i][j].size(); k++) {
						if (grid[i][j].get(k).itype != ngrid[i][j].get(k).itype) {
							return false;
						}
					}
				}
			}
			return true;
		}

		@Override
		public int hashCode() {
			if (hash != -1) {
				return hash;
			}
			return getHash();
		}

		public int compareTo(HBFSNode o) {
			int rt = Double.compare(getScore(), o.getScore());
			HBFSAgent.compareCalls++;
			if (HBFSAgent.compareCalls % (2*HBFSAgent.callReportFrequency) == 1)
				System.out.print("-");
			return rt;
		}

		public Stack<Types.ACTIONS> getActionSequence() {
			Stack<Types.ACTIONS> seq = new Stack<Types.ACTIONS>();
			HBFSNode current = this;
			while (true) {
				seq.push(current.causingAction);
				if (current.parent != null) {
					current = current.parent;
				} else {
					break;
				}
			}
			return seq;
		}

		public void displayActionSequence() {
			Stack<ACTIONS> s = getActionSequence();
			System.out.print("Actions: ");
			for (Types.ACTIONS a : s) {
				System.out.print(a + ";" );
			}
			System.out.println();
		}

		public static void setRootLoad(int load) {
			HBFSAgent.rootLoad = load;
		}

		public static void displayStateObservation(StateObservation so) {
			ArrayList<Observation>[][] grid = so.getObservationGrid();
			System.out.println("#Grid:      " + grid.length + " X " + grid[1].length);
			System.out.println("Actions:   " + so.getAvailableActions());
			System.out.println("Immovable: " + arrayListToString(so.getImmovablePositions()));
			System.out.println("Movable:   " + arrayListToString(so.getMovablePositions()));
			System.out.println("NPCs:      " + so.getNPCPositions());
			System.out.println("Resources: " + so.getResourcesPositions());
			System.out.println("A.Res. :   " + so.getAvatarResources());
			System.out.println("Events:    " + so.getEventsHistory().size());
			int eventScore = 0;
			for (Event ev : so.getEventsHistory()) {
				if (ev.activeTypeId == ev.passiveTypeId && ev.passiveTypeId != 0) {
					eventScore += 1;
				}
			}
			System.out.println("Event Score:   " + eventScore);
			if (so.getEventsHistory().size() > 0)
				System.out.println("Last Event:" + so.getEventsHistory().last().gameStep + "; " + so.getEventsHistory().last().fromAvatar 
						+ "; ptid:" + so.getEventsHistory().last().passiveTypeId + "; atid:" + so.getEventsHistory().last().passiveTypeId 
						+ "; pos:" + so.getEventsHistory().last().position);
			System.out.println("Position:  " + so.getAvatarPosition());

			int sequenceLength = so.getWorldDimension().height*so.getWorldDimension().width + 2;
			int hash = sequenceLength;
			int totalLoad = 0;
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					for (Observation o : grid[i][j]) {
						hash = (hash<<4)^(hash>>28)^o.itype;
					}
					totalLoad += grid[i][j].size();
				}
			}
			System.out.println("Total Load: " + totalLoad);
		}

		private static String arrayListToString(ArrayList<Observation>[] a) {
			if (a == null)
				return "null";
			String rt = "[" + a.length + "] ";
			for (ArrayList<Observation> e : a) {
				rt = rt + e.size() + "";
				if (!e.isEmpty()) {
					rt = rt + "<" + e.get(0).itype + ">";
				}
				rt = rt + " | ";
			}
			return rt;
		}

	}
	
	public int controllerState = STATE_PLANNING;
	public Stack<Types.ACTIONS> actionSequence = null; 
	
	public PriorityQueue<HBFSNode> pipe = null;
	public HashSet<HBFSNode> visited = null;
	public HBFSNode bfsRoot = null; 
	public HBFSNode hbfsSolution = null;
	
	public int stats_rejects = 0;
	public int stats_nonUseful = 0;
	public int turnAroundSpeed = -1;
	
	private void initializeBfs(StateObservation so) {
		System.out.println("##Initializing Controller...");
		controllerState = STATE_OTHER;
		
		pipe = new PriorityQueue<HBFSNode>(MAX_PIPE_LENGTH);
		visited = new HashSet<HBFSNode>(HBFSAgent.prime);
		stats_rejects = 0; stats_nonUseful = 0; turnAroundSpeed = -1; // reset protocol statistics
		HBFSAgent.maxScoreDifference = Double.NEGATIVE_INFINITY; 
		HBFSAgent.correspondingScore = Double.NEGATIVE_INFINITY;
		HBFSAgent.rootLoad = -1; HBFSAgent.equalCalls = 0; HBFSAgent.compareCalls = 0;
		
		// For (e.g.) boloadventures the forward model behaves unexpectedly (actions sometimes have no effect, sometimes they do)
//		so.advance(Types.ACTIONS.ACTION_NIL);
//		BFSNode.displayStateObservation(so);
//		so.advance(Types.ACTIONS.ACTION_LEFT); // no effect as first action
//		BFSNode.displayStateObservation(so);
//		so.advance(Types.ACTIONS.ACTION_LEFT);
//		BFSNode.displayStateObservation(so);
		
		bfsRoot = new HBFSNode(so, null, null, 0);
		HBFSNode.setRootLoad(bfsRoot.getLoad());
		HBFSNode.displayStateObservation(so);
				
		if (bfsRoot.so.isGameOver()) {
			throw new IllegalStateException();
		}
		
		pipe.add(bfsRoot);
		visited.add(bfsRoot);
		
		controllerState = STATE_PLANNING;
	}
	
	public void cleanBfs() {
		pipe.clear(); visited.clear();
		bfsRoot = null; hbfsSolution = null;
		actionSequence = null;
		//System.gc(); // gc can cause the initialization to time out.
	}
	
	private boolean performBfs() {
		if (pipe.isEmpty()) {
			controllerState = STATE_OTHER;
			System.out.println("performBfs was called on empty pipe. Changing to STATE_OTHER.");
			return false;
		}
		
		HBFSNode current = pipe.remove();
		
		for (Types.ACTIONS a : ACTIONS) {
			StateObservation soCopy = current.so.copy();
			soCopy.advance(a);
			if (soCopy.isGameOver()) {
				if (soCopy.getGameWinner() == Types.WINNER.PLAYER_WINS) {
					hbfsSolution = new HBFSNode(soCopy, a, current, current.depth+1);
					hbfsSolution.getEventScore();
					return true;
				}
			} else {
			
				if (visited.size() > MAX_REJECTION_SET_SIZE) {
					visited.clear();
					//System.gc();
				}
				
				if (pipe.size() > MAX_PIPE_LENGTH) {
					Stack<HBFSNode> backup = new Stack<HBFSNode>();
					for (int k = 0; k < CARRY_OVER_PIPE_LENGTH; k++) {
						backup.push(pipe.remove());
					}
					pipe.clear();
					pipe.addAll(backup);
					backup = null;
					//System.gc();
				}
				
				HBFSNode m = new HBFSNode(soCopy, a, current, current.depth+1);
				if (visited.add(m)) {
					pipe.add(m);
					//visited.add(m);
				} else {
					stats_rejects++;
				}
				
				m = null;
			}
		}
		
//		if (pipe.isEmpty()) { 
//			System.out.println("#Pipe Empty. Readding current node.");
//			current.so.advance(Types.ACTIONS.ACTION_NIL);
//			current.updateScore();
//			pipe.add(current);
//		}
		
		if (isVerbose) {
			current.displayActionSequence();
			displayAgentState(current);
		}
		return false;
	}
		
	/**
	 * Public constructor with state observation and time due.
	 * 
	 * @param so
	 *            state observation of the current game.
	 * @param elapsedTimer
	 *            Timer for the controller creation.
	 */
	public HBFSAgent(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		// Get the actions in a static array.
		System.out.println("##Creating HBFSAgent...");
		ArrayList<Types.ACTIONS> act = so.getAvailableActions();
		ACTIONS = new Types.ACTIONS[act.size()];
		for (int i = 0; i < ACTIONS.length; ++i) {
			ACTIONS[i] = act.get(i);
		}
		NUM_ACTIONS = ACTIONS.length;

		initializeBfs(so);
		
		boolean hasTerminated = false;
		while (!hasTerminated && elapsedTimer.remainingTimeMillis() > INITIALIZATION_REMTIME && controllerState == STATE_PLANNING) {
			hasTerminated = performBfs();
		}
		if (controllerState != STATE_PLANNING)
			System.out.println("#Controller State: controllerState");
	}
	
	public void displayAgentState() {
		displayAgentState(null);
	}
	
	public void displayAgentState(HBFSNode node) {
		if (node == null) node = pipe.peek();
		if (node == null) {
			System.out.println("#Pipe Empty");
		}
		System.out.println();
		System.out.format("Pipe:%5d|R.Set:%5d|Rejects:%6d|Depth:%3d|Events:%3d|E.Score:%3.2f|D.Score:%3.2f|L.Score:%3.2f|Score:%3.2f|B.Delta:%3.2f|C.Score:%3.2f|Speed:%3d", 
				pipe.size(), visited.size(), stats_rejects, node.depth, node.so.getEventsHistory().size(), node.getEventScore(), node.getTileDiversityScore(), 
				node.getLoadScore(), node.getScore(), HBFSAgent.maxScoreDifference, HBFSAgent.correspondingScore, turnAroundSpeed);
	}
	
	/**
	 * Picks an action. This function is called every game step to request an
	 * action from the player.
	 * @param so
	 *            Observation of the current state.
	 * @param elapsedTimer
	 *            Timer when the action returned is due.
	 * @return An action for the current state
	 */
	public Types.ACTIONS act(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		GameRunner.setLastStateObservation(so);
		switch (controllerState) {
		case STATE_ACTING:
			if (actionSequence.isEmpty()) {
				if (isVerbose) HBFSNode.displayStateObservation(so);
				System.out.println("--Action Stack Empty.");		
				controllerState = STATE_IDLE;
				
				cleanBfs(); // free handles to allow the garbage collector to start cleaning.
				
				return Types.ACTIONS.ACTION_NIL;
			}
			
			if (isVerbose) {
				HBFSNode.displayStateObservation(so);
			    System.out.println("--Performing Action: " + actionSequence.peek());
			}
			return actionSequence.pop();
			
		case STATE_PLANNING:			
			if (so.getGameTick() % reportFrequency == 1) {
				displayAgentState();
			}
			boolean hasTerminated = false;
			turnAroundSpeed = 0;
			while (!hasTerminated && elapsedTimer.remainingTimeMillis() > ACTION_REMTIME && controllerState == STATE_PLANNING) {
				hasTerminated = performBfs();
				turnAroundSpeed+= 1;
//				if (pipe.size() < 5) {
//					displayAgentState();
//				}
					
			}
			
			if (hasTerminated) {
				System.out.println("Solution Found. ACTING Phase...");
				controllerState = STATE_ACTING;
				actionSequence = hbfsSolution.getActionSequence();
				System.out.println("Best Sequence Length: " + actionSequence.size());
			}
			if (so.getGameTick() > MAX_TICKS) {
				System.out.println("Timeout! ACTING Phase...");
				controllerState = STATE_ACTING;
				hbfsSolution = pipe.peek();
				actionSequence = hbfsSolution.getActionSequence();
				System.out.println("Timeout Sequence Length: " + actionSequence.size());
			}
				
			
			return Types.ACTIONS.ACTION_NIL;
		case STATE_IDLE:
		case STATE_OTHER:
			return Types.ACTIONS.ACTION_NIL;
		default:
			throw new IllegalStateException();
		}
	}

}
