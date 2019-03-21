package com.example.parthxparab.hearassist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class Report1 extends AppCompatActivity {


int gid1 = 0;
    ImageView img1;
    DbHelper dbHelper;
    String rpath = "",rname="";
    TextView reptv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report1);

        Bundle myBundle = getIntent().getExtras();
        gid1 = myBundle.getInt("gid");
        rpath = myBundle.getString("rpath");
        rname = myBundle.getString("gname");

        img1 = findViewById(R.id.reportimg);
        reptv = findViewById(R.id.reporttitle);


        dbHelper = new DbHelper(this);

        Bitmap bitmap = BitmapFactory.decodeFile(rpath);
        img1.setImageBitmap(bitmap);
        reptv.setText("TEST ID: "+rname);

    }
}