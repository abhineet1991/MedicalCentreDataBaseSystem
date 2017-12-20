package com.dist.main;

import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import frontEndIDL.FrontEndIDL;
import frontEndIDL.FrontEndIDLHelper;

/** 
 * @author abhineet.gupta
 * studentId 012426427
 */

public class DSMSClientFrontEnd {
	private static Logger log = Logger.getLogger(DSMSClientFrontEnd.class
			.getName());
	private static FileHandler fh;

	// Return basic menu.
	public static void showMenu() {
		System.out.println("\n********\n");
		System.out.println("Please select an option (1-5)");
		System.out.println("1.Create Doctor Record");
		System.out.println("2.Create Nurse Record ");
		System.out.println("3.Get Records Count ");
		System.out.println("4.Edit Record ");
		System.out.println("5.Transfer Record ");
		System.out.println("6.Logout");
	}
	
	// Return server choose menu.
	public static void serverMenu() {
		System.out.println("\n****Welcome to Staff Management System****\n");
		System.out.println("Please select an option (1-2)");
		System.out.println("1.Login");
		System.out.println("2.Exit");
	}
	

	public static void main(String[] args) {
		try {
			/*fh = new FileHandler("logs/client/DSMSClientMtl.log");
			log.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);*/

			log.info("Inside main method of DSMSClient class");

			ORB orb = ORB.init(args, null);
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			FrontEndIDL server = FrontEndIDLHelper.narrow(ncRef.resolve_str("FEService"));;

			int userChoice1 = 0;
			int userChoice2 = 0;
			Scanner keyboard = new Scanner(System.in);
			serverMenu();
			String managerId = "";
			while (true) {
				Boolean valid1 = false;
				// Enforces a valid integer input.
				while (!valid1) {
					try {
						userChoice1 = keyboard.nextInt();
						valid1 = true;
					} catch (Exception e) {
						System.out.println("Invalid Input, please enter an Integer");
						valid1 = false;
						keyboard.nextLine();
					}
				}
				
				switch(userChoice1) {
				case 1: 
					System.out.println("Enter Manager ID: ");
					managerId = keyboard.next();
					if(!(managerId.contains("MTL") || managerId.contains("LVL") ||
							managerId.contains("DDO"))) {
						System.out.println("Invalid manager id");
						serverMenu();
						continue;
					}
					break;
				case 2:
					System.exit(0);
					break;
				default:
					System.out.println("Invalid Input, please enter a choice");
					serverMenu();
					continue;
				}
				
				boolean loggedIn = true;
				showMenu();
				
				while (loggedIn) {
					Boolean valid2 = false;

					// Enforces a valid integer input.
					while (!valid2) {
						try {
							userChoice2 = keyboard.nextInt();
							valid2 = true;
						} catch (Exception e) {
							System.out
									.println("Invalid Input, please enter an Integer");
							valid2 = false;
							keyboard.nextLine();
						}
					}

					// Manage user selection.
					switch (userChoice2) {
					case 1: {
						log.info(" Create Doctor Record option selected ");
						String firstName = "";
						String lastName = "";
						String address = "";
						String phone = "";
						String specialization = "";
						String location = "";						
						System.out.println("Please enter First Name");
						firstName = keyboard.next();

						System.out.println("Please enter Last Name");
						lastName = keyboard.next();

						System.out.println("Please enter Address");
						address = keyboard.next();

						System.out.println("Please enter Phone No.");
						phone = keyboard.next();

						System.out.println("Please enter Specialization");
						specialization = keyboard.next();

						System.out.println("Please enter Location");
						location = keyboard.next();
						String result = server.createDRecord(managerId, firstName,
								lastName, address, phone, specialization,
								location);
						if (!"fail".equals(result)) {
							log
									.info("Doctor Record created successfully with Record ID : "
											+ result);
							System.out
									.println("Doctor Record created successfully with Record ID : "
											+ result);
						} else {
							log.info("Failed to create record.");
						}
						showMenu();
						break;
					}
					case 2: {
						log.info(" Create Nurse Record option selected ");
						String firstName = "";
						String lastName = "";
						String designation = "";
						String status = "";
						String statusDate = "";
						System.out.println("Please enter First Name");
						firstName = keyboard.next();

						System.out.println("Please enter Last Name");
						lastName = keyboard.next();

						System.out.println("Please enter Designation.");
						designation = keyboard.next();

						System.out.println("Please enter Status");
						status = keyboard.next();

						System.out.println("Please enter Status Date.");
						statusDate = keyboard.next();

						String result = server.createNRecord(managerId, firstName,
								lastName, designation, status, statusDate);
						if (!"fail".equals(result)) {
							log
									.info("Nurse Record created successfully with Record ID : "
											+ result);
							System.out
									.println("Nurse Record created successfully with Record ID : "
											+ result);
						} else {
							log.info("Failed to create record.");
						}
						showMenu();
						break;
					}
					case 3: {
						log.info(" Get Records Count option selected ");
						String recordType = "";
						System.out.println("Please Enter Record Type.");
						recordType = keyboard.next();

						String count = server.getCount(managerId, recordType);
						if (!"fail".equals(count)) {
							log.info("Records Count is " + count);
							System.out.println("Records Count is " + count);
						} else {
							log.info("Failed to get count of records.");
						}

						showMenu();
						break;
					}
					case 4: {
						log.info(" Edit Records  option selected ");
						String recordID = "";
						String fieldName = "";
						String newValue = "";
						System.out.println("Please Enter Record Id.");
						recordID = keyboard.next();

						System.out.println("Please Enter Field Name.");
						fieldName = keyboard.next();

						System.out.println("Please Enter new Value.");
						newValue = keyboard.next();

						String result = server.editRecord(managerId, recordID, fieldName,
								newValue);
						if (!"fail".equals(result)) {
							log
									.info("Record edited successfully with Record ID : "
											+ result);
							System.out
									.println("Record edited successfully with Record ID : "
											+ result);
						} else {
							log.info("Failed to edit record.");
						}

						showMenu();
						break;
					}
					case 5: {
						log.info(" Transfer Record option selected ");
						String recordId = "";
						String remoteClinicServerName = "";
					
						System.out.println("Please Enter Record Id.");
						recordId = keyboard.next();

						System.out.println("Please Remote Clinic Server Name.");
						remoteClinicServerName = keyboard.next();

						String result = server.transferRecord(managerId, recordId, remoteClinicServerName);
						if (!"fail".equals(result)) {
							log
									.info("Record Transfered successfully at remote clinic to Record ID : "
											+ result);
							System.out
									.println("Record Transfered successfully  at remote clinic to Record ID : "
											+ result);
						} else {
							log.info("Failed to edit record.");
						}

						showMenu();
						break;
					}
					case 6:
						log.info(" Logout option selected ");
						loggedIn = false;
						break;
					default:
						log.info("Invalid Input, please try again.");
						System.out.println("Invalid Input, please try again.");
					}
				}
				serverMenu();
			}
		} catch (Exception e) {
			log.info("Error in DSMSClient class " + e);
			e.printStackTrace();

		}
	}
}
