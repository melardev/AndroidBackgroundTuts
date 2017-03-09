package com.melardev.backgrounddemos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.melardev.backgrounddemos.IntentServiceDemo.ACTION_FETCH_IMG;
import static com.melardev.backgrounddemos.IntentServiceDemo.ACTION_FETCH_TXT;
import static com.melardev.backgrounddemos.IntentServiceDemo.EXTRA_OUT_IMG;
import static com.melardev.backgrounddemos.IntentServiceDemo.EXTRA_OUT_TXT;
import static com.melardev.backgrounddemos.IntentServiceDemo.EXTRA_RECEIVER;
import static com.melardev.backgrounddemos.IntentServiceDemo.EXTRA_URL;

public class ActivityIntentService extends AppCompatActivity {

    private static final String URL_IMG = "https://www.android.com/static/2016/img/share/andy-lg.png";
    private static final String URL_TXT = "https://github.com/opendatajson/football.json/blob/master/2011-12/at.1.clubs.json";
    private ImageView imgIntent;
    private TextView txtIntent;
    private MyIntentServiceReceiver intentReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service);

        imgIntent = (ImageView) findViewById(R.id.imgIntentService);
        txtIntent = (TextView) findViewById(R.id.txtIntentService);

        intentReceiver = new MyIntentServiceReceiver(this, new Handler(Looper.getMainLooper()));
    }

    public void loadImgClick(View view) {
        startService(new Intent(this, IntentServiceDemo.class)
                .setAction(ACTION_FETCH_IMG)
                .putExtra(IntentServiceDemo.EXTRA_URL, URL_IMG)
                .putExtra(IntentServiceDemo.EXTRA_RECEIVER, intentReceiver));
    }

    public void loadTextClick(View view) {
        startService(new Intent(this, IntentServiceDemo.class)
                .setAction(ACTION_FETCH_TXT)
                .putExtra(EXTRA_URL, URL_TXT)
                .putExtra(EXTRA_RECEIVER, intentReceiver));
    }

    private void onTxtDownloaded(String txt) {
        txtIntent.setText(txt);
    }

    private void onImageDownloaded(Bitmap bmp) {
        imgIntent.setImageBitmap(bmp);
    }

    class MyIntentServiceReceiver extends ResultReceiver {

        private final ActivityIntentService activityIntentService;

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param activityIntentService
         * @param handler
         */
        public MyIntentServiceReceiver(ActivityIntentService activityIntentService, Handler handler) {
            super(handler);
            this.activityIntentService = activityIntentService;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultData.containsKey(EXTRA_OUT_IMG))
                activityIntentService.onImageDownloaded((Bitmap) resultData.getParcelable(EXTRA_OUT_IMG));
            else if (resultData.containsKey(EXTRA_OUT_TXT))
                activityIntentService.onTxtDownloaded(resultData.getString(EXTRA_OUT_TXT));
        }
    }
}
