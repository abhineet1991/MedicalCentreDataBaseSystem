package com.dist.frontend;

import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.ORB;

import com.dist.communication.CrashResponse;
import com.dist.communication.FECommunication;
import com.dist.config.ServerConfig;

import frontEndIDL.FrontEndIDLPOA;

public class FrontEnd extends FrontEndIDLPOA{
	
	private String clinicLocation;
	private CrashResponse crashResponseObj;
	private int groupLeader;
	public static int RequestID;
	public static Map<Integer, ServerConfig> configMap = new HashMap<Integer, ServerConfig>();
	private ORB orb;
	
	public FrontEnd(){
		initConfigMap();
		//this.clinicLocation = clinicLocation;
		this.crashResponseObj = new CrashResponse(this);
		
		Thread thread = new Thread(crashResponseObj);
		thread.start();
	}
	
	public void setORB(ORB orb_val) {
		orb = orb_val;
	}
	
	public static void initConfigMap() {
		ServerConfig server1 = new ServerConfig(1, 5100, 5200, 5300, 4000, true);
		ServerConfig server2 = new ServerConfig(2, 6100, 6200, 6300, 4000, false);
		ServerConfig server3 = new ServerConfig(3, 7100, 7200, 7300, 4000, false);
	
		configMap.put(server1.serverId, server1);
		configMap.put(server2.serverId, server2);
		configMap.put(server3.serverId, server3);
	}
	/*Function to send packet for a doctor record. Input: Doctor details, Output: Doctor obj packet, Updated log file*/
	public String createDRecord (String managerID, String fName, String lName, String add, String phn, String spclztn, String loc){
		setClinicLocation(managerID);	
		
		Response responseObj = null;
		Request reqObj = new Request(giveID(), "createDRecord",managerID, fName, lName, add,  phn, spclztn, loc, clinicLocation);
		
		System.out.println("Debug: In FrontEnd createDRecord printing sent data: requestID : "+getRequestID()+fName+lName+ phn);
		
		FECommunication feCommunicationObj = new FECommunication(reqObj);
		feCommunicationObj.send();
		responseObj = feCommunicationObj.recieve();
		return responseObj.getResultStr();
		
	}
	
	/*Function to create a nurse record. Input: Doctor details, Output: Doctor obj, Updated log file*/
	public String	createNRecord (String managerID, String fName, String lName, String desig,String stat_Date, String stat){
		setClinicLocation(managerID);
		
		Response responseObj = null;
		Request reqObj = new Request (giveID(),"createNRecord",managerID,fName, lName, desig,stat_Date, stat, clinicLocation);
		FECommunication feCommunicationObj = new FECommunication(reqObj);
		feCommunicationObj.send();
		responseObj = feCommunicationObj.recieve();
		return responseObj.getResultStr();
		
	}
	
	public String editRecord (String managerID, String recordID, String fieldName, String newValue){
		setClinicLocation(managerID);
		
		Response responseObj = null;
		Request reqObj = new Request (giveID(),"editRecord",managerID,recordID, fieldName, newValue, clinicLocation);
		FECommunication feCommunicationObj = new FECommunication(reqObj);
		feCommunicationObj.send();
		responseObj = feCommunicationObj.recieve();
		return responseObj.getResultStr();
		
	}
	
	public String getCount(String managerID, String recordType){
		setClinicLocation(managerID);
		
		Response responseObj = null;
		Request reqObj = new Request (giveID(),"getCount",managerID, recordType, clinicLocation);
		FECommunication feCommunicationObj = new FECommunication(reqObj);
		feCommunicationObj.send();
		responseObj = feCommunicationObj.recieve();
		return responseObj.getResultStr();

	}
	
	public String transferRecord(String managerID,String recordID, String location){
		setClinicLocation(managerID);
		
		Request reqObj = new Request();
		Response responseObj = null;
		
		reqObj.setRequestID(giveID());
		reqObj.setManagerID(managerID);
		reqObj.setRecordID(recordID);
		reqObj.setLocation(location);
		reqObj.setClinicLocation(clinicLocation);
		reqObj.setMethodName("transferRecord");
		
		FECommunication feCommunicationObj = new FECommunication(reqObj);
		feCommunicationObj.send();
		responseObj = feCommunicationObj.recieve();
		return responseObj.getResultStr();
		
	}

	public synchronized static int giveID(){
		return ++RequestID;
		
	}
	public static int getRequestID() {
		return RequestID;
	}

	public static void setRequestID() {
		giveID();
		//System.out.println("Debug: In setREquestID reuqestID: "+RequestID +"and giveID"+ giveID());
	}

	public String getClinicLocation() {
		return clinicLocation;
	}

	public void setClinicLocation(String managerID) {
		if(managerID.contains("MTL")){
			clinicLocation = "MTL";
		}
		else if(managerID.contains("LVL")){
			clinicLocation = "LVL";
		}
		else if(managerID.contains("DDO")){
			clinicLocation = "DDO";
		}
	}

	public CrashResponse getCrashResponseObj() {
		return crashResponseObj;
	}

	public void setCrashResponseObj(CrashResponse crashResponseObj) {
		this.crashResponseObj = crashResponseObj;
	}

	public int getGroupLeader() {
		return groupLeader;
	}

	public void setGroupLeader(int groupLeader) {
		this.groupLeader = groupLeader;
	}
	
	
}
