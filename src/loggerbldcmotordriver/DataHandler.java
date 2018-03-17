
package loggerbldcmotordriver;

/**
 *
 * @author simon
 */
public class DataHandler extends Thread
{
    
    private RingBuffer<IntegerData> sourceBuffer;
    private IDataSink dataSink;

    public DataHandler(RingBuffer<IntegerData> sourceBuffer, IDataSink dataSink) {
        super("Data Handler");
        
        this.sourceBuffer = sourceBuffer;
        this.dataSink = dataSink;
        
        this.start();
    }

    @Override
    public void run() {
        while(true){
            IntegerData data = sourceBuffer.get();
            if(data != null){
                    dataSink.put(data);
            }
        }
    }
}
