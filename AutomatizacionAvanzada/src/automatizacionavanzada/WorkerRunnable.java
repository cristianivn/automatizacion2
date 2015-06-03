/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatizacionavanzada;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author NORE
 */
public class WorkerRunnable implements Runnable {

    protected Socket clientSocket = null;
    protected String serverText = null;
    private InputStreamReader reader;
    private BufferedReader reader2;
    private String current;
    private boolean alive = false;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText = serverText;
    }

    public void run() {
        try {
            InputStream input = clientSocket.getInputStream();
            reader = new InputStreamReader(input);
            reader2 = new BufferedReader(reader);
            System.out.println("CLIENT CONNECTED!!");
            /*OutputStream output = clientSocket.getOutputStream();
             long time = System.currentTimeMillis();
             output.write(("HTTP/1.1 200 OK\n\nWorkerRunnable: "
             + this.serverText + " - "
             + time
             + "").getBytes());
             output.close();
             input.close();
             System.out.println("Request processed: " + time);*/
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }

        while (!alive) {
            readSerial();
        }
    }

    private void readSerial() {
        try {
            current = reader2.readLine();
            if (current != null) {
                System.out.println(current);
            }
        } catch (IOException | NumberFormatException e) {
            //  System.err.println("communication not established");
        }
    }
}
