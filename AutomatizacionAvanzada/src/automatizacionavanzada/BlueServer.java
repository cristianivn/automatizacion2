/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatizacionavanzada;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BlueServer implements Runnable {

    /**
     * @param args the command line arguments
     */
    StreamConnectionNotifier notifier;
    StreamConnection connection = null;

    public BlueServer() {
        // TODO code application logic here
        LocalDevice local = null;

        // setup the server to listen for connection
        try {
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);

            UUID uuid = new UUID( //the uid of the service, it has to be unique,
                    "1101", true);
            String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
            notifier = (StreamConnectionNotifier) Connector.open(url);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // waiting for connection

    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("waiting for connection...");
                connection = notifier.acceptAndOpen();
                System.out.println("SE CONECTO ALGUIEN");
                Thread processThread = new Thread(new ProcessConnectionThread(connection));
                processThread.start();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private static class ProcessConnectionThread implements Runnable {

        private StreamConnection con;
        private BufferedReader reader2;

        public ProcessConnectionThread(StreamConnection connection) {
            con = connection;
            try {
                reader2 = new BufferedReader(new InputStreamReader(con.openInputStream()));
            } catch (IOException ex) {
                Logger.getLogger(BlueServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            while (true) {
                readSerial();
            }
        }

        private void readSerial() {
            try {
                String dato = reader2.readLine();
                System.out.println("DATO DEL CLIENTE ANDROID: " + dato);
                switch (dato) {
                    case "a":
                        Crixus.getInstance().getModbus().writeCommandThread(0, true);
                        break;
                    case "d":
                        Crixus.getInstance().getModbus().writeCommandThread(5, true);
                        break;
                }
            } catch (IOException | NumberFormatException e) {
                //  System.err.println("communication not established");
            }
        }
    }
}
