package loggerbldcmotordriver;

/**
 *
 * @author simon
 */
public class DiscretRandomSignalGenerator extends Thread
{

    private int min_sleep_ms;
    private int max_sleep_ms;
    private int max_amplitude;
    private int dc_value;

    private final TimeReference timeReference;
    private final RingBuffer<IntegerData> buffer;

    public DiscretRandomSignalGenerator(int min_sleep_ms, int max_sleep_ms, int max_amplitude, int dc_value, TimeReference timeReference, RingBuffer<IntegerData> buffer) {
        this.min_sleep_ms = min_sleep_ms;
        this.max_sleep_ms = max_sleep_ms;
        this.max_amplitude = max_amplitude;
        this.dc_value = dc_value;
        this.timeReference = timeReference;
        this.buffer = buffer;
        
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                int random_sleep_ms = (int) (Math.random() * (max_sleep_ms - min_sleep_ms)) + min_sleep_ms;
                Thread.sleep(random_sleep_ms);

                int random_value = (int) (Math.random() * (max_amplitude)) -max_amplitude/2 + dc_value;
                buffer.put(new IntegerData(timeReference.getElapsedTime_ms(), random_value));
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
