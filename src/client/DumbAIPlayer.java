package client;

import java.util.ArrayList;
import java.util.Random;

import util.Board;
import util.Player;

public class DumbAIPlayer extends Player{
	int playerNumber = 0;
	Random rand = new Random();
	public DumbAIPlayer(int pn){
		playerNumber = pn;
	}
	
	@Override
	public Board getMove(Board board) {
		ArrayList<Board> moves = board.getNextBoards(playerNumber);
		return moves.get(rand.nextInt(moves.size()));
	}
}
