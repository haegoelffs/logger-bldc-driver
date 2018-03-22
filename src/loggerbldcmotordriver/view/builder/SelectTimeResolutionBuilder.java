
package loggerbldcmotordriver.view.builder;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import loggerbldcmotordriver.view.builder.listener.ISelectTimeResolutionListener;

/**
 *
 * @author simon
 */
public class SelectTimeResolutionBuilder
{
    final ToggleGroup group;

    final RadioButton rb_res1, rb_res2, rb_res3, rb_res4, rb_res5;
    
    final VBox buttons;
    
    private TitledPane view;

    public SelectTimeResolutionBuilder(ISelectTimeResolutionListener listener) {
        group = new ToggleGroup();

        rb_res1 = new RadioButton("50ms");
        rb_res1.setToggleGroup(group);
        rb_res1.setSelected(true);

        rb_res2 = new RadioButton("100ms");
        rb_res2.setToggleGroup(group);
        
        rb_res3 = new RadioButton("200ms");
        rb_res3.setToggleGroup(group);
        
        rb_res4 = new RadioButton("400ms");
        rb_res4.setToggleGroup(group);
        
        rb_res5 = new RadioButton("1s");
        rb_res5.setToggleGroup(group);
        
        buttons = new VBox(rb_res1, rb_res2, rb_res3, rb_res4, rb_res5);
        buttons.setSpacing(5);
        
        view = new TitledPane("Auflösung Zeit", buttons);
        view.setCollapsible(false);
        
        group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
            if(rb_res1.isSelected()){
                listener.timeResolutionSelected(50);
            }else if(rb_res2.isSelected()){
                listener.timeResolutionSelected(100);
            }else if(rb_res3.isSelected()){
                listener.timeResolutionSelected(200);
            }else if(rb_res4.isSelected()){
                listener.timeResolutionSelected(400);
            }else if(rb_res5.isSelected()){
                listener.timeResolutionSelected(1000);
            }
        });
    }

    public TitledPane getView() {
        return view;
    }
}
