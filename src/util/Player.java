package util;

import java.net.Socket;

public abstract class Player {
	
	public String color;
	
	public abstract Board getMove(Board board);
	
}
