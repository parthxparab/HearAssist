package com.example.parthxparab.hearassist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



public class Browse extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    DbHelper dbHelper;

    private ListView mListView;
    ArrayList<Items> list;
    CustomAdapter adapter = null;
    TextView tv;
    int id =0;
    String name,path,user,age,report;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        mListView = findViewById(R.id.listView);
        tv = findViewById(R.id.placeholder);
        list = new ArrayList<>();
        adapter = new CustomAdapter(this, R.layout.list_itempxp, list);
        dbHelper = new DbHelper(this);

        FloatingActionButton fabadd = findViewById(R.id.fabadd);
        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent waveIntent = new Intent(Browse.this, FreqTest.class);
//                startActivity(waveIntent);
                presentActivity(view);



            }
        });

        //      populateListView() starts here

        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = dbHelper.getData();

        while(data.moveToNext())
        {

            //get the value from the database in column 1
            //then add it to the ArrayList
            id = data.getInt(0);
            name = data.getString(1);
            path = data.getString(2);
            user = data.getString(3);
            age = data.getString(4);
            report = data.getString(5);

            list.add(new Items(name, path, id,user,age,report));
            if(id != 0)
            {
                tv.setText("");
            }

        }
        adapter.notifyDataSetChanged();
        //create the list adapter and set the adapter
        mListView.setAdapter(adapter);


        //        populateListView(); code ends here


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Items it = (Items) adapterView.getItemAtPosition(i);

                Log.d(TAG, "name: " + it.getName());
                Log.d(TAG, "id: " + it.geId());
                Log.d(TAG, "path: " + it.getImage());

                String name1 = it.getName();
                int id1 = it.geId();
                String path1 = it.getImage();
                String user1 = it.getUser();
                String age1 = it.getAge();
                String report1=it.getReport();



                Intent bIntent = new Intent(Browse.this, BrowseImage.class);
                bIntent.putExtra("name", name1);
                bIntent.putExtra("id", id1);
                bIntent.putExtra("path", path1);
                bIntent.putExtra("user", user1);
                bIntent.putExtra("age", age1);
                bIntent.putExtra("report", report1);
                startActivity(bIntent);}


        });
    }


    public void presentActivity(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(this, Splash.class);
        intent.putExtra(Splash.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(Splash.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    @Override
    public void onBackPressed() {
        Intent waveIntent = new Intent(Browse.this, Select.class);
        waveIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(waveIntent);
        finish();
    }
}

