
package loggerbldcmotordriver.elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import loggerbldcmotordriver.references.AReferencePoint;
import loggerbldcmotordriver.references.DistanceAndAngleReferencePoint;
import loggerbldcmotordriver.references.IReferencePointContainer;
import loggerbldcmotordriver.references.ReferencePoint;

/**
 *
 * @author simon
 */
public class Line implements IReferencePointContainer, IDrawable
{
    private AReferencePoint reference, startPoint, endPoint;
    private int width;
    private Color color;

    public Line(AReferencePoint reference, int width, Color color) {
        this.reference = reference;
        this.width = width;
        this.color = color;
        
        this.startPoint = new ReferencePoint(0, 0, 0, reference);
        this.endPoint = new ReferencePoint(0, 0, 0, reference);
    }
    
    public Line setDimension(int x, int y){
        this.endPoint.setX(x);
        this.endPoint.setY(y);
        
        return this;
    }
    
    public Line setSymetric(int lenght, int angle){
        this.startPoint = new DistanceAndAngleReferencePoint(lenght/2, angle, reference);
        this.endPoint = new DistanceAndAngleReferencePoint(lenght/2, -angle, reference);
        
        return this;
    }

    @Override
    public void draw(GraphicsContext gc){
        gc.setLineWidth(width);
        gc.setStroke(color);
        gc.strokeLine(startPoint.getAbsolutX(), startPoint.getAbsolutY(),endPoint.getAbsolutX(), endPoint.getAbsolutY());
    }
    
    @Override
    public AReferencePoint getReferencePoint() {
        return startPoint;
    }
}
