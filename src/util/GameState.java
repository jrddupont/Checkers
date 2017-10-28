package util;

public class GameState {

	Player P1;
	Player P2;
	
	Board board = new Board();
	
	GameState(Player p1, Player p2)
	{
		P1 = p1;
		P2 = p2;
	}
	
}
