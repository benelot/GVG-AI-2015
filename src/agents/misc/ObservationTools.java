package agents.misc;

import java.util.ArrayList;

import agents.hbfs.HBFSAgent;
import core.game.Observation;
import core.game.StateObservation;

public class ObservationTools {
	
	// Analysis of either the root node or the node2 of the node transition root->...->node1->node2
	public static class DefaultAnalysis {
		int load;             // total number of tiles
		int tileTransforms;   // total number of tile transforms w.r.t. root (a tile vanishes or transforms into another one, movement does not count)
		int tileMovements;    // total number of tile movements w.r.t. root (a tile moves from one pos. to another)
		int relevantEvents;   // not so important; total number of relevant events w.r.t. root (all except irrelevant events)
		int irrelevantEvents; // not so important; total number of relevant events w.r.t. root (events that involve walls etc...)
		int trappedTiles;
		
	}
	
	// Computes hash code for the StateObservation. Used to organize the list of
	// visited states.
	// Rotating hash for sequences of small values:
	// http://burtleburtle.net/bob/hash/doobs.html
	public static int getHash(StateObservation so) {
		int sequenceLength = so.getWorldDimension().height
				* so.getWorldDimension().width + 2;
		if (HBFSAgent.RESPECT_AGENT_ORIENTATION) sequenceLength+=2;
		if (HBFSAgent.REPSECT_AGENT_SPEED) sequenceLength+=1;
		int hash = sequenceLength;
		ArrayList<Observation>[][] grid = so.getObservationGrid();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				for (Observation o : grid[i][j]) {
					hash = (hash << 4) ^ (hash >> 28) ^ o.itype;
				}
			}
		}
		hash = (hash << 4) ^ (hash >> 28) ^ ((int) so.getAvatarPosition().x);
		hash = (hash << 4) ^ (hash >> 28) ^ ((int) so.getAvatarPosition().y);
		
		if (HBFSAgent.RESPECT_AGENT_ORIENTATION) {
			hash = (hash << 4) ^ (hash >> 28) ^ ((int) so.getAvatarOrientation().x);
			hash = (hash << 4) ^ (hash >> 28) ^ ((int) so.getAvatarOrientation().y);
		}
		
		if (HBFSAgent.REPSECT_AGENT_SPEED) {
			hash = (hash << 4) ^ (hash >> 28) ^ ((int) so.getAvatarSpeed());
		}
		
		//hash = hash % HBFSAgent.prime;
		return hash;
	}
	
	public static int getLoad(StateObservation so) {
		int load = 0;
		ArrayList<Observation>[][] grid = so.getObservationGrid();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				load += grid[i][j].size();
			}
		}
		return load;
	}
	
	// Analysis for root node
	public static DefaultAnalysis getAnalysis(StateObservation so) {
		return null;
	}
	
	// Potential speed up: First check whether events took place, if so, update the state observation
	public static DefaultAnalysis getAnalysis(StateObservation so, StateObservation parent, DefaultAnalysis parentAnalysis) {
		return null;
	}
	
}
