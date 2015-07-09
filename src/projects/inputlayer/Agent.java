package projects.inputlayer;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;
import tools.Vector2d;
import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import core.game.Game;
import ontology.Types;
import tools.Utils;


public class Agent extends AbstractPlayer {

	/**
	 * A random number generator.
	 */
	protected Random randomGenerator;
	
	
	ArrayList<Observation>[][] grid;
	
	protected int blockSize;
	
	public int nBlocksX;
	
	public int nBlocksY;
	
	public String[][][] map;
	/**
	 * Constructor. Your "Setup function". Will be run once to initialize your
	 * controller resources. It must return in 1 second maximum.
	 * 
	 * @param stateObs
	 *            The state of the game.
	 * @param elapsedTimer
	 *            The time that has elapsed already.
	 */
	public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		randomGenerator = new Random();
		// get game dimensions and Avatar starting position.
        blockSize = stateObs.getBlockSize();
		// nBlocksX = stateObs.getWorldDimension().width / blockSize;
		// nBlocksY = stateObs.getWorldDimension().height / blockSize;
        
		// initialize map to zero
		grid = stateObs.getObservationGrid();
		nBlocksX = grid[0].length;
		nBlocksY = grid.length;
		map = new String[nBlocksY][nBlocksX][20];
	}

	/**
	 * Act function. Called every game step, it must return an action in 40 ms
	 * maximum.
	 */
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		
			ArrayList<Observation>[] npcPositions = stateObs.getNPCPositions();
	        ArrayList<Observation>[] fixedPositions = stateObs.getImmovablePositions();
	        ArrayList<Observation>[] movingPositions = stateObs.getMovablePositions();
	        ArrayList<Observation>[] resourcesPositions = stateObs.getResourcesPositions();
	        ArrayList<Observation>[] portalPositions = stateObs.getPortalsPositions();
	    	// Vector2d avatarPosition = stateObs.getAvatarPosition();
	        grid = stateObs.getObservationGrid();
	        	        
	        printDebug(npcPositions,"npc");
	        printDebug(fixedPositions,"fix");
	        printDebug(movingPositions,"mov");
	        printDebug(resourcesPositions,"res");
	        printDebug(portalPositions,"por");
	        System.out.println();               
	        
	        long remaining = elapsedTimer.remainingTimeMillis();
			System.out.print(remaining);

			// get 3D map of Observations
			// here map only contains the itypes
			// the first layer (map [][][0]) contains most sprites, always the walls
			// the other layers only contain sprites, if there are more than one at the same position
			// one sprite can be up to 4 times in the map, as it can be between blocks
			// The categories are: TYPE_AVATAR TYPE_RESOURCE  TYPE_PORTAL TYPE_NPC TYPE_STATIC TYPE_FROMAVATAR TYPE_MOVABLE
		    // itypes are different in every game, it is the number of the definition in the VGDL-file
			// 0 is always wall, 1 is always avatar, 2 is the first defined sprite and so on (unfortunately this does not provide us with a lot of information)
			for (int i = 0; i < nBlocksX; i++) {
			for (int j = 0; j < nBlocksY; j++) {
				ArrayList<Observation> observationOfPosition = grid[j][i];
				for (int k = 0; k < observationOfPosition.size(); k++) {
						map[j][i][k] = String.valueOf(observationOfPosition.get(k).itype);
				}
			}
		}
		
		// Print the first 3 layers of the map

		System.out.print("map0");
		for (int i = 0; i < nBlocksX; i++) {
			for (int j = 0; j < nBlocksY; j++) {
				if (map[j][i][0] != null) {
					System.out.print(map[j][i][0]);
				} else {
					System.out.print(" ");
				}
				map[j][i][0] = null;
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.print("map1");
		for (int i = 0; i < nBlocksX; i++) {
			for (int j = 0; j < nBlocksY; j++) {
				if (map[j][i][1] != null) {
					System.out.print(map[j][i][1]);
				} else {
					System.out.print(" ");
				}
				map[j][i][1] = null;
			}
			System.out.println();
		}		

		System.out.println();
		System.out.print("map2");
		for (int i = 0; i < nBlocksX; i++) {
			for (int j = 0; j < nBlocksY; j++) {
				if (map[j][i][2] != null) {
					System.out.print(map[j][i][2]);
				} else {
					System.out.print(" ");
				}
				map[j][i][2] = null;
			}
			System.out.println();
		}		

		
		System.out.println();
		
		System.out.println();
		//a Resource is e.g. a life bar or the honey count 
		System.out.print("Resource Values: ");
		System.out.print(String.valueOf(stateObs.getAvatarResources().values()));
		System.out.println();
		System.out.print("Score: ");
		System.out.print(String.valueOf(stateObs.getGameScore()));
		System.out.println();
		System.out.print("Tick: ");
		System.out.print(String.valueOf(stateObs.getGameTick()));
		System.out.println();
		System.out.print("Avatar Orientation: ");
		System.out.print(String.valueOf(stateObs.getAvatarOrientation().toString()));
		System.out.println();
		System.out.print("Avatar Speed: ");
		System.out.print(String.valueOf(stateObs.getAvatarSpeed()));
		System.out.println();
		System.out.print("Last Avatar action: ");
		System.out.print(String.valueOf(stateObs.getAvatarLastAction().toString()));
		System.out.println();
		
		// List of all NPC Positions
		// The same is possible for FromAvatarSprites, Immovable, Movable, Portals, Resources
		System.out.print("all NPC Positions:");
		System.out.println();
		ArrayList<Observation> [] resli = stateObs.getNPCPositions();   //stateObs.getFromAvatarSpritesPositions() ...
		for (int i = 0; i < resli.length; i++) {
			for (int j = 0; j < resli[i].size(); j++) {
				System.out.print("category:");
				System.out.print(String.valueOf(resli[i].get(j).category));
				System.out.print(" iType:");
				System.out.print(String.valueOf(resli[i].get(j).itype));
				System.out.print(" position:");
				System.out.print(String.valueOf(resli[i].get(j).position.toString()));
				System.out.print(" uniqueID:");
				System.out.print(String.valueOf(resli[i].get(j).obsID));
				System.out.println();
			}
		}

		System.out.println();
				
		System.out.println();

		// Get the available actions in this game.
		// ArrayList<ACTIONS> actions = stateObs.getAvailableActions();

		// Determine an index randomly and get the action to return.
		// int index = randomGenerator.nextInt(actions.size());
		// ACTIONS action = actions.get(index);

		Vector2d move = Utils.processMovementActionKeys(Game.ki.getMask());
        boolean useOn = Utils.processUseKey(Game.ki.getMask());

        //In the keycontroller, move has preference.
        Types.ACTIONS action = Types.ACTIONS.fromVector(move);

        if(action == Types.ACTIONS.ACTION_NIL && useOn)
            action = Types.ACTIONS.ACTION_USE;
        
        return action;
	}

	

	/**
     * Prints the number of different types of sprites available in the "positions" array.
     * Between brackets, the number of observations of each type.
     * @param positions array with observations.
     * @param str identifier to print
     */
    private void printDebug(ArrayList<Observation>[] positions, String str)
    {
        if(positions != null){
            System.out.print(str + ":" + positions.length + "(");
            for (int i = 0; i < positions.length; i++) {
                System.out.print(positions[i].size() + ",");
            }
            System.out.print("); ");
        }else System.out.print(str + ": 0; ");
    }

    
    
    /**
     * Gets the player the control to draw something on the screen.
     * It can be used for debug purposes.
     * @param g Graphics device to draw to.
     */
    public void draw(Graphics2D g)
    {
        int half_block = (int) (blockSize*0.5);
        for(int j = 0; j < grid[0].length; ++j)
        {
            for(int i = 0; i < grid.length; ++i)
            {
                if(grid[i][j].size() > 0)
                {
                    Observation firstObs = grid[i][j].get(0); //grid[i][j].size()-1
                    //Three interesting options:
                    int print =firstObs.itype; //firstObs.category; //firstObs.itype; //firstObs.obsID;
                    g.drawString(print + "", i*blockSize+half_block,j*blockSize+half_block);
                }
            }
        }
    }
	
	


}
