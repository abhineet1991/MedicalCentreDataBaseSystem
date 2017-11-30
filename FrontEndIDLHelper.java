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
}