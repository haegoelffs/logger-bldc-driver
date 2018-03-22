
package loggerbldcmotordriver;

import loggerbldcmotordriver.com.TimeData;
import loggerbldcmotordriver.elements.IDataPoint;

/**
 *
 * @author simon
 */
public class TimeDataGroup implements IDataPoint
{
    private TimeData firstData, lastData;
    
    private long start_in_us;
    private long duration_in_us;

    public TimeDataGroup(long duration_in_us) {
        this.duration_in_us = duration_in_us;
    }

    public TimeData getFirstData() {
        return firstData;
    }

    public TimeDataGroup setFirstData(TimeData firstData) {
        this.firstData = firstData;
        return this;
    }

    public TimeData getLastData() {
        return lastData;
    }

    public TimeDataGroup setLastData(TimeData lastData) {
        this.lastData = lastData;
        return this;
    }

    public long getStart_in_us() {
        return start_in_us;
    }

    public TimeDataGroup setStart_in_us(long start_in_us) {
        this.start_in_us = start_in_us;
        return this;
    }

    public long getDuration_in_us() {
        return duration_in_us;
    }

    public TimeDataGroup setDuration_in_us(long duration_in_us) {
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
        TimeData data = firstData;
        long value = 0;
        
        while(true){
            value += data.getData();
            
            if(data==lastData){
                return value/cnt;
            }
            
            data = data.getYounger();
            cnt++;
        }
    }
}