package util;

import java.net.Socket;

public abstract class Player {
	
	final private byte HELLO = 0;
	final private byte EXIT = 1;
	final private byte AUNTENTICATE = 2;
	final private byte GAME_START = 3;
	final private byte GAME_END = 4;
	final private byte REMATCH_REQUEST = 5;
	final private byte MOVE_REQUEST = 6;
	
	private String color;
	private Socket socket;
	
}
