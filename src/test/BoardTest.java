package test;

import util.Board;

public class BoardTest {
	
	public static void main(String[] args) {
		Board b = new Board();
		b.getForwardMoves(Board.PLAYER_1);
		b.getForwardMoves(Board.PLAYER_2);
		
		System.out.println("XXXXX");
		Board.shittyPrint(b.getMoves(Board.PLAYER_2, 22));
		System.out.println("----");
	}
}
