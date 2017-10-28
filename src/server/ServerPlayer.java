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
import util.NetworkedPlayer;

public class ServerPlayer extends NetworkedPlayer {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject getMail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendPacket(byte opCode, Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Board getMove(Board board) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getServerPort() {
		return ssocket.getLocalPort();
	}

}
