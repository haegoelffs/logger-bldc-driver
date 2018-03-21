
package loggerbldcmotordriver.elements;

import java.util.HashMap;
import java.util.Map;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import loggerbldcmotordriver.TimeReference;
import loggerbldcmotordriver.references.AbsoluteReferencePoint;
import loggerbldcmotordriver.references.ReferencePoint;

/**
 *
 * @author simon
 */
public class PlotContainer extends AnimationTimer implements IDrawingAreaManager
{
    private TimeReference timeReference;
    
    private Canvas plot;
    private GraphicsContext gc;
    
    private Grid grid;
    private Map<String, AGraph> graphs;
    
    // default settings
    private int x_size_in_px = 1000;
    private int y_size_in_px = 500;
    
    double xScale_px_per_ms = 0.1;
    
    private int x_grid_interval = 10;
    private int y_grid_interval = 10;

    public PlotContainer(TimeReference timeReference) {
        this.timeReference = timeReference;
        
        this.plot = new Canvas(x_size_in_px, y_size_in_px);
        gc = plot.getGraphicsContext2D();
        
        this.grid = new Grid(
                new ReferencePoint(0, 0, 0, AbsoluteReferencePoint.INSTANCE), 
                x_size_in_px, 
                y_size_in_px, 
                x_grid_interval, 
                y_grid_interval, 
                0, 
                y_size_in_px/2);
        
        this.graphs = new HashMap<>();
        
        resetArea();
        start();
    }
    
    public LineGraph addLineGraph(String name, double yScale_per_value){
        LineGraph graph = new LineGraph(
                this, 
                grid.getZero_point(), 
                x_size_in_px, 
                xScale_px_per_ms, 
                yScale_per_value);
        
        graphs.put(name, graph);
        
        return graph;
    }

    @Override
    public void handle(long now) {
        long elapsed_t_ms = timeReference.getElapsedTime_ms();
        
        for(AGraph graph : graphs.values()){
            graph.draw(gc, elapsed_t_ms);
        }
    }

    @Override
    public void resetArea() {
        gc.clearRect(0, 0, x_size_in_px, y_size_in_px);
        grid.draw(gc);
    }
    
    // getter & setter
    public Canvas getPlot() {
        return plot;
    }
    
}
