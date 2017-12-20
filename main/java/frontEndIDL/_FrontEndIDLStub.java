package frontEndIDL;

/**
 * Interface definition: FrontEndIDL.
 * 
 * @author OpenORB Compiler
 */
public class _FrontEndIDLStub extends org.omg.CORBA.portable.ObjectImpl
        implements FrontEndIDL
{
    static final String[] _ids_list =
    {
        "IDL:frontEndIDL/FrontEndIDL:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = frontEndIDL.FrontEndIDLOperations.class;

    /**
     * Operation createDRecord
     */
    public String createDRecord(String managerID, String fname, String lname, String address, String phone, String specialization, String location)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("createDRecord",true);
                    _output.write_string(managerID);
                    _output.write_string(fname);
                    _output.write_string(lname);
                    _output.write_string(address);
                    _output.write_string(phone);
                    _output.write_string(specialization);
                    _output.write_string(location);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("createDRecord",_opsClass);
                if (_so == null)
                   continue;
                frontEndIDL.FrontEndIDLOperations _self = (frontEndIDL.FrontEndIDLOperations) _so.servant;
                try
                {
                    return _self.createDRecord( managerID,  fname,  lname,  address,  phone,  specialization,  location);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation createNRecord
     */
    public String createNRecord(String managerID, String fname, String lname, String designation, String status, String statusDate)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("createNRecord",true);
                    _output.write_string(managerID);
                    _output.write_string(fname);
                    _output.write_string(lname);
                    _output.write_string(designation);
                    _output.write_string(status);
                    _output.write_string(statusDate);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("createNRecord",_opsClass);
                if (_so == null)
                   continue;
                frontEndIDL.FrontEndIDLOperations _self = (frontEndIDL.FrontEndIDLOperations) _so.servant;
                try
                {
                    return _self.createNRecord( managerID,  fname,  lname,  designation,  status,  statusDate);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getCount
     */
    public String getCount(String managerID, String recordType)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getCount",true);
                    _output.write_string(managerID);
                    _output.write_string(recordType);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getCount",_opsClass);
                if (_so == null)
                   continue;
                frontEndIDL.FrontEndIDLOperations _self = (frontEndIDL.FrontEndIDLOperations) _so.servant;
                try
                {
                    return _self.getCount( managerID,  recordType);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation editRecord
     */
    public String editRecord(String managerID, String recordID, String fieldName, String newValue)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("editRecord",true);
                    _output.write_string(managerID);
                    _output.write_string(recordID);
                    _output.write_string(fieldName);
                    _output.write_string(newValue);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("editRecord",_opsClass);
                if (_so == null)
                   continue;
                frontEndIDL.FrontEndIDLOperations _self = (frontEndIDL.FrontEndIDLOperations) _so.servant;
                try
                {
                    return _self.editRecord( managerID,  recordID,  fieldName,  newValue);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation transferRecord
     */
    public String transferRecord(String managerID, String recordID, String remoteClinicServer)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("transferRecord",true);
                    _output.write_string(managerID);
                    _output.write_string(recordID);
                    _output.write_string(remoteClinicServer);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("transferRecord",_opsClass);
                if (_so == null)
                   continue;
                frontEndIDL.FrontEndIDLOperations _self = (frontEndIDL.FrontEndIDLOperations) _so.servant;
                try
                {
                    return _self.transferRecord( managerID,  recordID,  remoteClinicServer);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
