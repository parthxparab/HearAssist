package com.example.parthxparab.hearassist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Report extends AppCompatActivity {


    int [] reportData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Bundle myBundle = getIntent().getExtras();
         reportData= myBundle.getIntArray("Report");

        TextView mL1 = findViewById(R.id.l1);
        TextView mL2 = findViewById(R.id.l2);
        TextView mL3 = findViewById(R.id.l3);
        TextView mL4 = findViewById(R.id.l4);
        TextView mL5 = findViewById(R.id.l5);
        TextView mR1 = findViewById(R.id.r1);
        TextView mR2 = findViewById(R.id.r2);
        TextView mR3 = findViewById(R.id.r3);
        TextView mR4 = findViewById(R.id.r4);
        TextView mR5 = findViewById(R.id.r5);

        mR1.setText(reportData[0]);
        mR2.setText(reportData[1]);
        mR3.setText(reportData[2]);
        mR4.setText(reportData[3]);
        mR5.setText(reportData[4]);

        mL1.setText(reportData[5]);
        mL2.setText(reportData[6]);
        mL3.setText(reportData[7]);
        mL4.setText(reportData[8]);
        mL5.setText(reportData[9]);


    }
}