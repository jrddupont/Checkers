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
import util.Netwrk;

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
	
	@SuppressWarnings("unchecked")
	@Override
	public void processPacket(JSONObject json) {
		switch(((Long) json.get(Netwrk.OPCODE)).byteValue())
		{
		case Netwrk.HELLO:			
			if(((Long) json.get(Netwrk.PORT)).intValue() > 0)
			{
				try {
					socket.close();
					port = ((Long) json.get(Netwrk.PORT)).intValue();
					
					socket = new Socket(serverIP, port);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.printf("Accepted into a game\n");
			}
			else System.out.printf("Tried to join a full game\n");
			break;
		case Netwrk.GAME_START:
			game.playerOneUserName = json.get(Netwrk.PLAYER_ONE_UNAME).toString();
			game.playerTwoUserName = json.get(Netwrk.PLAYER_TWO_UNAME).toString();
			game.playerOneWins = ((Long) json.get(Netwrk.PLAYER_ONE_WINS)).intValue();
			game.playerTwoWins = ((Long) json.get(Netwrk.PLAYER_TWO_WINS)).intValue();
			game.playerOneLosses = ((Long) json.get(Netwrk.PLAYER_ONE_LOSSES)).intValue();
			game.playerTwoLosses = ((Long) json.get(Netwrk.PLAYER_TWO_LOSSES)).intValue();
			game.playerOneTies = ((Long) json.get(Netwrk.PLAYER_ONE_TIES)).intValue();
			game.playerTwoTies = ((Long) json.get(Netwrk.PLAYER_TWO_TIES)).intValue();
			game.gameID = ((Long) json.get(Netwrk.GAME_ID)).intValue();
			game.board.board[0] = ((Long) json.get(Netwrk.PLAYER_ONE_BOARD)).intValue();
			game.board.board[1] = ((Long) json.get(Netwrk.PLAYER_TWO_BOARD)).intValue();
			game.board.board[2] = ((Long) json.get(Netwrk.KINGS_BOARD)).intValue();
			System.out.printf("Joined game %d\n", game.gameID);
			System.out.println("Game started!");
			break;
		case Netwrk.MOVE_REQUEST:
			game.board = (Board) json.get(Netwrk.GAME_BOARD);
			//indicate player turn
			//wait for player to make move
			
			JSONObject out = new JSONObject();
			out.put(Netwrk.OPCODE, Netwrk.MOVE_REQUEST);
			out.put(Netwrk.PLAYER_ONE_BOARD, game.board.board[0]);
			out.put(Netwrk.PLAYER_TWO_BOARD, game.board.board[1]);
			out.put(Netwrk.KINGS_BOARD, game.board.board[2]);
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
