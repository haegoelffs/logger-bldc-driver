
package loggerbldcmotordriver.view.figures;

import loggerbldcmotordriver.framework.RingBuffer;
import loggerbldcmotordriver.framework.RingBufferException;
import loggerbldcmotordriver.framework.StaticRingBuffer;
import loggerbldcmotordriver.view.references.AReferencePoint;

/**
 *
 * @author simon
 * @param <T>
 */
public abstract class AGraph<T> implements IDrawableElement
{
    protected AReferencePoint zero_position;

    protected long xAxis_start_ms;

    protected final StaticRingBuffer<T> drawingElementsPool;

    protected final RingBuffer<IDataPoint> dataToDraw_buffer;
    protected RingBuffer<IDataPoint> drawedData_buffer;

    protected int lenght_px;

    // axis scale
    protected double timeScale_px_per_ms;
    protected double yScale_px_per_value;

    /**
     * Draws the values from left to right. If the screen is full, it reset it.
     *
     * @param manager
     * @param zero_position
     * @param lenght_px
     * @param timeScale_px_per_ms
     * @param yScale_px_per_value
     */
    public AGraph(
            AReferencePoint zero_position,
            int lenght_px,
            double timeScale_px_per_ms,
            double yScale_px_per_value) {
        this.zero_position = zero_position;

        this.lenght_px = lenght_px;
        this.timeScale_px_per_ms = timeScale_px_per_ms;
        this.yScale_px_per_value = yScale_px_per_value;

        // init buffer for dots
        dataToDraw_buffer = new RingBuffer<>(IDataPoint.class, 500);
        drawedData_buffer = new RingBuffer<>(IDataPoint.class, lenght_px);
        drawingElementsPool = initDrawingElementsPool();
    }
    
    public void put(IDataPoint data) {
        try {
            dataToDraw_buffer.put(data);
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void reset(long xAxis_start_ms){
        this.xAxis_start_ms = xAxis_start_ms;
    }

    // getter & setter
    public void setTimeScale_px_per_ms(double xScale_px_per_ms) {
        this.timeScale_px_per_ms = xScale_px_per_ms;
    }
    public double getTimeScale_px_per_ms() {
        return this.timeScale_px_per_ms;
    }

    public void setYScale_px_per_value(double yScale_px_per_value) {
        this.yScale_px_per_value = yScale_px_per_value;
    }
    public double getYScale_px_per_value() {
        return this.yScale_px_per_value;
    }

    public int getLenght_px() {
        return lenght_px;
    }
    public void setLenght_px(int lenght_px) {
        this.lenght_px = lenght_px;
    }

    public long getxAxis_start_ms() {
        return xAxis_start_ms;
    }

    public RingBuffer<IDataPoint> getDrawedData_buffer() {
        return drawedData_buffer;
    }
    public void setDrawedData_buffer(RingBuffer<IDataPoint> drawedData_buffer) {
        this.drawedData_buffer = drawedData_buffer;
    }
    
    // protected methods
    protected abstract StaticRingBuffer<T> initDrawingElementsPool();
    
    protected int calculate_scaled_value(long value, double scale){
        return (int)(value*scale);
    }

}
