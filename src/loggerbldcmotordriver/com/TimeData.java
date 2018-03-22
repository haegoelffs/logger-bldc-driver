
package loggerbldcmotordriver.com;

/**
 *
 * @author simon
 */
public class TimeData
{
    // data
    private long timestamp_us;
    private long data;

    // position in time axis
    private TimeData older, younger;
    
    public TimeData(long timestamp_us, long data) {
        this.timestamp_us = timestamp_us;
        this.data = data;
    }
    
    public TimeData set(long timestamp_us, long data, TimeData older){
        this.timestamp_us = timestamp_us;
        this.data = data;
        this.older = older;
        
        return this;
    }

    public long getTimestamp_us() {
        return timestamp_us;
    }

    public TimeData setTimestamp_us(long timestamp_us) {
        this.timestamp_us = timestamp_us;
        return this;
    }

    public long getData() {
        return data;
    }

    public TimeData setValue(long data) {
        this.data = data;
        return this;
    }

    public TimeData getOlder() {
        return older;
    }

    public TimeData setOlder(TimeData older) {
        this.older = older;
        return this;
    }

    public TimeData getYounger() {
        return younger;
    }

    public TimeData setYounger(TimeData younger) {
        this.younger = younger;
        return this;
    }
    
    @Override
    public String toString() {
        return String.format("Time: %dms Value: %d", timestamp_us, data);
    }
}
