package util;


public abstract class Player {
	
	public String color;
	public int playerNumber;
	
	public abstract Board getMove(Board board);
	
}
