
package loggerbldcmotordriver.elements;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import loggerbldcmotordriver.references.AReferencePoint;
import loggerbldcmotordriver.references.ReferencePoint;

/**
 *
 * @author simon
 */
public class StaticTimeAxis implements IDrawable
{
    private final AReferencePoint right_lower_corner, zero_point;
    
    private int line_intervall_ms, line_intervall_px, zero_point_px;

    private double xScale_px_per_ms;
    
    private final List<Line> lines;

    public StaticTimeAxis(AReferencePoint right_lower_corner, int zero_point_px ,int line_intervall_ms, double xScale_px_per_ms) {
        this.right_lower_corner = right_lower_corner;
        this.line_intervall_ms = line_intervall_ms;
        this.xScale_px_per_ms = xScale_px_per_ms;
        this.zero_point_px = zero_point_px;
        
        line_intervall_px = (int)(line_intervall_ms*xScale_px_per_ms);
        
        zero_point = new ReferencePoint(0, -zero_point_px, 0, right_lower_corner);
        
        lines = new ArrayList<>();
        
        Line zeroLine = new Line(zero_point, 2, Color.BLACK).setDimension(-right_lower_corner.getX(), 0);
        lines.add(zeroLine);
        
        int nr_of_lines = (int)(right_lower_corner.getX()/line_intervall_px)+1;
        
        for(int cnt = 0; cnt<nr_of_lines; cnt++){
            ReferencePoint position = new ReferencePoint(-cnt*line_intervall_px, 0, 0, right_lower_corner);
            lines.add(new Line(position, 1, Color.GRAY).setDimension(0, -right_lower_corner.getY()));
        }
    }

    public AReferencePoint getZero_point() {
        return zero_point;
    }

    @Override
    public void draw(GraphicsContext gc) {
        for(Line line : lines){
            line.draw(gc);
        }
    }
}
