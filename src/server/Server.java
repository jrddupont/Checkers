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
import util.Netwrk;

public class Server {
	
	private ServerSocket ssocket;
	private Socket socket;
	private BufferedReader buffReader;
	private PrintWriter printer;
	
	private Map<Integer, Game> games = new TreeMap<Integer, Game>();
	
	final private JSONObject out = new JSONObject();
	
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
				opcode = ((Long)data.get(Netwrk.OPCODE)).byteValue();
				
				if(opcode == Netwrk.HELLO) {
					// validate from DB
					playerName = (String)data.get(Netwrk.USER_NAME);
					
					// get info from DB
					
					int gameID = ((Long)data.get(Netwrk.GAME_ID)).intValue();
					
					// the player wants to join an existing game
					if(games.containsKey(gameID)) {						
						// if that game is waiting for a player
						if((tempGame = games.get(gameID)).getGameState().PlayerTwo == null) {
							tempPlayer = new ServerPlayer();
							
							gamePort = tempPlayer.getServerPort();
							new Thread(tempPlayer).start();							
							
							tempGame.getGameState().PlayerTwo = tempPlayer;
							tempGame.getGameState().playerTwoUserName = playerName;
							
							System.out.printf("%s joined game %d\n", playerName, gameID);
						} else {
							// reply with an error code in the port
							data.put(Netwrk.USER_NAME, Netwrk.HELLO);
							data.put(Netwrk.PORT, -1);
							
							printer = new PrintWriter(socket.getOutputStream(), true);
							
							printer.println(data.toJSONString());
							System.out.printf("%s tried to join full game %d\n", playerName, gameID);
							continue;
						}
						
					} else {
					  // the player wants to make a new game						
						tempGS = new GameState();
						tempGS.gameID = gameID;
						tempGS.playerOneUserName = playerName;
						
						tempPlayer = new ServerPlayer();
						gamePort = tempPlayer.getServerPort();
						
						new Thread(tempPlayer).start();
						
						
						tempGS.PlayerOne = tempPlayer;
						
						games.put(gameID, new Game(tempGS));
						System.out.printf("%s joined vacant game %d\n", playerName, gameID);
					}
					
					
					
					out.put(Netwrk.OPCODE, Netwrk.HELLO);
					out.put(Netwrk.PORT, gamePort);
					
					printer = new PrintWriter(socket.getOutputStream(), true);
					
					printer.println(out.toJSONString());
					
					if(games.get(gameID).getGameState().PlayerTwo != null)
					{
						tempGS = games.get(gameID).getGameState();
						out.clear();
						out.put(Netwrk.OPCODE, Netwrk.GAME_START);
						out.put(Netwrk.PLAYER_ONE_UNAME, tempGS.playerOneUserName);
						out.put(Netwrk.PLAYER_TWO_UNAME, tempGS.playerTwoUserName);
						out.put(Netwrk.PLAYER_ONE_WINS, tempGS.playerOneWins);
						out.put(Netwrk.PLAYER_TWO_WINS, tempGS.playerTwoWins);
						out.put(Netwrk.PLAYER_ONE_LOSSES, tempGS.playerOneLosses);
						out.put(Netwrk.PLAYER_TWO_LOSSES, tempGS.playerTwoLosses);
						out.put(Netwrk.PLAYER_ONE_TIES, tempGS.playerOneTies);
						out.put(Netwrk.PLAYER_TWO_TIES, tempGS.playerTwoTies);
						out.put(Netwrk.GAME_ID, tempGS.gameID);
						out.put(Netwrk.PLAYER_ONE_BOARD, tempGS.board.board[0]);
						out.put(Netwrk.PLAYER_TWO_BOARD, tempGS.board.board[1]);
						out.put(Netwrk.KINGS_BOARD, tempGS.board.board[2]);
						
						((NetworkedPlayer) games.get(gameID).getGameState().PlayerOne).sendPacket(out); 
						((NetworkedPlayer) games.get(gameID).getGameState().PlayerTwo).sendPacket(out);
						//games.get(gameID).startGame(); //Evan pls
						
						System.out.printf("Game %d started\n", gameID);
					}
				} else if(opcode == Netwrk.SERVER_LIST_REQUEST) {
					out.put(Netwrk.OPCODE, Netwrk.SERVER_LIST_REQUEST);
					
					// return a list of games awaiting players
					for(Map.Entry<Integer, Game> game : games.entrySet()) {
						
						printer = new PrintWriter(socket.getOutputStream(), true);
						
						tempGS = game.getValue().getGameState();
						
						if(tempGS.PlayerTwo == null) {
							out.put(Netwrk.GAME_ID, tempGS.gameID);
							out.put(Netwrk.PLAYER_ONE_UNAME, tempGS.playerOneUserName);
							
							printer.println(out.toJSONString());
						}
					}
					
					out.put(Netwrk.GAME_ID, "null");
					out.put(Netwrk.PLAYER_ONE_UNAME, "null");
					
					printer.println(out.toJSONString());
				}
					
				out.clear();
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
