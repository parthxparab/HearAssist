package com.example.parthxparab.hearassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Report extends AppCompatActivity {


    int [] reportData;
    String ager,namer,reportpath,filename,datename;
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton repsave1;
    LinearLayout replayout;
    TextView name,age;
    ProgressDialog pd;
    private static final @ColorInt
    int BG = Color.parseColor("#101010");
    private static final @ColorInt
    int TXT = Color.parseColor("#ffffff");



    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "HearAssist");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("TAG", "Can't create directory to save the image");
            return null;
        }
        Date now = new Date();
        filename = ""+reportpath;
//        filename1 = filename;
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(context, pictureFile.getAbsolutePath());
        return pictureFile;


    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue scanning gallery.");
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Bundle myBundle = getIntent().getExtras();
        reportData = myBundle.getIntArray("reparray");
        namer = myBundle.getString("userrep");
        reportpath = myBundle.getString("reportlocation");
        ager = myBundle.getString("agerep");
        pd = new ProgressDialog(Report.this);




        repsave1 = findViewById(R.id.repsave);
        replayout = findViewById(R.id.replayout);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);

        name.setText("NAME: "+namer.toUpperCase());
        age.setText("AGE: "+ager.toUpperCase()+" YEARS");

        repsave1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd.setMessage("saving your image");
                pd.show();

                new Handler().postDelayed(new Runnable() {

                    /*
                     * Showing splash screen with a timer. This will be useful when you
                     * want to show case your app logo / company
                     */

                    @Override
                    public void run() {
                        LinearLayout savingLayout = (LinearLayout) findViewById(R.id.replayout);
                        File file = saveBitMap(Report.this, savingLayout);
                        if (file != null) {
                            pd.cancel();
                            Log.i("TAG", "Image saved to the gallery!");

                        } else {
                            pd.cancel();
                            Log.i("TAG", "Oops! Image could not be saved.");
                        }
                    }
                }, 1000);

                DynamicToast.make(Report.this, "Image Saved on Device!", TXT, BG, 6000).show();


            }
        });







  /*       mL1 = findViewById(R.id.l1);
         mL2 = findViewById(R.id.l2);
         mL3 = findViewById(R.id.l3);
         mL4 = findViewById(R.id.l4);
         mL5 = findViewById(R.id.l5);
         mR1 = findViewById(R.id.r1);
         mR2 = findViewById(R.id.r2);
         mR3 = findViewById(R.id.r3);
         mR4 = findViewById(R.id.r4);
         mR5 = findViewById(R.id.r5);

        mR1.setText(""+reportData[0]);
        mR2.setText(""+reportData[1]);
        mR3.setText(""+reportData[2]);
        mR4.setText(""+reportData[3]);
        mR5.setText(""+reportData[4]);

        mL1.setText(""+reportData[5]);
        mL2.setText(""+reportData[6]);
        mL3.setText(""+reportData[7]);
        mL4.setText(""+reportData[8]);
        mL5.setText(""+reportData[9]);*/

    }
}