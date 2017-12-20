package com.dist.frontend;

import java.io.Serializable;

public class Response implements Serializable{
	private int requestId;
	
	public Response(int requestId) {
		super();
		this.requestId = requestId;
	}
	
	private String resultStr;
	private boolean doctorAdded;
	private boolean nurseAdded;
	private boolean recordEdited;
	private boolean recordTransfered;
	private int getCount;
	private String methodName;
	
	
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public String getResultStr() {
		return resultStr;
	}
	public void setResultStr(String resultStr) {
		this.resultStr = resultStr;
		if(resultStr != "fail")
		{
			doctorAdded = true;
		}
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public boolean isDoctorAdded() {
		return doctorAdded;
	}
	public void setDoctorAdded(boolean doctorAdded) {
		this.doctorAdded = doctorAdded;
	}
	public boolean isNurseAdded() {
		return nurseAdded;
	}
	public void setNurseAdded(boolean nurseAdded) {
		this.nurseAdded = nurseAdded;
	}
	public boolean isRecordEdited() {
		return recordEdited;
	}
	public void setRecordEdited(boolean recordEdited) {
		this.recordEdited = recordEdited;
	}
	public boolean isRecordTransfered() {
		return recordTransfered;
	}
	public void setRecordTransfered(boolean recordTransfered) {
		this.recordTransfered = recordTransfered;
	}
	public int getGetCount() {
		return getCount;
	}
	public void setGetCount(int getCount) {
		this.getCount = getCount;
	}
	
	
	
}
