package com.melardev.backgrounddemos;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ActivityJobScheduler extends AppCompatActivity {

    private static final int MY_JOB_ID = 1;
    private static final String WEB_SERVICE_URL = "web_service_url";
    private JobScheduler mJobScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acitivity_job_scheduler);

        mJobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void scheduleJob(View v) {
        //https://kb.sos-berlin.com/pages/viewpage.action?pageId=3638048
        //Scheduler uses UTC times for all its internal operations. This ensures a continual flow of operation without breaks or repetitions regardless of any changes in local time.
        JobInfo.Builder builder = new JobInfo.Builder(MY_JOB_ID, new ComponentName(getPackageName(), JobSchedulerService.class.getName()));
        builder.setPersisted(true); //persist across device reboots
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // only if wifi avaiblable so they are not using bandwith
        builder.setPeriodic(60000); //run once each 60 seconds
        builder.setRequiresDeviceIdle(true); // run only if the device is idle
        builder.setRequiresCharging(false); // run only if the device is charging

        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(WEB_SERVICE_URL, "http://example.com?upload.php");
        builder.setExtras(bundle);

        //builder.setBackoffCriteria(1000, JobInfo.BACKOFF_POLICY_LINEAR);
        //BACKOFF_POLICY_LINEAR After one failure retry at 1 * your value, then at 2 * (your value), then 3 * (your value) and so on
        //BACKOFF_POLICY_EXPONENTIAL After one failure retry at your value, then at (your value)^2, (your value)^3 and so on
        //builder.setMinimumLatency(5 * 1000); //latency
        //builder.setOverrideDeadline(50 * 1000); //wait for criteria to be met, in 50 seconds if they have not then run anyways


        int test = mJobScheduler.schedule(builder.build());
        if (test <= 0) {
            //If something goes wrong
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cancelJob(View v) {
        //mJobScheduler.cancelAll();
        mJobScheduler.cancel(MY_JOB_ID);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public class JobSchedulerService extends JobService {
        private JobParameters params;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Your work , i.e upload some date to your server
                PersistableBundle bundle = params.getExtras();
                String url = bundle.getString(WEB_SERVICE_URL);
                Toast.makeText(getApplicationContext(), "JobService task running", Toast.LENGTH_SHORT).show();
                jobFinished(params, false);
            }

        };

        @Override
        public boolean onStartJob(final JobParameters params) {
            this.params = params;
            new Handler().post(runnable);

            return true;
        }

        @Override
        public boolean onStopJob(JobParameters params) {
            return false;
        }

    }
}
