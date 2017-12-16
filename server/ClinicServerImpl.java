package com.a2.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import com.a2.logging.Logger;
import com.dist.ServerReplica.ServerReplica;
import com.medicalcentre.beans.DRecord;
import com.medicalcentre.beans.NRecord;
import com.medicalcentre.beans.Record;
/** 
 * @author abhineet.gupta
 * studentId 012426427
 */

public class ClinicServerImpl implements Runnable {
	private HashMap<Character, ArrayList<Record>> recordMap = new HashMap<Character, ArrayList<Record>>();
	private Thread t;

	private Logger logger;
	private long id = 0;
	private String udpServerThreadName;
	private int udpServerPort;
	private String serverName;
	private int udpPeerServerPort1;
	private int udpPeerServerPort2;
	
	private String resultGetRecordCounts = "fail";
	private String res1;
	private String res2;
	private String res3;
	private ServerReplica servRep;
	private enum EditableFunc {
		Address, Phone, Location, Designation, Status, StatusDate
	}
	
	/*
	 * Create map with key-value pairs where key is each character from A-Z and
	 * value is empty list
	 */
	public ClinicServerImpl(String serverName, int udpServerPort, int udpPeerServerPort1, int udpPeerServerPort2, ServerReplica servRep) {
		logger = new Logger("logs/server/ClinicServer"+serverName+".log");
		for (int i = 65; i <= 90; i++) {
			recordMap.put(new Character((char) i), new ArrayList<Record>());
		}
		this.serverName = serverName;
		this.udpServerPort = udpServerPort;
		this.udpServerThreadName = serverName + "UdpThread";
		this.udpPeerServerPort1 = udpPeerServerPort1;
		this.udpPeerServerPort2 = udpPeerServerPort2;
		this.servRep = servRep;
		this.start();
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/*
	 * Creates new Doctor record
	 */
	public String createDRecord(String managerId, String firstName, String lastName, String address, String phone, String specialization,
			String location) {
		log("Start ClinicServerImpl:: createDRecord() method");
		log("firstName : " + firstName);
		log("lastName : " + lastName);
		log("address : " + address);
		log("phone : " + phone);
		log("specialization : " + specialization);
		log("location : " + location);
		String result = "fail";
		if (location.equalsIgnoreCase(DRecord.LOCATION_MONTREAL) || location.equalsIgnoreCase(DRecord.LOCATION_LAVAL)
				|| location.equalsIgnoreCase(DRecord.LOCATION_DDO)) {

			String recordId = "DR" + getNextId();
			Record drecord = new DRecord(recordId, firstName, lastName, address, phone, specialization, location);

			ArrayList<Record> list = recordMap.get(getFirstChar(lastName));

			// list object is synchronized before adding a record handling
			// concurrency issues
			synchronized (list) {
				list.add(drecord);
				result = recordId;
			}
			log("Record created with id : " + recordId);
		}
		log("Start ClinicServerImpl:: createDRecord() method");
		return result;
	}

	/*
	 * Creates new Nurse record
	 */
	public String createNRecord(String managerId, String firstName, String lastName, String designation, String status, String statusDate) {
		log("Start ClinicServerImpl:: createNRecord() method");
		log("firstName : " + firstName);
		log("designation : " + designation);
		log("status : " + status);
		log("statusDate : " + statusDate);

		String result = "fail";
		if ((designation.equals(NRecord.DESIG_JR) || designation.equals(NRecord.DESIG_SR))
				&& (status.equals(NRecord.STATUS_ACTIVE) || status.equals(NRecord.STATUS_TERMINATED))) {
			String recordId = "NR" + getNextId();
			Record nrecord = new NRecord(recordId, firstName, lastName, designation, status, statusDate);

			ArrayList<Record> list = recordMap.get(getFirstChar(lastName));

			// list object is synchronized before adding a record handling
			// concurrency issues
			synchronized (list) {
				list.add(nrecord);
				result = recordId;
			}
			log("Record created with id : " + recordId);
		}
		log("Start ClinicServerImpl:: createNRecord() method");
		return result;
	}

	/*
	 * Returns the counts of record with given recordtype from all the servers
	 */
	public String getRecordCounts(String managerId, final String recordType) {
		log("Start ClinicServerImpl:: getRecordCounts() method");
		log("RecordType : " + recordType);

		Thread myThreads[] = new Thread[2];

		// UDP Client thread for server 1
		myThreads[0] = new Thread(new Runnable() {
			public void run() {
				DatagramSocket aSocket = null;
				try {
					// Send request for getRecordCount
					log("Send request to server 2 for getRecordCount");
					aSocket = new DatagramSocket();
					byte[] m = recordType.getBytes();
					InetAddress aHost = InetAddress.getByName("localhost");
					int serverPort = udpPeerServerPort1;
					DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
					aSocket.send(request);
					byte[] buffer = new byte[1000];
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(reply);
					res2 = new String(reply.getData(), 0, reply.getLength());
					log("Server 2 RecordCount : " + res2);
				} catch (SocketException e) {
					log("ClinicServerImpl:: getRecordCounts(): " + e.getMessage());
					resultGetRecordCounts = "fail";
				} catch (IOException e) {
					log("ClinicServerImpl:: getRecordCounts(): " + e.getMessage());
					resultGetRecordCounts = "fail";
				} finally {
					if (aSocket != null)
						aSocket.close();
				}
			}
		});

		// UDP Client thread for server 2
		myThreads[1] = new Thread(new Runnable() {
			public void run() {
				DatagramSocket aSocket = null;
				try {
					// Send request for getRecordCount
					log("Send request to server 2 for getRecordCount");
					aSocket = new DatagramSocket();
					byte[] m = recordType.getBytes();
					InetAddress aHost = InetAddress.getByName("localhost");
					int serverPort = udpPeerServerPort2;
					DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
					aSocket.send(request);
					byte[] buffer = new byte[1000];
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(reply);
					res3 = new String(reply.getData(), 0, reply.getLength());
					log(" Server 2 RecordCount : " + res3);
				} catch (SocketException e) {
					log("ClinicServerImpl:: getRecordCounts(): " + e.getMessage());
					resultGetRecordCounts = "fail";
				} catch (IOException e) {
					log("ClinicServerImpl:: getRecordCounts(): " + e.getMessage());
					resultGetRecordCounts = "fail";
				} finally {
					if (aSocket != null)
						aSocket.close();
				}
			}
		});

		// starts the thread
		for (Thread thread : myThreads) {
			thread.start();
		}

		// to make current thread wait for the completion of the client threads
		// to get the respective count
		for (Thread thread : myThreads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		res1 = Integer.toString(getLocalRecordCount(recordType));
		res1 = serverName + " : " + res1;
		log("Server 1 RecordCount : " + res1);
		resultGetRecordCounts = res1 + ", " + res2 + ", " + res3;

		log("Result Total Record Count : " + resultGetRecordCounts);
		log("End ClinicServerImpl:: getRecordCounts() method");
		return resultGetRecordCounts;
	}

	/*
	 * Edits a field of record with given recordID
	 */
	public String editRecord(String managerId, String recordID, String fieldName, String newValue) {
		log("Start ClinicServerImpl:: getLocalRecordCount() method");
		log("recordID: " + recordID);
		log("fieldName: " + fieldName);
		log("newValue: " + newValue);
		String[] editableFields = { "Address", "Phone", "Location", "Designation", "Status", "Status Date" };
		String result = "fail";
		if (Arrays.asList(editableFields).contains(fieldName)) {
			ArrayList<ArrayList<Record>> listList = new ArrayList<ArrayList<Record>>(recordMap.values());
			boolean finished = false;
			for (Iterator iterator = listList.iterator(); (iterator.hasNext()) && !finished;) {
				ArrayList<Record> arrayList = (ArrayList<Record>) iterator.next();
				for (Iterator iterator2 = arrayList.iterator(); iterator2.hasNext();) {
					Record record = (Record) iterator2.next();
					if (record.getRecordId().equals(recordID)) {
						// synchronized block for record
						EditableFunc func = EditableFunc.valueOf(fieldName);
						synchronized (record) {
							switch (func) {
							case Address:
								((DRecord) record).setAddress(newValue);
								result = record.getRecordId();
								break;
							case Phone:
								((DRecord) record).setPhone(newValue);
								result = record.getRecordId();
								break;
							case Location:
								if (newValue.equalsIgnoreCase(DRecord.LOCATION_MONTREAL)
										|| newValue.equalsIgnoreCase(DRecord.LOCATION_LAVAL)
										|| newValue.equalsIgnoreCase(DRecord.LOCATION_DDO)) {
									((DRecord) record).setLocation(newValue);
									result = record.getRecordId();
								}
								break;
							case Designation:
								if (newValue.equals(NRecord.DESIG_JR) || newValue.equals(NRecord.DESIG_SR)) {
									((NRecord) record).setDesignation(newValue);
									result = record.getRecordId();
								}
								break;
							case Status:
								if (newValue.equals(NRecord.STATUS_ACTIVE)
										|| newValue.equals(NRecord.STATUS_TERMINATED)) {
									((NRecord) record).setStatus(newValue);
									result = record.getRecordId();
								}
								break;
							case StatusDate:
								((NRecord) record).setStatusDate(newValue);
								result = record.getRecordId();
								break;
							default:
								break;
							}
						}
						finished = true;
						break;
					}
				}
			}
		}
		log("Result : " + result);
		log("End ClinicServerImpl:: editRecord() method");
		return result;
	}

	/*
	 * gets record count with record type for this server
	 */
	public synchronized int  getLocalRecordCount(String recordType) {
		log("Start ClinicServerImpl:: getLocalRecordCount() method");
		int count = 0;
		log("ClinicServerImpl:: getLocalRecordCount() ::recordType" + recordType);
		ArrayList<ArrayList<Record>> listList = new ArrayList<ArrayList<Record>>(recordMap.values());
		for (Iterator iterator = listList.iterator(); iterator.hasNext();) {
			ArrayList<Record> arrayList = (ArrayList<Record>) iterator.next();
			for (Iterator iterator2 = arrayList.iterator(); iterator2.hasNext();) {
				Record record = (Record) iterator2.next();
				if (record.getRecordId().contains(recordType)) {
					count++;
				}
			}
		}
		log("ClinicServerImpl:: getLocalRecordCount() :: Local Record count (" + recordType + ")" + count);
		log("End ClinicServerImpl:: getLocalRecordCount() method");
		return count;
	}

	/*
	 * generates sequence id for record
	 */
	public synchronized long getNextId() {
		id++;
		return id;
	}
	
	/*
	 * generates record with given recordId to the remoteClinicServer
	 */
	public String transferRecord(String managerId, String recordId, String remoteClinicServerName) {
		log("Start ClinicServerImpl:: transferRecord() method");
		log("managerId : " + managerId);
		log("recordId : " + recordId);
		log("remoteClinicServerName : " + remoteClinicServerName);
		
		String result = "fail";
		try 
		{
			ArrayList<ArrayList<Record>> listList = new ArrayList<ArrayList<Record>>(recordMap.values());
			boolean finished = false;
			for (Iterator iterator = listList.iterator(); (iterator.hasNext()) && !finished;) {
				ArrayList<Record> arrayList = (ArrayList<Record>) iterator.next();
				for (int i = 0; i < arrayList.size() ; i++) {
					Record record = (Record) arrayList.get(i);
					if (record.getRecordId().equals(recordId)) {
						log("Record found with id : " + recordId);
						// synchronized block for record
						synchronized (record) 
						{
							 ClinicServerImpl server = servRep.getClinicObject(remoteClinicServerName);
							if(server != null) 
							{
								String reply = "";
								if(recordId.contains("DR")) {
									log("Request sent to " + remoteClinicServerName + " server to create doctor record");
									reply = server.createDRecord(managerId, record.getFirstName(), record.getLastName(), 
											((DRecord)record).getAddress(), ((DRecord)record).getPhone(),
											((DRecord)record).getSpecialization(), ((DRecord)record).getLocation());
								} else if (recordId.contains("NR")) {
									log("Request sent to " + remoteClinicServerName + " server to create nurse record");
									reply = server.createNRecord(managerId, record.getFirstName(), record.getLastName(), 
											((NRecord)record).getDesignation(), ((NRecord)record).getStatus(),
											((NRecord)record).getStatusDate());
								}
								
								if(!"fail".equals(reply) || !"".equals(reply)) {
									log("Record create at remote server " + remoteClinicServerName);
									arrayList.remove(record);
									log("Record deleted at local server ");
									result = reply;
								}
							}
						}
						finished = true;
						break;
					}
				}
			}
		} catch(Exception e) {
			System.out.println("Error in Manager class " + e);  
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * return the first char in upper case
	 */
	private Character getFirstChar(String str) {
		Character ch = new Character(Character.toUpperCase(str.charAt(0)));
		return ch;
	}
	
	/*
	 * Logs 
	 */
	public void log(String logInfo) {
		logInfo = serverName + " server ::: " + logInfo;
		logger.info(logInfo);
	}
	
	/*
	 * Starts the UDP Server thread
	 */
	public void start() {
		log("Start ClinicServerImpl:: start() method");

		if (t == null) {
			t = new Thread(this, udpServerThreadName);
			t.start();
			log("ClinicServerImpl:: start() ::UDP Server Thread Started");
		}
		log("End ClinicServerImpl:: start() method");
	}

	/*
	 * Thread for UDP Server
	 */
	public void run() {
		log("Start ClinicServerImpl:: run() method");
		DatagramSocket asocket = null;
		try {
			asocket = new DatagramSocket(udpServerPort);

			while (true) {
				byte[] m = new byte[1000];
				DatagramPacket request = new DatagramPacket(m, m.length);
				asocket.receive(request);
				String reqStr = new String(request.getData(), 0, request.getLength());
				log("ClinicServerImpl:: run() :: Request for getRecordCount of type " + reqStr);
				int count = 0;
				if (reqStr.equals("NR") || reqStr.equals("DR")) {
					count = getLocalRecordCount(reqStr);
				}
				byte[] buffer = new byte[1000];
				String res = serverName + " : " + Integer.toString(count);
				log("ClinicServerImpl:: run() :: RecordCount sent: " + res);
				buffer = res.getBytes();
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length, request.getAddress(),
						request.getPort());
				asocket.send(reply);
			}
		} catch (SocketException e) {
			log("ClinicServerImpl:: run() method:" + e.getMessage());
		} catch (IOException e) {
			log("ClinicServerImpl:: run() method:" + e.getMessage());
		} finally {
			if (asocket != null) {
				asocket.close();
			}
		}
		log("End ClinicServerImpl:: run() method");
	}
}