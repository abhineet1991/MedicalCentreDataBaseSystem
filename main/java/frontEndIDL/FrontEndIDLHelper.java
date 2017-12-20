package frontEndIDL;

/** 
 * Helper class for : FrontEndIDL
 *  
 * @author OpenORB Compiler
 */ 
public class FrontEndIDLHelper
{
    /**
     * Insert FrontEndIDL into an any
     * @param a an any
     * @param t FrontEndIDL value
     */
    public static void insert(org.omg.CORBA.Any a, frontEndIDL.FrontEndIDL t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract FrontEndIDL from an any
     *
     * @param a an any
     * @return the extracted FrontEndIDL value
     */
    public static frontEndIDL.FrontEndIDL extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return frontEndIDL.FrontEndIDLHelper.narrow( a.extract_Object() );
        }
        catch ( final org.omg.CORBA.BAD_PARAM e )
        {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the FrontEndIDL TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "FrontEndIDL" );
        }
        return _tc;
    }

    /**
     * Return the FrontEndIDL IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:frontEndIDL/FrontEndIDL:1.0";

    /**
     * Read FrontEndIDL from a marshalled stream
     * @param istream the input stream
     * @return the readed FrontEndIDL value
     */
    public static frontEndIDL.FrontEndIDL read(org.omg.CORBA.portable.InputStream istream)
    {
        return(frontEndIDL.FrontEndIDL)istream.read_Object(frontEndIDL._FrontEndIDLStub.class);
    }

    /**
     * Write FrontEndIDL into a marshalled stream
     * @param ostream the output stream
     * @param value FrontEndIDL value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, frontEndIDL.FrontEndIDL value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to FrontEndIDL
     * @param obj the CORBA Object
     * @return FrontEndIDL Object
     */
    public static FrontEndIDL narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof FrontEndIDL)
            return (FrontEndIDL)obj;

        if (obj._is_a(id()))
        {
            _FrontEndIDLStub stub = new _FrontEndIDLStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to FrontEndIDL
     * @param obj the CORBA Object
     * @return FrontEndIDL Object
     */
    public static FrontEndIDL unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof FrontEndIDL)
            return (FrontEndIDL)obj;

        _FrontEndIDLStub stub = new _FrontEndIDLStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
