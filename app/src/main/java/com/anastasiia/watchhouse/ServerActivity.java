package com.anastasiia.watchhouse;


import android.content.pm.ActivityInfo;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;


public class ServerActivity extends AppCompatActivity {
    TextView response;
    Button btnStop;
    Server server;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    MediaRecorder recorder;
    Chronometer chronometer;
    Boolean isRecording;


    protected void startRecording(){
        if (!isRecording){
            String saveFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/video.mp4";
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
            CamcorderProfile cpHigh = CamcorderProfile
                    .get(CamcorderProfile.QUALITY_HIGH);
            recorder.setProfile(cpHigh);
            recorder.setPreviewDisplay(surfaceHolder.getSurface());
            recorder.setOutputFile(saveFile);
            try {
                recorder.prepare();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            isRecording = true;
            Toast.makeText(this, "Starting a record!", Toast.LENGTH_LONG).show();
            recorder.start();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();


        }
    }
    protected void stopRecording() {
        if(recorder!=null &&  isRecording == true){
            Toast.makeText(this, "Stop recording!", Toast.LENGTH_LONG).show();
            recorder.stop();
            recorder.release();
            chronometer.stop();
            //surfaceView = null;
            //surfaceHolder = null;
            isRecording = false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        response = (TextView) findViewById(R.id.textView);
        server = new Server(this);
        response.setText(server.getIpAddress() + ":" + server.getPort());
        //btnStop  = (Button)findViewById(R.id.btnStop);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        isRecording = false;
        /*
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });
        */

        response.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().compareTo("Record") == 0){
                    startRecording();
                } if (s.toString().compareTo("Stop") == 0){
                    stopRecording();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


}
