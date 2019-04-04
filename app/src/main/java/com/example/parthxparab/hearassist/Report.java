package com.example.parthxparab.hearassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Report extends AppCompatActivity {


    private static final String TAG = "Report.java";
    private static final @ColorInt
    int BG = Color.parseColor("#101010");
    private static final @ColorInt
    int TXT = Color.parseColor("#ffffff");
    int[] reportData;
    String ager, namer, reportpath, filename, datename, filename1;
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton repsave1;
    LinearLayout replayout;
    TextView name, age;
    DbHelper dbHelper;
    ProgressDialog pd;
    TextView mR0, mR1, mR2, mR3, mR4, mL0, mL1, mL2, mL3, mL4;


    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "HearAssist");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("TAG", "Can't create directory to save the image");
            return null;
        }
        filename = "" + reportpath;
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

    public void AddData(String name, String path, String user, String age, String report) {
        boolean insertData = dbHelper.addData(name, path, user, age, report);

        if (insertData) {
            Log.d("SQLDATA", "Data added to db");
        } else {
            Log.d("SQLDATA", "Data not added to db");
        }
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
        datename = myBundle.getString("datename");
        filename1 = myBundle.getString("filename");
        pd = new ProgressDialog(Report.this);

        dbHelper = new DbHelper(this);


        Log.d("Report.java", "" + Arrays.toString(reportData));

        repsave1 = findViewById(R.id.repsave);
        replayout = findViewById(R.id.replayout);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        mR0 = findViewById(R.id.r0);
        mR1 = findViewById(R.id.r1);
        mR2 = findViewById(R.id.r2);
        mR3 = findViewById(R.id.r3);
        mR4 = findViewById(R.id.r4);
        mL0 = findViewById(R.id.l0);
        mL1 = findViewById(R.id.l1);
        mL2 = findViewById(R.id.l2);
        mL3 = findViewById(R.id.l3);
        mL4 = findViewById(R.id.l4);

        name.setText("NAME: " + namer.toUpperCase());
        age.setText("AGE: " + ager.toUpperCase() + " YEARS");
        mR0.setText("" + reportData[0]);
        mR1.setText("" + reportData[1]);
        mR2.setText("" + reportData[2]);
        mR3.setText("" + reportData[3]);
        mR4.setText("" + reportData[4]);
        mL0.setText("" + reportData[6]);
        mL1.setText("" + reportData[7]);
        mL2.setText("" + reportData[8]);
        mL3.setText("" + reportData[9]);
        mL4.setText("" + reportData[10]);


        repsave1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * Checking Internet Connection
                 */
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    //new GetDataTask().execute();
                    new SendRequest().execute();
                } else {
                    Snackbar.make(view, "Internet Connection Not Available", Snackbar.LENGTH_LONG).show();
                }

                pd.setMessage("saving your image");
                pd.show();

                new Handler().postDelayed(new Runnable() {

                    /*
                     * Showing splash screen with a timer. This will be useful when you
                     * want to show case your app logo / company
                     */

                    @Override
                    public void run() {
                        LinearLayout savingLayout = findViewById(R.id.replayout);
                        File file = saveBitMap(Report.this, savingLayout);
                        if (file != null) {
                            AddData(datename, filename1, namer, ager, filename);
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


    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        Log.d(TAG, result.toString());
        return result.toString();
    }

    public class SendRequest extends AsyncTask<String, Void, String> {


        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                //Trending POSTMAN URL
                //https://script.google.com/macros/s/AKfycbx8wJXOBGJgyLy8FZtDIeF3AiRYa2EGFVgzRQzsXAjWNRQYy75m/exec?id=1yVXIeAuSEg6f61uPVyOPIQsR-SharMIWajF_rI787hE&sheet=Sheet1&age=3&R250Hz=30&R500Hz=30&R1000Hz=30&R2000Hz=20&R4000Hz=20&L250Hz=30&L500Hz=30&L1000Hz=20&L2000Hz=30&L4000Hz=20

                URL url = new URL("https://script.google.com/macros/s/AKfycbx8wJXOBGJgyLy8FZtDIeF3AiRYa2EGFVgzRQzsXAjWNRQYy75m/exec");

                JSONObject postDataParams = new JSONObject();

                String id = "1yVXIeAuSEg6f61uPVyOPIQsR-SharMIWajF_rI787hE";
                String sheet = "Sheet1";
                int ageCategory = 0;

                if (Integer.valueOf(ager) <= 20)
                    ageCategory = 1;
                else if (Integer.valueOf(ager) > 20 && Integer.valueOf(ager) <= 40)
                    ageCategory = 2;
                else if (Integer.valueOf(ager) > 40 && Integer.valueOf(ager) <= 60)
                    ageCategory = 3;
                else
                    ageCategory = 4;


                postDataParams.put("age", ageCategory);

                postDataParams.put("R250Hz", reportData[0]);
                postDataParams.put("R500Hz", reportData[1]);
                postDataParams.put("R1000Hz", reportData[2]);
                postDataParams.put("R2000Hz", reportData[3]);
                postDataParams.put("R4000Hz", reportData[4]);

                postDataParams.put("L250Hz", reportData[6]);
                postDataParams.put("L500Hz", reportData[7]);
                postDataParams.put("L1000Hz", reportData[8]);
                postDataParams.put("L2000Hz", reportData[9]);
                postDataParams.put("L4000Hz", reportData[10]);

                postDataParams.put("id", id);
                postDataParams.put("sheet", sheet);


                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer();
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();

        }
    }


}