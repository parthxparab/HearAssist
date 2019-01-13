package com.example.parthxparab.hearassist;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class FreqTest extends AppCompatActivity {

    private int state = 0;
    private static final String TAG = "FreqTest";
    public static boolean running = true;
    final int duration = 20; // length of tone in seconds
    final int sampleRate = 44100; // sample rate at 44,100 times per second
    final int numSamples = duration * sampleRate;// in order to avoid
    // distortion
    // number of samples = duration multiplied by sample rate
    final double sample[] = new double[numSamples];
    //double frequencyOfTone = 1000; // frequency in Hz
    final int testingFrequencies[] = {250, 500, 1000, 2000, 4000, 8000};
    final Double decibelsArray[] = {1.0, 3.16227766, 10.0, 31.6227766, 100.0, 316.227766, 1000.0, 3162.27766, 10000.0, 31622.7766};
    final Integer dbArray[] = {0, 10, 20, 30, 40, 50, 60, 70, 80, 90};
    final byte generatedSound[] = new byte[2 * numSamples]; // array of
    int index = 0;
    int frequency, t;
    int finalDbAnswer[] = {40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40};
    int tempDbArray[] = new int[100000];
    int yes_no[] = new int[100000];
    int khatam, i, x = 4;
    int indexofthis = 0;
    TextView informationTextView;
    int ear = 0;
    ArrayList<Integer> leftEar = new ArrayList<>();
    ArrayList<Integer> rightEar = new ArrayList<>();
    Handler handler;
    Runnable runnable;
    private boolean heard = false;
    private boolean loop = true;
    private ImageView ImageView1;
    private Context context = FreqTest.this;
    private FloatingActionButton btn,btn1;

    public static void stopThread() {
        running = false;
    }

    /**
     * Randomly picks time gap between test tones in ms
     *
     * @return
     */
    public int randomTime() {
        int time;
        double num = Math.random();
        if (num < 0.3) {
            time = 2000;
        } else if (num < 0.67 && num >= 0.3) {
            time = 2500;
        } else {
            time = 3000;
        }
        return time;
    }

    public int checksub(int yes_no[]) {
        int m = 0;
        if (yes_no.length < 4)
            return 100;
        else {
            for (int i = 0; i < yes_no.length - 3; i++) {
                if (yes_no[i] == 1) {
                    if (yes_no[i + 1] == 0 && yes_no[i + 2] == 1 && yes_no[i + 3] == 0) {
                        m = 1;
                        break;
                    }
                }
            }
            if (m == 1)
                return i;  //check for i-1
            else
                return 100;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freq_test);
        btn = (FloatingActionButton) findViewById(R.id.y);
        btn1 = (FloatingActionButton) findViewById(R.id.g);
        handler = new Handler();

        ImageView1 = (ImageView) findViewById(R.id.gif_wave);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(state == 0)
                {
                ImageView1.setImageResource(R.drawable.wave1);
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadImageOriginal();
                    }
                };
                handler.postDelayed(runnable, 500);
                Toast.makeText(FreqTest.this, "Frequency Heard", Toast.LENGTH_SHORT).show();
                state = 0;
                }
                else if (state == 1) {
                    Snackbar.make(v, "Test Completed Successfully", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();                 }
            }
        });
        loadImageOriginal();

            informationTextView = findViewById(R.id.idInformation);

        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        assert am != null;
        am.setStreamVolume(AudioManager.STREAM_MUSIC, 9, 0);

        Thread timingThread = new Thread(new Runnable() {

            public void run() {
                while (loop) {
                    if (!running) {
                        return;
                    }
                    if (heard) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException x) {
                        }
                    }
                }

            }

        }

        );

        Thread screenThread = new Thread(new Runnable() {
            public void run() {
                while (loop) {
                    if (!running) {
                        return;
                    }
                    if (heard) {
                        while (heard) {

                        }
                    }
                }
            }
        });
        Thread testRunningThread = new Thread(new Runnable() {
            public void run() {
                final testThread testThread = new testThread();
                testThread.run();

            }
        });

            testRunningThread.start();
        screenThread.start();
        timingThread.start();
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(state == 0) {
                    Snackbar.make(v, "Test Not Complete", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                else if(state == 1) {
                    Intent in1 = new Intent(FreqTest.this, Graph.class);
                    in1.putExtra("Array", finalDbAnswer);
                    startActivity(in1);
                }

            }
        });



   /*     Intent in = new Intent(FreqTest.this, Graph.class);
        //Bundle b = new Bundle();
        in.putExtra("right", rightEar);
        in.putExtra("left", leftEar);
        Log.d(TAG,"Thread stopped");
        startActivity(in);
        Log.d(TAG,"New activity launched!");*/
    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        //Log.i("Touch Alert", "Screen was hit!" + a++ + heard);
        heard = true;
        return super.dispatchTouchEvent(e);
    }

    @Override
    public void onStop() {
        super.onStop();
        stopThread();
    }

    public byte[] genTone(double volume, int frequencyOfTone) {

        //float angle = 0;
        //double sample[] = new double[numSamples];
        //byte generatedSnd[] = new byte[2 * numSamples];
        /*for (int i = 0; i < numSamples; i++){
            sample[i] = Math.sin(angle);
        }*/
        // fill out the array with samples
        for (int counter = 0; counter < numSamples; ++counter) {
            sample[counter] = Math.sin(2 * Math.PI * counter / (sampleRate / frequencyOfTone));
            // x(t)=A Sin 2pi n fa/fs
        }
        int idx = 0;
        for (final double dVal : sample) {
            final short val = (short) ((dVal * volume));
            //volume controlled by the value multiplied by dVal; max value is 32767
            generatedSound[idx++] = (byte) (val & 0x00ff);
            generatedSound[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
        return generatedSound;
    }

    private void loadImageOriginal() {
        int resourceid = R.drawable.wave;
        RequestOptions options = new RequestOptions();
        options.dontTransform();

        Glide
                .with(context)
                .load(resourceid)
                .apply(options)
                .into(ImageView1);


    }


    /**
     * Writes the parameter byte array to an AudioTrack and plays the array
     *
     * @param generatedSnd- input 16-bit PCM Array
     */
    public AudioTrack playSound(byte[] generatedSnd) {
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length, AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        if (ear == 0) {

            audioTrack.setStereoVolume(0.0f, AudioTrack.getMaxVolume());

        } else {
            audioTrack.setStereoVolume(AudioTrack.getMaxVolume(), 0.0f);
        }
        audioTrack.play();
        return audioTrack;
    }

    public class testThread extends Thread {
        public void run() {
            for (int k = 0; k < 2; k++) {
                ear = k;
                for (int i = 0; i < testingFrequencies.length; i++) {

                    Log.d(TAG, "frequency: " + frequency);

                    x = 4;
                    for (int j = 0; j < yes_no.length; j++) {
                        yes_no[j] = 2;
                    }
                    frequency = testingFrequencies[i];
                    boolean temp = true;
                    while (temp) {
                        //  khatam = 0;
                        khatam = checksub(yes_no);
                        if (khatam != 100) {
                            finalDbAnswer[indexofthis] = (x * 10);
                            Log.d(TAG, "Frequency:" + frequency + "\ntempDbArray: " + Arrays.toString(tempDbArray));
                            indexofthis++;
                            break;

                        }
                        heard = false;
                        if (!running) {
                            return;
                        }
                        if (x > 9) {
                            finalDbAnswer[indexofthis] = 90;
                            Log.d(TAG, "frequency: " + frequency);
                            Log.d(TAG, "frequency_dbvalue: " + (x * 10));
                            indexofthis++;
                            break;
                        }
                        AudioTrack audioTrack = null;
                        if (x >= 0 && x < 10) {
                            audioTrack = playSound(genTone(decibelsArray[x], frequency));
                        }
                        try {
                            Thread.sleep(randomTime());
                        } catch (InterruptedException ignored) {
                        }
                        audioTrack.release();
                        if (heard) {
                            yes_no[index] = 1;
                            tempDbArray[index] = x;
                            Log.d(TAG, "frequency: " + frequency);
                            Log.d(TAG, "frequency_dbvalue: " + (x * 10));
                            index++;
                            x = x - 1;
                            if (x < 0) {
                                finalDbAnswer[indexofthis] = 0;
                                indexofthis++;
                                break;
                            }

                        } else {
                            yes_no[index] = 0;
                            tempDbArray[index] = x;
                            Log.d(TAG, "frequency: " + frequency);
                            Log.d(TAG, "frequency_dbvalue: " + (x * 10));
                            index++;
                            x = x + 1;
                            if (x > 9) {
                                finalDbAnswer[indexofthis] = 90;
                                indexofthis++;
                                break;
                            }
                        }
                    }

                }
                Log.d(TAG, "FINAL DB: " + Arrays.toString(finalDbAnswer));

            }


            for (int v = 0; v < 12; v++) {
                if (v < 6) {
                    rightEar.add(finalDbAnswer[v]);
                } else {
                    leftEar.add(finalDbAnswer[v]);
                }
            }

            //loop = false;
            state=1;

        }

    }
}
