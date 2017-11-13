package com.medicalcentre.beans;

/** 
 * @author abhineet.gupta
 * studentId 012426427
 */

public class NRecord extends Record{
	private String designation;
	private String status;
	private String statusDate;
	
	public static final String DESIG_JR = "junior";
	public static final String DESIG_SR = "senior";
	public static final String STATUS_ACTIVE = "active";
	public static final String STATUS_TERMINATED = "terminated";
	
	public static final String FIELDNAME_DESIG = "Designation";
	public static final String FIELDNAME_STATUS = "Status";
	public static final String FIELDNAME_STATUS_DATE = "StatusDate";
	
	public NRecord(String recordId, String firstName, String lastName, String designation, String status,
			String statusDate) {
		super(recordId, firstName, lastName);
		this.designation = designation;
		this.status = status;
		this.statusDate = statusDate;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}
}
