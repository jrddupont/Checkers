package util;

import java.util.ArrayList;
import java.util.Random;

/*
 * XXX whenever player.getMove() is called, PlayerDisconnectException needs to be called
 *     catch it and set gamestate.endStatus = gamestate.EXIT_REQUESTED
 *     this will let the server know to clean up resources, and should not affect local play
 */

public class Game {
	private GameState gameState;
	
	public Game(GameState gs) {
		gameState = gs;
	}
	
	public void start() {
		
		GameState gs = gameState;
		
		int turn = 0;
		int turnCount = 0;
		System.out.printf("Turn:%d\n", turnCount);
		System.out.println(gs.board);
		
		boolean running = true;
		while(running){
			turnCount++;
			System.out.printf("Turn:%d \t Player: %s\n", turnCount, turn==0 ? "One" : "Two");
			ArrayList<Board> possibleMoves = gs.board.getNextBoards(turn);
			//Board move = players[turn].getMove(gs.board);
			if (possibleMoves.size() == 0) {
				System.out.println("Game over.");
				if (turn==Board.PLAYER_1) {
					System.out.println("Player two wins");
					gs.playerTwoWins++;
					gs.playerOneLosses++;
					
				} else {
					System.out.println("Player one wins");
					gs.playerOneWins++;
					gs.playerTwoLosses++;
				}
				gs.PlayerOne.gameEnd(gs);
				gs.PlayerTwo.gameEnd(gs);
				return;
			}
			Board move;
			try {
				move = (turn==0 ? gs.PlayerOne.getMove(gs.board) : gs.PlayerTwo.getMove(gs.board));
				//if(possibleMoves.contains(move)){ //this works now thanks to .equals() and .hashcode() overrides in Board
					gs.board = move;
					System.out.println(move.toString());
					turn = turn == 0 ? 1 : 0;
				//}
				
			} catch (PlayerDisconnectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				running = false;
				gs.endStatus = gs.EXIT_REQUESTED;
			}
		}
	}
	public void restartGame()
	{
		
	}
	public GameState getGameState() {
		return gameState;
	}
	
	public Thread getCurrentThread() {
		return this.getCurrentThread();
	}
}
