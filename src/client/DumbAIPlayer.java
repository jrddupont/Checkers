package client;

import java.util.ArrayList;
import java.util.Random;

import util.Board;

/**
 * Picks random possible moves (Sometimes)
 *
 */
public class DumbAIPlayer extends AIPlayer{
	int playerNumber = 0;
	Random rand = new Random();
	
	public DumbAIPlayer(int pn){
		playerNumber = pn;
	}
	
	// Picks a random move
	@Override
	public Board getMove(Board board) {
		ArrayList<Board> moves = board.getNextBoards(playerNumber);
		return moves.get(rand.nextInt(moves.size()));
	}
}
