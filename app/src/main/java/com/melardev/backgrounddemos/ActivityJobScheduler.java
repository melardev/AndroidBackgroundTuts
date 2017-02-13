package com.melardev.backgrounddemos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ActivityJobScheduler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_scheduler);
    }


    public void jobScheduler(View view) {
       /* ComponentName jobSrvc= new ComponentName(this, MyJobService.class);
        JobInfo.Builder jobIBuilder = new JobInfo.Builder(MY_JOB_ID, jobSrvc);
        jobIBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        jobIBuilder.setRequiresCharging(true);
        jobIBuilder.setRequiresDeviceIdle(true);
        jobIBuilder.setPersisted(true);
        long interval = TimeUnit.HOURS.toMillis(5L);
        jobIBuilder.setPeriodic(interval);

        long maxExecutionTime = TimeUnit.MINUTES.toMillis(5L);
        jobIBuilder.setOverrideDeadline(maxExecutionTime);*/

    }
}
