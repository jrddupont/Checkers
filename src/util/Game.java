package util;

import java.util.ArrayList;

public class Game {
	private GameState gameState;
	
	public Game(GameState gs) {
		gameState = gs;
		Player p1 = null;
		Player p2 = null;
		Player[] players = {p1, p2};
		int turn = 0;
		gs.board = new Board();
		while(true){
			ArrayList<Board> possibleMoves = gs.board.getNextStates(turn);
			Board move = players[turn].getMove(gs.board);
			if(possibleMoves.contains(move) || true){ // TODO this will not work
				gs.board = move;
				System.out.println(move.toString());
				turn = turn == 0 ? 1 : 0;
			}
		}
	}
	
	public GameState getGameState() {
		return gameState;
	}
}
