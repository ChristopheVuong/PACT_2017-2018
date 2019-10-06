package com.example.fixe1to.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.pm.PackageManager;
import android.Manifest;
import android.app.Activity;

import com.example.fixe1to.myapplication.utils.UserErrorHandler;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_INTERNET = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.INTERNET
    };

    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = "";
        setContentView(R.layout.main_activity);
        final RelativeLayout layout = (RelativeLayout)RelativeLayout.inflate(this, R.layout.main_activity, null);
        setContentView(layout);
        verifyStoragePermissions(this);
    }

    public String getDataToSend() {
        return data;
    }

    public void setDataToSend(String data) {
        this.data = data;
    }

    public TextView getDisplay() {
        return (TextView)findViewById(R.id.textView);
    }

    public void validate(View v) {
        final EditText dataToSend = (EditText)findViewById(R.id.data);
        final String result = dataToSend.getText().toString();
        setDataToSend(result);
    }

    public static void verifyStoragePermissions(Activity activity) {
        ActivityCompat.requestPermissions(
               	activity,
               	PERMISSIONS_STORAGE,
               	REQUEST_INTERNET
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
            case REQUEST_INTERNET: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Client().execute(this);
                } else {
                    new UserErrorHandler(this, "You have to grant access to data on your phone for the client server communication");
                }
                return;
            }
        }
    }
}