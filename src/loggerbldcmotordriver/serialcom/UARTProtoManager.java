package loggerbldcmotordriver.serialcom;

/**
 *
 * @author simon
 */
public class UARTProtoManager
{
    static {
        // Source: https://www.chilkatsoft.com/java-loadLibrary-Linux.asp
        try {
            System.load("/home/simon/NetBeansProjects/LoggerBLDCMotorDriver/src/native/uartProtoManager.so");
        }
        catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }

    // names
    private final static char NAME_GENERIC_MESSAGE = '1';

    private final static char NAME_CURRENT_PHASE_A_SHUNT = '2';
    private final static char NAME_CURRENT_PHASE_A_HALL = '3';
    private final static char NAME_CURRENT_PHASE_B_SHUNT = '4';
    private final static char NAME_CURRENT_PHASE_B_HALL = '5';
    private final static char NAME_CURRENT_SETPOINT = '6';
    private final static char NAME_CURRENT_CONTROL_OUTPUT = '7';

    private final static char NAME_VOLTAGE_PHASE_A = '8';
    private final static char NAME_VOLTAGE_PHASE_B = '9';
    private final static char NAME_VOLTAGE_PHASE_C = 'A';

    private final static char NAME_STATE_DRIVE = 'B';
    private final static char NAME_ROTATION_FREQUENZY = 'C';

    private final static char NAME_ROTORPOSITION = 'D';
    private final static char NAME_ROTORPOSITION_SETPOINT = 'E';
    private final static char NAME_ROTORPOSITION_CONTROL_OUTPUT = 'F';

    private final static char NAME_CYCLE_TIME = 'G';

    private final Listener listener;

    public UARTProtoManager(Listener listener) {
        this.listener = listener;
    }

    public void start() throws Exception{
        int errorCode = this.startNative("/dev/ttyUSB0", 115200, false, false, 8);
        
        if(errorCode != 0){
            throw new Exception("Communication Error");
        }
    }
    
    // native methods
    private native int startNative(String port, int baudrate, boolean parity, boolean twoStopBits, int nrDatabits);

    private void buffer_intData(byte data_name, long timestamp, long data) {
        switch((char)data_name){
            case NAME_ROTATION_FREQUENZY:
                listener.bufferRotationFrequenzy(timestamp, data);
                break;
                
            case NAME_CYCLE_TIME:
                break;
        }
    }

    private void buffer_stringData(byte data_name, long timestamp, byte[] data) {
        if(((char)data_name) == NAME_GENERIC_MESSAGE){
            StringBuilder sb = new StringBuilder();
            for (byte b : data) {
                sb.append((char)b);
            }
            listener.bufferGenericMsg(timestamp, sb.toString());
        }
    }

    private void printString(byte[] data) {
        for (byte b : data) {
            System.out.print((char) b);
        }
    }
    
    public interface Listener{
        public void bufferGenericMsg(long timestamp, String msg);
        public void bufferRotationFrequenzy(long timestamp, long frequenzy);
    }
}
