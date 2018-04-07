
package loggerbldcmotordriver.serialcom;

/**
 *
 * @author simon
 * @param <T>
 */
public class Data<T>
{
    // data
    private long timestamp_us;
    private T value;
    
    private Data<T> older, younger;
    
    public Data(long timestamp_us, T value) {
        this.timestamp_us = timestamp_us;
        this.value = value;
    }
    
    // getter & setter
    public long getTimestamp_us() {
        return timestamp_us;
    }

    public Data<T> setTimestamp_us(long timestamp_us) {
        this.timestamp_us = timestamp_us;
        return this;
    }

    public T getValue() {
        return value;
    }

    public Data<T> setValue(T data) {
        this.value = data;
        return this;
    }

    public Data<T> getOlder() {
        return older;
    }

    public Data<T> setOlder(Data<T> older) {
        this.older = older;
        return this;
    }

    public Data<T> getYounger() {
        return younger;
    }

    public Data<T> setYounger(Data<T> younger) {
        this.younger = younger;
        return this;
    }
    
    @Override
    public String toString() {
        return String.format("Time: %dus Value: %s", timestamp_us, value.toString());
    }
}
