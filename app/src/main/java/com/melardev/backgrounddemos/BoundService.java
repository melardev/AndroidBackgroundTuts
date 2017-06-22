package com.melardev.backgrounddemos;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BoundService extends Service {

    /**
     * Service that allows multiple clients to bind to, the comunication is done through
     * a request-response model
     * Service will be created at first client bound, and destroyed when all the clients have unbound
     * now your onBind method has to return an Object that implements IBinder and allows remote procedure calls
     * in or cross process
     * */

    public BoundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onUnbind(Intent intent) {
//Called when ALL the clients have unbound
        return super.onUnbind(intent);
    }
}
