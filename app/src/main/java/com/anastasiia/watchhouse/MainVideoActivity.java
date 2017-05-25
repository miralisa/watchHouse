package com.anastasiia.watchhouse;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class MainVideoActivity extends AppCompatActivity {

    ImageButton bServeur, bClient;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video);
        bServeur = (ImageButton)findViewById(R.id.btnServeur);
        bClient = (ImageButton) findViewById(R.id.btnClient);
        textView = (TextView) findViewById(R.id.textView);


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
        } else {
            textView.setText("This application needs a connection to the Internet, please, turn it on.");
            // display error
        }


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
