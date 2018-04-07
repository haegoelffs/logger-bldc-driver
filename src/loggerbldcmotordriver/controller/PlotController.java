
package loggerbldcmotordriver.controller;

import loggerbldcmotordriver.datahandler.IDataSink;
import loggerbldcmotordriver.serialcom.TimeValueData;
import loggerbldcmotordriver.view.figures.AGraph;
import loggerbldcmotordriver.view.figures.LivePlotContainer;

/**
 *
 * @author simon
 */
public class PlotController
{
    private LivePlotContainer plotContainer;
    private AGraph graph;
    
    private long timeaxis_start_us;
    private long time_range_us;
    
    
    public void checkTimerange(long timestamp_us){
        if (timestamp_us < timeaxis_start_us
                || timestamp_us > timeaxis_start_us + time_range_us) {
            // timestamp of data outside of range
            // calculate new time range
            timeaxis_start_us = timeaxis_start_us + time_range_us;
            
            // reset plot
            plotContainer.reset(timeaxis_start_us);
            
            this.checkTimerange(timestamp_us);
        }
    }
    
}
