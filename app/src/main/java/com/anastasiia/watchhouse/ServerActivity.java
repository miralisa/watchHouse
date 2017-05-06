package com.anastasiia.watchhouse;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.anastasiia.watchhouse.motiondetection.MotionDetector;
import com.anastasiia.watchhouse.motiondetection.MotionDetectorCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ServerActivity extends AppCompatActivity {
    TextView response;
    Button btnStop;
    Server server;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    MediaRecorder recorder;
    Chronometer chronometer;
    Boolean isRecording;
    //DetectionMode dm;
    //
    //private TextView txtStatus;
    private MotionDetector motionDetector;
    private MotionDetectorCallback motionDetectorCallback;
    private Camera mCamera;
    public int count = 0;
    private boolean detectionMode = false;
    SmsManager smsManager;
    File mediaStorageDir;


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
        //Awake ecran
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        response = (TextView) findViewById(R.id.textView);
        server = new Server(this);
        response.setText(server.getIpAddress() + ":" + server.getPort());
        //btnStop  = (Button)findViewById(R.id.btnStop);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        isRecording = false;

        mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "watchHouse");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("watchHouse", "failed to create directory");
            }
        }
        /*
        smsManager = SmsManager.getDefault();
        //smsManager.
        // ("+33612481787", null, "Motion detected", null, null);
        //smsManager.sendMultimediaMessage(this, Uri.parse( mediaStorageDir.getPath() + File.separator + "WH_" +"3-4-2017_20h40s53" + ".jpg"), null,null, null);
        Toast.makeText(this, mediaStorageDir.getPath() + File.separator + "WH_" +"3-4-2017_20h40s53" + ".jpg",Toast.LENGTH_LONG).show();

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
        sendIntent.putExtra("address","+33612481787");
        sendIntent.putExtra("sms_body", "Motion detected");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+mediaStorageDir.getPath() + File.separator + "WH_" +"3-4-2017_20h40s53" + ".jpg"));
        sendIntent.setType("image/jpg");
        startActivity(sendIntent);
        */
        Toast.makeText(this, mediaStorageDir.getPath() + File.separator + "WH_" +"3-4-2017_20h40s53" + ".jpg",Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("WatchUHouse@gmail.com",
                            "jbdf4mnP");

                    sender.sendMail("WatchUHouse: Datection movement", "Here is msg",
                            mediaStorageDir.getPath() + File.separator + "WH_" +"6-4-2017_14h25s2" + ".jpg",
                            "anastasiia.prysiazhniuk@gmail.com", "12miralis@gmail.com");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();


        /*
        dm = new DetectionMode();
        dm.setListener(new DetectionMode.ChangeListener() {
            @Override
            public void onChange() {
                Toast.makeText(getApplicationContext(),"DetectionMode changed", Toast.LENGTH_LONG).show();


            }
        }); */
        motionDetector = new MotionDetector(getApplicationContext(), (SurfaceView) findViewById(R.id.surfaceViewDetection));
        motionDetectorCallback = new MotionDetectorCallback() {
            @Override
            public void onMotionDetected() {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(100);
                Toast.makeText(getApplicationContext(), "Motion detected", Toast.LENGTH_LONG).show();

                Thread restart_preview = new Thread() {
                                public void run() {
                                    try {
                                        mCamera.takePicture(null, null, mPicture);
                                        Thread.sleep(1000);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                            };
                            restart_preview.start();


            }
            @Override
            public void onTooDark() {
                Toast.makeText(getApplicationContext(), "Too dark here", Toast.LENGTH_LONG).show();
            }
        };

        Toast.makeText(getApplicationContext(), "Motion detection mode", Toast.LENGTH_LONG).show();
        onDetectionModeResume();

        if(motionDetectorCallback!=null) {
            Toast.makeText(getApplicationContext(), "Motion detection mode "+motionDetectorCallback, Toast.LENGTH_LONG).show();

            motionDetector.setMotionDetectorCallback(motionDetectorCallback);
            // Config Options
            motionDetector.setCheckInterval(700);
            motionDetector.setLeniency(35);
            //motionDetector.setMinLuma(1000);
        }

        response.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().compareTo("Record") == 0){
                    startRecording();
                }
                if (s.toString().compareTo("Stop") == 0){
                    stopRecording();
                }
                if (s.toString().compareTo("Detect") == 0){
                    //dm.setDetectionMode(true);
                    Toast.makeText(getApplicationContext(), "Motion detection mode", Toast.LENGTH_LONG).show();
                    onDetectionModeResume();

                    if(motionDetectorCallback!=null) {
                        Toast.makeText(getApplicationContext(), "Motion detection mode "+motionDetectorCallback, Toast.LENGTH_LONG).show();

                        motionDetector.setMotionDetectorCallback(motionDetectorCallback);
                        // Config Options
                        motionDetector.setCheckInterval(700);
                        motionDetector.setLeniency(35);
                        //motionDetector.setMinLuma(1000);
                    }



                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    //@Override
    protected void onDetectionModeResume() {
        //super.onResume();
        if(motionDetector!=null) {
            motionDetector.onResume();
            if (motionDetector.checkCameraHardware()) {
                Toast.makeText(getApplicationContext(), "On motion mode", Toast.LENGTH_LONG).show();
                mCamera = motionDetector.getCamera();

            } else {
                Toast.makeText(getApplicationContext(), "No camera available", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(motionDetector!=null) {
            motionDetector.onPause();
        }
    }

    android.hardware.Camera.PictureCallback mPicture = new android.hardware.Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {

                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();


            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
            mCamera.startPreview();
        }

    };


    private File getOutputMediaFile() {
        // Create a media file name
        Calendar cc = Calendar.getInstance();
        int year=cc.get(Calendar.YEAR);
        int month=cc.get(Calendar.MONTH);
        int day = cc.get(Calendar.DAY_OF_MONTH);
        int hour = cc.get(Calendar.HOUR_OF_DAY);
        int minute = cc.get(Calendar.MINUTE);
        int second = cc.get(Calendar.SECOND);

        String date = day+"-"+month+"-"+year+"_"+hour+"h"+minute+"s"+second;

        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "WH_" + date + ".jpg");
        //count++;

        Log.d("watchHouse","count"+ date);

        return mediaFile;
    }




}

/*
class DetectionMode {
    private boolean dtectionMode = false;
    private ChangeListener listener;

    public boolean isDetectionMode() {
        return dtectionMode;
    }

    public void setDetectionMode(boolean dtectionMode) {
        this.dtectionMode = dtectionMode;
        if (listener != null) listener.onChange();
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}
*/