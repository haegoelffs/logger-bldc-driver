package loggerbldcmotordriver.com;

import loggerbldcmotordriver.RingBuffer;
import loggerbldcmotordriver.RingBufferException;
import loggerbldcmotordriver.TimeDataGroup;

/**
 *
 * @author simon
 */
public class DataPool
{

    private static final int DEFAULT_POOL_SIZE = 4000;

    private final RingBuffer<TimeData> timeData_pool;

    private final RingBuffer<TimeDataGroup> timeDataGroup_pool;

    public DataPool() {
        timeData_pool = new RingBuffer<>(TimeData.class, DEFAULT_POOL_SIZE);
        for (int cnt = 0; cnt < timeData_pool.getCapacity(); cnt++) {
            try {
                timeData_pool.put(new TimeData(0, 0));
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

    public TimeData getTimeData() {
        try {
            TimeData temp = timeData_pool.getNullSafe();
            return temp;
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void recycleTimeData(TimeData data) {
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

}
