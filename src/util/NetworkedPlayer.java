package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.simple.JSONObject;

public abstract class NetworkedPlayer extends Player {
	
	final protected byte HELLO = 0;
	final protected byte EXIT = 1;
	final protected byte AUNTENTICATE = 2;
	final protected byte GAME_START = 3;
	final protected byte GAME_END = 4;
	final protected byte REMATCH_REQUEST = 5;
	final protected byte MOVE_REQUEST = 6;
	
	protected Socket socket;
	protected int port = 12321;
	protected String serverIP = "127.0.0.1";
	protected BufferedReader buffReader;
	
	
	protected abstract void processPacket(JSONObject json);
	
	protected abstract JSONObject getMail();
	
	protected abstract void sendPacket(byte opCode, Object data);
	
}
