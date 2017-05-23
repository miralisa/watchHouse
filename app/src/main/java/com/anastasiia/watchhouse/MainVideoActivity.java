package com.anastasiia.watchhouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainVideoActivity extends AppCompatActivity {

    ImageButton bServeur, bClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video);
        bServeur = (ImageButton)findViewById(R.id.btnServeur);
        bClient = (ImageButton) findViewById(R.id.btnClient);


        bClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent clientActivity = new Intent(getBaseContext(), ClientActivity.class);
               startActivity(clientActivity);

            }
        });

        bServeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent serverActivity = new Intent(getBaseContext(), ServerActivity.class);
                startActivity(serverActivity);

            }
        });
    }
}
