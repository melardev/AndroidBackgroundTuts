package com.melardev.backgrounddemos;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

public class ActivityThread extends AppCompatActivity {

    private Thread mThread;
    private static ProgressDialog dlg;
    private boolean testingBadLifecycleHandling = true;
    private boolean activityDestroyed;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (dlg != null && dlg.isShowing()) {
                    dlg.dismiss();
                    dlg = null;
                }
            }
        };

        mThread = (Thread) getLastCustomNonConfigurationInstance();

        if (mThread != null && mThread.isAlive()) {
            if (!testingBadLifecycleHandling)
                showAndSetDlg();
        } else
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    downloadHeavyData();
                }
            });
    }

    private void showAndSetDlg() {
        dlg = ProgressDialog.show(this, "Downloading ...", "Please wait", true, false);
    }

    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void downloadHeavyData() {
        try {
            int logsAfterActivityDestroyed = 0;
            for (int i = 0; i < 1000; i++) {
                Thread.sleep(500);
                if (activityDestroyed /*isDestroyed()*/) {
                    if (logsAfterActivityDestroyed < 20) {
                        Log.d(Thread.currentThread().getName(), "Count After Activity Destroyed = " + logsAfterActivityDestroyed);
                        logsAfterActivityDestroyed++;
                    } else
                        break;
                } else {
                    Log.d(Thread.currentThread().getName(), "Count = " + i);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dismissDownloadingDlg();
    }

    void dismissDownloadingDlg() {
        handler.sendEmptyMessage(0);
    }

    @Override
    protected void onDestroy() {
        activityDestroyed = true;
        if (!testingBadLifecycleHandling) {
            if (dlg != null && dlg.isShowing()) {
                dlg.dismiss();
                dlg = null;
            }
        }
        super.onDestroy();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mThread;
    }

    public void onDownloadClicked(View view) {
        showAndSetDlg();
        mThread.start();
    }
}
