
package loggerbldcmotordriver.controller;

import loggerbldcmotordriver.serialcom.TimeValueData;
import loggerbldcmotordriver.view.figures.IDataPoint;

/**
 *
 * @author simon
 */
public class GraphData implements IDataPoint
{
    private TimeValueData firstData, lastData;
    
    private long start_in_us;
    private long duration_in_us;

    public GraphData() {
    }

    public TimeValueData getFirstData() {
        return firstData;
    }

    public GraphData setFirstData(TimeValueData firstData) {
        this.firstData = firstData;
        return this;
    }

    public TimeValueData getLastData() {
        return lastData;
    }

    public GraphData setLastData(TimeValueData lastData) {
        this.lastData = lastData;
        return this;
    }

    public long getStart_in_us() {
        return start_in_us;
    }

    public GraphData setStart_in_us(long start_in_us) {
        this.start_in_us = start_in_us;
        return this;
    }

    public long getDuration_in_us() {
        return duration_in_us;
    }

    public GraphData setDuration_in_us(long duration_in_us) {
        this.duration_in_us = duration_in_us;
        return this;
    }    

    @Override
    public long getTimestamp_us() {
        return start_in_us + duration_in_us/2;
    }

    @Override
    public long getValue() {
        return calcMeanOverDataValues();
    }
    
    // private methods
    private long calcMeanOverDataValues()
    {
        int cnt = 1;
        TimeValueData data = firstData;
        long value = 0;
        
        while(true){
            value += data.getData();
            
            if(data==lastData){
                return value/cnt;
            }
            
            data = data.getNext();
            cnt++;
        }
    }
}
