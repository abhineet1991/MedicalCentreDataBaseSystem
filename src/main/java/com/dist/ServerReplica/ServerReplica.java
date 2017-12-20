package com.dist.ServerReplica;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import com.a2.server.ClinicServerImpl;
import com.dist.config.ServerConfig;
import com.dist.fifo.Fifo;
import com.dist.frontend.Request;
import com.dist.heartbeat.HeartBeat;

public class ServerReplica implements Runnable{

	private int serverId;
	private int RequestID;
	private boolean GroupLeader;
	private boolean Crashed;
	private DatagramSocket replicaSocket;
	private int myReplicaPort;
	private int replicaPorts[];
	private ClinicServerImpl serverObjects[];
	private Fifo fifoObj;
	public static int lastReqProcessed, lastRespSent;
	Request reqObj;
	public static Map<Integer, ServerConfig> configMap = new HashMap<Integer, ServerConfig>(); 
	private HeartBeat heartbeatObj;
	
	//public ServerReplica(int serverId, int myReplicaPort, int replicaPorts[], ClinicServer serverObjects[], FIFO fifoObj){
	public ServerReplica(int serverId)
	{
		this.serverId = serverId;
		initConfigMap();
		
		ServerConfig serverConfig = configMap.get(new Integer(serverId));
		
		//this.myReplicaPort = myReplicaPort;
		//this.replicaPorts = new int[replicaPorts.length];
		//this.serverObjects = new ClinicServerImpl[serverObjects.length];
		
		this.myReplicaPort = serverConfig.feCommPort;
		this.replicaPorts = new int[2];
		
		
		lastReqProcessed = 0;
		lastRespSent = 0;
		/*Debug: in ServerReplica ctor */
		setGroupLeader(serverConfig.isGroupLeader);
		setCrashed(false);
		System.out.println("Debug: In ServerReplica ctor myReplicaPort: " + myReplicaPort);
		
		int i = 0;
		for (ServerConfig serConfig : configMap.values()) {
			if(serConfig.serverId != serverId)
			{
				this.replicaPorts[i] = serConfig.feCommPort;
				++i;
			}
		}
		this.fifoObj = new Fifo(serverId);
		this.heartbeatObj = new HeartBeat(serverId);
		
		serverObjects = new ClinicServerImpl[3];
		serverObjects[0] = new ClinicServerImpl("MTL",serverId * 6501, serverId * 6502, serverId * 6503, this);
		serverObjects[1] = new ClinicServerImpl("LVL",serverId * 6502, serverId * 6501, serverId * 6503, this);
		serverObjects[2] = new ClinicServerImpl("DDO",serverId * 6503, serverId * 6501, serverId * 6502, this);

		
		try {
			replicaSocket = new DatagramSocket(myReplicaPort);
			System.out.println("Debug: In ServerReplica ctor after replicaSocekt: ");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void initConfigMap() {
		ServerConfig server1 = new ServerConfig(1, 5100, 5200, 5300, 4000, true);
		ServerConfig server2 = new ServerConfig(2, 6100, 6200, 6300, 4000, false);
		ServerConfig server3 = new ServerConfig(3, 7100, 7200, 7300, 4000, false);
	
		configMap.put(server1.serverId, server1);
		configMap.put(server2.serverId, server2);
		configMap.put(server3.serverId, server3);
		
		lastReqProcessed = 0;
		lastRespSent= 0;
		
	}
	
	public void run(){
		System.out.println("Debug: In run of ServerReplica");
		if(isGroupLeader())
		{
			listen();
		}
		else
		{
			ServerReplicaHelper serverReplicaHelperObj = new ServerReplicaHelper(this,null, fifoObj);
			System.out.println("Debug: In ServerReplica after creating object of SRHelper");
			Thread thread  = new Thread(serverReplicaHelperObj); 
			thread.start();
		}	
	}
	
	public ClinicServerImpl getClinicObject(String location){

		for(int i =0; i<serverObjects.length; i++){
			if(serverObjects[i].getServerName().equals(location))
				return serverObjects[i];
		}
		return null;
	}

	public void listen(){
		DatagramPacket requestPacket = null;
		ServerReplicaHelper serverReplicaHelperObj = null;

		while(true){
			byte[] buf = new byte[3000];
			requestPacket = new DatagramPacket(buf, buf.length);

			try {
				replicaSocket.receive(requestPacket);
				System.out.println("Debug: In ServerReplica priting requestPacket: "+requestPacket);
				serverReplicaHelperObj = new ServerReplicaHelper(this,requestPacket, fifoObj);
				System.out.println("Debug: In ServerReplica after creating object of SRHelper");
				Thread thread  = new Thread(serverReplicaHelperObj); 
				thread.start();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public ClinicServerImpl[] getServerObjects() {
		return serverObjects;
	}

	public void setServerObjects(ClinicServerImpl[] serverObjects) {
		this.serverObjects = serverObjects;
	}

	public DatagramSocket getReplicaSocket() {
		return replicaSocket;
	}

	public void setReplicaSocket(DatagramSocket replicaSocket) {
		this.replicaSocket = replicaSocket;
	}

	public int getMyReplicaPort() {
		return myReplicaPort;
	}

	public void setMyReplicaPort(int myReplicaPort) {
		this.myReplicaPort = myReplicaPort;
	}

	public int[] getReplicaPorts() {
		return replicaPorts;
	}

	public void setReplicaPorts(int[] replicaPorts) {
		this.replicaPorts = replicaPorts;
	}

	public Fifo getFifoObj() {
		return fifoObj;
	}

	public void setFifoObj(Fifo fifoObj) {
		this.fifoObj = fifoObj;
	}

	public Request getReqObj() {
		return reqObj;
	}

	public void setReqObj(Request reqObj) {
		this.reqObj = reqObj;
	}

	public int getRequestID() {
		return RequestID;
	}

	public void setRequestID(int requestID) {
		RequestID = requestID;
	}

	public boolean isGroupLeader() {
		return GroupLeader;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public void setGroupLeader(boolean groupLeader) {
		GroupLeader = groupLeader;
	}

	public boolean isCrashed() {
		return Crashed;
	}

	public void setCrashed(boolean crashed) {
		Crashed = crashed;
	}

	public static int getLastReqProcessed() {
		return lastReqProcessed;
	}

	public static synchronized void setLastReqProcessed(int lastReqProcessed) {
		ServerReplica.lastReqProcessed = lastReqProcessed;
	}

	public static int getLastRespSent() {
		return lastRespSent;
	}

	public static void setLastRespSent(int lastRespSent) {
		ServerReplica.lastRespSent = lastRespSent;
	}
}
