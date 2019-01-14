package com.example.parthxparab.hearassist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class Info extends AppCompatActivity {

    TextView tv,tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        tv=findViewById(R.id.tv);
        tv1=findViewById(R.id.tv1);
        tv.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        tv1.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);


    }
}
