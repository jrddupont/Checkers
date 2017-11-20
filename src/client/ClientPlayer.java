package client;
import java.io.IOException;
import java.net.Socket;

import org.json.simple.JSONObject;

import util.Board;
import util.GameState;
import util.NetworkedPlayer;
import util.Netwrk;
import util.PlayerDisconnectException;

public class ClientPlayer extends NetworkedPlayer{
	
	public GameState gameState = new GameState();
	public ClientPlayer()
	{
		try {
			socket = new Socket(serverIP, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void go() {
		for(;;) {
			try {
				processPacket(getMail());
			} catch (PlayerDisconnectException e) {
				e.printStackTrace();
			}
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
		
		case Netwrk.EXIT:
			gameState.endStatus = gameState.EXIT_REQUESTED;	
			break;
			
		case Netwrk.GAME_START:
			gameState.playerOneUserName = json.get(Netwrk.PLAYER_ONE_UNAME).toString();
			gameState.playerTwoUserName = json.get(Netwrk.PLAYER_TWO_UNAME).toString();
			gameState.playerOneWins = ((Long) json.get(Netwrk.PLAYER_ONE_WINS)).intValue();
			gameState.playerTwoWins = ((Long) json.get(Netwrk.PLAYER_TWO_WINS)).intValue();
			gameState.playerOneLosses = ((Long) json.get(Netwrk.PLAYER_ONE_LOSSES)).intValue();
			gameState.playerTwoLosses = ((Long) json.get(Netwrk.PLAYER_TWO_LOSSES)).intValue();
			gameState.playerOneTies = ((Long) json.get(Netwrk.PLAYER_ONE_TIES)).intValue();
			gameState.playerTwoTies = ((Long) json.get(Netwrk.PLAYER_TWO_TIES)).intValue();
			gameState.gameID = ((Long) json.get(Netwrk.GAME_ID)).intValue();
			gameState.board.getBoard()[0] = ((Long) json.get(Netwrk.PLAYER_ONE_BOARD)).intValue();
			gameState.board.getBoard()[1] = ((Long) json.get(Netwrk.PLAYER_TWO_BOARD)).intValue();
			gameState.board.getBoard()[2] = ((Long) json.get(Netwrk.KINGS_BOARD)).intValue();
			System.out.printf("Joined game %d\n", gameState.gameID);
			System.out.println("Game started!");
			break;
			
		case Netwrk.GAME_END:
			gameState.endStatus = gameState.GAME_ENDED;			
			break;
			
		case Netwrk.REMATCH_REQUEST:
			gameState.endStatus++;
			break;
			
		case Netwrk.MOVE_REQUEST:
			gameState.board = (Board) json.get(Netwrk.GAME_BOARD);
			//indicate player turn
			//wait for player to make move
			
			JSONObject out = new JSONObject();
			out.put(Netwrk.OPCODE, Netwrk.MOVE_REQUEST);
			out.put(Netwrk.PLAYER_ONE_BOARD, gameState.board.getBoard()[0]);
			out.put(Netwrk.PLAYER_TWO_BOARD, gameState.board.getBoard()[1]);
			out.put(Netwrk.KINGS_BOARD, gameState.board.getBoard()[2]);
			sendPacket(out);
			break;
		}
	}

	@Override
	public Board getMove(Board board) throws PlayerDisconnectException{
		// TODO Auto-generated method stub
		return null;
	}
}
