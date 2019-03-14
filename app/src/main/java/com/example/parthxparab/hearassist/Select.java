package com.example.parthxparab.hearassist;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton;



public class Select extends AppCompatActivity {

    FloatingActionButton begin,browse,stop;
    TextView infoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        begin= findViewById(R.id.fab1);
        infoView=findViewById(R.id.textinfo);
        browse=findViewById(R.id.fab2);
        stop=findViewById(R.id.fab3);

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent waveIntent = new Intent(Select.this, Name.class);
                startActivity(waveIntent);
                finish();
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent waveIntent = new Intent(Select.this, Browse.class);
                startActivity(waveIntent);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Button Functionality pending", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();            }
        });

        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent waveIntent = new Intent(Select.this, Info.class);
                startActivity(waveIntent);
            }
        });



    }

}
