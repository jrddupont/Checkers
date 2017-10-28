package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class NetworkedPlayer extends Player {
	
	final private byte HELLO = 0;
	final private byte EXIT = 1;
	final private byte AUNTENTICATE = 2;
	final private byte GAME_START = 3;
	final private byte GAME_END = 4;
	final private byte REMATCH_REQUEST = 5;
	final private byte MOVE_REQUEST = 6;
	
	private Socket socket;
	
	private BufferedReader buffReader;
	
	
	public abstract void processPacket(String json);
	
	public abstract String getMail();
	
	public abstract void sendPacket(byte opCode, String data);
	
}
