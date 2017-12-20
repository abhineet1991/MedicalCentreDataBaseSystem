package frontEndIDL;

/**
 * Interface definition: FrontEndIDL.
 * 
 * @author OpenORB Compiler
 */
public interface FrontEndIDLOperations
{
    /**
     * Operation createDRecord
     */
    public String createDRecord(String managerID, String fname, String lname, String address, String phone, String specialization, String location);

    /**
     * Operation createNRecord
     */
    public String createNRecord(String managerID, String fname, String lname, String designation, String status, String statusDate);

    /**
     * Operation getCount
     */
    public String getCount(String managerID, String recordType);

    /**
     * Operation editRecord
     */
    public String editRecord(String managerID, String recordID, String fieldName, String newValue);

    /**
     * Operation transferRecord
     */
    public String transferRecord(String managerID, String recordID, String remoteClinicServer);

}
