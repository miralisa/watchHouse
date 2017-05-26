package com.anastasiia.watchhouse;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class Settings extends AppCompatActivity {

    TextView name, email, ip;
    ImageButton iBname, iBemail, iBip;
    DBHandler db;
    final Context context = this;
    TextView textViewEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = new DBHandler(this);

        name = (TextView)findViewById(R.id.textUserName);
        email = (TextView)findViewById(R.id.textUserEmail);
        ip = (TextView)findViewById(R.id.textUserIP);

        iBname = (ImageButton)findViewById(R.id.imageButtonName);
        iBemail = (ImageButton)findViewById(R.id.imageButtonEmail);
        iBip = (ImageButton)findViewById(R.id.imageButtonIp);

        final User u1 = db.getUser(1);
        name.setText(u1.getName());
        email.setText(u1.getEmail());
        ip.setText(u1.getIp());

        LayoutInflater li = LayoutInflater.from(context);

        View promptsView = li.inflate(R.layout.prompts_settings, null);

        textViewEdit = (TextView) promptsView.findViewById(R.id.editTextView);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        final EditText changed = (EditText) promptsView
                .findViewById(R.id.editTextSettings);


        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        iBname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewEdit.setText("Name:");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // Inserting
                                        Log.d("Insert: ", "Inserting ..");
                                        name.setText(changed.getText().toString());
                                        u1.setName(changed.getText().toString());
                                        db.updateUser(u1);

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        iBemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewEdit.setText("Email:");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // Inserting
                                        Log.d("Insert: ", "Inserting ..");
                                        email.setText(changed.getText().toString());
                                        u1.setEmail(changed.getText().toString());
                                        db.updateUser(u1);

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        iBip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewEdit.setText("IP:");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // Inserting
                                        Log.d("Insert: ", "Inserting ..");
                                        ip.setText(changed.getText().toString());
                                        u1.setIp(changed.getText().toString());
                                        db.updateUser(u1);

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

    }
}
