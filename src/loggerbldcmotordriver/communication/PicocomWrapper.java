package loggerbldcmotordriver.communication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author sdb
 */
public class PicocomWrapper
{
    public PicocomWrapper() {
    }

    public void printHelp() throws Exception {
        try {
            String line;
            Process p = Runtime.getRuntime().exec("picocom -h");

            DataInputStream dataInputStream = new DataInputStream(p.getInputStream());
            dataInputStream.readByte();

            BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = bri.readLine()) != null) {
                System.out.println(line);
            }
            bri.close();
            while ((line = bre.readLine()) != null) {
                System.out.println(line);
            }
            bre.close();
            p.waitFor();
            System.out.println("Done.");
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }

    public InputStream run(BaudRate baudrate, Parity parity, DataBits databits, FlowControl flowcontrol, String device) throws IOException {
        // sudo picocom --baud 115200  --parity n --databits 8 --flow n -l /dev/ttyUSB0
        String line;
        Process p = Runtime.getRuntime().exec(
                String.format("picocom --baud %s  --parity %s --databits %s --flow %s %s", baudrate, parity, databits, flowcontrol, device));

        return p.getInputStream();
    }

    public enum FlowControl
    {
        software("s"),
        hardware("h"),
        none("n");

        private String s;

        private FlowControl(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }

    public enum Parity
    {
        odd("o"),
        even("e"),
        none("n");

        private String s;

        private Parity(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }

    public enum BaudRate
    {
        br300baud("300"),
        br600baud("600"),
        br1200baud("1200"),
        br2400baud("2400"),
        br4800baud("4800"),
        br9600baud("39600"),
        br19200baud("19200"),
        br38400baud("38400"),
        br57600baud("57600"),
        br115200baud("115200");

        private String s;

        private BaudRate(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }
    
    public enum DataBits
    {
        db5bits("5"),
        db6bits("6"),
        db7bits("7"),
        db8bits("8");

        private String s;

        private DataBits(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }
}
