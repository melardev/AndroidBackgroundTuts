package com.melardev.backgrounddemos;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import static com.melardev.backgrounddemos.ActivityJobScheduler.WEB_SERVICE_URL;

/**
 * Created by melardev on 3/2/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {
    private JobParameters params;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            //Your work , i.e upload some date to your server

            PersistableBundle bundle = params.getExtras();
            String url = bundle.getString(WEB_SERVICE_URL);
            Log.d("TAG", "JobService Demo");
            jobFinished(params, false); // we have to call this method if we returned true
        }

    };

    @Override
    public boolean onStartJob(final JobParameters params) {
        this.params = params;
        new Thread(runnable).start();

        //All work is completed -> return false;
        return true; // we are not done yet, we have a background thread running
    }


    @Override
    public boolean onStopJob(JobParameters params) {

        //Called if the job is canceled before it finishes i.e when wifi has gone
        return true; //true if we want to reschedule the job
    }

}
