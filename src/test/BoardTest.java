package test;

import util.Board;

public class BoardTest {
	
	public static void main(String[] args) {
		Board b = new Board();
		//b.getForwardMoves(Board.PLAYER_1);
		//b.getForwardMoves(Board.PLAYER_2);
		
		Board.shittyPrint(b.getMovablePieces(Board.PLAYER_1));
		
		System.out.print(b);
	}
}
