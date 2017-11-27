package util;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
	public static final int PLAYER_1	= 0;
	public static final int PLAYER_2	= 1;
	public static final int KINGS 		= 2;

	public static final int START_1 = 0b00000000000000000000111111111111; //starting board for first player
	public static final int START_2 = 0b11111111111100000000000000000000; //starting board for second player

	int mask3Neg5 	= 0b11100000111000001110000011100000; //3, -5
	int mask5Neg3 	= 0b00000111000001110000011100000111; //5, -3
	
	int mask34Neg54 = 0b01110000011100000111000001110000;
	int mask54Neg34 = 0b00001110000011100000111000001110;
	int mask43Neg45 = mask5Neg3;
	int mask45Neg43 = mask3Neg5;
	
	private int[] board = new int[3];

	public Board() {
		board[PLAYER_1] = START_1;
		board[PLAYER_2] = START_2;
		board[KINGS] = 0;
	}

	public Board(int[] board) {
		this.board = board;
	}

	private static int shift(int b, int offset) {
		if(offset < 0) {
			return b >>> -offset;
		} else {
			return b << offset;
		}
	}

	public int getMovablePieces(int player) { //mask of all pieces that may be moved
		
		int piecesCanJump = 0;
		
		for (int i = 0; i < 32; i++) {
			if (getJumps(player, i) != 0) {
				piecesCanJump |= 1<<i;
			}
		}
		
		if (piecesCanJump!=0) return piecesCanJump;
		
		int dir = player==PLAYER_1 ? 1 : -1;
		return (shift(~board[player] & ~board[(player+1) % 2], -dir*(4-dir)) & board[player] & mask3Neg5)
				| (shift(~board[player] & ~board[(player+1) % 2], -dir*(4+dir)) & board[player] & mask5Neg3)
				| (shift(~board[player] & ~board[(player+1) % 2], -dir*4) & board[player]);
	}

	public ArrayList<Board> getForwardMoves(int player) {
		ArrayList<Integer> forwardMoveList = new ArrayList<>(20);
		int dir; //direction the pieces move to go forward

		if (player == PLAYER_1) { 
			dir = 1; //player 1 moves in a positive direction
		} else {
			dir = -1; //player 2 moves in a negative direction
		}

		int plus3Minus5 = (~board[player] & ~board[(player+1) % 2] & shift(board[player] & mask3Neg5, dir*(4 - dir)));
		addForwardMoves(forwardMoveList, board[player], plus3Minus5, (4-dir)*dir);

		int plus5Minus3 = (~board[player] & ~board[(player+1) % 2] & shift(board[player] & mask5Neg3, dir*(4 + dir)));
		addForwardMoves(forwardMoveList, board[player], plus5Minus3, (4+dir)*dir);

		int plus4Minus4 = (~board[player] & ~board[(player+1) % 2] & shift(board[player], dir*4));
		addForwardMoves(forwardMoveList, board[player], plus4Minus4, dir*4);

		ArrayList<Board> boardList = new ArrayList<>(forwardMoveList.size());

		for(Integer move : forwardMoveList) {
			int[] newBoard = new int[3];
			newBoard[player] = move;
			newBoard[(player+1) % 2] = board[(player+1) %2];
			boardList.add(new Board(newBoard));
		}

		return boardList;
	}


	public ArrayList<Board> getJumpMoves(int player) {
		ArrayList<Board> jumpMoveList = new ArrayList<>(5);
		int dir; //direction the pieces are moving to go forward

		if(player == PLAYER_1) {
			dir = 1;
		} else {
			dir = -1;
		}

		int pos34Neg54 = 
				~board[player] & ~board[(player + 1) % 2] //destination space is empty
						& shift(board[(player + 1) % 2], 4*dir) //other player occupies space in the middle
						& shift(board[player], 8*dir-1) //player occupies space where jump originated
						& mask34Neg54; //legal move
		
		//System.out.println("pos 34 neg 54");
		//shittyPrint(pos34Neg54);

		int pos54Neg34 =
				~board[player] & ~board[(player + 1) % 2] //destination
						& shift(board[(player + 1) % 2], 4*dir) //middle
						& shift(board[player], 8*dir+1) //origin
						& mask54Neg34;
		
		//System.out.println("pos 54 neg 34");
		//shittyPrint(pos54Neg34);

		int pos43Neg45 =
				~board[player] & ~board[(player + 1) % 2]
						& shift(board[(player + 1) % 2], 4*dir-1)
						& shift(board[player], 8*dir-1)
						& mask43Neg45;
		
		//System.out.println("pos 43 neg 45");
		//shittyPrint(pos43Neg45);

		int pos45Neg43 =
				~board[player] & ~board[(player + 1) % 2]
						& shift(board[(player + 1) % 2], 4*dir+1)
						& shift(board[player], 8*dir+1)
						& mask45Neg43;
		
		//System.out.println("pos 45 neg 43");
		//shittyPrint(pos45Neg43);
		
		addJumpMoves(jumpMoveList, player, board, pos34Neg54, 4*dir-1 , 4*dir);
		addJumpMoves(jumpMoveList, player, board, pos54Neg34, 4*dir+1, 4*dir);
		addJumpMoves(jumpMoveList, player, board, pos43Neg45, 4*dir, 4*dir-1);
		addJumpMoves(jumpMoveList, player, board, pos45Neg43, 4*dir, 4*dir+1);
		
		return jumpMoveList;
	}

	private static void addForwardMoves(ArrayList<Integer> boardList, int board, int moves, int moveOffset) {
		int x = Integer.lowestOneBit(moves);
		while(x != 0) {
			boardList.add(x | board & ~(shift(x, -moveOffset)));
			moves &= ~x;
			x = Integer.lowestOneBit(moves);
		}	
	}
	
	private static void addJumpMoves(ArrayList<Board> boardList, int player, int[] boardArr, int moves, int offset1, int offset2) {
		int x = Integer.lowestOneBit(moves);
		while (x != 0) {
			int[] newBoardArr = new int[3];
			newBoardArr[player] = x | boardArr[player] & ~(shift(x,-(offset2+offset1)));
			newBoardArr[(player + 1) % 2] = boardArr[(player + 1) % 2] & ~(shift(x, -offset2));
			boardList.add(new Board(newBoardArr));
			moves &= ~x;
			x = Integer.lowestOneBit(moves);
		}
	}

	//convert a single board integer into a 32 character string, reversing to correct for endianness
	private static String boardIntToLine(int b) { 
		return String.format("%32s", Integer.toBinaryString(Integer.reverse(b))).replace(' ', '0'); //string conversion cancer
	}

	public static void shittyPrint(int b) {
		String boardStr = boardIntToLine(b);
		for (int row = 0; row < 8; row++) {
			System.out.println(boardStr.substring(row*4, row*4+4));
		}
		System.out.println();
	}

	public String toString() { //pretty print!

		//pretty board is 9 rows of 17 chars, plus newlines makes 9 x 18 = 162
		StringBuilder out = new StringBuilder(162); 

		//convert each board integer into a string and then a character array
		char[] p1Str = boardIntToLine(board[PLAYER_1]).toCharArray();
		char[] p2Str = boardIntToLine(board[PLAYER_2]).toCharArray();
		char[] kingStr = boardIntToLine(board[KINGS]).toCharArray();

		out.append(" _______________ \n"); //top border

		for(int row=0; row<8; row++) {

			for(int piece=0; piece<4; piece++) {

				int pos = piece + row*4; //add row offset

				char pieceChar = '-'; //character representing piece, default # for unoccupied square

				if (p1Str[pos]=='1') {

					if (p2Str[pos]=='1') { //if pieces overlap, print X to signify badness
						pieceChar = 'X'; 
					} 
					else if (kingStr[pos]=='1') { //player1 is 'r' (for red), capital if king
						pieceChar = 'R';
					} 
					else {
						pieceChar = 'r';
					}
				}

				else if (p2Str[pos]=='1') {

					if (kingStr[pos] == '1') { //player2 is 'b' (for black), capital if king
						pieceChar = 'B';
					} else {
						pieceChar = 'b';
					}
				}

				boolean isEvenIndexedRow = (row % 2 == 0);

				//even indexed rows have the 'light' space preceding
				if(isEvenIndexedRow) { 
					out.append("|_");
				}

				//add the piece :)
				out.append("|" + pieceChar);

				//even indexed rows have the 'light' space following
				if(!isEvenIndexedRow) {
					out.append("|_");
				}

			} //end positions loop

			out.append("|\n"); //add right border and newline to each row

		} //end row loop

		return out.toString();
	}


	@Override
	public int hashCode() {
		return Arrays.hashCode(board);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Board)) {
			return false;
		}
		Board other = (Board) obj;
		if (!Arrays.equals(board, other.board)) {
			return false;
		}
		return true;
	}

	public ArrayList<Board> getNextBoards(int player){

		ArrayList<Board> jumpMoves = getJumpMoves(player);
		if(jumpMoves.isEmpty()) {
			return getForwardMoves(player);
		}
		else {
			return jumpMoves;
		}

	}
	public int getAllMoves(int player, int pieceIndex){
		int output = getJumps(player, pieceIndex);
		if(output == 0){
			return getNonJumps(player, pieceIndex);
		}else{
			return output;
		}
	}
	private int getNonJumps(int player, int pieceIndex) { //mask of all positions that may be moved into without jumping
		int dir = player==PLAYER_1 ? 1 : -1;
		int piece = 1<<pieceIndex;
		return (~board[player] & ~board[(player+1) % 2] & shift(piece & mask3Neg5 & board[player], dir*(4 - dir)))
				| (~board[player] & ~board[(player+1) % 2] & shift(piece & mask5Neg3 & board[player], dir*(4 + dir))) 
				| (~board[player] & ~board[(player+1) % 2] & shift(piece & board[player], dir*4));
	}

	private int getJumps(int player, int pieceIndex){ // Mask of single jumps a given piece can make
		
		int dir = player==PLAYER_1 ? 1 : -1;
		int piece = 1<<pieceIndex;
		
		int pos34Neg54 = 
				~board[player] & ~board[(player + 1) % 2] //destination space is empty
						& shift(board[(player + 1) % 2], 4*dir) //other player occupies space in the middle
						& shift(piece & board[player], 8*dir-1) //player occupies space where jump originated
						& mask34Neg54; //legal move

		int pos54Neg34 =
				~board[player] & ~board[(player + 1) % 2] //destination
						& shift(board[(player + 1) % 2], 4*dir) //middle
						& shift(piece & board[player], 8*dir+1) //origin
						& mask54Neg34;
		

		int pos43Neg45 =
				~board[player] & ~board[(player + 1) % 2]
						& shift(board[(player + 1) % 2], 4*dir-1)
						& shift(piece & board[player], 8*dir-1)
						& mask43Neg45;
	
		int pos45Neg43 =
				~board[player] & ~board[(player + 1) % 2]
						& shift(board[(player + 1) % 2], 4*dir+1)
						& shift(piece & board[player], 8*dir+1)
						& mask45Neg43;
		
		return pos34Neg54 | pos54Neg34 | pos43Neg45 | pos45Neg43;
	}

	public int jumpAndGetJumps(int from, int jumpedTo){ // Mask of moves a piece can make after jumping to a position (Also mutates the board)
		return 0;
	}

	public void moveTo(int from, int to){ // Mutate the board into new board

	}

	public int playerAt(int position){ //returns PLAYER_0, PLAYER_1 or -1 if empty
		if (getBit(board[PLAYER_1], position) == 1) return PLAYER_1;
		if (getBit(board[PLAYER_2], position) == 1) return PLAYER_2;
		return -1;
	}

	public boolean hasKingAt(int position){
		return getBit(board[Board.KINGS], position) == 1;
	}

	int getBit(int mask, int position){
		return (mask >> position) & 1;
	}

	public int[] getBoard() {
		return board;
	}
}
