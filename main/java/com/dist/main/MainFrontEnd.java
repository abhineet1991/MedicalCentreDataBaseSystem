package com.dist.main;

import com.dist.frontend.FrontEnd;

public class MainFrontEnd {

	public static void main(String[] args) {
		
		FrontEnd frontEndObj = new FrontEnd();
		String r1 = frontEndObj.createDRecord("MTL1001", "Abhineet", "Gupta", "asd", "123", "sdf", "MTL");
		printRes(r1);
		String r2 =frontEndObj.createNRecord("MTL1001", "n1", "an1", "junior", "active", "21/05/2015");
		printRes(r2);
		String r3 = frontEndObj.createDRecord("MTL1002", "Shashank", "Singh", "asd", "123", "sdf", "MTL");
		printRes(r3);
		String r4 = frontEndObj.createNRecord("MTL1002", "n2", "en1", "senior", "terminated", "21/05/2015");;
		printRes(r4);
		String r6 = frontEndObj.transferRecord("MTL1001", "DR1", "LVL");
		printRes(r6);
		String r5 = frontEndObj.getCount("MTL1001", "DR");
		printRes(r5);
	}
	
	static void printRes(String res)
	{
		if(!"fail".equals(res)) {
			System.out.println("Result :: " +res);
		}
		else {
			System.out.println("Failed to create record.");
		}
	}

}
