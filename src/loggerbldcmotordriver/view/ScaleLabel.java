
package loggerbldcmotordriver.view;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import loggerbldcmotordriver.view.figures.IDrawableElement;
import loggerbldcmotordriver.view.references.AReferencePoint;
import loggerbldcmotordriver.view.references.ReferencePoint;

/**
 *
 * @author simon
 */
public class ScaleLabel implements IDrawableElement
{
    private List<TextAndPosition> labels;
    
    private Color color;
    private AReferencePoint pos_first_element;
    
    private int first_value, intervall_px, intervall_value, nr_elements;
    private String unit;
    
    private Orientation orientation;
    
    public ScaleLabel(
            AReferencePoint pos_first_element, 
            int first_value, 
            int intervall_px, 
            int intervall_value, 
            int nr_elements, 
            Color color, 
            String unit,
            Orientation orientation){
        this.pos_first_element = pos_first_element;
        this.first_value = first_value;
        this.intervall_px = intervall_px;
        this.intervall_value = intervall_value;     
        this.nr_elements = nr_elements;
        this.color = color;
        this.unit = unit;
        this.orientation = orientation;
        
        labels = new ArrayList<>(nr_elements);
        
        reset();
    }
    
    private void reset(){
        labels.clear();
        
        for(int cnt = 0; cnt<nr_elements; cnt++){
            switch(orientation){
                case HORIZONTAL:
                    labels.add(
                            new TextAndPosition(
                                    first_value + cnt*intervall_value + unit, 
                                    new ReferencePoint(cnt*intervall_px, 0, 0, pos_first_element)));
                    break;
                case VERTICAL:
                    labels.add(
                            new TextAndPosition(
                                    first_value + cnt*intervall_value + unit, 
                                    new ReferencePoint(0, - cnt*intervall_px, 0, pos_first_element)));
                    break;
            }
        }
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        
        for(TextAndPosition tp : labels){
            gc.fillText(tp.text, tp.position.getAbsolutX(), tp.position.getAbsolutY());
        }
    }
    
    private class TextAndPosition{
        String text;
        ReferencePoint position;

        public TextAndPosition(String text, ReferencePoint position) {
            this.text = text;
            this.position = position;
        }
    }
    
    // enums
    public enum Orientation
    {
        HORIZONTAL,
        VERTICAL
    }
}
