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
public class StaticValueAxis implements IDrawable
{

    private final AReferencePoint left_upper_corner;

    private int intervall_px;

    private final List<Line> lines;

    public StaticValueAxis(AReferencePoint left_upper_corner, int size_x, int size_y, int intervall_px, Orientation orientation, int start_grid) {
        this.left_upper_corner = left_upper_corner;
        this.intervall_px = intervall_px;

        AReferencePoint right_lower_corner = new ReferencePoint(size_x, size_y, 0, left_upper_corner);

        lines = new ArrayList<>();

        switch (orientation) {
            case HORIZONTAL: {
                // negative direction --> to top
                int cnt = 0;
                while (true) {
                    ReferencePoint position = new ReferencePoint(0, start_grid - cnt * intervall_px, 0, left_upper_corner);
                    if (position.getY() > 0) {
                        // inside the drawing area
                        lines.add(new Line(position, 1, Color.GRAY).setDimension(right_lower_corner.getX(), 0));
                        cnt++;
                    }
                    else {
                        break;
                    }
                }

                // positive direction --> to bottom
                cnt = 1;
                while (true) {
                    ReferencePoint position = new ReferencePoint(0, start_grid + cnt * intervall_px, 0, left_upper_corner);
                    if (position.getY() < right_lower_corner.getY()) {
                        // inside the drawing area
                        lines.add(new Line(position, 1, Color.GRAY).setDimension(right_lower_corner.getX(), 0));
                        cnt++;
                    }
                    else {
                        break;
                    }
                }
            }
            break;

            case VERTICAL: {
                // negative direction --> to left
                int cnt = 0;
                while (true) {
                    ReferencePoint position = new ReferencePoint(start_grid - cnt * intervall_px, 0, 0, left_upper_corner);
                    if (position.getX() < 0) {
                        // inside the drawing area
                        lines.add(new Line(position, 1, Color.GRAY).setDimension(0, right_lower_corner.getY()));
                        cnt++;
                    }
                    else {
                        break;
                    }
                }

                // positive direction --> to right
                cnt = 1;
                while (true) {
                    ReferencePoint position = new ReferencePoint(start_grid + cnt * intervall_px, 0, 0, left_upper_corner);
                    if (position.getX() > right_lower_corner.getX()) {
                        // inside the drawing area
                        lines.add(new Line(position, 1, Color.GRAY).setDimension(0, right_lower_corner.getY()));
                        cnt++;
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

    public enum Orientation
    {
        HORIZONTAL,
        VERTICAL
    }
}
