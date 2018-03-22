
package loggerbldcmotordriver;

import loggerbldcmotordriver.elements.IDataSink;
import loggerbldcmotordriver.com.DataPool;
import loggerbldcmotordriver.com.TimeData;

/**
 *
 * @author simon
 */
public class DataHandler extends Thread
{
    private DataPool dataPool;
    private RingBuffer<TimeData> sourceBuffer;
    
    private IDataSink dataSink;
    
    private int resolution_in_us;

    public DataHandler(RingBuffer<TimeData> sourceBuffer, IDataSink dataSink, DataPool dataPool, int resolution_in_us) {
        super("Data Handler");
        
        this.sourceBuffer = sourceBuffer;
        this.dataSink = dataSink;
        this.dataPool = dataPool;
        this.resolution_in_us = resolution_in_us;
        
        this.start();
    }

    @Override
    public void run() {
        TimeDataGroup currentGroup = dataPool.getTimeDataGroup().setDuration_in_us(resolution_in_us).setStart_in_us(0);
        TimeData previousData = null;
        
        while(true){
            TimeData data = sourceBuffer.get();
            if(data != null){
                if(currentGroup.getStart_in_us() + currentGroup.getDuration_in_us() > data.getTimestamp_us()){
                    // timestamp of data inside this group
                    previousData = data;
                    
                    if(currentGroup.getFirstData() == null){
                        currentGroup.setFirstData(data);
                    }
                }else{
                    // timestamp of data outside of the group --> open new group
                    currentGroup.setLastData(previousData);
                    dataSink.put(currentGroup);
                    
                    currentGroup = dataPool.getTimeDataGroup()
                            .setDuration_in_us(resolution_in_us)
                            .setStart_in_us(currentGroup.getStart_in_us() + resolution_in_us)
                            .setFirstData(data);
                }
            }
        }
    }
}
