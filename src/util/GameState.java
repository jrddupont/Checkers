package util;

// This will act as a struct
// With love,
//  Dan and Ben

public class GameState {

	public Player redPlayer = null;
	public Player blackPlayer = null;
	
	public Board board = new Board();
	
	public String redUserName = "";
	public int redWins = 0;
	public int redLosses = 0;
	public int redTies = 0;
	
	public String blackUserName = "";
	public int blackWins = 0;
	public int blackLosses = 0;
	public int blackTies = 0;
	
	public int turn = 0;
	public int gameID = 0;
	
}
