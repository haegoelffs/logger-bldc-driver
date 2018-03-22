package loggerbldcmotordriver;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import loggerbldcmotordriver.com.DataPool;
import loggerbldcmotordriver.com.TimeData;
import loggerbldcmotordriver.com.CommunicationManager;
import loggerbldcmotordriver.com.SimulatorWrapper;
import loggerbldcmotordriver.elements.DotsGraph;
import loggerbldcmotordriver.elements.IDataPoint;
import loggerbldcmotordriver.elements.IDataSink;
import loggerbldcmotordriver.elements.LineGraph;
import loggerbldcmotordriver.elements.PlotContainer;

/**
 *
 * @author simon
 */
public class LoggerBLDCMotorDriver extends Application implements IDataSink
{
    private int x_size_in_px = 1000;
    private int y_size_in_px = 512;

    double xScale_px_per_ms = 0.1;
    double yScale_px_per_value = 0.25;
    
    DataPool dataPool;
    
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
        dataPool = new DataPool();
        
        
        plotContainer = new PlotContainer(timeReference,this,xScale_px_per_ms);
        root.getChildren().add(plotContainer.getPlot());
        LineGraph noiseGraph = plotContainer.addLineGraph("noise", yScale_px_per_value);
        
        RingBuffer<TimeData> buffer = new RingBuffer<>(TimeData.class, 100);
        //DiscreteSinusGenerator sinGen = new DiscreteSinusGenerator(50, 1, 100, 0, timeReference, buffer);
        //DiscretRandomSignalGenerator ranGen = new DiscretRandomSignalGenerator(5, 50, 400, 0, timeReference, buffer);
        
        CommunicationManager comManager = new CommunicationManager(dataPool, new SimulatorWrapper());
        
        DataHandler dataHandler = new DataHandler(comManager.getRotationFrequenzy_buffer(), noiseGraph, dataPool, 100000);

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void put(IDataPoint data) {
        if(data instanceof TimeData){
            dataPool.recycleTimeData((TimeData)data);
        }else if(data instanceof TimeDataGroup){
            TimeDataGroup timeDataGroup = ((TimeDataGroup) data);
            TimeData temp = timeDataGroup.getFirstData();
            
            while(true){
                if(temp == ((TimeDataGroup) data).getLastData()){
                    dataPool.recycleTimeData(temp);
                    break;
                }
                
                temp = temp.getYounger();
                dataPool.recycleTimeData(temp.getOlder());
            }
            
            dataPool.recycleTimeDataGroup(timeDataGroup);
        }
    }
}
