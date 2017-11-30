package com.dist.config;

public class ServerConfig {
	public int serverId;
	public int feCommPort;
	public int heartbeatPort;
	public int fifoPort;
	public int feport;
	public boolean isAlive;
	public boolean isGroupLeader;
	
	public ServerConfig(int serverId, int feCommPort, int heartbeatPort, int fifoPort, int feport, boolean isGroupLeader) {
		super();
		this.serverId = serverId;
		this.feCommPort = feCommPort;
		this.heartbeatPort = heartbeatPort;
		this.fifoPort = fifoPort;
		this.feport = feport;
		this.isAlive = true;
		this.isGroupLeader = isGroupLeader;
	}
}


