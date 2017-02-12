package com.melardev.backgrounddemos;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class IntentSerivceDemo extends IntentService {

    public static final String ACTION_FETCH_IMG = "fetch_img";
    public static final String ACTION_FETCH_JSON = "fetch_json";

    public static final String EXTRA_STRING = "extra_string";

    public IntentSerivceDemo() {
        super("IntentSerivceDemo");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, IntentSerivceDemo.class);
        intent.setAction(ACTION_FETCH_IMG);
        intent.putExtra(EXTRA_STRING, param1);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean isCurrentThread = Looper.getMainLooper().isCurrentThread();
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH_IMG.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_STRING);
            } else if (ACTION_FETCH_JSON.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_STRING);
            }
        }
    }


    private void handleActionFoo(String param1, String param2) {

    }

    private void handleActionBaz(String param1, String param2) {

    }
}
