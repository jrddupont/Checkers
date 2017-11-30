package util;


public abstract class Player {
	
	public String color;
	public int playerNumber;
	public boolean rematch = false;
	
	public abstract Board getMove(Board board) throws PlayerDisconnectException;
	public abstract void gameEnd(GameState g);
}
