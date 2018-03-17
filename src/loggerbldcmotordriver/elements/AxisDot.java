
package loggerbldcmotordriver.elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import loggerbldcmotordriver.IntegerData;
import loggerbldcmotordriver.references.AReferencePoint;
import loggerbldcmotordriver.references.ReferencePoint;

/**
 *
 * @author simon
 */
public class AxisDot extends Dot implements ITimeSynchronizedDrawable
{
    private IntegerData data;
    
    // axis scale
    private double xScale_px_per_ms;
    private double yScale_px_per_value;
    
    public AxisDot(IntegerData data, AReferencePoint dataZero, int radius, Color color, double xScale_px_per_ms, double yScale_px_per_value) {
        super(new ReferencePoint(0, (int)(yScale_px_per_value*data.getData()), 0, dataZero), radius, color);
        
        this.data = data;
        
        this.xScale_px_per_ms = xScale_px_per_ms;
        this.yScale_px_per_value = yScale_px_per_value;
    }

    public IntegerData getData() {
        return data;
    }

    public void setData(IntegerData data) {
        this.data = data;
        this.getPosition().setX(0);
        this.getPosition().setY((int)(yScale_px_per_value*data.getData()));
    }

    public double getxScale_px_per_ms() {
        return xScale_px_per_ms;
    }

    public void setxScale_px_per_ms(double xScale_px_per_ms) {
        this.xScale_px_per_ms = xScale_px_per_ms;
    }

    public double getyScale_px_per_value() {
        return yScale_px_per_value;
    }

    public void setyScale_px_per_value(double yScale_px_per_value) {
        this.yScale_px_per_value = yScale_px_per_value;
    }

    @Override
    public void draw(GraphicsContext gc, long elapsed_t_ms) {
        getPosition().setX((int) (xScale_px_per_ms * (-elapsed_t_ms + data.getTimestamp_ms())));
        this.draw(gc);
    }
}
