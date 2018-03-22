package loggerbldcmotordriver.communication;

import loggerbldcmotordriver.framework.MessageData;
import loggerbldcmotordriver.framework.TimeValueData;
import loggerbldcmotordriver.framework.DataPool;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import loggerbldcmotordriver.framework.RingBuffer;
import loggerbldcmotordriver.framework.RingBufferException;

/**
 *
 * @author simon
 */
public class CommunicationManager extends Thread
{

    private static final Logger LOG = Logger.getLogger(CommunicationManager.class.getName());

    // headers
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

    private RingBuffer<TimeValueData> rotationFrequenzy_buffer;
    private TimeValueData lastLongData;

    private RingBuffer<MessageData> messages_buffer;

    private State state;

    // flags
    private boolean rot_freq_enabled, rot_pos_enabled, rot_pos_setpoint_enabled, rot_pos_controller_out_enabled, cycletime_enabled;

    public CommunicationManager(DataPool dataPool, PicocomWrapper picocom) {
        super("Data Handler");
        this.dataPool = dataPool;
        this.picocom = picocom;

        rotationFrequenzy_buffer = new RingBuffer<>(TimeValueData.class, 500);
        lastLongData = dataPool.getTimeData();

        messages_buffer = new RingBuffer<>(MessageData.class, 100);

        state = State.standby;

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

            long timestamp = 0;
            int header = 0;
            StringBuilder data_builder = new StringBuilder();

            while (true) {
                byte nextByte = dataStream.readByte();

                switch (state) {
                    case standby:
                        if (nextByte == ASCIITable.STX) {
                            timestamp = 0;
                            header = 0;
                            data_builder.setLength(0);

                            state = State.read_timestamp;
                        }
                        break;

                    case read_timestamp:
                        if (nextByte == ASCIITable.SEPERATOR) {
                            state = State.read_header;
                        }
                        else {
                            try {
                                timestamp = timestamp * 10 + ASCIITable.getNumericValue(nextByte);
                            }
                            catch (ASCIITable.ASCIIException ex) {
                                state = State.standby;
                                Logger.getLogger(CommunicationManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        break;

                    case read_header:
                        if (nextByte == ASCIITable.SEPERATOR) {
                            state = State.read_data;
                        }
                        else {
                            try {
                                header = header * 10 + ASCIITable.getNumericValue(nextByte);
                            }
                            catch (ASCIITable.ASCIIException ex) {
                                state = State.standby;
                                Logger.getLogger(CommunicationManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        break;

                    case read_data:
                        if (nextByte == ASCIITable.SEPERATOR) {
                            state = State.store;
                        }else if(nextByte == ASCIITable.ETX){
                            state = State.store_and_finish;
                        }
                        else {
                            data_builder.append((char) nextByte);
                        }
                        break;

                    case store:
                        store(timestamp, header, data_builder.toString());
                        header = 0;
                        data_builder.setLength(0);

                        state = State.read_header;
                        break;

                    case store_and_finish:
                        store(timestamp, header, data_builder.toString());
                        state = State.standby;
                        break;
                }
            }
        }
        catch (IOException ex) {

        }
    }

    // enable / disable
    public void setRot_freq_enabled(boolean rot_freq_enabled) {
        this.rot_freq_enabled = rot_freq_enabled;
    }

    public void setRot_pos_enabled(boolean rot_pos_enabled) {
        this.rot_pos_enabled = rot_pos_enabled;
    }

    public void setRot_pos_setpoint_enabled(boolean rot_pos_setpoint_enabled) {
        this.rot_pos_setpoint_enabled = rot_pos_setpoint_enabled;
    }

    public void setRot_pos_controller_out_enabled(boolean rot_pos_controller_out_enabled) {
        this.rot_pos_controller_out_enabled = rot_pos_controller_out_enabled;
    }

    public void setCycletime_enabled(boolean cycletime_enabled) {
        this.cycletime_enabled = cycletime_enabled;
    }

    // getter & setter
    public RingBuffer<TimeValueData> getRotationFrequenzy_buffer() {
        return rotationFrequenzy_buffer;
    }

    public RingBuffer<MessageData> getMessages_buffer() {
        return messages_buffer;
    }

    // private methods
    private void store(long timestamp, int header, String data) {
        try {
            switch (header) {
                case GENERIC_STRING: {
                    messages_buffer.put(dataPool.getMessageData().set(timestamp, data));
                }
                break;

                case ROTATION_FREQUENZY:
                    if (rot_freq_enabled) {
                        TimeValueData temp = dataPool.getTimeData().set(timestamp, Long.parseLong(data), lastLongData);
                        lastLongData.setYounger(temp);

                        rotationFrequenzy_buffer.put(temp);
                        lastLongData = temp;
                    }
                    break;
            }
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
        catch (NumberFormatException ex) {
            LOG.log(Level.WARNING, ex.getMessage());
        }
    }

    public enum State
    {
        standby,
        read_timestamp,
        read_header,
        read_data,
        store,
        store_and_finish;
    }
}
