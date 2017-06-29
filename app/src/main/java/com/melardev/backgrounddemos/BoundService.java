package com.melardev.backgrounddemos;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BoundService extends Service {

    private final String TAG = BoundService.class.getName();
    private final IBinder mBinder = new MyBinder();
    private String[] names = {"Melardev", "Amin", "Laila", "Marwan", "Omar", "Hassan", "Nabil", "Khalil"};
    private Random random = new Random(System.currentTimeMillis());

    public BoundService() {
        Log.d(TAG, "constructor");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
//Called when ALL the clients have unbound
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");
        return Service.START_NOT_STICKY;
    }

    public String getName() {
        return names[random.nextInt(names.length)];
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "BoundService::onDestroy()");
        super.onDestroy();
    }

    public class MyBinder extends Binder {

        BoundService getService() {
            return BoundService.this;//return this instance of BoundService
        }
    }

}
