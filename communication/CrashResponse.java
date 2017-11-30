package com.dist.communication;
/** 
 * @author abhineet.gupta
 * studentId 012426427
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.dist.config.ServerConfig;
import com.dist.frontend.FrontEnd;

public class CrashResponse implements Runnable{
	private DatagramSocket crashSocket;
	private FrontEnd frontEndObj;

	public CrashResponse(FrontEnd frondEndObj) {
		this.frontEndObj = frondEndObj;
		try {
			crashSocket = new DatagramSocket(25000);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		listenForLeaderCrash();
	}
	
	/*Port for listening for Group Leader crash, received crahsInfo from isAlive Only when group leader crashes
	 * calls sendBufferToNewGL to send the stored buffer to new group leader*/
	public void listenForLeaderCrash(){
		DatagramPacket newGroupLeader = null;

		while(true){
			byte[] buf = new byte[1024];
			newGroupLeader = new DatagramPacket(buf, buf.length);

			try {
				crashSocket.receive(newGroupLeader);
				
				String replyStr = "ACK";
				byte[] buffer = new byte[1000];
				System.out.println(" :: CrashResponse:: listenForLeaderCrash() :: ACK sent: " + replyStr);
				buffer = replyStr.getBytes();
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length, newGroupLeader.getAddress(),
						newGroupLeader.getPort());
				crashSocket.send(reply);
				
				String newLeaderServerId = new String(newGroupLeader.getData(), 0, newGroupLeader.getLength());
				
				for(ServerConfig server : FrontEnd.configMap.values())
				{
					if(server.isGroupLeader)
					{
						server.isGroupLeader = false;
						server.isAlive = false;
					}
				}
				FrontEnd.configMap.get(Integer.parseInt(newLeaderServerId)).isGroupLeader = true;
				int newGroupLeaderPort = FrontEnd.configMap.get(Integer.parseInt(newLeaderServerId)).feCommPort;
				
				sendBufferToNewGL(newGroupLeaderPort);
				
				FECommunication.setGroupLeaderPort(newGroupLeaderPort);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*serialize buffer and send it to new group leader*/
	public void sendBufferToNewGL(int leaderPort){
		DatagramSocket socket = null;
		ByteArrayOutputStream bs = null;
		ObjectOutputStream os = null;

		frontEndObj.setGroupLeader(leaderPort);

		try {
			socket = new DatagramSocket();
			bs = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bs);
			os.writeObject(FECommunication.bufferList);
			
			byte[] sendBuffer = bs.toByteArray();
			DatagramPacket requestPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getLocalHost(), leaderPort);
			socket.send(requestPacket);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 
}
