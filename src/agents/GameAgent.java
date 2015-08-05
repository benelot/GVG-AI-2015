package agents;

import core.player.AbstractPlayer;

/**
 * General Agent properties that we want to be able to use in every agent.
 * @author Benjamin Ellenberger
 *
 */
public abstract class GameAgent extends AbstractPlayer {
	
	public abstract void cleanMemory();

}
