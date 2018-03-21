
package loggerbldcmotordriver.com;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author simon
 */
public class SimulatorWrapper extends PicocomWrapper
{
    public SimulatorWrapper() {
    }

    @Override
    public InputStream run(BaudRate baudrate, Parity parity, DataBits databits, FlowControl flowcontrol, String device) throws IOException {
        Process p = Runtime.getRuntime().exec("java -jar /home/simon/NetBeansProjects/SimulatorBLDCMotorDriver/dist/SimulatorBLDCMotorDriver.jar");

        return p.getInputStream();
    }
}
