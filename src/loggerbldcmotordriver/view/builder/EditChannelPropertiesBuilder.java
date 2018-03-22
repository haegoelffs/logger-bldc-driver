package loggerbldcmotordriver.view.builder;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import loggerbldcmotordriver.view.builder.listener.ISelectTimeResolutionListener;
import loggerbldcmotordriver.view.elements.NumberField;

/**
 *
 * @author simon
 */
public class EditChannelPropertiesBuilder
{

    final ComboBox<String> cb_select_channel;
    final NumberField nf_resolution, nf_first_value;
    final Button b_save;

    final VBox elements;

    private TitledPane view;

    public EditChannelPropertiesBuilder(ISelectTimeResolutionListener listener) {
        cb_select_channel = new ComboBox<>();
        cb_select_channel.getItems().addAll(
                GUIStringCollection.CHANNEL_FREQUENZY,
                GUIStringCollection.CHANNEL_ROT_POS,
                GUIStringCollection.CHANNEL_ROT_POS_SETPOINT,
                GUIStringCollection.CHANNEL_ROT_POS_CONTROL_OUT,
                GUIStringCollection.CHANNEL_CYCLE_TIME);

        nf_resolution = new NumberField();
        nf_first_value = new NumberField();
        b_save = new Button(GUIStringCollection.SAVE);

        elements = new VBox(
                new Label(GUIStringCollection.CHANNEL),
                cb_select_channel,
                new Label(GUIStringCollection.RESOLUTION),
                nf_resolution,
                new Label(GUIStringCollection.FIRST_VALUE),
                nf_first_value,
                b_save);
        elements.setSpacing(5);

        view = new TitledPane(GUIStringCollection.CHANNEL_RES, elements);
        view.setCollapsible(false);

    }

    public TitledPane getView() {
        return view;
    }
}
