package com.example.parthxparab.hearassist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Report1 extends AppCompatActivity {


    int [] reportData;
    TextView mL1,mL2,mL3,mL4,mL5,mR1,mR2,mR3,mR4,mR5,namepxp,agepxp;
    String namef = "",agef = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report1);


        Bundle myBundle = getIntent().getExtras();
        namef=myBundle.getString("userrep");
        agef=myBundle.getString("agerep");

        namepxp = findViewById(R.id.namex1);
        agepxp = findViewById(R.id.agex1);

        namepxp.setText("NAME: "+namef.toUpperCase());
        agepxp.setText("AGE: "+agef.toUpperCase()+" YEARS");


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