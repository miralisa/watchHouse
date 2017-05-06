package com.anastasiia.watchhouse;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientActivity extends AppCompatActivity {

    TextView response;
    Button btnRecord, btnStop, btnMotionDetection;
    String ip = "192.168.1.91";
    int port = 5555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mclient);
        response = (TextView) findViewById(R.id.response);
        btnRecord = (Button) findViewById(R.id.buttonRecord);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnMotionDetection = (Button) findViewById(R.id.btnDetection);




        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client record = new Client(ip, port, response);
                record.execute("Record");

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(ClientActivity.this, btnRecord);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(ClientActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu



        }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client stop = new Client(ip, port, response);
                stop.execute("Stop");
            }
        });

        btnMotionDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client detection = new Client(ip, port, response);
                detection.execute("Detect");

            }
        });

    }
}
