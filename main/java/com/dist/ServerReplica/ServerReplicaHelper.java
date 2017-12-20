package com.dist.ServerReplica;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;

import com.a2.server.ClinicServerImpl;
import com.dist.config.ServerConfig;
import com.dist.fifo.Fifo;
import com.dist.frontend.Request;
import com.dist.frontend.Response;

public class ServerReplicaHelper implements Runnable {

	private DatagramPacket requestPacket;
	private ServerReplica serverReplicaObj;
	private Request reqObj;
	private String clinicLocation;
	private Fifo fifoObj;

	/* ENUM */
	private enum switchFunc {
		createDRecord, createNRecord, editRecord, getCount, transferRecord
	}

	public ServerReplicaHelper(ServerReplica serverReplicaObj,
			DatagramPacket requestPacket, Fifo fifoObj) {
		this.requestPacket = requestPacket;
		this.serverReplicaObj = serverReplicaObj;
		this.fifoObj = fifoObj;
		if (serverReplicaObj.isGroupLeader()) {
			ByteArrayInputStream bs = null;
			ObjectInput is = null;

			byte[] b = requestPacket.getData();
			bs = new ByteArrayInputStream(b);
			try {
				is = new ObjectInputStream(bs);
				System.out.println("In SRH ctor requestpacket: "
						+ requestPacket);
				reqObj = (Request) is.readObject();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/* run method override */
	public void run() {
		if (serverReplicaObj.isGroupLeader()) {
			System.out.println("Debug: In run of SRH: GroupLEader");
			groupLeaderCrashHandle();
		} else {
			System.out.println("Debug: In run of SRH: Non-GroupLEader");
			nonGroupLeaderTask();
		}
	}

	/* function to handle group leader crash */
	public void groupLeaderCrashHandle() {
		ByteArrayInputStream bs = null;
		ObjectInputStream in = null;

		if (serverReplicaObj.isCrashed()) {

			try {
				bs = new ByteArrayInputStream(requestPacket.getData());
				in = new ObjectInputStream(bs);

				Queue bufferList = new LinkedList();
				bufferList = (Queue) in.readObject();

				for (Object reqObj : bufferList) {
					groupLeaderTask((Request) reqObj);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		groupLeaderTask(reqObj);
	}

	/* function for Group Leader Task */
	public void groupLeaderTask(Request reqObj) {
		System.out
				.println("Debug: In SRH groupLeaderTask : Reuqest id and method name : "
						+ reqObj.getRequestID()//
						+ " " + reqObj.getMethodName());

		sendAndRecieveOtherReplicas();

		localExecution(reqObj);

		Response response = null;
		while (true) {
			synchronized (fifoObj.respMap) {
				int lastRespId = ServerReplica.getLastRespSent();
				if (!fifoObj.respMap.isEmpty()
						&& fifoObj.respMap.containsKey(new Integer(
								lastRespId + 1))) {
					response = fifoObj.respMap.get(new Integer(lastRespId + 1));
					System.out
							.println("Debug: In sendAndRecieveOtherReplicas response is : "
									+ response.getRequestId());
					ServerReplica.setLastRespSent(response.getRequestId());
					break;
				}
			}
		}

		sendToFrontEnd(response);
	}

	public void localExecution(final Request req) {
		Thread myThread = new Thread();
		myThread = new Thread(new Runnable() {
			public void run() {
				Response myLocationResponse = myLocationServers(req);
				synchronized (fifoObj.respMap) {
					if (!fifoObj.respMap.containsKey(myLocationResponse
							.getRequestId())) {
						fifoObj.respMap.put(myLocationResponse.getRequestId(),
								myLocationResponse);
					}
				}
			}
		});
		myThread.start();
	}

	/*
	 * function for Group Leader to send serialized packet to other 2 server
	 * replicas using FIFO
	 */
	public void sendAndRecieveOtherReplicas() {

		/* Creating FIFO object to broadcast it to other server replicas */
		Fifo fifoObj = serverReplicaObj.getFifoObj();

		fifoObj.broadcast(requestPacket.getData());

		System.out.println("Debug: sendAndRecieveOtherReplicas end");
	}

	public Object getObjectFromByteArray(byte[] b) {
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

	/* function for Group Leader Process to send request to its own server */
	public Response myLocationServers(Request reqObj) {
		System.out.println("Debug: In Myloci , Reuqest id and method name : "
				+ reqObj.getRequestID() + " " + reqObj.getMethodName());
		Response responseObj = null;

		/* Sending packet to its own location server */
		clinicLocation = reqObj.getClinicLocation();
		System.out.println("Debug: cliniclocation " + clinicLocation);

		/* to check whether the req is processed before or not */
		if (reqObj.getRequestID() == serverReplicaObj.lastReqProcessed + 1) {

			/* Loop to check location of server */
			if (clinicLocation.equals("MTL")) {
				responseObj = resolveRequest(reqObj, serverReplicaObj
						.getClinicObject("MTL"));
			} else if (clinicLocation.equals("LVL")) {
				responseObj = resolveRequest(reqObj, serverReplicaObj
						.getClinicObject("LVL"));
			} else if (clinicLocation.equals("DDO")) {
				responseObj = resolveRequest(reqObj, serverReplicaObj
						.getClinicObject("DDO"));
			}
			serverReplicaObj.setLastReqProcessed(reqObj.getRequestID());
		}
		return responseObj;
	}

	/* function to serialize final response and send it to front end */
	public void sendToFrontEnd(Response finalResponse) {

		try {

			System.out.println("Debug: sendToFrontEnd() response id :: "
					+ finalResponse.getRequestId() + " : "
					+ finalResponse.getResultStr());
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bs);

			os.writeObject(finalResponse);

			os.close();
			bs.close();
			DatagramSocket socket = new DatagramSocket();

			byte[] buf = bs.toByteArray();
			DatagramPacket finalResponsePacket = new DatagramPacket(buf,
					buf.length, requestPacket.getAddress(), requestPacket
							.getPort());
			/* Debug: print add and port of req pakcet */
			System.out.println("Debug: req add :" + InetAddress.getLocalHost()
					+ " and port " + requestPacket.getPort());
			socket.send(finalResponsePacket);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* function for non-group leader process */
	public void nonGroupLeaderTask() {
		Request req = null;
		while (true) {

			while (true) {
				synchronized (fifoObj.reqMap) {
					if (fifoObj.reqMap.containsKey(new Integer(ServerReplica
							.getLastReqProcessed() + 1))) {
						req = fifoObj.reqMap.get(new Integer(ServerReplica
								.getLastReqProcessed() + 1));
						break;
					}
				}
			}
			Response responseObj = myLocationServers(req);

			for (ServerConfig server : ServerReplica.configMap.values()) {
				if (server.serverId != serverReplicaObj.getServerId()
						&& server.isGroupLeader) {
					fifoObj.unicast(serializeObj(responseObj), server.serverId);
				}
			}
		}
	}

	/* function for non-group leader to send result back to Group Leader Process */
	public byte[] serializeObj(Object obj) {

		ByteArrayOutputStream bs = null;
		ObjectOutputStream os = null;

		byte[] b = new byte[2048];
		try {

			bs = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bs);
			os.writeObject(obj);
			b = bs.toByteArray();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return b;
	}

	/*
	 * function to resolve request using deserialized request object and
	 * ClinicServerImpl object
	 */
	public Response resolveRequest(Request reqObj, ClinicServerImpl serverObj) {
		System.out.println("Debug: In resolveRequest");
		Response responseObj = new Response(reqObj.getRequestID());
		String methodName = reqObj.getMethodName();

		switchFunc switchFuncObj = switchFunc.valueOf(methodName);

		switch (switchFuncObj) {

		case createDRecord: {
			responseObj.setMethodName("createDRecord");
			responseObj.setResultStr(serverObj.createDRecord(reqObj
					.getManagerID(), reqObj.getFirstName(), reqObj
					.getLastName(), reqObj.getAddress(), reqObj.getPhone(),
					reqObj.getSpecialization(), reqObj.getLocation()));
			return responseObj;

		}
		case createNRecord: {
			responseObj.setMethodName("createNRecord");
			responseObj.setResultStr(serverObj.createNRecord(reqObj
					.getManagerID(), reqObj.getFirstName(), reqObj
					.getLastName(), reqObj.getDesignation(), reqObj
					.getStatus_Date(), reqObj.getStatus()));
			return responseObj;
		}
		case editRecord: {
			responseObj.setMethodName("editRecord");
			responseObj.setResultStr(serverObj.editRecord(
					reqObj.getManagerID(), reqObj.getRecordID(), reqObj
							.getFieldName(), reqObj.getNewValue()));
			return responseObj;
		}
		case getCount: {
			responseObj.setMethodName("getCount");
			responseObj.setResultStr(serverObj.getRecordCounts(reqObj
					.getManagerID(), reqObj.getRecordType()));
			return responseObj;
		}
		case transferRecord: {
			responseObj.setMethodName("transferRecord");
			responseObj.setResultStr(serverObj
					.transferRecord(reqObj.getManagerID(),
							reqObj.getRecordID(), reqObj.getLocation()));
			return responseObj;
		}
		default: {
			System.out.println("This is default");
			return null;
		}
		}
	}

	/* Getters and Setters */
	public DatagramPacket getRequestPacket() {
		return requestPacket;
	}

	public void setRequestPacket(DatagramPacket requestPacket) {
		this.requestPacket = requestPacket;
	}

	public ServerReplica getServerReplicaObj() {
		return serverReplicaObj;
	}

	public void setServerReplicaObj(ServerReplica serverReplicaObj) {
		this.serverReplicaObj = serverReplicaObj;
	}

	public Request getReqObj() {
		return reqObj;
	}

	public void setReqObj(Request reqObj) {
		this.reqObj = reqObj;
	}

	public String getClinicLocation() {
		return clinicLocation;
	}

	public void setClinicLocation(String clinicLocation) {
		this.clinicLocation = clinicLocation;
	}

}
