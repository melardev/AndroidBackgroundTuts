package com.melardev.backgrounddemos;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ActivityBoundService extends AppCompatActivity implements ServiceConnection {

    private static final String TAG = ActivityBoundService.class.getName();
    private BoundService s;
    private TextView txtBoundServiceResult;

    @Override
    protected void onStart() {
        super.onStart();
        //bind to the service here, or in a click listener, etc.
        Intent intent = new Intent(this, BoundService.class);
        tryBind(intent, Context.BIND_AUTO_CREATE);//Create the service if does not exist already
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bound_service);
        txtBoundServiceResult = (TextView) findViewById(R.id.txtBoundServiceResult);

    }

    void tryBind(Intent intent, int flags) {
        if (!bindService(intent, this, flags))
            unbindService(this); //read the docs, if we fail you must still unbind to release the connection
    }

    @Override
    protected void onStop() {
        //finish(); onStop by its own will not destroy the activity, so if you do not call unbind or finish, the bound service will
        //still be alive
        unbindService(this);
//stopService(new Intent(this, BoundService.class)); Will not stop your bound service
        Log.d(TAG, "onStop called");
        super.onStop();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        BoundService.MyBinder b = (BoundService.MyBinder) binder;
        s = b.getService();
        Toast.makeText(ActivityBoundService.this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Activity onDestroy");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Toast.makeText(s, "onServiceDisconnected", Toast.LENGTH_SHORT).show();
        s = null;
    }

    public void getNameFromBoundService(View view) {
        //startService(new Intent(this, BoundService.class));
        txtBoundServiceResult.append(" " + s.getName());
    }
}
