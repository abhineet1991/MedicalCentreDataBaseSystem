package com.dist.election;

import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;


public class ElectionAlgo {
	int currentProcess=1;
	static int n=3;
    static int processID[] = new int[100];
    static int status[] = new int[100];
    static int leader;

    //static int startElection;
    
    public static String bullyElect(int ele, int deadProcess) throws ConnectException, IOException
    {
    	String response="";
    	elect(ele, deadProcess);
       	System.out.println("The new leader is "+ leader);
       	while (true) {
       		DatagramSocket aSocket = null;
			try {
				int port = 25000;
				aSocket = new DatagramSocket();
				InetAddress aHost = InetAddress.getByName("localhost");
				String str = String.valueOf(leader);
				DatagramPacket request = new DatagramPacket(str.getBytes(), str.getBytes().length, aHost, port);
				aSocket.send(request);

				aSocket.setSoTimeout(1000);
				String replyStr = "";
				try{
					byte[] buffer = new byte[1000];
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(reply);
					
					replyStr = new String(reply.getData(), 0, reply.getLength());
					System.out.println("::ElectionAlgo::bullyElect::::Recevieve ACK from server:: " + port + " :: reply:::" + replyStr);
				} catch (SocketTimeoutException e) {
					System.out.println("::ElectionAlgo:::bullyElect::::SocketTimeoutException :: waiting for ACK");
				}
				if(replyStr.contains("ACK")){
					break;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			} finally {
				try {
					aSocket.close(); }
	            catch(Exception e) {
	                e.printStackTrace(); }
			}
       	}
       	return response;
    }
    static void elect(int ele, int deadProcess)
    {   
    	status[deadProcess-1]=0;

        ele = ele-1;
        leader = ele+1;
        for(int i=0;i<n;i++)
        {
            if(processID[ele]<processID[i])
            {
                System.out.println("Election message is sent from "+(ele+1)+" to "+(i+1));
                              
                
                if(status[i]==1) //isAlive()==true
                    elect(i+1, deadProcess);
            }
           }
        
    } 
	
}
