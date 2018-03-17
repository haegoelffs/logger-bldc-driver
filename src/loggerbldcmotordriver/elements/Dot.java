
package loggerbldcmotordriver.elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import loggerbldcmotordriver.references.ReferencePoint;

/**
 *
 * @author simon
 */
public class Dot implements IDrawable
{
    private ReferencePoint position;
    
    private int radius;
    private Color color;

    public Dot(ReferencePoint position, int radius, Color color) {
        this.position = position;
        this.radius = radius;
        this.color = color;
    }
    
    public ReferencePoint getPosition() {
        return position;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(position.getAbsolutX()-radius, position.getAbsolutY()-radius, radius, radius);
    }
}
