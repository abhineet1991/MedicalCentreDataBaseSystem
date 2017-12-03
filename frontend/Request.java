package com.dist.frontend;

import java.io.Serializable;

public class Request implements Serializable{
	private int RequestID;
	
	private String methodName;
	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	private String specialization;
	private String location;
	private String designation;
	private String status_Date;
	private String status;
	private String recordID;
	private String fieldName;
	private String newValue;
	private String recordType;
	private String managerID;
	private String clinicLocation;
	
	
	public Request(int RequestID,String methodName, String managerID , String fName, String lName, String add, String phn, String spclztn, String loc, String clinicLocation){
		this.RequestID = RequestID;
		this.managerID = managerID;
		this.firstName = fName;
		this.lastName = lName;
		this.address = add;
		this.phone = phn;
		this.specialization = spclztn;
		this.location = loc;
		this.methodName = methodName;
		this.clinicLocation = clinicLocation;
		System.out.println("Debug: In Request ctor: requestID: and name is "+RequestID + fName);
	}
	
	public Request (int RequestID,String methodName, String managerID ,String recordType, String clinicLocation){
		this.RequestID = RequestID;
		this.managerID = managerID;
		this.recordType = recordType;
		this.methodName = methodName;
		this.clinicLocation = clinicLocation;
	}
	
	public Request(){
		
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public int getRequestID() {
		return RequestID;
	}
	public void setRequestID(int requestID) {
		RequestID = requestID;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getStatus_Date() {
		return status_Date;
	}
	public void setStatus_Date(String status_Date) {
		this.status_Date = status_Date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRecordID() {
		return recordID;
	}
	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getManagerID() {
		return managerID;
	}

	public void setManagerID(String managerID) {
		this.managerID = managerID;
	}

	public String getClinicLocation() {
		return clinicLocation;
	}

	public void setClinicLocation(String clinicLocation) {
		this.clinicLocation = clinicLocation;
	}
	
	
	
	
	
}
