package loggerbldcmotordriver;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import loggerbldcmotordriver.com.DataPool;
import loggerbldcmotordriver.com.LongData;
import loggerbldcmotordriver.com.CommunicationManager;
import loggerbldcmotordriver.com.SimulatorWrapper;
import loggerbldcmotordriver.elements.DotsGraph;
import loggerbldcmotordriver.elements.LineGraph;
import loggerbldcmotordriver.elements.PlotContainer;

/**
 *
 * @author simon
 */
public class LoggerBLDCMotorDriver extends Application
{

    private int x_size_in_px = 1000;
    private int y_size_in_px = 512;

    double xScale_px_per_ms = 0.1;
    double yScale_px_per_value = 1;
    
    // drawing elements
    DotsGraph dotsGraph;
    LineGraph lineGraph;
    PlotContainer plotContainer;
    
    @Override
    public void start(Stage stage) {
        stage.setTitle("Logger BLDC Motor - FHNW Bachelor Thesis Bühlmann & Rotzler 2018");

        Group root = new Group();
        Scene theScene = new Scene(root);
        stage.setScene(theScene);
        

        TimeReference timeReference = new TimeReference();
        DataPool dataPool = new DataPool();
        
        
        plotContainer = new PlotContainer(timeReference);
        root.getChildren().add(plotContainer.getPlot());
        LineGraph noiseGraph = plotContainer.addLineGraph("noise", yScale_px_per_value);
        
        RingBuffer<LongData> buffer = new RingBuffer<>(LongData.class, 100);
        //DiscreteSinusGenerator sinGen = new DiscreteSinusGenerator(50, 1, 100, 0, timeReference, buffer);
        //DiscretRandomSignalGenerator ranGen = new DiscretRandomSignalGenerator(5, 50, 400, 0, timeReference, buffer);
        
        CommunicationManager comManager = new CommunicationManager(dataPool, new SimulatorWrapper());
        
        DataHandler dataHandler = new DataHandler(comManager.getRotationFrequenzy_buffer(), noiseGraph, 100);

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
