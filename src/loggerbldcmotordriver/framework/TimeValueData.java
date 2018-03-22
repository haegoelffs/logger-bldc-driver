
package loggerbldcmotordriver.framework;

/**
 *
 * @author simon
 */
public class TimeValueData
{
    // data
    private long timestamp_us;
    private long value;

    // position in time axis
    private TimeValueData older, younger;
    
    public TimeValueData(long timestamp_us, long value) {
        this.timestamp_us = timestamp_us;
        this.value = value;
    }
    
    public TimeValueData set(long timestamp_us, long data, TimeValueData older){
        this.timestamp_us = timestamp_us;
        this.value = data;
        this.older = older;
        
        return this;
    }

    public long getTimestamp_us() {
        return timestamp_us;
    }

    public TimeValueData setTimestamp_us(long timestamp_us) {
        this.timestamp_us = timestamp_us;
        return this;
    }

    public long getData() {
        return value;
    }

    public TimeValueData setValue(long data) {
        this.value = data;
        return this;
    }

    public TimeValueData getOlder() {
        return older;
    }

    public TimeValueData setOlder(TimeValueData older) {
        this.older = older;
        return this;
    }

    public TimeValueData getYounger() {
        return younger;
    }

    public TimeValueData setYounger(TimeValueData younger) {
        this.younger = younger;
        return this;
    }
    
    @Override
    public String toString() {
        return String.format("Time: %dms Value: %d", timestamp_us, value);
    }
}
