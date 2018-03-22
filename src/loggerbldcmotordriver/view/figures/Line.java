
package loggerbldcmotordriver.view.figures;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import loggerbldcmotordriver.view.references.AReferencePoint;
import loggerbldcmotordriver.view.references.DistanceAndAngleReferencePoint;
import loggerbldcmotordriver.view.references.IReferencePointContainer;
import loggerbldcmotordriver.view.references.ReferencePoint;

/**
 *
 * @author simon
 */
public class Line implements IReferencePointContainer, IDrawable
{
    protected AReferencePoint reference, startPoint, endPoint;
    protected int width;
    protected Color color;

    public Line(AReferencePoint reference, int width, Color color) {
        this.reference = reference;
        this.width = width;
        this.color = color;
        
        this.startPoint = new ReferencePoint(0, 0, 0, reference);
        this.endPoint = new ReferencePoint(0, 0, 0, reference);
    }
    
    public Line set_from_to(int x_from, int y_from, int x_to, int y_to){
        this.startPoint.setX(x_from);
        this.startPoint.setY(y_from);
        
        this.endPoint.setX(x_to);
        this.endPoint.setY(y_to);
        
        return this;
    }
    
    public Line set_to(int x, int y){
        this.endPoint.setX(x);
        this.endPoint.setY(y);
        
        return this;
    }
    
    public Line set_from(int x, int y){
        this.startPoint.setX(x);
        this.startPoint.setY(y);
        
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
