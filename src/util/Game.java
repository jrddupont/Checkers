package util;

public class Game {
	private GameState gameState;
	
	public Game(GameState gs)
	{
		gameState = gs;
	}
	
	public GameState getGameState() {
		return gameState;
	}
}
