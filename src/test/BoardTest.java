package test;

import java.util.Arrays;

import util.Board;

public class BoardTest {
	
	public static void main(String[] args) {
		
		Board jumpBoard1 = new Board(
				new int[] 
						{0b00001111000000001111000000001111,
						 0b11110000000011110000000011110000,
								0});
		
		System.out.println("Jump board one\n" + jumpBoard1);
		
		System.out.println("Jump board 1 player 1 moves");
		System.out.println(jumpBoard1.getJumpMoves(Board.PLAYER_1));
		
	}
}
