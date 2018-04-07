package loggerbldcmotordriver.serialcom;

import loggerbldcmotordriver.framework.RingBuffer;
import loggerbldcmotordriver.framework.RingBufferException;

/**
 *
 * @author simon
 */
public class SerialDataBuffer implements UARTProtoManager.Listener, Runnable
{

    private UARTProtoManager uartProtoManager;

    private RingBuffer<TimeValueData> rotationFrequenzy_buffer;
    private TimeValueData lastRotationFrequenzyData;
    
    private RingBuffer<TimeValueData> cycleTime_buffer;
    private TimeValueData lastCycleTimeData;

    private RingBuffer<MessageData> messages_buffer;

    public SerialDataBuffer() {
        this.uartProtoManager = new UARTProtoManager(this);

        this.rotationFrequenzy_buffer = new RingBuffer<>(TimeValueData.class, 50);
        lastRotationFrequenzyData = null;

        this.messages_buffer = new RingBuffer<>(MessageData.class, 50);

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void bufferGenericMsg(long timestamp, String msg) {
        try {
            messages_buffer.put(new MessageData(timestamp, msg));
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void bufferRotationFrequenzy(long timestamp, long frequenzy) {
        try {
            TimeValueData data = new TimeValueData(timestamp, frequenzy, lastRotationFrequenzyData);
            lastRotationFrequenzyData.setNext(data);
            lastRotationFrequenzyData = data;

            rotationFrequenzy_buffer.put(data);
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public void bufferCycleTime(long timestamp, long cycleTime) {
        try {
            TimeValueData data = new TimeValueData(timestamp, cycleTime, lastCycleTimeData);
            lastCycleTimeData.setNext(data);
            lastCycleTimeData = data;

            cycleTime_buffer.put(data);
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public void run() {
        try {
            uartProtoManager.start();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // getter & setter
    public RingBuffer<MessageData> getMessages_buffer() {
        return messages_buffer;
    }

    public RingBuffer<TimeValueData> getRotationFrequenzy_buffer() {
        return rotationFrequenzy_buffer;
    }

    public RingBuffer<TimeValueData> getCycleTime_buffer() {
        return cycleTime_buffer;
    }
}
