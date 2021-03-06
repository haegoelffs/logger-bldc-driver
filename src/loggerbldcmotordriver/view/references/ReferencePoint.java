
package loggerbldcmotordriver.view.references;

/**
 *
 * @author simon.buehlmann
 */
public class ReferencePoint extends AReferencePoint
{
    private final AReferencePoint REFERENCE;
    
    public ReferencePoint(int x, int y, int angle, AReferencePoint reference)
    {
        super(x, y, angle);
        
        this.REFERENCE = reference;
    }
    
    /**
     * move relative to the actual position
     * @param x
     * @param y
     * @param angle 
     */
    public void relativeMove(int x, int y, int angle)
    {
        super.setX(super.getX() + x);
        super.setY(super.getY() + y);
        super.setAngle(super.getAngle() + angle);
    }
    
    /**
     * absolute move (this means relative to the reference)
     * @param x
     * @param y
     * @param angle 
     */
    public void absoluteMove(int x, int y, int angle)
    {
        super.setX(x);
        super.setY(y);
        super.setAngle(angle);
    }
    
    public AReferencePoint getReference()
    {
        return this.REFERENCE;
    }

    @Override
    public int getAbsolutX()
    {
        return this.REFERENCE.getAbsolutX() + super.getX();
    }

    @Override
    public int getAbsolutY()
    {
        return this.REFERENCE.getAbsolutY() + super.getY();
    }

    @Override
    public int getAbsolutAngle()
    {
        return AReferencePoint.calculateAngle( this.REFERENCE.getAbsolutAngle() + super.getAngle());
    }

    @Override
    public String toString()
    {
        return "X: " + this.getAbsolutX() + " Y: " + this.getAbsolutY() + " Angle: " + this.getAbsolutAngle();
    }
}
