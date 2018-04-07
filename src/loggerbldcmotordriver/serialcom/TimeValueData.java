
package loggerbldcmotordriver.serialcom;

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
    private TimeValueData previous, next;
    
    public TimeValueData(long timestamp_us, long value, TimeValueData previous) {
        this.timestamp_us = timestamp_us;
        this.value = value;
        this.previous = previous;
    }
    
    public TimeValueData set(long timestamp_us, long data, TimeValueData older){
        this.timestamp_us = timestamp_us;
        this.value = data;
        this.previous = older;
        
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

    public TimeValueData getPrevious() {
        return previous;
    }

    public TimeValueData setPrevious(TimeValueData older) {
        this.previous = older;
        return this;
    }

    public TimeValueData getNext() {
        return next;
    }

    public TimeValueData setNext(TimeValueData younger) {
        this.next = younger;
        return this;
    }
    
    @Override
    public String toString() {
        return String.format("Time: %dms Value: %d", timestamp_us, value);
    }
}
