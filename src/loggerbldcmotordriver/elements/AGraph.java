
package loggerbldcmotordriver.elements;

import javafx.scene.canvas.GraphicsContext;
import loggerbldcmotordriver.com.IntegerData;
import loggerbldcmotordriver.RingBuffer;
import loggerbldcmotordriver.RingBufferException;
import loggerbldcmotordriver.StaticRingBuffer;
import loggerbldcmotordriver.references.AReferencePoint;

/**
 *
 * @author simon
 * @param <T>
 */
public abstract class AGraph<T> implements ITimeSynchronizedDrawable, IDataSink
{
    protected AReferencePoint zero_position;

    protected long xAxis_start_ms;

    protected final StaticRingBuffer<T> drawingElementsPool;
    protected final IntegerData initData;

    protected final RingBuffer<IDataPoint> dataToDraw_buffer;
    protected RingBuffer<IDataPoint> drawedData_buffer;

    protected int lenght_px;

    // axis scale
    protected double xScale_px_per_ms;
    protected double yScale_px_per_value;

    /**
     * Draws the values from left to right. If the screen is full, it reset it.
     *
     * @param manager
     * @param zero_position
     * @param lenght_px
     * @param xScale_px_per_ms
     * @param yScale_px_per_value
     */
    public AGraph(
            AReferencePoint zero_position,
            int lenght_px,
            double xScale_px_per_ms,
            double yScale_px_per_value) {
        this.zero_position = zero_position;

        this.lenght_px = lenght_px;
        this.xScale_px_per_ms = xScale_px_per_ms;
        this.yScale_px_per_value = yScale_px_per_value;

        // init buffer for dots
        initData = new IntegerData(0, 0);
        dataToDraw_buffer = new RingBuffer<>(IDataPoint.class, 20);
        drawedData_buffer = new RingBuffer<>(IDataPoint.class, lenght_px);
        drawingElementsPool = initDrawingElementsPool();
    }
    
    @Override
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
    public void setXScale_px_per_ms(double xScale_px_per_ms) {
        this.xScale_px_per_ms = xScale_px_per_ms;
    }
    public double getXScale_px_per_ms() {
        return this.xScale_px_per_ms;
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

    public AReferencePoint getZero_position() {
        return zero_position;
    }
    public void setZero_position(AReferencePoint zero_position) {
        this.zero_position = zero_position;
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
