package loggerbldcmotordriver.serialcom;

import loggerbldcmotordriver.framework.MessageData;
import loggerbldcmotordriver.framework.DataPool;
import loggerbldcmotordriver.framework.RingBuffer;
import loggerbldcmotordriver.framework.RingBufferException;
import loggerbldcmotordriver.framework.TimeValueData;

/**
 *
 * @author simon
 */
public class DataManager implements UARTProtoManager.Listener
{
    private UARTProtoManager uartProtoManager;
    
    private DataPool dataPool;
    
    private RingBuffer<TimeValueData> rotationFrequenzy_buffer;
    private TimeValueData lastRotationFrequenzyData;
    
    private RingBuffer<MessageData> messages_buffer;

    public DataManager(DataPool dataPool) {
        this.uartProtoManager = new UARTProtoManager(this);
        this.dataPool = dataPool;
        
        this.rotationFrequenzy_buffer = new RingBuffer<>(TimeValueData.class, 50);
        lastRotationFrequenzyData = dataPool.getTimeData().set(0, 0, null);
        
        this.messages_buffer = new RingBuffer<>(MessageData.class, 50);
        
        Thread thread = new Thread(() -> {
            try {
                uartProtoManager.start();
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        
        thread.start();
    }
    
    @Override
    public void bufferGenericMsg(long timestamp, String msg) {
        try {
            messages_buffer.put(dataPool.getMessageData().set(timestamp, msg));
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void bufferRotationFrequenzy(long timestamp, long frequenzy) {
        try {
            TimeValueData data = dataPool.getTimeData().set(timestamp, frequenzy, lastRotationFrequenzyData);
            rotationFrequenzy_buffer.put(data);
            lastRotationFrequenzyData = data;
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }

    // getter & setter
    public RingBuffer<MessageData> getMessages_buffer() {
        return messages_buffer;
    }
    
    public RingBuffer<TimeValueData> getRotationFrequenzy_buffer(){
        return rotationFrequenzy_buffer;
    }
}
