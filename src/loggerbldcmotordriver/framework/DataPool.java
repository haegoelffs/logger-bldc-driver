package loggerbldcmotordriver.framework;

import loggerbldcmotordriver.framework.RingBuffer;
import loggerbldcmotordriver.framework.RingBufferException;
import loggerbldcmotordriver.datahandler.TimeDataGroup;

/**
 *
 * @author simon
 */
public class DataPool
{

    private static final int DEFAULT_POOL_SIZE = 4000;

    private final RingBuffer<TimeValueData> timeData_pool;

    private final RingBuffer<TimeDataGroup> timeDataGroup_pool;

    public DataPool() {
        timeData_pool = new RingBuffer<>(TimeValueData.class, DEFAULT_POOL_SIZE);
        for (int cnt = 0; cnt < timeData_pool.getCapacity(); cnt++) {
            try {
                timeData_pool.put(new TimeValueData(0, 0));
            }
            catch (RingBufferException ex) {
                throw new RuntimeException(ex);
            }
        }

        timeDataGroup_pool = new RingBuffer<>(TimeDataGroup.class, DEFAULT_POOL_SIZE);
        for (int cnt = 0; cnt < timeDataGroup_pool.getCapacity(); cnt++) {
            try {
                timeDataGroup_pool.put(new TimeDataGroup(0));
            }
            catch (RingBufferException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    
    
    public TimeValueData getTimeData() {
        try {
            TimeValueData temp = timeData_pool.getNullSafe();
            return temp;
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void recycleTimeData(TimeValueData data) {
        data.setTimestamp_us(0).setValue(0).setOlder(null).setYounger(null);

        try {
            timeData_pool.put(data);
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }

    
    
    public TimeDataGroup getTimeDataGroup() {
        try {
            TimeDataGroup temp = timeDataGroup_pool.getNullSafe();
            return temp;
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void recycleTimeDataGroup(TimeDataGroup data) {
        data.setDuration_in_us(0).setStart_in_us(0).setFirstData(null).setLastData(null);

        try {
            timeDataGroup_pool.put(data);
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }

    
    public MessageData getMessageData(){
        // no pool necessary --> mot a lot of data 
        return new MessageData(0, "");
    }
}
