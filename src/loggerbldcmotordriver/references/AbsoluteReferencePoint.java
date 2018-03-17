
package loggerbldcmotordriver.references;

/**
 *
 * @author simon.buehlmann
 */
public class AbsoluteReferencePoint extends AReferencePoint
{
    public final static AbsoluteReferencePoint INSTANCE = new AbsoluteReferencePoint();

    private AbsoluteReferencePoint()
    {
        super(0, 0, 0);
    }
    
    @Override
    public int getAbsolutX()
    {
        return super.getX();
    }

    @Override
    public int getAbsolutY()
    {
        return super.getY();
    }

    @Override
    public int getAbsolutAngle()
    {
        return super.getAngle();
    }
}
