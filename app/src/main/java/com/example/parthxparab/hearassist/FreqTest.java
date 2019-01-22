package com.example.parthxparab.hearassist;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class FreqTest extends AppCompatActivity {


  /*  public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    View rootLayout;

    private int revealX;
    private int revealY;*/

    private static final String TAG = "FreqTest";
    private static final @ColorInt
    int BG = Color.parseColor("#101010");
    private static final @ColorInt
    int TXT = Color.parseColor("#ffffff");
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
    int k = 0;
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
    TextView tv;
    private int state = 0;
    private boolean heard = false;
    private boolean loop = true;
    private ImageView ImageView1, ImageView2, Right, Left;
    private Context context = FreqTest.this;
    private FloatingActionButton btn, btn1;
    Integer flag = 0;

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
    AsyncTaskRunner runner = new AsyncTaskRunner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freq_test);




        btn = (FloatingActionButton) findViewById(R.id.y);
        btn1 = (FloatingActionButton) findViewById(R.id.g);
        handler = new Handler();
        ImageView1 = (ImageView) findViewById(R.id.gif_wave);
        ImageView2 = (ImageView) findViewById(R.id.no_wave);
        Right = (ImageView) findViewById(R.id.right1);
        Left = (ImageView) findViewById(R.id.left1);
        tv = (TextView) findViewById(R.id.heartext);

        DynamicToast.Config.getInstance()
                .setTextTypeface(Typeface.create(
                        Typeface.DEFAULT_BOLD, Typeface.NORMAL)).apply();
            loadImageOriginal();


        informationTextView = findViewById(R.id.idInformation);

        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        assert am != null;
        am.setStreamVolume(AudioManager.STREAM_MUSIC, 9, 0);

        runner.execute();




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (state == 1) {
                    btn.setVisibility(View.GONE);
                    btn1.setVisibility(View.VISIBLE);
                    ImageView1.setVisibility(View.GONE);
                    ImageView2.setVisibility(View.VISIBLE);

                }
                Log.d(TAG, "State: " + state);
                ImageView1.setImageResource(R.drawable.wave1);
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadImageOriginal();
                    }
                };
                handler.postDelayed(runnable, 2100);
                DynamicToast.make(FreqTest.this, "FREQUENCY HEARD", AppCompatResources.getDrawable(
                        FreqTest.this, R.drawable.toast_hear), TXT, BG).show();
            }
        });


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "State: " + state);
                Intent in1 = new Intent(FreqTest.this, Graph.class);
                in1.putExtra("Array", finalDbAnswer);
                startActivity(in1);
            }
        });

    }


/*
    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }
*/


    private void EarImage(final int m) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Right.setImageResource(R.drawable.ear1_h);
                Left.setImageResource(R.drawable.ear1);

                tv.setText("");

                if (m == 0) {
                    Right.setImageResource(R.drawable.ear_h);
                    tv.setText("RIGHT EAR PLAYING");
                } else {
                    Right.setImageResource(R.drawable.ear1_h);
                    Left.setImageResource(R.drawable.ear);
                    tv.setText("LEFT EAR PLAYING");

                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        heard = true;
        return super.dispatchTouchEvent(e);
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

    class AsyncTaskRunner extends AsyncTask<Void, Void, Integer> {



        @Override
        protected Integer doInBackground(Void... voids) {

            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isCancelled())
            {
                System.exit(0);}

                for (int k = 0; k < 2; k++) {


                    EarImage(k);
                    ear = k;
                    //Log.d("MainActivity: ", "ear: " + ear);
                    for (int i = 0; i < testingFrequencies.length; i++) {//testingFrequencies.length

                        Log.d("MainActivity: ", "frequency: " + frequency);

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
                                Log.d("CHECK::", "Frequency:" + frequency + "\ntempDbArray: " + Arrays.toString(tempDbArray));
                                indexofthis++;
                                break;

                            }
                            heard = false;

                            if (x > 9) {
                                finalDbAnswer[indexofthis] = 90;
                                Log.d("MainActivity: ", "frequency: " + frequency);
                                Log.d("MainActivity: ", "frequency_dbvalue: " + (x * 10));
                                indexofthis++;
                                break;
                            }
                            AudioTrack audioTrack = null;
                            if (x >= 0 && x < 10) {
                                audioTrack = playSound(genTone(decibelsArray[x], frequency));
                            }
                   /*         try {
                                Thread.sleep(randomTime());
                            } catch (InterruptedException ignored) {
                            }*/

                            try {
                                Thread.sleep(2500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            audioTrack.release();
                            if (heard) {
                                yes_no[index] = 1;
                                tempDbArray[index] = x;
                                Log.d("MainActivity: ", "frequency: " + frequency);
                                Log.d("MainActivity: ", "frequency_dbvalue: " + (x * 10));
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
                                Log.d("MainActivity: ", "frequency: " + frequency);
                                Log.d("MainActivity: ", "frequency_dbvalue: " + (x * 10));
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
                    Log.d("MainActivity: ", "FINAL DB: " + Arrays.toString(finalDbAnswer));
                }

            flag = 1;
            for (int v = 0; v < 12; v++) {
                if (v < 6) {
                    rightEar.add(finalDbAnswer[v]);
                } else {
                    leftEar.add(finalDbAnswer[v]);
                }
            }

            return flag;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(flag ==1){

                btn.setVisibility(View.GONE);
                btn1.setVisibility(View.VISIBLE);
                ImageView1.setVisibility(View.GONE);
                ImageView2.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {

        runner.cancel(true);

//        super.onBackPressed();
        Intent waveIntent = new Intent(FreqTest.this, Select.class);
        waveIntent.setFlags(waveIntent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(waveIntent);
        Glide.with(context).clear(ImageView1);

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        runner.cancel(true);

    }
}