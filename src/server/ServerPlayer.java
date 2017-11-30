package server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;

import org.json.simple.JSONObject;

import util.Board;
import util.GameState;
import util.NetworkedPlayer;
import util.Netwrk;
import util.PlayerDisconnectException;

public class ServerPlayer extends NetworkedPlayer 
							implements Runnable{
	public Board board = new Board();
	ServerSocket ssocket;
	
	public ServerPlayer() {
		try {
			ssocket = new ServerSocket(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void processPacket(JSONObject json) {
		switch(((Long) json.get(Netwrk.OPCODE)).byteValue())
		{
		case Netwrk.MOVE_REQUEST:
			board.getBoard()[0] = ((Long) json.get(Netwrk.PLAYER_ONE_BOARD)).intValue();
			board.getBoard()[1] = ((Long) json.get(Netwrk.PLAYER_TWO_BOARD)).intValue();
			board.getBoard()[2] = ((Long) json.get(Netwrk.KINGS_BOARD)).intValue();
			break;
		case Netwrk.EXIT:
			socket=null; //break everything to end game
			break;
		case Netwrk.REMATCH_REQUEST:
			rematch=true;
			break;
		}	
	}

	@SuppressWarnings("unchecked")
	@Override
	public Board getMove(Board board) throws PlayerDisconnectException {
		JSONObject out = new JSONObject();
		out.put(Netwrk.OPCODE, Netwrk.MOVE_REQUEST);
		out.put(Netwrk.PLAYER_ONE_BOARD, board.getBoard()[0]);
		out.put(Netwrk.PLAYER_TWO_BOARD, board.getBoard()[1]);
		out.put(Netwrk.KINGS_BOARD, board.getBoard()[2]);
		
		sendPacket(out);
		processPacket(getMail());
		return this.board;
	}
	
	public int getServerPort() {
		while(ssocket == null) System.out.printf("nullllll\n");
		
		int tempPort = ssocket.getLocalPort();
		return tempPort;
	}

	@Override
	public synchronized void run() {
		try {
			socket = ssocket.accept();
			
			notifyAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void gameEnd(GameState g) {
		JSONObject out = new JSONObject();
		out.put(Netwrk.OPCODE, Netwrk.GAME_END);
		//Maybe tell who won
		sendPacket(out);
		
	}
}
