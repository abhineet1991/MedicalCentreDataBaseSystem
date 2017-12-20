package com.medicalcentre.beans;

/** 
 * @author abhineet.gupta
 * studentId 012426427
 */
public class DRecord extends Record{
	private String address;
	private String phone;
	private String specialization;
	private String location;
	
	public static final String LOCATION_SAN_JOSE = "MTL";
	public static final String LOCATION_SAN_FRANSISCO = "LVL";
	public static final String LOCATION_DDO = "DDO";

	public static final String FIELDNAME_LOCATION = "Location";
	public static final String FIELDNAME_ADDRESS = "Address";
	public static final String FIELDNAME_PHONE = "Phone";
	
	public DRecord(String recordId, String firstName, String lastName, String address, String phone,
			String specialization, String location) {
		super(recordId, firstName, lastName);
		this.address = address;
		this.phone = phone;
		this.specialization = specialization;
		this.location = location;
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
}
