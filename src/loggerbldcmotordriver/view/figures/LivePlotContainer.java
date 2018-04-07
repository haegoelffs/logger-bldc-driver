package loggerbldcmotordriver.view.figures;

import java.util.HashMap;
import java.util.Map;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import loggerbldcmotordriver.view.ScaleLabel;
import loggerbldcmotordriver.view.references.AbsoluteReferencePoint;
import loggerbldcmotordriver.view.references.ReferencePoint;

/**
 *
 * @author simon
 */
public class LivePlotContainer extends AnimationTimer
{
    private Canvas plot;
    private GraphicsContext gc;

    private Grid grid;
    private ScaleLabel timeAxisScaleLabel;
    
    private Map<String, AGraph> graphs;
    private Map<String, ScaleLabel> scaleLabels;

    // default settings
    private int lenght_px;
    private int height_px;

    protected long timeaxis_start_us;
    
    private int time_ms_per_section;
    private double timescale_px_per_ms = 0.1;

    private int x_grid_interval_px = 100;
    private int y_grid_interval_px = 100;

    public LivePlotContainer(int x_size_in_px, int y_size_in_px, int time_ms_per_section, int x_grid_interval_px, int y_grid_interval_px) {
        this.time_ms_per_section = time_ms_per_section;
        this.lenght_px = x_size_in_px;
        this.height_px = y_size_in_px;
        this.x_grid_interval_px = x_grid_interval_px;
        this.y_grid_interval_px = y_grid_interval_px;
        
        timescale_px_per_ms = (double)x_grid_interval_px/time_ms_per_section;

        this.plot = new Canvas(x_size_in_px, y_size_in_px);
        gc = plot.getGraphicsContext2D();

        this.grid = new Grid(
                new ReferencePoint(0, 0, 0, AbsoluteReferencePoint.INSTANCE),
                x_size_in_px,
                y_size_in_px,
                x_grid_interval_px,
                y_grid_interval_px,
                0,
                y_size_in_px / 2);
        
        timeAxisScaleLabel = new ScaleLabel(
                new ReferencePoint(0, height_px, 0, grid.getVertical_scale().getPosition_first_line()),
                0, 
                x_grid_interval_px, 
                time_ms_per_section, 
                grid.getVertical_scale().getNrLines(), 
                Color.BLACK, 
                "ms",
                ScaleLabel.Orientation.HORIZONTAL);

        this.graphs = new HashMap<>();
        this.scaleLabels = new HashMap<>();

        reset(0);
        start();
    }

    public LineGraph addLineGraph(String name, String unit, Color color, int min_value, int max_value){
        // calc scale
        double yScale_px_per_value = (double)height_px/(max_value - min_value);
        
        // calc zero point
        ReferencePoint referencePoint = new ReferencePoint(0, (int)(min_value*yScale_px_per_value), 0, grid.getZero_point());
        
        // create graph
        LineGraph graph = new LineGraph(
                referencePoint,
                lenght_px,
                timescale_px_per_ms,
                yScale_px_per_value);
        
        graphs.put(name, graph);
        
        // create scale label
        int interval_value = (int)(y_grid_interval_px/yScale_px_per_value);
        
        ScaleLabel label = new ScaleLabel(
                grid.getHorizontal_scale().getPosition_last_line(), 
                min_value, 
                y_grid_interval_px, 
                interval_value, 
                grid.getHorizontal_scale().getNrLines(), 
                color, 
                unit,
                ScaleLabel.Orientation.VERTICAL);
        
        scaleLabels.put(name, label);

        reset(0);
        
        return graph;
    }

    @Override
    public void handle(long now) {
        for (AGraph graph : graphs.values()) {
            graph.draw(gc);
        }
    }

    public void reset(long timeaxis_start_us) {
        this.timeaxis_start_us = timeaxis_start_us;
        
        gc.clearRect(0, 0, lenght_px, height_px);
        grid.draw(gc);
        timeAxisScaleLabel.draw(gc);
        
        for (ScaleLabel label : scaleLabels.values()) {
            label.draw(gc);
        }

        for (AGraph graph : graphs.values()) {
            graph.reset(this.timeaxis_start_us);
        }
    }

    // getter & setter
    public Canvas getPlot() {
        return plot;
    }
}
