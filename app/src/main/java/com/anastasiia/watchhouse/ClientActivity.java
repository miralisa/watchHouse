package com.anastasiia.watchhouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    Boolean stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mclient);
        response = (TextView) findViewById(R.id.response);
        btnRecord = (Button) findViewById(R.id.buttonRecord);
        btnStop = (Button) findViewById(R.id.btnStop);

        final Client myClient = new Client("192.168.1.91", 5555, response);

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClient.execute();


            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }
}
