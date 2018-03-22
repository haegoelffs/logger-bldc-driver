package loggerbldcmotordriver.elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import loggerbldcmotordriver.RingBufferException;
import loggerbldcmotordriver.StaticRingBuffer;
import loggerbldcmotordriver.references.AReferencePoint;

/**
 *
 * @author simon
 */
public class LineGraph extends AGraph<Line>
{
    private Color color;
    private int line_width;

    private int last_x_pos;
    private int last_y_pos;

    public LineGraph(AReferencePoint zero_position, int lenght_px, double xScale_px_per_ms, double yScale_px_per_value) {
        super(zero_position, lenght_px, xScale_px_per_ms, yScale_px_per_value);
    }

    @Override
    protected StaticRingBuffer<Line> initDrawingElementsPool() {
        this.color = Color.RED;
        this.line_width = 2;
        
        StaticRingBuffer<Line> buffer = new StaticRingBuffer<>(Line.class, lenght_px);

        for (int cnt = 0; cnt < lenght_px; cnt++) {
            buffer.put(new Line(zero_position, line_width, color));
        }

        return buffer;
    }

    @Override
    public void reset(long xAxis_start_ms){
        super.reset(xAxis_start_ms);
        
        last_x_pos = 0;
        last_y_pos = 0;
    }

    @Override
    public void draw(GraphicsContext gc, long elapsed_t_ms) {
        IDataPoint data = dataToDraw_buffer.get();
        if (data != null) {
            try {
                // new data to draw
                drawedData_buffer.put(data);
            }
            catch (RingBufferException ex) {
                throw new RuntimeException(ex);
            }
            
            Line line = drawingElementsPool.getNext();
            // calc position
            long delta_t = data.getTimestamp_us()/1000- xAxis_start_ms;
            line.set_from(last_x_pos, last_y_pos);
            
            // calculate new position
            last_x_pos = calculate_scaled_value(delta_t, xScale_px_per_ms);
            last_y_pos = calculate_scaled_value(data.getValue(), yScale_px_per_value);
            line.set_to(last_x_pos, last_y_pos);
            
            line.draw(gc);
        }
    }

}
