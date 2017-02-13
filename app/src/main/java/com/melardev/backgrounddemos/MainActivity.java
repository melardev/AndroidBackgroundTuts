package com.melardev.backgrounddemos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testAsyncTask(View view) {
        startDemo(ActivityAsyncTask.class);
    }

    public void testAlarmManager(View view) {
        startDemo(ActivityAlarm.class);
    }

    public void testJobScheduler(View view) {
        startDemo(ActivityJobScheduler.class);
    }

    public void testService(View view) {
        startDemo(ActivityBasicService.class);
    }

    private void startDemo(Class activity) {
        Intent i = new Intent(this, activity);
        startActivity(i);
    }
}
