
package loggerbldcmotordriver.framework;

/**
 *
 * @author simon
 */
public class RingBufferException extends Exception
{
    public final static String MSG_BUFFER_OVERFLOW = "ringbuffer overflow";
    public final static String MSG_NULL_HANDED = "try to put null-pointer into buffer";
    public final static String MSG_BUFFER_EMPTY = "ringbuffer empty";

    public RingBufferException(String message) {
        super(message);
    }
}
