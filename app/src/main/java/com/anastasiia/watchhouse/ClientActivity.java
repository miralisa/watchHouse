package com.anastasiia.watchhouse;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientActivity extends AppCompatActivity {

    TextView response;
    Button btnRecord, btnStop;
    String ip = "192.168.1.91";
    int port = 5555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mclient);
        response = (TextView) findViewById(R.id.response);
        btnRecord = (Button) findViewById(R.id.buttonRecord);
        btnStop = (Button) findViewById(R.id.btnStop);


        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client record = new Client(ip, port, response);
                record.execute("Record");


            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client stop = new Client(ip, port, response);
                stop.execute("Stop");
            }
        });

    }
}
