package com.melardev.backgrounddemos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityBasicService extends AppCompatActivity {

    public static final String TAG = "BASICSERVICE";
    public static int count = 0;
    private EditText etxtSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_service);
        etxtSubmit = (EditText) findViewById(R.id.etxtSubmit);
    }

    public void addFirst(View view) {
        startService(new Intent(this, StartedServiceDemo.class)
                .setAction(StartedServiceDemo.ACTION_ADD_FIRST)
                .putExtra(StartedServiceDemo.EXTRA_STRING, etxtSubmit.getText().toString()));
    }

    public void addLast(View view) {
        startService(new Intent(this, StartedServiceDemo.class)
                .setAction(StartedServiceDemo.ACTION_ADD_LAST)
                .putExtra(StartedServiceDemo.EXTRA_STRING, etxtSubmit.getText().toString()));
    }

    public void stopClicked(View view) {
        startService(new Intent(ActivityBasicService.this, StartedServiceDemo.class)
                .setAction(StartedServiceDemo.ACTION_STOP));
    }

    @Override
    protected void onDestroy() {
        if (stopService(new Intent(ActivityBasicService.this, StartedServiceDemo.class)))
            Log.d(TAG, "stopped Succesfully");
        else
            Log.d(TAG, "stopped");
        super.onDestroy();
    }

    public void startForeground(View view) {
        startService(new Intent(this, BasicService.class)
                .putExtra("foreground", true));
    }

    public void stopForeground(View view) {
        startService(new Intent(this, BasicService.class).putExtra("foreground", true));
    }

    public void startNonForeground(View view) {
        startService(new Intent(this, BasicService.class)
                .putExtra("extra", "someText"));
    }

    public void stopNonForegroundService(View view) {
        stopService(new Intent(this, BasicService.class));
    }
}


