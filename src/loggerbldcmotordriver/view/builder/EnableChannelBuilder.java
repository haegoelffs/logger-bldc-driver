
package loggerbldcmotordriver.view.builder;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import loggerbldcmotordriver.view.builder.listener.IEnableChannelListener;

/**
 *
 * @author simon
 */
public class EnableChannelBuilder
{
    private final VBox elements;
    private final TitledPane view;
    
    private final CheckBox cb_ch1;
    private final CheckBox cb_ch2;
    private final CheckBox cb_ch3;
    private final CheckBox cb_ch4;
    private final CheckBox cb_ch5;

    public EnableChannelBuilder(IEnableChannelListener listener) {
        this.cb_ch1 = new CheckBox(GUIStringCollection.CHANNEL_FREQUENZY);
        this.cb_ch2 = new CheckBox(GUIStringCollection.CHANNEL_ROT_POS);
        this.cb_ch3 = new CheckBox(GUIStringCollection.CHANNEL_ROT_POS_SETPOINT);
        this.cb_ch4 = new CheckBox(GUIStringCollection.CHANNEL_ROT_POS_CONTROL_OUT);
        this.cb_ch5 = new CheckBox(GUIStringCollection.CHANNEL_CYCLE_TIME);
        
        this.elements = new VBox(cb_ch1, cb_ch2, cb_ch3, cb_ch4,cb_ch5);
        this.elements.setSpacing(5);
        
        this.view = new TitledPane(GUIStringCollection.CHANNELS, elements);
        this.view.setCollapsible(false);
        
        this.cb_ch1.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            listener.enableChannel(GUIStringCollection.CHANNEL_FREQUENZY, newValue);
        });
        this.cb_ch2.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            listener.enableChannel(GUIStringCollection.CHANNEL_ROT_POS, newValue);
        });
        this.cb_ch3.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            listener.enableChannel(GUIStringCollection.CHANNEL_ROT_POS_SETPOINT, newValue);
        });
        this.cb_ch4.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            listener.enableChannel(GUIStringCollection.CHANNEL_ROT_POS_CONTROL_OUT, newValue);
        });
        this.cb_ch5.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            listener.enableChannel(GUIStringCollection.CHANNEL_CYCLE_TIME, newValue);
        });
    }

    public TitledPane getView() {
        return view;
    }
}
