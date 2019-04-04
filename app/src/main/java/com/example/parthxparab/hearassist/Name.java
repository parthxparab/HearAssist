package com.example.parthxparab.hearassist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Name extends AppCompatActivity {

    EditText et1, et2;
    String user,age;
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton starttest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        starttest = findViewById(R.id.st);

        et1 = findViewById(R.id.input_name);

        et2 = findViewById(R.id.input_name1);


        starttest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 user = et1.getText().toString();
                 age = et2.getText().toString();
                Log.d("Name: ", "et1: " + user);
                Log.d("Name: ", "et2: " + age);

                Intent in1 = new Intent(Name.this, FreqTest.class);
                in1.putExtra("et1", user);
                in1.putExtra("et2",age);
                startActivity(in1);
                //finish();

            }
        });





    }
}
