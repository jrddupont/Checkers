package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import util.Board;
import util.GameState;
import util.NetworkedPlayer;

public class ServerPlayer extends NetworkedPlayer {
	public Board board = new Board();
	ServerSocket tempSocket;
	public ServerPlayer() {
		try {
			tempSocket = new ServerSocket(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void Open(int port) {
		try {
			System.out.println("1");
			socket = tempSocket.accept();
			System.out.println("1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void processPacket(JSONObject json) {
		switch(((Long) json.get("Opcode")).byteValue())
		{
		case MOVE_REQUEST:
			board = (Board) json.get("Board");
			break;
		}	
	}

	@SuppressWarnings("unchecked")
	@Override
	public Board getMove(Board board) {
		JSONObject out = new JSONObject();
		out.put("Opcode", MOVE_REQUEST);
		out.put("Red", board.board[0]);
		out.put("Black", board.board[1]);
		out.put("King", board.board[2]);
		sendPacket(out);
		processPacket(getMail());
		return board;
	}
	
	public int getServerPort() {
		int tempPort = tempSocket.getLocalPort();
		return tempPort;
	}

}
