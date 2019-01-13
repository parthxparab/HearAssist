package com.example.parthxparab.hearassist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;


public class Second extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView tv = (TextView) findViewById(R.id.tv);

        Bundle myBundle = getIntent().getExtras();
        int[] array1 = myBundle.getIntArray("Array");

        String strArray[] = new String[array1.length];

        for (int i = 0; i < array1.length; i++) {
            strArray[i] = String.valueOf(array1[i]);
        }

        tv.setText(Arrays.toString(strArray));

    }
}
