package loggerbldcmotordriver.view.figures;

import loggerbldcmotordriver.framework.IDataSink;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import loggerbldcmotordriver.framework.RingBuffer;
import loggerbldcmotordriver.view.TimeReference;
import loggerbldcmotordriver.view.references.AbsoluteReferencePoint;
import loggerbldcmotordriver.view.references.ReferencePoint;

/**
 *
 * @author simon
 */
public class PlotContainer extends AnimationTimer
{

    private TimeReference timeReference;

    private IDataSink dataSink;

    private Canvas plot;
    private GraphicsContext gc;

    private Grid grid;
    private Map<String, AGraph> graphs;

    // default settings
    private int x_size_in_px;
    private int y_size_in_px;

    protected long xAxis_start_ms, xAxis_stop_ms;
    double xAxis_scale_px_per_ms = 0.1;

    private int x_grid_interval = 100;
    private int y_grid_interval = 100;

    public PlotContainer(TimeReference timeReference, IDataSink dataSink, int x_size_in_px, int y_size_in_px, double xAxis_scale_px_per_ms) {
        this.timeReference = timeReference;
        this.dataSink = dataSink;
        this.xAxis_scale_px_per_ms = xAxis_scale_px_per_ms;
        this.x_size_in_px = x_size_in_px;
        this.y_size_in_px = y_size_in_px;

        this.plot = new Canvas(x_size_in_px, y_size_in_px);
        gc = plot.getGraphicsContext2D();

        this.grid = new Grid(
                new ReferencePoint(0, 0, 0, AbsoluteReferencePoint.INSTANCE),
                x_size_in_px,
                y_size_in_px,
                x_grid_interval,
                y_grid_interval,
                0,
                y_size_in_px / 2);

        this.graphs = new HashMap<>();

        reset(timeReference.getElapsedTime_ms());
        start();
    }

    public LineGraph addLineGraph(String name, double yScale_per_value) {
        LineGraph graph = new LineGraph(
                grid.getZero_point(),
                x_size_in_px,
                xAxis_scale_px_per_ms,
                yScale_per_value);

        graphs.put(name, graph);

        return graph;
    }

    @Override
    public void handle(long now) {
        long elapsed_t_ms = timeReference.getElapsedTime_ms();

        // check range
        if (elapsed_t_ms < xAxis_start_ms
                || elapsed_t_ms > xAxis_stop_ms) {
            // elapsed time outside of range --> reset graphs
            reset(elapsed_t_ms);
        }

        for (AGraph graph : graphs.values()) {
            graph.draw(gc, elapsed_t_ms);
        }
    }

    public void reset(long elapsed_t_ms) {
        this.xAxis_start_ms = elapsed_t_ms;
        this.xAxis_stop_ms = xAxis_start_ms + (long) (x_size_in_px / xAxis_scale_px_per_ms);

        gc.clearRect(0, 0, x_size_in_px, y_size_in_px);
        grid.draw(gc);

        for (AGraph graph : graphs.values()) {

            synchronized (graph) {
                RingBuffer<IDataPoint> drawedData_buffer = graph.getDrawedData_buffer();

                for (int cnt = 0; cnt < drawedData_buffer.getSize(); cnt++) {
                    dataSink.put(drawedData_buffer.get());
                }

                graph.reset(xAxis_start_ms);
            }
        }
    }

    // getter & setter
    public Canvas getPlot() {
        return plot;
    }

    public void set_timeMs_per_interval(int timeMs_per_interval) {
        xAxis_scale_px_per_ms = (double) x_grid_interval / timeMs_per_interval;

        for (AGraph graph : graphs.values()) {
            graph.setTimeScale_px_per_ms(xAxis_scale_px_per_ms);
        }

        reset(timeReference.getElapsedTime_ms());
    }
}
