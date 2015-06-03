package automatizacionavanzada;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ColorSensor implements Runnable {

    private CommPortIdentifier portId = null;
    private InputStream serialInt;
    private InputStreamReader reader;
    private BufferedReader reader2;
    private SerialPort serialPort;
    private OutputStream output;

    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 9600;
    private String current;
    public boolean readPort = true;

    public ColorSensor() {
        CommPortIdentifier portId = null;

        try {

            portId = CommPortIdentifier.getPortIdentifier("COM11");
            serialPort = (SerialPort) portId.open("hello", TIME_OUT);
            serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            //serialPort.a
            output = serialPort.getOutputStream();
            serialInt = serialPort.getInputStream();
            reader = new InputStreamReader(serialInt);
            reader2 = new BufferedReader(reader);

        } catch (NoSuchPortException npe) {
            System.out.println("no hay arduino");
        } catch (PortInUseException | UnsupportedCommOperationException | IOException ex) {
            Logger.getLogger(ColorSensor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {

        while (readPort) {
            readSerial();
        }
        System.out.println("PORT CLOSED");
    }

    public void close() {
        serialPort.close();
        System.out.println("port closed");
    }

    private byte[] readBuffer = new byte[400];

    private void readSerial() {
        try {
            current = reader2.readLine();
            System.out.println(current);
        } catch (IOException | NumberFormatException e) {
            //  System.err.println("communication not established");
        }
    }

    public String getCurrent() {
        return current;
    }

}
