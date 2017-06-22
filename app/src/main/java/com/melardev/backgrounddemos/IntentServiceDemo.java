package com.melardev.backgrounddemos;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IntentServiceDemo extends IntentService {

    public static final String EXTRA_RECEIVER = "receiver";
    public static final String ACTION_FETCH_IMG = "fetch_img";
    public static final String ACTION_FETCH_TXT = "fetch_txt";

    public static final String EXTRA_URL = "extra_url";
    public static final String EXTRA_OUT_IMG = "img";
    public static final String EXTRA_OUT_TXT = "txt";

    public IntentServiceDemo() {
        super("IntentServiceDemo");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean isMainThread = Looper.getMainLooper() == Looper.myLooper();
        Log.d("TAG", "this is " + (isMainThread ? "" : " not ") + " the main thread");
        if (intent != null) {
            final String action = intent.getAction();
            ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra(EXTRA_RECEIVER);
            Bundle bundle = new Bundle();
            try {
                URL url = new URL(intent.getStringExtra(EXTRA_URL));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();

                if (ACTION_FETCH_IMG.equals(action)) {
                    Bitmap img = downloadImage(input);
                    bundle.putParcelable(EXTRA_OUT_IMG, img);
                    receiver.send(0, bundle);
                } else if (ACTION_FETCH_TXT.equals(action)) {
                    String txt = downloadText(input);
                    bundle.putString(EXTRA_OUT_TXT, txt);
                    receiver.send(0, bundle);
                }

                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap downloadImage(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }


    private String downloadText(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

}
