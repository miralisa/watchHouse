package com.anastasiia.watchhouse;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.Message;


public class ClientActivity extends AppCompatActivity {

    TextView response;
    Button btnRecord, btnStop, btnMotionDetection, btnReadMails;
    ImageButton imageButton;
    String ip = "";
    int port = 5555;
    final Context context = this;
    DBHandler db;
    User u1;
    //private EditText result;
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mclient);
        response = (TextView) findViewById(R.id.response);
        btnRecord = (Button) findViewById(R.id.buttonRecord);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnMotionDetection = (Button) findViewById(R.id.btnDetection);
        btnReadMails = (Button)findViewById(R.id.btnMail);
        imageButton = (ImageButton)findViewById(R.id.settings);

        db = new DBHandler(this);

        LayoutInflater li = LayoutInflater.from(context);

        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userName = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserName);
        final EditText userEmail = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserEmail);
        final EditText userIP = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserIP);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // Inserting
                                Log.d("Insert: ", "Inserting ..");

                                User insert = new User(userName.getText().toString(), userEmail.getText().toString(), userIP.getText().toString());
                                db.addUser(insert);
                                List<User> users =db.getAllUsers();
                                for (User u : users) {
                                    String log =u.getId() +" "+u.getName();
                                    Log.d("user:", log);
                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        Log.d("db.getUsersCount(): ", db.getUsersCount()+"");

        if(db.getUsersCount()==0) {
             alertDialog.show();
        }




        btnReadMails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent mailClient = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                startActivity(mailClient);
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u1 = db.getUser(1);
                setIp(u1.getIp());
                String mail = u1.getEmail();
                Client email = new Client(getIp(), port, response);
                email.execute("Email", mail);


                Client record = new Client(getIp(), port, response);
                record.execute("Record");
                response.setText("You will receive a video on your email "+mail+".");

        }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u1 = db.getUser(1);
                setIp(u1.getIp());

                Client stop = new Client(getIp(), port, response);
                stop.execute("Stop");
                response.setText("'Detection movement' mode was stopped.");
            }
        });

        btnMotionDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u1 = db.getUser(1);
                setIp(u1.getIp());

                String mail = u1.getEmail();
                Client email = new Client(getIp(), port, response);
                email.execute("Email", mail);

                Client detection = new Client(getIp(), port, response);
                detection.execute("Detect");
                response.setText("You are on 'Detection movement' mode and you will receive notifications" +
                        " on your email "+mail+", if some motion was detected.");

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this, Settings.class);
                startActivity(intent);


            }

        });

    }
}
