package com.dist.main;
//package com.dds.sms.main;
//
//import java.rmi.RemoteException;
//import java.util.Properties;
//
//import org.omg.CORBA.ORB;
//import org.omg.CosNaming.NameComponent;
//import org.omg.CosNaming.NamingContextExt;
//import org.omg.CosNaming.NamingContextExtHelper;
//import org.omg.PortableServer.POA;
//import org.omg.PortableServer.POAHelper;
//
//import ClinicServerIDLInterface.ClinicServerIDLInterface;
//import ClinicServerIDLInterface.ClinicServerIDLInterfaceHelper;
//
//import com.dds.sms.server.ClinicServer;
//
//public class MainClinic {
//
//	public static void main(String[] args) {
//		
//		try{
//			Properties objPP = System.getProperties();
//		      // create and initialize the ORB
//		      ORB orb = ORB.init(args, objPP);
//
//		      // get reference to rootpoa & activate the POAManager
//		      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
//		      rootpoa.the_POAManager().activate();
//
//		      // create servant and register it with the ORB
//		      /*Creating objects of ClinicServer for 3 locations: Montreal (MTL), Laval (LVL) and DDO*/
//				ClinicServer MTLserver;
//				ClinicServer LVLserver;
//				ClinicServer DDOserver;
//
//				/*Creating Registry and assigning to respective objects*/
//				
//				MTLserver = new ClinicServer("MTL",5002, 2525);
//				LVLserver = new ClinicServer("LVL",5002, 3030);
//				DDOserver = new ClinicServer("DDO",5002, 3535);
//
//				MTLserver.createDRecord("Shubham", "Singh", "1802", "123456", "asd", "MTL");
//				LVLserver.createDRecord("Shams", "Azad", "1803", "32145", "asd", "LVL");
//				DDOserver.createDRecord("Parth", "Patel", "1804", "7864745", "asd", "DDO");
//				
//				
//				/*Creating threads for above objects and starting*/
//				
//				Thread mtlThread = new Thread(MTLserver);
//				Thread lvlThread = new Thread(LVLserver);
//				Thread ddoThread = new Thread(DDOserver);
//
//				mtlThread.start();
//				lvlThread.start();
//				ddoThread.start();
//								     
//
//		      // get object reference from the servant
//		      org.omg.CORBA.Object ref1 = rootpoa.servant_to_reference(MTLserver);
//		      org.omg.CORBA.Object ref2 = rootpoa.servant_to_reference(LVLserver);
//		      org.omg.CORBA.Object ref3 = rootpoa.servant_to_reference(DDOserver);
//
//		      ClinicServerIDLInterface href1 = ClinicServerIDLInterfaceHelper.narrow(ref1);
//		      ClinicServerIDLInterface href2 = ClinicServerIDLInterfaceHelper.narrow(ref2);
//		      ClinicServerIDLInterface href3 = ClinicServerIDLInterfaceHelper.narrow(ref3);
//		          
//		      // get the root naming context
//		      // NameService invokes the name service
//		      	org.omg.CORBA.Object objRef =
//		        orb.resolve_initial_references("NameService");
//		      // Use NamingContextExt which is part of the Interoperable
//		      // Naming Service (INS) specification.
//		      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
//
//		      // bind the Object Reference in Naming
////		      NameComponent path1[] = ncRef.to_name( MTLserver.getLocation() );
////		      NameComponent path2[] = ncRef.to_name( LVLserver.getLocation() );
////		      NameComponent path3[] = ncRef.to_name( DDOserver.getLocation() );
////		     
////		      ncRef.rebind(path1, href1);
////		      ncRef.rebind(path2, href2);
////		      ncRef.rebind(path3, href3);
//
//		      System.out.println("Clinic Server ready and waiting ...");
//
//		      // wait for invocations from clients
//		      orb.run();
//		    } 
//		        
//		      catch (Exception e) {
//		        System.err.println("ERROR: " + e);
//		        e.printStackTrace(System.out);
//		      }
//		          
//		    //  System.out.println("Clinic Server Exiting ...");
//		        
//		  }
//
//		
//
//}
