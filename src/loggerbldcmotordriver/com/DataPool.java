package loggerbldcmotordriver.com;

import loggerbldcmotordriver.RingBuffer;
import loggerbldcmotordriver.RingBufferException;

/**
 *
 * @author simon
 */
public class DataPool
{

    private static final int SIZE_LONG_DATA_POOL = 2000;

    private final RingBuffer<LongData> longData_pool;

    public DataPool() {
        longData_pool = new RingBuffer<>(LongData.class, SIZE_LONG_DATA_POOL);

        for (int cnt = 0; cnt < SIZE_LONG_DATA_POOL; cnt++) {
            try {
                longData_pool.put(new LongData(0, 0));
            }
            catch (RingBufferException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public LongData getLongData() {
        return longData_pool.get();
    }

    public void recycleLongData(LongData data) {
        try {
            longData_pool.put(data);
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }

}
