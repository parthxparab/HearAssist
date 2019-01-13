package com.example.parthxparab.hearassist;


import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton;



public class Select extends AppCompatActivity {

    FloatingActionButton begin,browse,stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        begin= findViewById(R.id.fab1);
        browse=findViewById(R.id.fab2);
        stop=findViewById(R.id.fab3);

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent waveIntent = new Intent(Select.this, FreqTest.class);
                startActivity(waveIntent);
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Select.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Services Stopped Successfully", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();            }
        });





    }

}
