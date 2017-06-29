package com.melardev.backgrounddemos;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.BackgroundExecutor;

import java.io.IOException;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@EActivity
//now a subclass of this activity will be generated, the name of that subclass would be called ActivityAsyncAnnotations_
public class ActivityAsyncAnnotations extends AppCompatActivity {


    /**
     * Resources
     * https://github.com/androidannotations/androidannotations/wiki
     * https://github.com/androidannotations/androidannotations/wiki/Cookbook
     * https://github.com/androidannotations/androidannotations/wiki/Enhance-activities
     *
     * @Click: https://github.com/androidannotations/androidannotations/wiki/ClickEvents
     * @ViewById : https://github.com/androidannotations/androidannotations/wiki/Injecting-Views
     */

    private static final String CANCELLABLE_TASK = "cancellable_task";
    private static final String NOT_PARALLEL_GROUP_1 = "not_parallel_group_1";

    @ViewById(R.id.txtAAResult)
    TextView txtResult;

    @ViewById(R.id.btnCancelAA)
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_annotations);

        //txtResult =(TextView)findViewById(R.id.txtAAResult);
    }

    /*@NonConfigurationInstance
    @Bean
    YourAsyncTask task;*/

    //@Background(delay = 2 * 2000)
    //@Background(serial = NOT_PARALLEL_GROUP_1)
    @Background
    void someBackgroundWork(String url) {
        logThreadInfo("someBackgroundWork");
        //Must have INTERNET PERMISSION IN MANIFEST
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                updateUi(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SecurityException exception) {
            Log.d(getClass().getName(), "No permissions to perform network operation");
            updateUi(getTheoricResponse());
        }
    }

    //@Background(serial = {NOT_PARALLEL_GROUP_1, NOT_PARALLEL_GROUP_2})
    //@Background(serial = NOT_PARALLEL_GROUP_1)
    @Background(id = CANCELLABLE_TASK)
    public void someOtherTask() {
        logThreadInfo("someOtherTask");

        String result;
        try {
            Thread.sleep(3 * 1000);
            //.... some heavy work
            result = "This is the message obtained from heavy work";
        } catch (InterruptedException e) {
            e.printStackTrace();
            result = "Task canceled before finishing...";
        }

        updateUi(result, true);
    }

    @UiThread
    public void updateUi(String result) {
        updateUi(result, false);
    }

    //@UiThread(propagation = UiThread.Propagation.REUSE) since 3.0
    //REUSE -> if already in main thread, call updateUi(String) directly, otherwise use Handler // FASTER
    //ENQUEUE -> DEFAULT, always call this method from Handler // SLOWER
    @UiThread
    public void updateUi(String result, boolean andDisableBtnCancel) {
        logThreadInfo("updateUi");
        txtResult.setText(result);
        if (andDisableBtnCancel)
            btnCancel.setEnabled(false);
    }


    //@Click(R.id.btnAnnotations)
    @Click({R.id.btnAA, R.id.btnAACancellable, R.id.btnCancelAA})
    void onBtnClicked(Button btn) {
        logThreadInfo("onBtnClicked");
        int id = btn.getId();
        if (id == R.id.btnAA)
            someBackgroundWork("https://reqres.in/api/users?page=2");
        else if (id == R.id.btnAACancellable) {
            someOtherTask();
            btnCancel.setEnabled(true);
        } else if (id == R.id.btnCancelAA) {
            BackgroundExecutor.cancelAll(CANCELLABLE_TASK,
                    true //cancel it anyways, even if it is already running
                    //false cancel it only if it is not running yet
            );
            btnCancel.setEnabled(false);
        }
    }

    void logThreadInfo(String methodName) {
        //Looper.getMainLooper().getThread() == Thread.currentThread()
        //Looper.getMainLooper().isCurrentThread() Requires API 23 (M)

        Log.d(getClass().getName() + "::" + methodName + "()", String.format(Locale.ENGLISH, "Thread Name : %s; is Main Thread ? %s", Thread.currentThread().getName(),
                Looper.getMainLooper() == Looper.myLooper() ? "yes" : "no"));
    }


    private String getTheoricResponse() {
        return "{\"page\":\"2\",\"per_page\":3,\"total\":12,\"total_pages\":4,\"data\":[{\"id\":4,\"first_name\":\"eve\",\"last_name\":\"holt\",\"avatar\":\"https://s3.amazonaws.com/uifaces/faces/twitter/marcoramires/128.jpg\"},{\"id\":5,\"first_name\":\"gob\",\"last_name\":\"bluth\",\"avatar\":\"https://s3.amazonaws.com/uifaces/faces/twitter/stephenmoon/128.jpg\"},{\"id\":6,\"first_name\":\"tracey\",\"last_name\":\"bluth\",\"avatar\":\"https://s3.amazonaws.com/uifaces/faces/twitter/bigmancho/128.jpg\"}]}";
    }
}
