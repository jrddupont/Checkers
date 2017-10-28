package util;

import java.net.Socket;

public abstract class Player {
	
	private String color;
	
	public abstract Board getMove(Board board);
	
}
