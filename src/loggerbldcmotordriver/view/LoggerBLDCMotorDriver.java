package loggerbldcmotordriver.view;

import loggerbldcmotordriver.datahandler.TimeDataGroup;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import loggerbldcmotordriver.framework.TimeValueData;
import loggerbldcmotordriver.framework.MessageData;
import loggerbldcmotordriver.datahandler.DataHandler;
import loggerbldcmotordriver.view.figures.DotsGraph;
import loggerbldcmotordriver.view.figures.IDataPoint;
import loggerbldcmotordriver.framework.IDataSink;
import loggerbldcmotordriver.view.figures.LineGraph;
import loggerbldcmotordriver.view.figures.PlotContainer;
import loggerbldcmotordriver.view.builder.EditChannelPropertiesBuilder;
import loggerbldcmotordriver.view.builder.listener.ISelectTimeResolutionListener;
import loggerbldcmotordriver.view.builder.ListLogMsgBuilder;
import loggerbldcmotordriver.view.builder.EnableChannelBuilder;
import loggerbldcmotordriver.view.builder.GUIStringCollection;
import loggerbldcmotordriver.view.builder.SelectTimeResolutionBuilder;
import loggerbldcmotordriver.view.builder.listener.IEnableChannelListener;

/**
 *
 * @author simon
 */
public class LoggerBLDCMotorDriver extends Application implements ISelectTimeResolutionListener, IEnableChannelListener, IDataSink<IDataPoint>
{

    private int x_size_in_px = 1000;
    private int y_size_in_px = 512;

    double xScale_px_per_ms = 0.1;
    double yScale_px_per_value = 0.25;

    // view
    DotsGraph dotsGraph;
    LineGraph frequenzy_graph;
    PlotContainer plotContainer;

    // model
    private DataHandler dataHandler;

    @Override
    public void start(Stage stage) {
        ObservableList<LogMsgData> messages = FXCollections.observableArrayList();
        
        // legend
        SelectTimeResolutionBuilder timeResBuilder = new SelectTimeResolutionBuilder(this);
        EnableChannelBuilder channelBuilder = new EnableChannelBuilder(this);
        EditChannelPropertiesBuilder editChannelPropertiesBuilder = new EditChannelPropertiesBuilder(this);
        ListLogMsgBuilder listLogMsgBuilder = new ListLogMsgBuilder(messages);

        HBox legend = new HBox(timeResBuilder.getView(), channelBuilder.getView(), editChannelPropertiesBuilder.getView());
        legend.setSpacing(5);

        Group root = new Group();

        VBox vbox = new VBox(root, legend, listLogMsgBuilder.getView());

        Scene theScene = new Scene(vbox);
        stage.setScene(theScene);
        stage.setMaximized(true);
        stage.setTitle("Logger BLDC Motor - FHNW Bachelor Thesis Bühlmann & Rotzler 2018");
        stage.show();

        x_size_in_px = (int) Screen.getPrimary().getVisualBounds().getWidth();

        TimeReference timeReference = new TimeReference();

        plotContainer = new PlotContainer(timeReference, this, x_size_in_px, y_size_in_px, xScale_px_per_ms);
        root.getChildren().add(plotContainer.getPlot());
        frequenzy_graph = plotContainer.addLineGraph("noise", yScale_px_per_value);

        dataHandler = new DataHandler(
                (MessageData data) -> {
                    messages.add(new LogMsgData(data));
                },
                (IDataPoint data) -> {
                    frequenzy_graph.put(data);
                });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void timeResolutionSelected(int ms_per_interval) {
        plotContainer.set_timeMs_per_interval(ms_per_interval);
    }

    @Override
    public void enableChannel(String channel, boolean enable) {
        switch (channel) {
            case GUIStringCollection.CHANNEL_FREQUENZY:
                dataHandler.getComManager().setRot_freq_enabled(enable);
                break;
            case GUIStringCollection.CHANNEL_ROT_POS:
                dataHandler.getComManager().setRot_pos_enabled(enable);
                break;
            case GUIStringCollection.CHANNEL_ROT_POS_SETPOINT:
                dataHandler.getComManager().setRot_pos_setpoint_enabled(enable);
                break;
            case GUIStringCollection.CHANNEL_ROT_POS_CONTROL_OUT:
                dataHandler.getComManager().setRot_pos_controller_out_enabled(enable);
                break;
            case GUIStringCollection.CHANNEL_CYCLE_TIME:
                dataHandler.getComManager().setCycletime_enabled(enable);
                break;
        }
    }

    @Override
    public void put(IDataPoint data) {
        dataHandler.recycleData(data);
    }
    
    
}
