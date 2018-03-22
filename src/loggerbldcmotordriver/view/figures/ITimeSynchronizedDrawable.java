
package loggerbldcmotordriver.view.figures;

import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author simon
 */
public interface ITimeSynchronizedDrawable
{
    public void draw(GraphicsContext gc, long elapsed_t_ms);
}
