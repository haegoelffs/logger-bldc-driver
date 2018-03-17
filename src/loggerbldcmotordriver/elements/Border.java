
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
public class Border implements IDrawable
{
    private final AReferencePoint left_upper_corner;
    
    private final List<Line> lines;

    public Border(AReferencePoint left_upper_corner, int x, int y, int width) {
        this.left_upper_corner = left_upper_corner;
        
        AReferencePoint right_lower_corner = new ReferencePoint(x, y, 0, left_upper_corner);
        
        lines = new ArrayList<>();
        lines.add(new Line(right_lower_corner, 2*width, Color.BLACK).setDimension(-x, 0)); // bottom
        lines.add(new Line(left_upper_corner, 2*width, Color.BLACK).setDimension(x, 0)); // top
        lines.add(new Line(right_lower_corner, 2*width, Color.BLACK).setDimension(0, -y)); // right
        lines.add(new Line(left_upper_corner, 2*width, Color.BLACK).setDimension(0, y)); // left
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        for(Line line : lines){
            line.draw(gc);
        }
    }
}