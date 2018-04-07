
package loggerbldcmotordriver.serialcom;

/**
 *
 * @author simon
 */
public class MessageData
{
    // data
    private long timestamp_us;
    private String msg;

    public MessageData(long timestamp_us, String msg) {
        this.timestamp_us = timestamp_us;
        this.msg = msg;
    }
    
    public MessageData set(long timestamp_us, String msg){
        this.timestamp_us = timestamp_us;
        this.msg = msg;
        
        return this;
    }

    public long getTimestamp_us() {
        return timestamp_us;
    }

    public void setTimestamp_us(long timestamp_us) {
        this.timestamp_us = timestamp_us;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
