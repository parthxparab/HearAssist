package com.example.parthxparab.hearassist;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    PermissionManager permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permission = new PermissionManager() {

            @Override
            public List<String> setPermission() {
                // If You Don't want to check permission automatically and check your own custom permission
                // Use super.setPermission(); or Don't override this method if not in use
                List<String> customPermission = new ArrayList<>();
                customPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                customPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);

                return customPermission;
            }
        };

        //To initiate checking permission
        permission.checkAndRequestPermissions(this);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent waveIntent = new Intent(MainActivity.this, Select.class);
                startActivity(waveIntent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        permission.checkResult(requestCode, permissions, grantResults);
        //To get Granted Permission and Denied Permission
        ArrayList<String> granted = permission.getStatus().get(0).granted;
        ArrayList<String> denied = permission.getStatus().get(0).denied;
    }
}
