
package loggerbldcmotordriver;

/**
 *
 * @author simon
 */
public class IntegerData
{
    private long timestamp_ms;
    private int data;

    public IntegerData(long timestamp_ms, int data) {
        this.timestamp_ms = timestamp_ms;
        this.data = data;
    }

    public long getTimestamp_ms() {
        return timestamp_ms;
    }

    public void setTimestamp_ms(long timestamp_ms) {
        this.timestamp_ms = timestamp_ms;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("Time: %dms Value: %d", timestamp_ms, data);
    }
    
    
}
