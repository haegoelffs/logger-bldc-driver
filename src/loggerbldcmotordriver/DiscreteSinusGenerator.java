package loggerbldcmotordriver;

import loggerbldcmotordriver.com.IntegerData;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author simon
 */
public class DiscreteSinusGenerator extends TimerTask
{
    private int sampling_interval_ms;
    private int frequenzy_hz;
    private int amplitude;
    private int dc_value;

    private final TimeReference timeReference;
    private final RingBuffer<IntegerData> buffer;

    public DiscreteSinusGenerator(int sampling_interval_ms, int frequenzy_hz, int amplitude, int dc_value, TimeReference timeReference, RingBuffer<IntegerData> buffer) {
        this.sampling_interval_ms = sampling_interval_ms;
        this.frequenzy_hz = frequenzy_hz;
        this.amplitude = amplitude;
        this.dc_value = dc_value;
        
        this.buffer = buffer;
        this.timeReference = timeReference;

        Timer uploadCheckerTimer = new Timer(true);
        uploadCheckerTimer.scheduleAtFixedRate(this, 0, sampling_interval_ms);
    }

    @Override
    public void run() {
        
        double elapsedTime =  (double)timeReference.getElapsedTime_ms()/1000;
        int sin_value = dc_value + (int)(amplitude*Math.sin(elapsedTime*frequenzy_hz*2*Math.PI));
        
        try {
            IntegerData data = new IntegerData(timeReference.getElapsedTime_ms(), sin_value);
            //System.out.println(data);
            buffer.put(data);
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }
}
