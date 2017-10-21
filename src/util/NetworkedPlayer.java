package util;

public abstract class NetworkedPlayer extends Player {
	
	public abstract void processPacket(String json);
	
	public abstract String getMail();
	
	public abstract void sendPacket(byte opCode, String data);
	
}
