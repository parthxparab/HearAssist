package com.example.parthxparab.hearassist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class BrowseImage extends AppCompatActivity {

    ImageView img;
    TextView tv1,tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_image);

        img = (ImageView) findViewById(R.id.graphimg);
        tv1 = (TextView) findViewById(R.id.graphtitle);
        tv2 = (TextView) findViewById(R.id.graphref);

        Bundle myBundle = getIntent().getExtras();
        int  gid = myBundle.getInt("id");
        String gname = myBundle.getString("name");
        String gpath = myBundle.getString("path");

        Bitmap bitmap = BitmapFactory.decodeFile(gpath);
        img.setImageBitmap(bitmap);
        tv1.setText("TEST "+gid);
        tv2.setText("Filename: "+gname+".jpg");

    }
}
