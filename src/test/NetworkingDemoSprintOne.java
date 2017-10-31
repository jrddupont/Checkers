package test;

import util.*;
import server.*;

import org.json.simple.JSONObject;

import client.*;

public class NetworkingDemoSprintOne {
	final protected static byte HELLO = 0;
	
	public static void main(String[] args) {
		ClientPlayer p1 = new ClientPlayer();
		ClientPlayer p2 = new ClientPlayer();
		ClientPlayer p3 = new ClientPlayer();
		
		JSONObject out = new JSONObject();
		out.put("Opcode", HELLO);
		out.put("GameID", 3);
		
		p1.sendPacket(out);
		p2.sendPacket(out);
		p3.sendPacket(out);
		p1.processPacket(p1.getMail());
		p2.processPacket(p2.getMail());
	//	p3.processPacket(p3.getMail());
	}
}