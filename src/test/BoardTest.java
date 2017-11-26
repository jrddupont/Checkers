package test;

import util.Board;

public class BoardTest {
	
	public static void main(String[] args) {
		
		Board jumpBoard1 = new Board(
				new int[] 
						{0b00001011000000001111000000001111,
						 0b11110000000011010000000011110000,
								0});
		
		System.out.println("Jump board one\n" + jumpBoard1);
		
		System.out.println("Jump board 1 player 1 moves");
		jumpBoard1.getJumpMoves(Board.PLAYER_1);
		
		System.out.println("Jump board 1 player 2 moves");
		jumpBoard1.getJumpMoves(Board.PLAYER_2);
		
		Board jumpBoard2 = new Board(
				new int[]
						{0b11110000000011110000000011110000,
						 0b00001111000000001111000000001111,
								0});
		
		
		
		System.out.println("Jump board two\n" + jumpBoard2);
		
		System.out.println("Jump board 2 player 1 moves");
		jumpBoard2.getJumpMoves(Board.PLAYER_1);
		
		System.out.println("Jump board 2 player 2 moves");
		jumpBoard2.getJumpMoves(Board.PLAYER_2);

	}
}
