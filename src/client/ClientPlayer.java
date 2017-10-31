package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import util.Board;
import util.GameState;
import util.NetworkedPlayer;

public class ClientPlayer extends NetworkedPlayer{
	
	public GameState game = new GameState();
	public ClientPlayer()
	{
		try {
			socket = new Socket(serverIP, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void processPacket(JSONObject json) {
		switch(((Long) json.get("Opcode")).byteValue())
		{
		case HELLO:			
			if(((Long) json.get("Port")).intValue() > 0)
			{
				try {
					socket.close();
					port = ((Long) json.get("Port")).intValue();
					
					socket = new Socket(serverIP, port);
					
					System.out.println("Joined game");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		case GAME_START:
			game.redUserName = json.get("redUserName").toString();
			game.blackUserName = json.get("blackUserName").toString();
			game.redWins = ((Long) json.get("redWins")).intValue();
			game.blackWins = ((Long) json.get("blackWins")).intValue();
			game.redLosses = ((Long) json.get("redLosses")).intValue();
			game.blackLosses = ((Long) json.get("blackLosses")).intValue();
			game.redTies = ((Long) json.get("redTies")).intValue();
			game.blackTies = ((Long) json.get("blackTies")).intValue();
			game.gameID = ((Long) json.get("gameID")).intValue();
			game.board.board[0] = ((Long) json.get("Red")).intValue();
			game.board.board[1] = ((Long) json.get("Black")).intValue();
			game.board.board[2] = ((Long) json.get("King")).intValue();
			System.out.println("Game started!");
			break;
		case MOVE_REQUEST:
			game.board = (Board) json.get("data");
			//indicate player turn
			//wait for player to make move
			
			JSONObject out = new JSONObject();
			out.put("Opcode", MOVE_REQUEST);
			out.put("Red", game.board.board[0]);
			out.put("Black", game.board.board[1]);
			out.put("King", game.board.board[2]);
			sendPacket(out);
			break;
		}
	}

	@Override
	public Board getMove(Board board) {
		// TODO Auto-generated method stub
		return null;
	}
}
