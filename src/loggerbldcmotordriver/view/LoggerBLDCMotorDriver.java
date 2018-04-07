package loggerbldcmotordriver.view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import loggerbldcmotordriver.datahandler.DataHandler;
import loggerbldcmotordriver.view.figures.DotsGraph;
import loggerbldcmotordriver.view.figures.LineGraph;
import loggerbldcmotordriver.view.figures.LivePlotContainer;

/**
 *
 * @author simon
 */
public class LoggerBLDCMotorDriver extends Application
{
    private int plot_lenght_px = 1000;
    private int plot_height_px = 400;
    // view
    DotsGraph dotsGraph;
    LivePlotContainer mainPlotContainer;

    // model
    private DataHandler dataHandler;

    @Override
    public void start(Stage stage) {
        ObservableList<LogMsgData> messages = FXCollections.observableArrayList();
        
        ListLogMsgBuilder listLogMsgBuilder = new ListLogMsgBuilder(messages);

        Group root = new Group();

        VBox vbox = new VBox(root, listLogMsgBuilder.getView());

        Scene theScene = new Scene(vbox);
        stage.setScene(theScene);
        stage.setMaximized(true);
        stage.setTitle("Logger BLDC Motor - FHNW Bachelor Thesis Bühlmann & Rotzler 2018");
        stage.show();

        plot_lenght_px = (int) Screen.getPrimary().getVisualBounds().getWidth();

        TimeReference timeReference = new TimeReference();

        mainPlotContainer = new LivePlotContainer(plot_lenght_px, plot_height_px, 100, 50, 50);
        root.getChildren().add(mainPlotContainer.getPlot());
        
        
        // init graphs
        LineGraph frequenzy_graph = mainPlotContainer.addLineGraph("frequenzy", "Hz", Color.BLUE, 0, 700);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
