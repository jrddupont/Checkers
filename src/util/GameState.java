package util;

// This will act as a struct
// With love,
//  Dan and Ben

public class GameState {

	public Player PlayerOne = null;
	public Player PlayerTwo = null;
	
	public Board board = new Board();
	
	public String playerOneUserName = "";
	public int playerOneWins = 0;
	public int playerOneLosses = 0;
	public int playerOneTies = 0;
	
	public String playerTwoUserName = "";
	public int playerTwoWins = 0;
	public int playerTwoLosses = 0;
	public int playerTwoTies = 0;
	
	public int turn = 0;
	public int gameID = 0;
	
	final public int GAME_IN_PROGRESS = 0;
	final public int GAME_ENDED = 1;
	final public int RESTART_REQUESTED = 2; // one player wants to restart
	final public int RESTART_CONFIRMED = 3; // both players want to restart
	final public int EXIT_REQUESTED = -1;
	
	public int endStatus = 0;
	
}
