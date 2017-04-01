package com.anastasiia.watchhouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainVideoActivity extends AppCompatActivity {

    Button bServeur, bClient;
    TextView response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video);
        bServeur = (Button)findViewById(R.id.btnServeur);
        bClient = (Button) findViewById(R.id.btnClient);
        response = (TextView) findViewById(R.id.responseTextView);
        final Server server = new Server(this);

        bClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client myClient = new Client("192.168.1.91", 5555, response);
                myClient.execute();
                response.setText("");


            }
        });

        bServeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                response.setText(server.getIpAddress() + ":" + server.getPort());

            }
        });
    }
}
