package com.dist.communication;
/** 
 * @author abhineet.gupta
 * studentId 012426427
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;

import com.dist.frontend.Request;
import com.dist.frontend.Response;

public class FECommunication {

	private Request reqObj;
	private DatagramSocket socket;
	private static int groupLeaderPort = 5100;
	
	/*Storing the last fifty reqObj in a list to maintain high availability*/
	public static Queue<Request> bufferList = new LinkedList<Request>();

	public FECommunication(Request reqObj){
		this.reqObj = reqObj;
		
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		System.out.println("Debug: In FEComunnication :: ctor() : requestID : "+reqObj.getRequestID() + " :: method name"+reqObj.getMethodName());
	}
	
	//TODO
	public static int getGroupLeaderPort(){
		return groupLeaderPort;
	}

	public static void setGroupLeaderPort(int groupLeaderPort) {
		FECommunication.groupLeaderPort = groupLeaderPort;
	}

	public void send(){

		System.out.println("Debug: FEComunnication ::send() ;: before addition: "+bufferList.size());
		/*until list is full*/
		if(bufferList.size()<=50){
			bufferList.add(reqObj);
			System.out.println("Debug: FEComunnication ::send():: buffer size after addition: "+bufferList.size());
		}
		/*if buffer becomes full, the entire buffer is cleared and filled with recent 50 request objects*/
		else{
			bufferList.remove();
			bufferList.add(reqObj);
		}

		ByteArrayOutputStream bs = null;
		ObjectOutput os = null;
		System.out.println("Debug: FEComunnication:: send() to port GroupLeader: "+groupLeaderPort);

		try {
			bs = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bs);
			os.writeObject(reqObj);

			byte[] sendBuffer = bs.toByteArray();
			
			//System.out.println("Debug: In send() of Front end priniting requestPakcet"+sendBuffer);
			
			DatagramPacket requestPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getLocalHost(), groupLeaderPort);
			socket.send(requestPacket);
			System.out.println("Debug: FEComunnication:: send, after socket.send");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public Response recieve(){

		byte[] recieveBuf = new byte[3000];
	//	ByteArrayInputStream bs = null;
		//ObjectInputStream in =  null;
		Response responseObj = null;

		/*Debug: print add and port of req pakcet*/
		System.out.println("Debug: FE communication :: recieve() ::"+ socket.getLocalPort()+" and add "+socket.getLocalAddress());
		
		DatagramPacket responsePacket = new DatagramPacket(recieveBuf, recieveBuf.length);
		ByteArrayInputStream bs = null;
		try {
	
			socket.receive(responsePacket);
			bs = new ByteArrayInputStream(responsePacket.getData());
			ObjectInputStream in = new ObjectInputStream(bs);
			responseObj = (Response)in.readObject();

			System.out.println("Debug: FE communication :: recieve() :: response recieved id :: " + responseObj.getRequestId() + " : " +responseObj.getResultStr() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseObj;

	}

}
