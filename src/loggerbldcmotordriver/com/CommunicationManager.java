package loggerbldcmotordriver.com;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import loggerbldcmotordriver.RingBuffer;
import loggerbldcmotordriver.RingBufferException;

/**
 *
 * @author simon
 */
public class CommunicationManager extends Thread
{

    private final static byte STX = 2;
    private final static byte ETX = 3;

    private final static byte GENERIC_STRING = 0;

    private final static byte CURRENT_PHASE_A_SHUNT = 1;
    private final static byte CURRENT_PHASE_A_HALL = 2;
    private final static byte CURRENT_PHASE_B_SHUNT = 3;
    private final static byte CURRENT_PHASE_B_HALL = 4;
    private final static byte CURRENT_SETPOINT = 5;
    private final static byte CURRENT_CONTROL_OUTPUT = 6;

    private final static byte VOLTAGE_PHASE_A = 7;
    private final static byte VOLTAGE_PHASE_B = 8;
    private final static byte VOLTAGE_PHASE_C = 9;

    private final static byte STATE_DRIVE = 10;
    private final static byte ROTATION_FREQUENZY = 11;

    private final static byte ROTORPOSITION = 12;
    private final static byte ROTORPOSITION_SETPOINT = 13;
    private final static byte ROTORPOSITION_CONTROL_OUTPUT = 14;

    private final static byte ERROR_CODE = 15;
    private final static byte CYCLE_TIME = 16;

    private PicocomWrapper picocom;

    private DataPool dataPool;

    private RingBuffer<LongData> rotationFrequenzy_buffer;

    public CommunicationManager(DataPool dataPool, PicocomWrapper picocom) {
        super("Data Handler");
        this.dataPool = dataPool;
        this.picocom = picocom;

        rotationFrequenzy_buffer = new RingBuffer<>(LongData.class, 500);

        this.start();
    }

    @Override
    public void run() {
        try {
            InputStream stream = picocom.run(
                    PicocomWrapper.BaudRate.br115200baud,
                    PicocomWrapper.Parity.even,
                    PicocomWrapper.DataBits.db8bits,
                    PicocomWrapper.FlowControl.none, "/dev/ttyUSB0");

            DataInputStream dataStream = new DataInputStream(stream);

            while (true) {
                Byte b = dataStream.readByte();

                if (b == STX) {
                    // read timestamp in us (unsigned int 32bit)
                    long timestamp_us = interpretAsLong(dataStream);

                    interpretHeaderAndData(dataStream, timestamp_us);
                }
            }
        }
        catch (IOException ex) {

        }
        catch (RingBufferException ex) {
            Logger.getLogger(CommunicationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // getter & setter
    public RingBuffer<LongData> getRotationFrequenzy_buffer() {
        return rotationFrequenzy_buffer;
    }

    // private methods
    private void interpretHeaderAndData(DataInputStream dataStream, long timestamp_us) throws IOException, RingBufferException {
        while (true) {
            byte b = dataStream.readByte();

            if (b == ETX) {
                return;
            }

            byte header = (byte) interpretAsLong(dataStream, (char)b);

            // log type
            switch (header & 0b00111111) {
                case GENERIC_STRING:
                    // following bytes are chars
                    StringBuilder stringBuilder = new StringBuilder();
                    while (true) {
                        byte data = dataStream.readByte();
                        if (data != 0) {
                            stringBuilder.append((char) data);
                        }
                        else {
                            break;
                        }
                    }
                    break;

                case ROTATION_FREQUENZY:
                    // unsigned int 32bit
                    long frequenzy = interpretAsLong(dataStream);
                    rotationFrequenzy_buffer.put(dataPool.getLongData().set(timestamp_us, frequenzy));
                    break;

                case ETX:
                    return;
            }
        }
    }

    private long interpretAsLong(DataInputStream dataStream, char prefix) throws IOException {
        StringBuilder timestamp = new StringBuilder();
        
        if(prefix != 0){
            timestamp.append(prefix);
        }
        
        while (true) {
            char nextChar = (char) dataStream.readByte();
            if (nextChar != ';') {
                timestamp.append(nextChar);
            }
            else {
                return Long.parseLong(timestamp.toString());
            }
        }
    }
    
    private long interpretAsLong(DataInputStream dataStream) throws IOException {
        return interpretAsLong(dataStream, (char)0);
    }
}
