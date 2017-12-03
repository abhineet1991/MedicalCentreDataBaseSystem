package com.dist.fifo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.dist.ServerReplica.ServerReplica;
import com.dist.config.ServerConfig;
import com.dist.frontend.Request;
import com.dist.frontend.Response;

public class Fifo implements Runnable
{
	private String udpServerThreadName;
	private int udpServerPort;
	int serverId;
	private String serverName;
	private Thread t;
	boolean result = true;
	public static int FREQ = 5000;
	public static int TIMEOUT = 500;
	public Response response;
	public Map<Integer, Response> respMap = new HashMap<Integer, Response>();
	public Map<Integer, Request> reqMap = new TreeMap<Integer, Request>();
	
	public Fifo(int serverId) {
		super();
		ServerConfig server = ServerReplica.configMap.get(new Integer(serverId));
		this.udpServerThreadName = "fifo_udpThread_REP" + serverId;
		this.udpServerPort = server.fifoPort;
		this.serverId = serverId;
		this.serverName = "FIFO_REP" + serverId;
		this.start();
	}
	
	boolean send(final byte []b, final int udpPeerServerPort, final int serverId) {
		result = true;
		Thread myThread = new Thread();
		myThread = new Thread(new Runnable() {
			public void run() {
				DatagramSocket aSocket = null;
				try {
					while(true)
					{
						// Send request for getRecordCount
						System.out.println(serverName+"::FIFO:::send::::Send request to server:: "+udpPeerServerPort);
						aSocket = new DatagramSocket();
						InetAddress aHost = InetAddress.getByName("localhost");
						DatagramPacket request = new DatagramPacket(b, b.length, aHost, udpPeerServerPort);
						aSocket.send(request);
						
						aSocket.setSoTimeout(3000);
						String replyStr = "";
						try{
							byte[] buffer = new byte[1000];
							DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
							aSocket.receive(reply);
							
							replyStr = new String(reply.getData(), 0, reply.getLength());
							System.out.println(serverName + "::FIFO::send::::Recevieve ACK from server:: " + udpPeerServerPort + " :: reply:::" + replyStr);
						} catch (SocketTimeoutException e) {
							System.out.println(serverName+"::FIFO:::send::::SocketTimeoutException :: waiting for ACK");
						}
						if(replyStr.contains("ACK")){
							break;
						}
					}
				} catch (SocketException e) {
					System.out.println(serverName + "::FIFO: :: send(): " + e.getMessage());
					//resultGetRecordCounts = "fail";
				} catch (IOException e) {
					System.out.println(serverName + "::FIFO::: send(): " + e.getMessage());
					//resultGetRecordCounts = "fail";
				} finally {
					if (aSocket != null)
						aSocket.close();
				}
			}
		});
		myThread.start();
		
		return result;
	}
	
	public void broadcast(byte []b)
	{
		System.out.println(serverName+"::FIFO: :: multicast :::");
		for (ServerConfig server : ServerReplica.configMap.values()) 
		{
			if(server.serverId != this.serverId && server.isAlive)
			{
				send(b, server.fifoPort, server.serverId);
			}
		}
	}
	
	/*
	 * Starts the UDP Server thread
	 */
	private void start() {
		System.out.println("Start Fifo::start() method");

		if (t == null) {
			t = new Thread(this, udpServerThreadName);
			t.start();
			System.out.println("Fifo::start() :: UDP Server Thread Started");
		}
		System.out.println("End Fifo::start() :: method");
	}
	
	public Object getObjectFromByteArray(byte[] b)
	{
		Object obj = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(b);
		ObjectInput in;
		try {
			in = new ObjectInputStream(bis);

			obj = in.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	/*
	 * Thread for UDP Server
	 */
	public void run() {
		System.out.println("Start Fifo:: run() method");
		Thread myThread = new Thread();
		myThread = new Thread(new Runnable() {
			
			public void run() {
				System.out.println("$$$$$$$$");
				
				while(true)
				{
					ServerConfig server = ServerReplica.configMap.get(new Integer(serverId));
					if(server.isGroupLeader)
					{
						grpLeaderTask();
					}
					else
					{
						nonGrpLeaderTask();
					}
				}
			}
		});
		
		myThread.start();
	}

	void grpLeaderTask(){
		DatagramSocket asocket = null;
		try {
			asocket = new DatagramSocket(udpServerPort);
			String replyRespStr = "";
			byte[] bufferResp = new byte[3000];
			System.out.println(serverName+ " :: Fifo:: grpLeaderTask()");
			DatagramPacket replyResp = new DatagramPacket(bufferResp, bufferResp.length);
			asocket.receive(replyResp);
			
			replyRespStr = new String(replyResp.getData(), 0, replyResp.getLength());
			Response response = (Response) getObjectFromByteArray(bufferResp);
			synchronized (respMap) {
				if (!respMap.containsKey(response.getRequestId()))
				{
					respMap.put(response.getRequestId(), response);
				}
			}
			System.out.println(serverName + "::FIFO:::grpLeaderTask::::Recevieve Response from server :: reply:::" + response.getResultStr());
			
			String replyStr = "ACK";
			byte[] buffer = new byte[1000];
			System.out.println(serverName+ " :: Fifo:: grpLeaderTask() :: ACK sent: " + replyStr);
			buffer = replyStr.getBytes();
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length, replyResp.getAddress(),	replyResp.getPort());
			asocket.send(reply);
		
		} catch (SocketException e) {
			System.out.println(serverName+ " :: Fifo:: grpLeaderTask() method:" + e.getMessage());
		} catch (IOException e) {
			System.out.println(serverName+ " :: Fifo:: grpLeaderTask() method:" + e.getMessage());
		} finally {
			if (asocket != null) {
				asocket.close();
			}
		}
	}
}
