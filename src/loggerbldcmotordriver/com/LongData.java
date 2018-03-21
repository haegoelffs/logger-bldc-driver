
package loggerbldcmotordriver.com;

/**
 *
 * @author simon
 */
public class LongData
{
    private long timestamp_us;
    private long data;

    public LongData(long timestamp_us, long data) {
        this.timestamp_us = timestamp_us;
        this.data = data;
    }
    
    public LongData set(long timestamp_us, long data){
        this.timestamp_us = timestamp_us;
        this.data = data;
        
        return this;
    }

    public long getTimestamp_us() {
        return timestamp_us;
    }

    public void setTimestamp_us(long timestamp_us) {
        this.timestamp_us = timestamp_us;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("Time: %dms Value: %d", timestamp_us, data);
    }
}
