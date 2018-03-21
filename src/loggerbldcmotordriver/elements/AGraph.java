
package loggerbldcmotordriver.elements;

import javafx.scene.canvas.GraphicsContext;
import loggerbldcmotordriver.IDataSink;
import loggerbldcmotordriver.com.IntegerData;
import loggerbldcmotordriver.RingBuffer;
import loggerbldcmotordriver.RingBufferException;
import loggerbldcmotordriver.StaticRingBuffer;
import loggerbldcmotordriver.com.LongData;
import loggerbldcmotordriver.references.AReferencePoint;
import loggerbldcmotordriver.references.AbsoluteReferencePoint;

/**
 *
 * @author simon
 * @param <T>
 */
public abstract class AGraph<T> implements ITimeSynchronizedDrawable, IDataSink
{
    protected AReferencePoint zero_position;
    protected final IDrawingAreaManager manager;

    protected long section_t_start, section_t_stop;

    protected final StaticRingBuffer<T> drawingElementsPool;
    protected final IntegerData initData;

    protected final RingBuffer<LongData> dataToDrawBuffer;

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
            IDrawingAreaManager manager,
            AReferencePoint zero_position,
            int lenght_px,
            double xScale_px_per_ms,
            double yScale_px_per_value) {
        this.manager = manager;
        this.zero_position = zero_position;

        this.lenght_px = lenght_px;
        this.xScale_px_per_ms = xScale_px_per_ms;
        this.yScale_px_per_value = yScale_px_per_value;
        
        this.section_t_start = 0;
        this.section_t_stop = (long) (lenght_px / xScale_px_per_ms);

        // init buffer for dots
        initData = new IntegerData(0, 0);
        dataToDrawBuffer = new RingBuffer<>(LongData.class, lenght_px);
        drawingElementsPool = initDrawingElementsPool();
    }

    @Override
    public void draw(GraphicsContext gc, long elapsed_t_ms) {
        // check range
        if (elapsed_t_ms < section_t_start
                || elapsed_t_ms > section_t_stop) {
            
            this.reset(elapsed_t_ms);
            
            return;
        }
    }
    
    @Override
    public void put(LongData data) {
        try {
            dataToDrawBuffer.put(data);
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void reset(long elapsed_t_ms){
        // outside of the range --> reset
            this.section_t_start = elapsed_t_ms;
            this.section_t_stop = section_t_start + (long) (lenght_px / xScale_px_per_ms);

            manager.resetArea();
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

    // protected methods
    protected abstract StaticRingBuffer<T> initDrawingElementsPool();
    
    protected int calculate_scaled_value(long value, double scale){
        return (int)(value*scale);
    }

}
