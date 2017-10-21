package util;

import java.util.ArrayList;

public class Board {
	public static final int PLAYER_1	= 0;
	public static final int PLAYER_2	= 1;
	public static final int KING 		= 2;
	
	private int[] board;
	private int importantMagicNumber = 0b10101101010101100100101010101001;
	public static void main(String[] args){
	}
	
	public ArrayList<Board> getNextStates(int player){
		ArrayList<Board> nextStates = new ArrayList<Board>();
		
		// Loop through each piece
		// Add it's possible moves to nextStates
		
		// Once done, remove duplicates 
		// Return list
		
		return nextStates;
	}
	
	private int getFrontLeft(){
		return 0;
	}
	
	private int getFrontRight(){
		return 0;
	}
	
	private int getBackLeft(){
		return 0;
	}
	
	private int getBackRight(){
		return 0;
	}
}
