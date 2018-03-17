package loggerbldcmotordriver;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import loggerbldcmotordriver.elements.Border;
import loggerbldcmotordriver.elements.IDrawingAreaManager;
import loggerbldcmotordriver.elements.JumpingValuesDrawer;
import loggerbldcmotordriver.elements.StaticTimeAxis;
import loggerbldcmotordriver.elements.StaticValueAxis;
import loggerbldcmotordriver.references.AbsoluteReferencePoint;
import loggerbldcmotordriver.references.ReferencePoint;

/**
 *
 * @author simon
 */
public class LoggerBLDCMotorDriver extends Application implements IDrawingAreaManager
{

    private int x_size_in_px = 512;
    private int y_size_in_px = 512;

    double xScale_px_per_ms = 0.1;
    double yScale_px_per_value = 1;
    
    // drawing elements
    StaticTimeAxis timeAxis;
    JumpingValuesDrawer jumpingValuesDrawer;
    Border border;
    StaticValueAxis staticValueAxis;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Logger BLDC Motor - FHNW Bachelor Thesis Bühlmann & Rotzler 2018");

        Group root = new Group();
        Scene theScene = new Scene(root);
        stage.setScene(theScene);

        Canvas canvas = new Canvas(x_size_in_px, y_size_in_px);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        TimeReference timeReference = new TimeReference();
        ReferencePoint right_lower_corner_time_axis = new ReferencePoint(x_size_in_px, y_size_in_px, 0, AbsoluteReferencePoint.INSTANCE);

        //TimeAxis axis = new TimeAxis(zeroPoint_of_plot, timeReference, x_size_in_px, 1000, 100);
        timeAxis = new StaticTimeAxis(right_lower_corner_time_axis, y_size_in_px / 2, 500, xScale_px_per_ms);

        //RunningValuesDrawer valueAxis = new RunningValuesDrawer(timeAxis.getZero_point(), x_size_in_px, xScale_px_per_ms, yScale_px_per_value, 4, Color.RED);
        jumpingValuesDrawer = new JumpingValuesDrawer(
                new ReferencePoint(0, y_size_in_px / 2, 0, AbsoluteReferencePoint.INSTANCE),
                x_size_in_px,
                xScale_px_per_ms,
                yScale_px_per_value,
                5,
                Color.RED,
                this);

        RingBuffer<IntegerData> buffer = new RingBuffer<>(IntegerData.class, 100);
        //DiscreteSinusGenerator sinGen = new DiscreteSinusGenerator(50, 1, 100, 0, timeReference, buffer);
        DiscretRandomSignalGenerator ranGen = new DiscretRandomSignalGenerator(5, 1000, 200, 0, timeReference, buffer);
        DataHandler dataHandler = new DataHandler(buffer, jumpingValuesDrawer);

        border = new Border(AbsoluteReferencePoint.INSTANCE, x_size_in_px, y_size_in_px, 4);
        staticValueAxis = new StaticValueAxis(
                AbsoluteReferencePoint.INSTANCE,
                x_size_in_px,
                y_size_in_px,
                100,
                StaticValueAxis.Orientation.HORIZONTAL,
                y_size_in_px / 2);

        this.resetArea(gc);
        
        new AnimationTimer()
        {
            double old_x = 0;
            double old_y = 0;

            public void handle(long currentNanoTime) {
                long elapsed_t_ms = timeReference.getElapsedTime_ms();
                jumpingValuesDrawer.draw(gc, elapsed_t_ms);
            }
        }.start();

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void resetArea(GraphicsContext gc) {
        gc.clearRect(0, 0, x_size_in_px, y_size_in_px);

        timeAxis.draw(gc);
        border.draw(gc);
        staticValueAxis.draw(gc);
    }
}
