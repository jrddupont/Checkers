package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class NetworkedPlayer extends Player {
	
	final protected byte HELLO = 0;
	final protected byte EXIT = 1;
	final protected byte AUNTENTICATE = 2;
	final protected byte GAME_START = 3;
	final protected byte GAME_END = 4;
	final protected byte REMATCH_REQUEST = 5;
	final protected byte MOVE_REQUEST = 6;
	protected Socket socket = null;
	protected int port = 12321;
	protected String serverIP = "127.0.0.1";
	protected BufferedReader buffReader;
	
	
	protected abstract void processPacket(JSONObject json);
	
	public JSONObject getMail()
	{
		JSONParser parser = new JSONParser();
		JSONObject data = null;
		try {
			buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("1");
			data = (JSONObject) ((Object) parser.parse(buffReader.readLine()));
			System.out.println("2");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void sendPacket(JSONObject data)
	{	
		System.out.printf("socket null: %b\n", socket == null);
		while(socket == null) {
			try {
				System.out.printf("waiting");
				wait();
				System.out.printf("running");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		PrintWriter printer;
		try {
			printer = new PrintWriter(socket.getOutputStream(), true);
			printer.println(data.toJSONString());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}	
}
