package projects.MCTS;

import java.awt.Dimension;

import tools.Vector2d;
import core.game.StateObservation;

public class RewardMap {

	private double[][] rewMap;
	private int rewMapWidth;
	private int rewMapHeight;
	private Dimension dim;
	private Dimension worldDim;
	private int blockSize;
	
	/**
     * Initialize Reward map with state observation, it automatically gets the World dimensions and sets all rewards to initVal
     * @param StateObservation
     * @param initVal
     * 
     */
	public RewardMap(StateObservation stateObs, double initVal) {
		blockSize = stateObs.getBlockSize();
		worldDim = stateObs.getWorldDimension();
		rewMapWidth = worldDim.width/blockSize;
		rewMapHeight = worldDim.height/blockSize;
		//System.out.println(blockSize+" "+worldDim+" "+rewMapWidth+" "+rewMapHeight);
		dim = new Dimension(rewMapWidth, rewMapHeight);
		
		rewMap = new double[rewMapWidth][rewMapHeight];
		for (int i = 0; i < rewMapWidth; i++) {
			for (int j = 0; j < rewMapHeight; j++) {
				rewMap[i][j] = initVal;
			}
		}
	}

		
	public void print(){
		for (int j = 0; j < rewMapHeight; j++) {
			for (int i = 0; i < rewMapWidth; i++) {
				double val =rewMap[i][j];
				if (val<0){
					System.out.printf("%.3f",val);
					System.out.print(" ");
				}else{
					System.out.print(" ");
					System.out.printf("%.3f",val);
					System.out.print(" ");
				}

				}
			System.out.println();
			}
		System.out.println();
		System.out.println();

	}

	public Dimension getDimension(){
		return dim;
	}
	
	public double[][] getRewardValues(){
		return rewMap;
	}
	
	public double getReward(int X,int Y){
		if(X<0||Y<0){return -1;}
		if(X>=rewMapWidth||Y>=rewMapHeight){return 0;}
		return rewMap[X][Y];
	}
	
	public double getRewardwithWorldPixelPos( int pixelX ,int pixelY){
		int X = Math.floorDiv((int) (pixelX+0.1),blockSize);
		int Y = Math.floorDiv((int) (pixelY+0.1),blockSize);
		if(X<0||Y<0){return -1;}
		if(X>=rewMapWidth||Y>=rewMapHeight){return 0;}
		return rewMap[X][Y];
	}
	
	public double getRewardAtWorldPosition(Vector2d posVec){
		int X = Math.floorDiv((int) (posVec.x+0.1),blockSize);
		int Y = Math.floorDiv((int) (posVec.y+0.1),blockSize);
		if(X<0||Y<0){return -1;}
		if(X>=rewMapWidth||Y>=rewMapHeight){return 0;}
		return rewMap[X][Y];
	}

	public void setReward(int X, int Y, double value){
		rewMap[X][Y]=value;
	}
	
	public void setRewardAtWorldPosition(Vector2d posVec, double value){
		int X = Math.floorDiv((int) (posVec.x+0.1),blockSize);
		int Y = Math.floorDiv((int) (posVec.y+0.1),blockSize);
		if(X>=0 && Y>0 && X<rewMapWidth && Y<rewMapHeight){
			rewMap[X][Y]=value;	
		}
	}
	
	public void incrementRewardAtWorldPosition(Vector2d posVec, double incValue){
		int X = Math.floorDiv((int) (posVec.x+0.1),blockSize);
		int Y = Math.floorDiv((int) (posVec.y+0.1),blockSize);
		if(X>=0 && Y>0 && X<rewMapWidth && Y<rewMapHeight){
		rewMap[X][Y] += incValue;
		}
	}
	
	public void addOtherMap(RewardMap MapToAdd){
		double[][] addMap = MapToAdd.getRewardValues();
			for (int i = 0; i < rewMapWidth; i++) {
				for (int j = 0; j < rewMapHeight; j++) {
					rewMap[i][j] += addMap[i][j] ;
				}
			}		
	}

	/**
     * Increment by the given value, but max to 1
     * 
     */
	public void incrementAll(double incValue){
		for (int i = 0; i < rewMapWidth; i++) {
			for (int j = 0; j < rewMapHeight; j++) {
				if(rewMap[i][j] < 1){
					rewMap[i][j] += incValue;
				}
				
			}
		}	
	}
	
}
