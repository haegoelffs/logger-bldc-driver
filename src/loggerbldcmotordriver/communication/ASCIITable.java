package loggerbldcmotordriver.communication;

/**
 *
 * @author simon
 */
public class ASCIITable
{

    public final static byte NULL = 0;

    public final static byte STX = 2;
    public final static byte ETX = 3;
    public final static byte SEPERATOR = 30;
    public final static byte UNIT_SEPERATOR = 31;

    public final static byte NUMBER_0 = 48;
    public final static byte NUMBER_1 = 49;
    public final static byte NUMBER_2 = 50;
    public final static byte NUMBER_3 = 51;
    public final static byte NUMBER_4 = 52;
    public final static byte NUMBER_5 = 53;
    public final static byte NUMBER_6 = 54;
    public final static byte NUMBER_7 = 55;
    public final static byte NUMBER_8 = 56;
    public final static byte NUMBER_9 = 57;

    public static byte getNumericValue(byte b) throws ASCIIException {
        switch (b) {
            case NUMBER_0:
                return 0;
            case NUMBER_1:
                return 1;
            case NUMBER_2:
                return 2;
            case NUMBER_3:
                return 3;
            case NUMBER_4:
                return 4;
            case NUMBER_5:
                return 5;
            case NUMBER_6:
                return 6;
            case NUMBER_7:
                return 7;
            case NUMBER_8:
                return 8;
            case NUMBER_9:
                return 9;
            default:
                throw new ASCIIException(String.format("Not a valid ASCII numeric value. Byte = %d",b));
        }
    }
    
    public static class ASCIIException extends Exception{

        public ASCIIException(String message) {
            super(message);
        }
        
    }
}
