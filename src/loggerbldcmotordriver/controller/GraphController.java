
package loggerbldcmotordriver.controller;

import loggerbldcmotordriver.datahandler.IDataSink;
import loggerbldcmotordriver.serialcom.TimeValueData;
import loggerbldcmotordriver.view.figures.AGraph;
import loggerbldcmotordriver.view.figures.IDataPoint;

/**
 *
 * @author simon
 */
public class GraphController implements IDataSink<TimeValueData>
{
    private AGraph graph;
    private PlotController plotController;
    
    private int resolution_in_us;

    private GraphData currentGraphData;
    private TimeValueData previousDataPoint;

    public GraphController(AGraph graph, PlotController plotController, int resolution_in_us) {
        this.graph = graph;
        this.plotController = plotController;
        this.resolution_in_us = resolution_in_us;
    }
    
    public void displayData(IDataPoint data) {
        plotController.checkTimerange(data.getTimestamp_us());
        graph.put(data);
    }
    
    public void adjustTimeResolution(TimeValueData data) {
        if (data != null) {
            // new data available
            if (currentGraphData == null) {
                // create new graph data
                currentGraphData = new GraphData()
                        .setDuration_in_us(resolution_in_us)
                        .setStart_in_us(data.getTimestamp_us() - data.getTimestamp_us() % resolution_in_us)
                        .setFirstData(data);

                previousDataPoint = data;
            }
            else {
                // check: timestamp of new data inside of the current group?
                if (currentGraphData.getStart_in_us() + currentGraphData.getDuration_in_us() > data.getTimestamp_us()) {
                    // timestamp inside of the bounds from the current group
                    previousDataPoint = data;
                }
                else {
                    // timestamp outside of the bounds from the current graph data
                    currentGraphData.setLastData(previousDataPoint);
                    displayData(currentGraphData);

                // create new graph data
                currentGraphData = new GraphData()
                        .setDuration_in_us(resolution_in_us)
                        .setStart_in_us(data.getTimestamp_us() - data.getTimestamp_us() % resolution_in_us)
                        .setFirstData(data);

                    previousDataPoint = data;
                }
            }
        }
    }

    @Override
    public void put(TimeValueData data) {
        adjustTimeResolution(data);
    }
}
