package com.melardev.backgrounddemos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
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

    public void testAlarmReschedule(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), AlarmTriggeredActvity.class), 0);
        ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, calendar.getTime().getTime(), pi);
    }

    public void testIntentService(View view) {
        startDemo(ActivityIntentService.class);
    }

    public void testHandler(View view) {
        startDemo(ActivityHandler.class);
    }

    public void testHandlerThread(View view) {
        startDemo(ActivityHandlerThread.class);
    }

    public void testAA(View view) {
        //startDemo(ActivityAsyncAnnotations.class); IllegalStateException!
        startDemo(ActivityAsyncAnnotations_.class);
        //ActivityAsyncAnnotations_.intent(this).start();
    }

    public void testThread(View view) {
        startDemo(ActivityThread.class);
    }

    public void testBoundService(View view) {
        startDemo(ActivityBoundService.class);
    }
}
