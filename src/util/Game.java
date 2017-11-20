package util;

import java.util.ArrayList;
import java.util.Random;

public class Game {
	private GameState gameState;
	
	public Game(GameState gs) {
		gameState = gs;
		Player p1 = null;
		Player p2 = null;
		Player[] players = {p1, p2};
		int turn = 0;
		gs.board = new Board();
		Random r = new Random();
		
		int turnCount = 0;
		System.out.printf("Turn:%d\n", turnCount);
		System.out.println(gs.board);
		
		while(true){
			turnCount++;
			System.out.printf("Turn:%d \t Player: %s\n", turnCount, turn==0 ? "One" : "Two");
			ArrayList<Board> possibleMoves = gs.board.getNextStates(turn);
			//Board move = players[turn].getMove(gs.board);
			if (possibleMoves.size() == 0) {
				System.out.println("Game over.");
				return;
			}
			Board move = possibleMoves.get(r.nextInt(possibleMoves.size()));
			if(possibleMoves.contains(move)){ //this works now thanks to .equals() and .hashcode() overrides in Board
				gs.board = move;
				System.out.println(move.toString());
				turn = turn == 0 ? 1 : 0;
			}
		}
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public Thread getCurrentThread() {
		return this.getCurrentThread();
	}
}
