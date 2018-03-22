
package loggerbldcmotordriver.view;

/**
 *
 * @author simon
 */
public class TimeReference
{
    long t_start_ms;

    public TimeReference() {
        reset();
    }
    
    public final void reset(){
        t_start_ms = System.currentTimeMillis();
    }
    
    public int getElapsedTime_ms(){
        return (int)(System.currentTimeMillis() - t_start_ms);
    }
}
