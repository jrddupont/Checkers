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

import util.Netwrk;

public abstract class NetworkedPlayer extends Player {
	
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
			data = (JSONObject) ((Object) parser.parse(buffReader.readLine()));
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
		try {
			while(socket == null) wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
