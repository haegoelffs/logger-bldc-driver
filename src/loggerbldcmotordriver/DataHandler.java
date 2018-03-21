
package loggerbldcmotordriver;

import loggerbldcmotordriver.com.LongData;

/**
 *
 * @author simon
 */
public class DataHandler extends Thread
{

    private RingBuffer<LongData> sourceBuffer;
    
    private IDataSink dataSink;
    
    private int resolution_in_ms;

    public DataHandler(RingBuffer<LongData> sourceBuffer, IDataSink dataSink, int resolution_in_ms) {
        super("Data Handler");
        
        this.sourceBuffer = sourceBuffer;
        this.dataSink = dataSink;
        this.resolution_in_ms = resolution_in_ms;
        
        this.start();
    }

    @Override
    public void run() {
        long time_last_data_ms = 0;
        
        while(true){
            LongData data = sourceBuffer.get();
            if(data != null){
                long timestamp_ms = data.getTimestamp_us()/1000;
                
                if(time_last_data_ms < timestamp_ms){
                    // new data
                    dataSink.put(data);
                }
            }
        }
    }
}
