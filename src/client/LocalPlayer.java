package client;

import util.Board;
import util.GameState;
import util.Player;
import util.PlayerDisconnectException;

public class LocalPlayer extends Player {
	//need to set rematch to false when game restarts
	@Override
	public void gameEnd(GameState g)
	{
		System.out.println();
		//gui end game
	}
	public Board getMove(Board board) throws PlayerDisconnectException{
		// TODO Auto-generated method stub
		return null;
	}

}
