package com.anastasiia.watchhouse;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static android.R.id.message;

/**
 * Created by anastasiia on 01.04.17.
 */

public class Client extends AsyncTask<Void, Void, Void> {

    String dstAddress;
    int dstPort;
    String response = "";
    TextView textResponse;
    Socket socket = null;
    String tag = "@---Client---@";


    Client(String addr, int port, TextView textResponse) {
        Log.d(tag, "debut creation");
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        Log.d(tag, "creation ok");
      }

    public String reception(){
        Log.d(tag, "debut reception");
        DataInputStream inputStream = null;
        try {
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            response = "IOException: " + e.toString();
        }
        try {
            response = inputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            response = "IOException: " + e.toString();
        }
        Log.d(tag, "reception ok");
        return response;
    }

    public String envoi(String msg){
        Log.d(tag, "debut envoi");
        DataOutputStream outputStream = null;
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(tag, "envoi ok");
        return response;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d(tag, "debut doItBackground");
        try {
            socket = new Socket(dstAddress, dstPort);

            String msgReply = "Activate record";
            envoi(msgReply);
            response = reception();

                //envoi("Stop record");
                //Log.d(sep, "after sending, before reception");


        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        }
        Log.d(tag, "doItBackground ok");
        return null;//response;
    }

    @Override
    protected void onPostExecute(Void result) {
        textResponse.setText(response);
        super.onPostExecute(result);
    }

}
