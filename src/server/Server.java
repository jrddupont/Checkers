package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import util.Game;
import util.GameState;

public class Server {
	
	final private byte HELLO = 0;
	final private byte EXIT = 1;
	final private byte AUNTENTICATE = 2;
	final private byte GAME_START = 3;
	final private byte GAME_END = 4;
	final private byte REMATCH_REQUEST = 5;
	final private byte MOVE_REQUEST = 6;
	
	ServerSocket ssocket;
	Socket socket;
	BufferedReader buffReader;
	PrintWriter printer;
	
	Map<Integer, Game> games = new TreeMap<Integer, Game>();
	
	@SuppressWarnings("unchecked")
	public Server(int port) {
		
		String jsonString;
		JSONObject data = new JSONObject();;
		JSONParser parser = new JSONParser();
		
		Game tempGame;
		GameState tempGS;
		
		byte opcode;
		ServerPlayer tempPlayer;
		String playerName;
		int gamePort = 0;
		
		
		try {
			ssocket = new ServerSocket(port);
			
			for(;;) {
				socket = ssocket.accept();
				buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				try {
					data = (JSONObject) ((Object) parser.parse(buffReader.readLine()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				
				// do stupid check
				opcode = ((Long)data.get("Opcode")).byteValue();
				
				if(opcode == HELLO) {
					// validate from DB
					playerName = (String)data.get("Username");
					
					// get info from DB
					
					int gameID = ((Long)data.get("GameID")).intValue();
					
					if(games.containsKey(gameID)) {
						if((tempGame = games.get(gameID)).getGameState().blackPlayer == null) {
							tempPlayer = new ServerPlayer();
							gamePort = tempPlayer.getServerPort();
							
							tempGame.getGameState().blackPlayer = tempPlayer;
						} else {
							data.put("Opcode", HELLO);
							data.put("Port", -1);
							
							printer = new PrintWriter(socket.getOutputStream(), true);
							
							printer.println(data.toJSONString());
						}
						
					} else {
						tempGS = new GameState();
						
						tempPlayer = new ServerPlayer();
						gamePort = tempPlayer.getServerPort();
						
						tempGS.redPlayer = tempPlayer;
						
						games.put(gameID, new Game(tempGS));
					}
					
					data = new JSONObject();
					
					data.put("Opcode", HELLO);
					data.put("Port", gamePort);
					
					printer = new PrintWriter(socket.getOutputStream(), true);
					
					printer.println(data.toJSONString());
				
				}
					
				
				socket.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	static public void main(String args[]) {
		Server serverDriver = new Server(12321);
	}
	
}
