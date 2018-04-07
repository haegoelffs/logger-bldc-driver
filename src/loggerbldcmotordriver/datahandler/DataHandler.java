package loggerbldcmotordriver.datahandler;

import loggerbldcmotordriver.framework.RingBuffer;
import loggerbldcmotordriver.serialcom.SerialDataBuffer;
import loggerbldcmotordriver.serialcom.TimeValueData;

/**
 *
 * @author simon
 */
public class DataHandler implements Runnable
{
    private final Thread thread;
    private final SerialDataBuffer serialDataBuffer;
    
    private IDataSink<TimeValueData> rotationFrequenzy_ds;
    private IDataSink<TimeValueData> cycleTime_ds;
    
    public DataHandler() {
        thread = new Thread(this);
        serialDataBuffer = new SerialDataBuffer();
    }

    @Override
    public void run() {
        while(true){
            handleDataToGraphController(rotationFrequenzy_ds, serialDataBuffer.getRotationFrequenzy_buffer());
            handleDataToGraphController(cycleTime_ds, serialDataBuffer.getCycleTime_buffer());
        }
    }
    
    private void handleDataToGraphController(IDataSink<TimeValueData> ds, RingBuffer<TimeValueData> buffer){
        TimeValueData data = buffer.get();
        if(ds != null && data != null){
            ds.put(data);
        }
    }
}
