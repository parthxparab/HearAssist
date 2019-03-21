package com.example.parthxparab.hearassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static java.lang.Math.abs;


public class Graph extends AppCompatActivity {
    private static final @ColorInt
    int BG = Color.parseColor("#101010");
    private static final @ColorInt
    int TXT = Color.parseColor("#ffffff");
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton apply1, discard, save1;
    GraphView graph;
    RelativeLayout layout;
    ProgressDialog pd;
    int pxp;
    TextView utv,atv;
    int[] freqData, freqData1;
    String user1, age1;
    String filename = "", datename = "", report = "",reporttemp=""/*,filename1=""*/;
    DbHelper dbHelper;
    private ArrayList<Integer> a = new ArrayList<Integer>();
    private ArrayList<Integer> b = new ArrayList<Integer>();
    private ArrayList<Integer> c = new ArrayList<Integer>();

    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "HearAssist");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("TAG", "Can't create directory to save the image");
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm", Locale.UK);
        Date now = new Date();
        datename = "" + formatter.format(now);
        reporttemp = datename + "x";
        filename = pictureFileDir.getPath() + File.separator + datename + ".jpg";
        report = pictureFileDir.getPath() + File.separator + reporttemp + ".jpg";
//        filename1 = filename;

//        AddData(datename, filename, user1, age1, report);
        Log.d("Graph: ", "USER: " + user1 + " AGE: " + age1);
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

    public void AddData(String name, String path, String user, String age, String report) {
        boolean insertData = dbHelper.addData(name, path, user, age, report);

        if (insertData) {
            Log.d("SQLDATA", "Data added to db");
        } else {
            Log.d("SQLDATA", "Data not added to db");
        }
    }

    private DataPoint[] generateData(ArrayList p, ArrayList q) {
        int count = p.size();
        DataPoint[] values = new DataPoint[count];
        for (int i = 0; i < count; i++) {
            int x = (int) p.get(i);
            int y = (int) q.get(i);
            y = abs(100 - y); //ADDED NEW. 03/02/2019
            DataPoint v = new DataPoint(x, y);
            values[i] = v;

        }
        return values;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        dbHelper = new DbHelper(this);

//        apply1 = findViewById(R.id.apply1);
        discard = findViewById(R.id.discard);
        save1 = findViewById(R.id.apply1);
        utv = (TextView) findViewById(R.id.usertv1);
        atv = (TextView) findViewById(R.id.agetv1);
        layout = findViewById(R.id.graph_layout);
        pd = new ProgressDialog(Graph.this);

        DynamicToast.Config.getInstance()
                .setTextTypeface(Typeface.create(
                        Typeface.DEFAULT_BOLD, Typeface.NORMAL)).apply();

        Bundle myBundle = getIntent().getExtras();
        freqData = myBundle.getIntArray("Array");

        freqData1 = freqData;
        user1 = myBundle.getString("user");
        age1 = myBundle.getString("age");
        Log.d("Graph: ", "USER: " + user1 + " AGE: " + age1);

        utv.setText("NAME: "+user1.toUpperCase());
        atv.setText("AGE: "+age1.toUpperCase()+" YEARS");


        for (pxp = 0; pxp < freqData.length; pxp++) {
            if (pxp < 6) {
                b.add(freqData[pxp]);
            } else {
                c.add(freqData[pxp]);
            }
        }

        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd.setMessage("Generating Report");
                pd.show();

                new Handler().postDelayed(new Runnable() {

                    /*
                     * Showing splash screen with a timer. This will be useful when you
                     * want to show case your app logo / company
                     */

                    @Override
                    public void run() {
                        RelativeLayout savingLayout = findViewById(R.id.graph_layout);
                        File file = saveBitMap(Graph.this, savingLayout);
                        if (file != null) {
                            pd.cancel();
                            Intent waveIntent = new Intent(Graph.this, Report.class);
                            waveIntent.putExtra("reparray", freqData1);
                            waveIntent.putExtra("userrep", user1);
                            waveIntent.putExtra("agerep", age1);
                            waveIntent.putExtra("reportlocation", report);
                            waveIntent.putExtra("datename", datename);
                            waveIntent.putExtra("filename", filename);

                            startActivity(waveIntent);
                            Log.i("TAG", "Image saved to the gallery!");

                        } else {
                            pd.cancel();
                            Log.i("TAG", "Oops! Image could not be saved.");
                        }
                    }
                }, 1000);

//                DynamicToast.make(Graph.this, "Image Saved on Device!", TXT, BG, 6000).show();


            }
        });


        /*apply1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent waveIntent = new Intent(Graph.this, Report.class);
                waveIntent.putExtra("reparray", freqData1);
                waveIntent.putExtra("userrep", user1);
                waveIntent.putExtra("agerep", age1);
                waveIntent.putExtra("reportlocation", report);
                waveIntent.putExtra("datename", datename);
                waveIntent.putExtra("filename", filename);



                startActivity(waveIntent);
            }
        });*/

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Audiogram Discarded", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        Integer x0[] = {2, 4, 6, 8, 10, 12};
        Integer y0[] = {10, 4, 6, 1, 20, 30, 60, 70};

        Integer y1[] = {70, 20, 5, 10, 50, 9, 90, 10};

        graph = findViewById(R.id.graph);


        for (int i = 0; i < 6; i++) {
            a.add(x0[i]);
            //     b.add(y0[i]);
            //    c.add(y1[i]);
        }

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        String[] V = {"100", "90", "80", "70", "60", "50", "40", "30", "20", "10", "0"};
        //Reverse 0=100, 10=90, 20=80, 30=70 and so on
        String[] H = {"125", "250", "500", "1k", "2k", "4k", "8k  "};
        // 0=125, 2=250, 4=500, 6=1000, 8=2000, 10=4000, 12=8000
        staticLabelsFormatter.setVerticalLabels(V);
        staticLabelsFormatter.setHorizontalLabels(H);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("f[Hz]");
        gridLabel.setVerticalAxisTitle("dB");

        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(12);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        int imageResource = getResources().getIdentifier("@drawable/graph_bg", null, getPackageName());
        Drawable graph_background = getResources().getDrawable(imageResource);
        graph.setBackground(graph_background);

        for (int i = 0; i < 6; i++) {

            LineGraphSeries series0 = new LineGraphSeries<DataPoint>(generateData(a, b));
            graph.addSeries(series0);
            series0.setColor(Color.RED);
            series0.setThickness(5);
            PointsGraphSeries series1 = new PointsGraphSeries<>(generateData(a, b));

            graph.addSeries(series1);
            series1.setShape(PointsGraphSeries.Shape.POINT);
            series1.setColor(Color.RED);
            series1.setSize(9);

            LineGraphSeries series2 = new LineGraphSeries<DataPoint>(generateData(a, c));
            graph.addSeries(series2);
            series2.setThickness(5);

            PointsGraphSeries series3 = new PointsGraphSeries<>(generateData(a, c));
            graph.addSeries(series3);
            series3.setCustomShape(new PointsGraphSeries.CustomShape() {
                @Override
                public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                    paint.setStrokeWidth(7);
                    canvas.drawLine(x - 8, y - 8, x + 8, y + 8, paint);
                    canvas.drawLine(x + 8, y - 8, x - 8, y + 8, paint);

                }
            });


        }

    }


    @Override
    public void onBackPressed() {

//        super.onBackPressed();
        Intent waveIntent = new Intent(Graph.this, Select.class);
        waveIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(waveIntent);
        finish();
    }


}