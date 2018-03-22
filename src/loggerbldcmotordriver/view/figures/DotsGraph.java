package loggerbldcmotordriver.view.figures;

import loggerbldcmotordriver.framework.IDataSink;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import loggerbldcmotordriver.framework.RingBuffer;
import loggerbldcmotordriver.framework.RingBufferException;
import loggerbldcmotordriver.framework.StaticRingBuffer;
import loggerbldcmotordriver.view.references.AReferencePoint;
import loggerbldcmotordriver.view.references.ReferencePoint;

/**
 *
 * @author simon
 */
public class DotsGraph implements ITimeSynchronizedDrawable, IDataSink<IDataPoint>
{
    private final AReferencePoint zero_position;

    private long section_t_start, section_t_stop;

    private final StaticRingBuffer<Dot> dotsPool;

    private final RingBuffer<IDataPoint> dataToDrawBuffer;

    private int lenght_px;

    // axis scale
    private double xScale_px_per_ms;
    private double yScale_px_per_value;

    /**
     * Draws the values from left to right. If the screen is full, it reset it.
     *
     * @param zero_position
     * @param lenght_px
     * @param xScale_px_per_ms
     * @param yScale_px_per_value
     * @param dotsRadius
     * @param dotsColor
     * @param manager
     */
    public DotsGraph(
            AReferencePoint zero_position,
            int lenght_px,
            double xScale_px_per_ms,
            double yScale_px_per_value,
            int dotsRadius,
            Color dotsColor) {
        this.zero_position = zero_position;

        this.lenght_px = lenght_px;
        this.xScale_px_per_ms = xScale_px_per_ms;
        this.yScale_px_per_value = yScale_px_per_value;

        this.section_t_start = 0;
        this.section_t_stop = (long) (lenght_px / xScale_px_per_ms);

        // init buffer for dots
        dotsPool = new StaticRingBuffer<>(Dot.class, lenght_px);

        dataToDrawBuffer = new RingBuffer<>(IDataPoint.class, lenght_px);

        for (int cnt = 0; cnt < lenght_px; cnt++) {
            dotsPool.put(new Dot(new ReferencePoint(0, 0, 0, zero_position), dotsRadius, dotsColor));
        }
    }

    @Override
    public void draw(GraphicsContext gc, long elapsed_t_ms) {
        // check range
        if (elapsed_t_ms >= section_t_start
                && elapsed_t_ms <= section_t_stop) {

            IDataPoint data = dataToDrawBuffer.get();
            if (data != null) {
                // new data to draw

                Dot dot = dotsPool.getNext();
                // calc position
                long delta_t = data.getTimestamp_us()/1000- section_t_start;
                dot.getPosition().setX((int) (delta_t * xScale_px_per_ms));
                dot.getPosition().setY((int) (data.getValue()* yScale_px_per_value));

                // draw
                dot.draw(gc);
            }
        }
        else {
            // outside of the range --> reset
            this.section_t_start = elapsed_t_ms;
            this.section_t_stop = section_t_start + (long) (lenght_px / xScale_px_per_ms);
        }
    }

    // getter & setter
    @Deprecated
    public final void setSpeedOfXAxis(double px_per_second) {
        this.xScale_px_per_ms = px_per_second / 1000;
    }

    public void setxScale_px_per_ms(double xScale_px_per_ms) {
        this.xScale_px_per_ms = xScale_px_per_ms;
    }

    public void setyScale_px_per_value(double yScale_px_per_value) {
        this.yScale_px_per_value = yScale_px_per_value;
    }

    @Override
    public void put(IDataPoint data) {
        try {
            dataToDrawBuffer.put(data);
        }
        catch (RingBufferException ex) {
            throw new RuntimeException(ex);
        }
    }

}
