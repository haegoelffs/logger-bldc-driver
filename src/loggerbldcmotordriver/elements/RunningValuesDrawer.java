package loggerbldcmotordriver.elements;

import loggerbldcmotordriver.IntegerData;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import loggerbldcmotordriver.IDataSink;
import loggerbldcmotordriver.StaticRingBuffer;
import loggerbldcmotordriver.references.AReferencePoint;

/**
 *
 * @author simon
 */
public class RunningValuesDrawer implements ITimeSynchronizedDrawable, IDataSink
{
    
    private final AReferencePoint positionOfNow;

    private final StaticRingBuffer<AxisDot> dotBuffer;
    private final IntegerData initData;

    private int lenght_px;

    // axis scale
    private double xScale_px_per_ms;
    private double yScale_px_per_value;

    public RunningValuesDrawer(AReferencePoint positionOfNow, int lenght_px, double xScale_px_per_ms, double yScale_px_per_value, int dotsRadius, Color dotsColor) {
        this.positionOfNow = positionOfNow;
        this.lenght_px = lenght_px;
        this.xScale_px_per_ms = xScale_px_per_ms;
        this.yScale_px_per_value = yScale_px_per_value;

        // init buffer for dots
        dotBuffer = new StaticRingBuffer<>(AxisDot.class, lenght_px);
        initData = new IntegerData(0, 0);

        for (int cnt = 0; cnt < lenght_px; cnt++) {
            dotBuffer.put(new AxisDot(initData, positionOfNow, dotsRadius, dotsColor, xScale_px_per_ms, yScale_px_per_value));
        }
    }
    
    @Override
    public void draw(GraphicsContext gc, long elapsed_t_ms) {
        // draw all datas
        for (AxisDot dot : dotBuffer.getAll()) {
            if (dot.getData() != initData) {
                dot.draw(gc, elapsed_t_ms);

                if (Math.abs(dot.getPosition().getX()) > lenght_px) {
                    // outside of the drawing area --> reset
                    dot.setData(initData);
                }
            }
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
    public void put(IntegerData data) {
        dotBuffer.getNext().setData(data);
    }
}
