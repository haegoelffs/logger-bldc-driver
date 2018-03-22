
package loggerbldcmotordriver.view.figures;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import loggerbldcmotordriver.view.references.AReferencePoint;
import loggerbldcmotordriver.view.references.ReferencePoint;

/**
 *
 * @author simon
 */
public class Grid implements IDrawable
{
    private final static int DEFAULT_BORDER_WIDTH = 4;
    private final static Color DEFAULT_LINE_COLOR = Color.LIGHTGRAY;
    
    private final Border border;
    private final Scale horizontal_scale, vertical_scale;
    
    private final ReferencePoint zero_point;

    public Grid(
            AReferencePoint left_upper_corner, 
            int size_x, 
            int size_y, 
            int grid_intervall_x, 
            int grid_intervall_y, 
            int zero_x, 
            int zero_y) {
        
        this.zero_point = new ReferencePoint(zero_x, zero_y, 0, left_upper_corner);
        
        border = new Border(left_upper_corner, size_x, size_y, DEFAULT_BORDER_WIDTH, DEFAULT_LINE_COLOR);
        
        horizontal_scale = new Scale(left_upper_corner, size_x, size_y, grid_intervall_y, Scale.Orientation.HORIZONTAL, zero_y);
        vertical_scale = new Scale(left_upper_corner, size_x, size_y, grid_intervall_x, Scale.Orientation.VERTICAL, zero_x);
        
    }

    public ReferencePoint getZero_point() {
        return zero_point;
    }

    @Override
    public void draw(GraphicsContext gc) {
        horizontal_scale.draw(gc);
        vertical_scale.draw(gc);
        border.draw(gc);
    }
}
