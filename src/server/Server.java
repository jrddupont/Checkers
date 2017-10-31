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
import util.NetworkedPlayer;

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
	
	/*
	 * Sever starts and waits for new players that want to join or make a game. 
	 */
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
			System.out.printf("Starting server\n");
			ssocket = new ServerSocket(port);
			
			for(;;) {
				System.out.printf("\nwaiting for new player to connect...\n");
				socket = ssocket.accept();
				System.out.printf("connected to new player\n");
				
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
					
					// the player wants to join an existing game
					if(games.containsKey(gameID)) {						
						// if that game is waiting for a player
						if((tempGame = games.get(gameID)).getGameState().blackPlayer == null) {
							tempPlayer = new ServerPlayer();
							
							gamePort = tempPlayer.getServerPort();
							new Thread(tempPlayer).start();							
							
							tempGame.getGameState().blackPlayer = tempPlayer;
							
							System.out.printf("%s joined game %d\n", playerName, gameID);
						} else {
							// reply with an error code in the port
							data.put("Opcode", HELLO);
							data.put("Port", -1);
							
							printer = new PrintWriter(socket.getOutputStream(), true);
							
							printer.println(data.toJSONString());
							System.out.printf("%s tried to join full game %d\n", playerName, gameID);
							continue;
						}
						
					} else {
					  // the player wants to make a new game						
						tempGS = new GameState();
						
						tempPlayer = new ServerPlayer();
						gamePort = tempPlayer.getServerPort();
						
						new Thread(tempPlayer).start();
						
						
						tempGS.redPlayer = tempPlayer;
						
						games.put(gameID, new Game(tempGS));
						System.out.printf("%s joined vacant game %d\n", playerName, gameID);
					}
					
					JSONObject out = new JSONObject();
					
					out.put("Opcode", HELLO);
					out.put("Port", gamePort);
					
					printer = new PrintWriter(socket.getOutputStream(), true);
					
					printer.println(out.toJSONString());
					
					if(games.get(gameID).getGameState().blackPlayer != null)
					{
						tempGS = games.get(gameID).getGameState();
						out = new JSONObject();
						out.put("Opcode", MOVE_REQUEST);
						out.put("redUserName", tempGS.redUserName);
						out.put("blackUserName", tempGS.blackUserName);
						out.put("redWins", tempGS.redWins);
						out.put("blackWins", tempGS.blackWins);
						out.put("redLosses", tempGS.redLosses);
						out.put("blackLosses", tempGS.blackLosses);
						out.put("redTies", tempGS.redTies);
						out.put("blackTies", tempGS.blackTies);
						out.put("gameID", tempGS.gameID);
						out.put("Red", tempGS.board.board[0]);
						out.put("Black", tempGS.board.board[1]);
						out.put("King", tempGS.board.board[2]);
						
						((NetworkedPlayer) games.get(gameID).getGameState().redPlayer).sendPacket(out); 
						((NetworkedPlayer) games.get(gameID).getGameState().blackPlayer).sendPacket(out);
						//games.get(gameID).startGame(); //Evan pls
						
						System.out.printf("sent gamestates to players\n");
						System.out.printf("Game %d started\n", gameID);
					}
				}
					
				socket.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public static void main(String[] args) {
		Server server = new Server(12321);
	}
}
