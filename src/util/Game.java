package util;

public class Game {
	private String redUserName;
	private int redWins;
	private int redLosses;
	private int redTies;
	
	private String blackUserName;
	private int blackWins;
	private int blackLosses;
	private int blackTies;
	
	private int turn;
	private Board board;
	private int gameID;

	GameState gameState;
	
	Game(Player p1, Player p2)
	{
		gameState = new GameState(p1, p2);
	}
}
