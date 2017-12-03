package com.dist.heartbeat;

import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dist.ServerReplica.ServerReplica;
import com.dist.config.ServerConfig;
import com.dist.election.ElectionAlgo;

public class HeartBeat implements Runnable{
	private String udpServerThreadName;
	private int udpServerPort;
	private int serverId;
	private String serverName;
	private List<Integer> portValuesToRemove = new ArrayList<Integer>();
	
	private Thread t;
	boolean result = true;
	private List<Boolean> resultList = new ArrayList<Boolean>();
	
	public static int FREQ = 5000;
	public static int TIMEOUT = 500;
	
	public HeartBeat(int serverId) {
		super();
		ServerConfig server = ServerReplica.configMap.get(new Integer(serverId));
		this.udpServerThreadName = "hrtbt_udpThread_REP" + serverId;
		this.udpServerPort = server.heartbeatPort;
		this.serverId = serverId;
		this.serverName = "hrt_bt_REP" + serverId;
		
		this.start();
	}
	
	boolean checkIsAlive(final int udpPeerServerPort, final boolean isValidate) {
		result = true;
		Thread myThread = new Thread();
		myThread = new Thread(new Runnable() {
			public void run() {
				DatagramSocket aSocket = null;
				try {
					// Send request for getRecordCount
					System.out.println(serverName+"::HeartBeat::checkIsAlive::::Send request to server:: "+udpPeerServerPort+isValidate);
					aSocket = new DatagramSocket();
					aSocket.setSoTimeout(TIMEOUT);
					byte[] m = "isAlive".getBytes();
					InetAddress aHost = InetAddress.getByName("localhost");
					DatagramPacket request = new DatagramPacket(m, m.length, aHost, udpPeerServerPort);
					aSocket.send(request);
					byte[] buffer = new byte[1000];
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					if(isValidate)
					{
						System.out.println(isValidate);
					}
					aSocket.receive(reply);
					
					String replyStr = new String(reply.getData(), 0, reply.getLength());
					System.out.println(serverName+"::HeartBeat::::checkIsAlive::::Recevieve reply from server:: "+udpPeerServerPort+" :: isValidate ::"+isValidate+" :: reply:::"+replyStr);
					if(!replyStr.contains("yes") && !isValidate)
					{
						if(!validateServerStatus(udpPeerServerPort))
						{
							System.out.println(serverName+"::HeartBeat::checkIsAlive::Validate server:::"+udpPeerServerPort+":::false");
							portValuesToRemove.add(new Integer(udpPeerServerPort));
							System.out.println(serverName+"::HeartBeat::checkIsAlive::portList port removed" + udpPeerServerPort);
							result = false;
						}
					}
					else if(!replyStr.contains("yes") && isValidate)
					{
						result = false;
					}
				
				} 
				catch (SocketTimeoutException e) {
					System.out.println(serverName+"::HeartBeat::checkIsAlive::SocketTimeoutException");
					if(!isValidate && !validateServerStatus(udpPeerServerPort))
					{
						portValuesToRemove.add(new Integer(udpPeerServerPort));
						result = false;
						System.out.println(serverName+"::HeartBeat::checkIsAlive::::Server failed with port: " + udpPeerServerPort);
					}
					else if (isValidate) {
						result = false;
					}
					System.out.println(serverName+"::HeartBeat::checkIsAlive::: " + e.getMessage());
					//resultGetRecordCounts = "fail";
				}
				catch (SocketException e) {
					System.out.println(serverName+"::HeartBeat::checkIsAlive::: " + e.getMessage());
					//resultGetRecordCounts = "fail";
				} catch (IOException e) {
					System.out.println(serverName+"::HeartBeat::checkIsAlive::: " + e.getMessage());
					//resultGetRecordCounts = "fail";
				} finally {
					if (aSocket != null)
						aSocket.close();
				}
			}
		});
		myThread.start();
		
		try {
			myThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("$$$$$$$$$$$"+serverName+"::HeartBeat::checkIsAlive::::Server status with port: " + udpPeerServerPort + " :::: " + result);
		return result;
	}
	
	boolean validateServerStatus(final int udpPeerServerPort)
	{
		result = true;
		resultList.clear();
		for (ServerConfig server : ServerReplica.configMap.values()) 
		{
			if(server.serverId != this.serverId && server.isAlive)
			{
				final int peerPort = server.heartbeatPort;
				if(peerPort != udpPeerServerPort)
				{
					Thread myThread = new Thread();
					myThread = new Thread(new Runnable() {
						public void run() {
							DatagramSocket aSocket = null;
							try {
								// Send request for getRecordCount
								System.out.println(serverName+"::HeartBeat::validateServerStatus::validateServerStatus :::send request to server :::"+peerPort);
								aSocket = new DatagramSocket();
								byte[] m = Integer.toString(udpPeerServerPort).getBytes();
								InetAddress aHost = InetAddress.getByName("localhost");
								DatagramPacket request = new DatagramPacket(m, m.length, aHost, peerPort);
								aSocket.send(request);
								byte[] buffer = new byte[1000];
								DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
								aSocket.receive(reply);
								String replyStr = new String(reply.getData(), 0, reply.getLength());
								System.out.println(serverName+"::HeartBeat::validateServerStatus::receive reply from server:: "+udpPeerServerPort+" :: reply:::"+replyStr);
								if(replyStr.contains("false"))
								{
									resultList.add(false);
								}
								
							} catch (SocketException e) {
								System.out.println(serverName+"::HeartBeat::validateServerStatus :: " + e.getMessage());
								//resultGetRecordCounts = "fail";
							} catch (IOException e) {
								System.out.println(serverName+"::HeartBeat::validateServerStatus: " + e.getMessage());
								//resultGetRecordCounts = "fail";
							} finally {
								if (aSocket != null)
									aSocket.close();
							}
						}
					});
					myThread.start();
					try {
						myThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			
			}
		}
		
		int count = 0;
		for (boolean res : resultList) {
			if (!res) {
				count++;
			}
			else
			{
				count--;
			}
		}
		
		result = (count > 0) ? false: true;
		System.out.println(serverName+"::HeartBeat::validateServerStatus: Server status with port: " + udpPeerServerPort + " :::: " + result);
		return result;
	}
	
	/*
	 * Starts the UDP Server thread
	 */
	public void start() {
		System.out.println(serverName+"::Start HeartBeat:: start() method");

		if (t == null) {
			t = new Thread(this, udpServerThreadName);
			t.start();
			System.out.println(serverName+"::HeartBeat:: start() ::UDP Server Thread Started");
		}
		System.out.println(serverName+"::End HeartBeat:: start() method");
	}
	
	/*
	 * Thread for UDP Server
	 */
	public void run() {
		System.out.println(serverName+"::Start HeartBeat:: run() method");
		Thread myThread = new Thread();
		myThread = new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				DatagramSocket asocket = null;
				try {
					asocket = new DatagramSocket(udpServerPort);
		
					while (true) {
						byte[] m = new byte[1000];
						DatagramPacket request = new DatagramPacket(m, m.length);
						asocket.receive(request);
						String reqStr = new String(request.getData(), 0, request.getLength());
						System.out.println(serverName+"::HeartBeat:: run() :: Request for getRecordCount of type " + reqStr);
						String replyStr = "";
						if("isAlive".equals(reqStr))
						{
							replyStr = serverName + ":yes@" + (new Date());
						}
						else
						{
							int port = Integer.parseInt(reqStr);
							boolean replybool = checkIsAlive(port, true);
							replyStr = String.valueOf(replybool);
						}
						byte[] buffer = new byte[1000];
						System.out.println(serverName+"::HeartBeat:: run() :: Reply sent: " + replyStr);
						buffer = replyStr.getBytes();
						DatagramPacket reply = new DatagramPacket(buffer, buffer.length, request.getAddress(),
								request.getPort());
						asocket.send(reply);
					}
				} catch (SocketException e) {
					System.out.println(serverName+"::HeartBeat:: run() method:" + e.getMessage());
				} catch (IOException e) {
					System.out.println(serverName+"::HeartBeat:: run() method:" + e.getMessage());
				} finally {
					if (asocket != null) {
						asocket.close();
					}
				}
			}
		});
		
		myThread.start();
	
		try
		{
			while(true)
			{
				t.sleep(FREQ);
				for (ServerConfig server : ServerReplica.configMap.values()) 
				{
					if(server.serverId != this.serverId && server.isAlive)
					{
						checkIsAlive(server.heartbeatPort, false);
					}
				}
				for(int portToRemove : portValuesToRemove)
				{
					for (ServerConfig server : ServerReplica.configMap.values()) 
					{
						if(server.heartbeatPort == portToRemove)
						{
							server.isAlive = false;
							if (server.isGroupLeader) {
								try {
									ElectionAlgo.bullyElect(this.serverId, server.serverId);
								} catch (ConnectException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		} catch(InterruptedException e)
		{
			System.out.println(e);
		}
	}
}
