package com.example.parthxparab.hearassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class BrowseImage extends AppCompatActivity {

    ImageView img;
    TextView tv1,tv2,tv3,tv4;
    ProgressDialog pd;
    DbHelper dbHelper;
    int BG = Color.parseColor("#101010");
    private static final @ColorInt
    int TXT = Color.parseColor("#ffffff");
    String filename1="",datename1="",namex="",agex="",reportx = "";
    int  gid =0;
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton report, discard1, save2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_image);

        img = findViewById(R.id.graphimg);
        tv1 = findViewById(R.id.graphtitle);
        tv2 = findViewById(R.id.graphref);
        tv3 = findViewById(R.id.usertv);
        tv4 = findViewById(R.id.agetv);


        dbHelper = new DbHelper(this);


        DynamicToast.Config.getInstance()
                .setTextTypeface(Typeface.create(
                        Typeface.DEFAULT_BOLD, Typeface.NORMAL)).apply();

        report = findViewById(R.id.apply2);
        discard1 = findViewById(R.id.discard1);
        save2 = findViewById(R.id.save1);
        pd = new ProgressDialog(BrowseImage.this);


        Bundle myBundle = getIntent().getExtras();
        gid = myBundle.getInt("id");
        final String gname = myBundle.getString("name");
        String gpath = myBundle.getString("path");
        final String user = myBundle.getString("user");
        namex = user;
        final String age = myBundle.getString("age");
        agex = age;
        String report1 = myBundle.getString("report");
         reportx = report1;


        Bitmap bitmap = BitmapFactory.decodeFile(gpath);
        img.setImageBitmap(bitmap);
        tv1.setText(""+gname);
        tv2.setText("filename: "+gname+".jpg");
        tv3.setText("NAME : "+user.toUpperCase());
        tv4.setText("AGE : "+age.toUpperCase()+" YEARS");

        save2.setOnClickListener(new View.OnClickListener() {
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
                        File file = saveBitMap(BrowseImage.this, img);
                        if (file != null) {
                            pd.cancel();
                            Log.i("TAG", "Drawing saved to the gallery!");

                        } else {
                            pd.cancel();
                            Log.i("TAG", "Oops! Image could not be saved.");
                        }
                    }
                }, 1000);

                DynamicToast.make(BrowseImage.this, "IMAGE SAVED TO DEVICE ", TXT, BG,6000).show();

            }});

        discard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbHelper.deleteData(gname);
                DynamicToast.make(BrowseImage.this, "TEST DELETED", TXT, BG).show();

                Intent intent = new Intent(BrowseImage.this,Browse.class);
                startActivity(intent);

            }});

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in1 = new Intent(BrowseImage.this, Report1.class);
                in1.putExtra("userrep", namex);
                in1.putExtra("agerep", agex);
                in1.putExtra("gid", gid);
                in1.putExtra("rpath", reportx);
                in1.putExtra("gname",gname);
                startActivity(in1);
            }});

    }

    private File saveBitMap(Context context, ImageView drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "HearAssist");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("TAG", "Can't create directory to save the image");
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm", Locale.UK);
        Date now = new Date();
        filename1 = pictureFileDir.getPath() + File.separator + formatter.format(now) + ".jpg";
        Log.d("filename1",filename1);
        datename1=""+formatter.format(now);
        File pictureFile = new File(filename1);
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

    private Bitmap getBitmapFromView(ImageView view) {
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
    public void onBackPressed() {

//        super.onBackPressed();
        Intent waveIntent = new Intent(BrowseImage.this, Browse.class);
        waveIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(waveIntent);
        finish();
    }


}

