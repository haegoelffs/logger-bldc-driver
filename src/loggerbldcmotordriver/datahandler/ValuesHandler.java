package loggerbldcmotordriver.datahandler;

import loggerbldcmotordriver.framework.RingBuffer;
import loggerbldcmotordriver.framework.IDataSink;
import loggerbldcmotordriver.framework.DataPool;
import loggerbldcmotordriver.framework.TimeValueData;
import loggerbldcmotordriver.view.figures.IDataPoint;

/**
 *
 * @author simon
 */
public class ValuesHandler
{

    private DataPool dataPool;
    private RingBuffer<TimeValueData> sourceBuffer;
    private IDataSink<IDataPoint> dataSink;

    private int resolution_in_us;

    private TimeDataGroup currentGroup;
    private TimeValueData previousData;

    public ValuesHandler(RingBuffer<TimeValueData> sourceBuffer, IDataSink dataSink, DataPool dataPool, int resolution_in_us) {
        this.sourceBuffer = sourceBuffer;
        this.dataSink = dataSink;
        this.dataPool = dataPool;
        this.resolution_in_us = resolution_in_us;

        currentGroup = null;
        previousData = null;
    }

    public void proceed() {
        TimeValueData data = sourceBuffer.get();
        if (data != null) {
            // new data available
            if (currentGroup == null) {
                // open new group
                currentGroup = dataPool.getTimeDataGroup()
                        .setDuration_in_us(resolution_in_us)
                        .setStart_in_us(data.getTimestamp_us() - data.getTimestamp_us() % resolution_in_us)
                        .setFirstData(data);

                previousData = data;
            }
            else {
                // check: timestamp of new data inside of the current group?
                if (currentGroup.getStart_in_us() + currentGroup.getDuration_in_us() > data.getTimestamp_us()) {
                    // timestamp inside of the bounds from the current group
                    previousData = data;
                }
                else {
                    // timestamp outside of the bounds from the current group
                    // close the current group
                    currentGroup.setLastData(previousData);
                    dataSink.put(currentGroup);

                    // open new group
                    currentGroup = dataPool.getTimeDataGroup()
                            .setDuration_in_us(resolution_in_us)
                            .setStart_in_us(data.getTimestamp_us() - data.getTimestamp_us() % resolution_in_us)
                            .setFirstData(data);

                    previousData = data;
                }
            }
        }
    }
}
