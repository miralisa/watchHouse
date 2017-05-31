package com.anastasiia.watchhouse;


import android.content.DialogInterface;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
import java.util.Calendar;


public class CameraActivity extends AppCompatActivity {
    TextView response;
    Button btnStop;
    Camera server;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    MediaRecorder recorder;
    Chronometer chronometer;
    Boolean isRecording;
    User u;

    private MotionDetector motionDetector;
    private MotionDetectorCallback motionDetectorCallback;
    private android.hardware.Camera mCamera;
    File mediaStorageDir;
    String saveFile;
    DBHandler db;
    private String email;


    protected void startRecording(){
        saveFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WH_"+getDateTime(0)+".mp4";
        if (!isRecording){
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
            CamcorderProfile cpHigh = CamcorderProfile
                    .get(CamcorderProfile.QUALITY_HIGH);
            recorder.setProfile(cpHigh);
            recorder.setMaxDuration(10000);
            recorder.setPreviewDisplay(surfaceHolder.getSurface());
            recorder.setOutputFile(saveFile);
            recorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                        recorder.stop();
                        chronometer.stop();
                        sendMail(0,saveFile,"video", getDateTime(1));
                        recorder.release();
                    }
                }
            });
            try {
                recorder.prepare();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Toast.makeText(this, "Starting a record!", Toast.LENGTH_LONG).show();
            recorder.start();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();

        }
         //return saveFile;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        //Awake ecran
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        response = (TextView) findViewById(R.id.textView);
        server = new Camera(this);
        response.setText(server.getIpAddress() + ":" + server.getPort());
        //btnStop  = (Button)findViewById(R.id.btnStop);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        isRecording = false;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please enter this IP address on your smartphone in mode Watcher.\nIP :"+server.getIpAddress());
        alertDialogBuilder.setNegativeButton("Close",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        /*
        db = new DBHandler(this);
        if(db.getUsersCount()==0) {
            Toast.makeText(getApplicationContext(), "You should run a Watcher mode first!", Toast.LENGTH_LONG).show();

        }else{
            u = db.getUser(1);
            Toast.makeText(getApplicationContext(), "You will receive notifications on email: "+u.getEmail(), Toast.LENGTH_LONG).show();

        } */

        motionDetector = new MotionDetector(getApplicationContext(), (SurfaceView) findViewById(R.id.surfaceViewDetection));
        motionDetectorCallback = new MotionDetectorCallback() {
            @Override
            public void onMotionDetected() {
                //Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                //v.vibrate(100);
                Toast.makeText(getApplicationContext(), "Motion Detected", Toast.LENGTH_LONG).show();
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

        //Toast.makeText(getApplicationContext(), "Motion detection mode", Toast.LENGTH_LONG).show();
        onResumeMD();



        response.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String sentences[] = s.toString().split(" ");
                String firstWord = sentences[0];

                if(s.toString().compareTo("Record") == 0){
                    motionDetector.onPause();
                    startRecording();

                }
                if (s.toString().compareTo("Stop") == 0){
                    Toast.makeText(getApplicationContext(), "Motion detection mode was stopped", Toast.LENGTH_LONG).show();
                    motionDetector.onPause();

                }
                if (firstWord.compareTo("Email") == 0){
                    String email = sentences[1];
                    setEmail(email);
                    Toast.makeText(getApplicationContext(), "You will receive notifications on email: "+email, Toast.LENGTH_LONG).show();

                }
                if (s.toString().compareTo("Detect") == 0){
                    if(motionDetectorCallback!=null) {
                        Toast.makeText(getApplicationContext(), "Motion detection mode ", Toast.LENGTH_LONG).show();

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

    private String getEmail() {
        return email;
    }
    private void setEmail(String email) {
        this.email = email;
    }
    //@Override
    protected void onResumeMD() {
        //super.onResume();
        if(motionDetector!=null) {
            motionDetector.onResume();
            if (motionDetector.checkCameraHardware()) {
                Toast.makeText(getApplicationContext(), "Camera detected", Toast.LENGTH_LONG).show();
                mCamera = motionDetector.getCamera();

            } else {
                Toast.makeText(getApplicationContext(), "No camera available", Toast.LENGTH_LONG).show();

            }
        }
    }


    android.hardware.Camera.PictureCallback mPicture = new android.hardware.Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
            final File pictureFile = getOutputMediaFile();
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
           sendMail(0,pictureFile.getAbsolutePath(),"image", getDateTime(1));
           mCamera.startPreview();
        }

    };

    private void sendMail(final int sec, final String file, final String typeFile, final String date) {
        Toast.makeText(getApplicationContext(), "Sending mail to "+ getEmail(), Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("WatchUHouse@gmail.com",
                            "jbdf4mnP");
                    Thread.sleep(sec);

                    sender.sendMail("WatchHouse: Motion detection "+date,
                            "Hey,\n\nWe detected some motion at your home, please, take a look to the "+typeFile+" below.\n" +
                                    "P.S. If you want to take a video and see what is happening now, please," +
                                    " go to WatchHouse and click on Record Video button.\n\nStay safe,\n" +
                                    "Your WatchHouse.", file,
                            "WatchUHouse@gmail.com", getEmail());
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();

    }

    private String getDateTime(int bool){
        Calendar cc = Calendar.getInstance();
        int year = cc.get(Calendar.YEAR);
        int month = cc.get(Calendar.MONTH);
        int day = cc.get(Calendar.DAY_OF_MONTH);
        int hour = cc.get(Calendar.HOUR_OF_DAY);
        int minute = cc.get(Calendar.MINUTE);
        int second = cc.get(Calendar.SECOND);
        String date ="";
        if(bool == 0) {
            date = day + "-" + month + "-" + year + " " + hour + "h" + minute + "m" + second + "s";
        } else
        if(bool == 1){
            date = day + "-" + month + "-" + year + " " + hour + "h" + minute + "m";
        }

        return date;
    }

    private File getOutputMediaFile() {
        // Create a media file name

        mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "watchHouse");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("watchHouse", "failed to create directory");
            }
        }

        String date = getDateTime(0);

        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "WH_" + date + ".jpg");

        return mediaFile;
    }




}
