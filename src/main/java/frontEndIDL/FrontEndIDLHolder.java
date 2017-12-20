package frontEndIDL;

/**
 * Holder class for : FrontEndIDL
 * 
 * @author OpenORB Compiler
 */
final public class FrontEndIDLHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal FrontEndIDL value
     */
    public frontEndIDL.FrontEndIDL value;

    /**
     * Default constructor
     */
    public FrontEndIDLHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public FrontEndIDLHolder(frontEndIDL.FrontEndIDL initial)
    {
        value = initial;
    }

    /**
     * Read FrontEndIDL from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = FrontEndIDLHelper.read(istream);
    }

    /**
     * Write FrontEndIDL into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        FrontEndIDLHelper.write(ostream,value);
    }

    /**
     * Return the FrontEndIDL TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return FrontEndIDLHelper.type();
    }

}
