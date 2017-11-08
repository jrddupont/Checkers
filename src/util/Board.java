package util;

import java.util.ArrayList;

public class Board {
	public static final int PLAYER_1	= 0;
	public static final int PLAYER_2	= 1;
	public static final int KING 		= 2;
	
	public static final int START_1 = 0b00000000000000000000111111111111; //starting board for first player
	public static final int START_2 = 0b11111111111100000000000000000000; //starting board for second player
	
	int mask3Neg5 	= 0b00001110000011100000111000001110; //3, -5
	int mask5Neg3 	= 0b01110000011100000111000001110000; //5, -3
	
	
	public int[] board = new int[3];

	public Board() {
		System.out.println("Initializing board:\n");
		System.out.println("Initializing player 1 board:");
		board[PLAYER_1] = START_1;
		shittyPrint(board[PLAYER_1]);
		System.out.println("Player 1 board initialized.\n\nInitializing player 2 board:");
		board[PLAYER_2] = START_2;
		shittyPrint(board[PLAYER_2]);
		System.out.println("Player 2 board initialized\n.");
		
		board[KING] = 0;
	}
	
	private int shift(int b, int offset) {
		if(offset < 0) {
			return b >>> -offset;
		} else {
			return b << offset;
		}
	}
	
	public int getMoves(int player, int pieceIndex) {
		int dir = player==PLAYER_1 ? 1 : -1;
		int piece = 1<<pieceIndex;
		return (~board[player] & ~board[(player+1) % 2] & shift(piece & mask3Neg5 & board[player], dir*(4 - dir)))
				| (~board[player] & ~board[(player+1) % 2] & shift(piece & mask5Neg3 & board[player], dir*(4 + dir))) 
				| (~board[player] & ~board[(player+1) % 2] & shift(piece & board[player], dir*4));
	}
	
	public ArrayList<Integer> getForwardMoves(int player) {
		int dir; //direction the pieces move to go forward
		
		if (player == PLAYER_1) { 
			dir = 1; //player 1 moves in a positive direction
		} else { 
			dir = -1; //player 2 moves in a negative direction
		}
		
		int plus3Minus5 = (~board[player] & ~board[(player+1) % 2] & shift(board[player] & mask3Neg5, dir*(4 - dir)));
		System.out.println((4-dir)*dir);
		shittyPrint(plus3Minus5);
		int plus5Minus3 = (~board[player] & ~board[(player+1) % 2] & shift(board[player] & mask5Neg3, dir*(4 + dir)));
		System.out.println((4+dir)*dir);
		shittyPrint(plus5Minus3);
		int plus4Minus4 = (~board[player] & ~board[(player+1) % 2] & shift(board[player], dir*4));
		System.out.println(4*dir);
		shittyPrint(plus4Minus4);
		
		return null;
	}
	
	public static void shittyPrint(int b) {
		String boardStr = String.format("%32s", Integer.toBinaryString(Integer.reverse(b))).replace(' ', '0'); //string conversion cancer
		for (int row = 0; row < 8; row++) {
			System.out.println(boardStr.substring(row*4, row*4+4));
		}
		System.out.println();
	}
	
	public ArrayList<Board> getNextStates(int player){
		ArrayList<Board> nextStates = new ArrayList<Board>();
		
		
		return nextStates;
	}
}
