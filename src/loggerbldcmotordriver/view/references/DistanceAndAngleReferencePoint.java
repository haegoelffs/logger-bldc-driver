
package loggerbldcmotordriver.view.references;


/**
 *
 * @author simon.buehlmann
 */
public class DistanceAndAngleReferencePoint extends AReferencePoint
{
    private final AReferencePoint REFERENCE;
    
    private int distance;
    
    public DistanceAndAngleReferencePoint(int distance, int angle, AReferencePoint reference)
    {
        super();
        this.REFERENCE = reference;
        this.distance = distance;
        this.setAngle(angle);
    }

    @Override
    public int getX()
    {
        int tempX = (int)(Math.cos(Math.toRadians(this.getAbsolutAngle())) * this.distance);
        return tempX;
    }

    @Override
    public int getY()
    {
        int tempY = (int)(Math.sin(Math.toRadians(this.getAbsolutAngle())) * this.distance);
        return tempY;
    }
    
    @Override
    public int getAbsolutX()
    {
        return this.REFERENCE.getAbsolutX() + this.getX();
    }

    @Override
    public int getAbsolutY()
    {
        return this.REFERENCE.getAbsolutY() + this.getY();
    }

    @Override
    public int getAbsolutAngle()
    {
        return AReferencePoint.calculateAngle(this.REFERENCE.getAbsolutAngle() + this.getAngle());
    }
}
