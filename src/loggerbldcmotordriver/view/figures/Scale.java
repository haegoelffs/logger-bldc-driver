package loggerbldcmotordriver.view.figures;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import loggerbldcmotordriver.view.references.AReferencePoint;
import loggerbldcmotordriver.view.references.ReferencePoint;

/**
 *
 * @author simon
 */
public class Scale implements IDrawableElement
{
    private static int DEFAULT_LINE_WIDTH = 1;
    private static Color DEFAULT_LINE_COLOR = Color.LIGHTGRAY;

    private int intervall_px;
    private int start_grid;

    private final List<Line> lines;
    
    private int line_width = DEFAULT_LINE_WIDTH;
    private Color line_color = DEFAULT_LINE_COLOR;
    
    private Orientation orientation;
    
    private final AReferencePoint left_upper_corner;
    private AReferencePoint right_lower_corner;
    private AReferencePoint position_first_line;
    private AReferencePoint position_last_line;
    
    public Scale(AReferencePoint left_upper_corner, int size_x, int size_y, int intervall_px, Orientation orientation, int start_grid) {
        this.left_upper_corner = left_upper_corner;
        this.intervall_px = intervall_px;
        this.orientation = orientation;
        this.start_grid = start_grid;
        
        right_lower_corner = new ReferencePoint(size_x, size_y, 0, left_upper_corner);
        
        lines = new ArrayList<>();
        
        reset();
    }

    public final void reset(){
        switch (orientation) {
            case HORIZONTAL: {
                position_first_line = new ReferencePoint(0, start_grid%intervall_px, 0, left_upper_corner);
                
                int cnt = 0;
                while (true) {
                    ReferencePoint position = new ReferencePoint(0, position_first_line.getY() + cnt * intervall_px, 0, left_upper_corner);
                    if (position.getY() < right_lower_corner.getY()) {
                        // inside the drawing area
                        lines.add(new Line(position, line_width, line_color).set_to(right_lower_corner.getX(), 0));
                        cnt++;
                        position_last_line = position;
                    }
                    else {
                        break;
                    }
                }
            }
            break;

            case VERTICAL: {
                position_first_line = new ReferencePoint(start_grid%intervall_px, 0, 0, left_upper_corner);
                
                int cnt = 0;
                while (true) {
                    ReferencePoint position = new ReferencePoint(position_first_line.getX() + cnt * intervall_px, 0, 0, left_upper_corner);
                    if (position.getX() < right_lower_corner.getX()) {
                        // inside the drawing area
                        lines.add(new Line(position, line_width, line_color).set_to(0, right_lower_corner.getY()));
                        cnt++;
                        position_last_line = position;
                    }
                    else {
                        break;
                    }
                }
            }
            break;
        }
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        for (Line line : lines) {
            line.draw(gc);
        }
    }

    // getter & setter
    public void setIntervall_px(int intervall_px) {
        this.intervall_px = intervall_px;
        reset();
    }

    public void setStart_grid(int start_grid) {
        this.start_grid = start_grid;
        reset();
    }

    public void setLine_width(int line_width) {
        this.line_width = line_width;
        reset();
    }

    public void setLine_color(Color line_color) {
        this.line_color = line_color;
        reset();
    }
    
    public int getNrLines(){
        return lines.size();
    }

    public AReferencePoint getLeft_upper_corner() {
        return left_upper_corner;
    }

    public AReferencePoint getRight_lower_corner() {
        return right_lower_corner;
    }

    public AReferencePoint getPosition_first_line() {
        return position_first_line;
    }

    public AReferencePoint getPosition_last_line() {
        return position_last_line;
    }
    
    // enums
    public enum Orientation
    {
        HORIZONTAL,
        VERTICAL
    }
}
