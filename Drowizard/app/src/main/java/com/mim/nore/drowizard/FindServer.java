package com.mim.nore.drowizard;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class FindServer extends Activity {

    private Client con;
    private RadioGroup radGroup;
    private LinearLayout layout;
    private EditText cajita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_server);
        cajita= (EditText) findViewById(R.id.cajita);
        radGroup = (RadioGroup) findViewById(R.id.radioGrupo);
        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.color1:
                        Toast.makeText(getApplicationContext(), "color 1 ", Toast.LENGTH_SHORT).show();
                        if (con != null) {
                            con.writeCommand("colorOneRemove");
                        }
                        break;
                    case R.id.color2:
                        Toast.makeText(getApplicationContext(), "color 2 ", Toast.LENGTH_SHORT).show();
                        if (con != null) {
                            con.writeCommand("colorTwoRemove");
                        }
                        break;
                }
            }
        });

        layout = (LinearLayout) findViewById(R.id.layout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_server, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void connectToServer(View v) {

        try {
            con = new Client("192.168.43.12", 9000);
            new Thread(con).start();
            Toast.makeText(
                    FindServer.this, "Conectando.....", Toast.LENGTH_SHORT
            ).show();
            v.setEnabled(false);

            /*int childs= layout.getChildCount();
            for(int i=0;i<childs;i++){
                View w=layout.getChildAt(i);
                if(v instanceof RadioGroup){
                    int radiosCount= ((RadioGroup) w).getChildCount();
                    for(int k=0;k<radiosCount;k++){
                        View z=((RadioGroup) w).getChildAt(k);
                        z.setEnabled(true);
                    }
                }else if(v instanceof Button){
                    if(!(v.getId()==R.id.connect)){
                        v.setEnabled(true);
                    }
                }
            }*/
        } catch (Exception e) {

        }
    }

    public void setColor(View v) {
        if (con != null) {
            con.writeCommand("setting color");
        }
    }

    public void start(View v) {
        if (con != null) {
            con.writeCommand("start procces");
        }
    }

    public void test(View v) {
        if (con != null) {
            con.writeCommand("test ultrasonic");
        }
    }

    public void setQuantity(View v) {
        if (con != null) {
            if(cajita.getText()!=null &&cajita.getText().length()>0) {
                con.writeCommand("quan"+cajita.getText());
            }
        }
    }

    private class Client implements Runnable {
        private String ip;
        private int port;
        private Socket socket = null;
        private boolean alive = false;
        private PrintWriter out;

        public Client(String ip, int port) {
            this.ip = ip;
            this.port = port;

        }

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, port);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                writeCommand("hola crayola!!!!");
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_LONG).show();
                alive = true;
            }
            // while(!alive){

            //}

        }

        public void writeCommand(String command) {

            out.println(command);
            out.flush();

        }
    }
}
