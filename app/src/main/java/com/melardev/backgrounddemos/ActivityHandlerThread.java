package com.melardev.backgrounddemos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityHandlerThread extends AppCompatActivity {

    private static final int GET_DOWNLOAD_IMG = 0;

    public static final int RESULT_SUCCESS = 90;
    public static final int RESULT_FAIL = 91;

    private HandlerBackground backgroundHandler;

    private static final String URL_IMG = "https://www.android.com/static/2016/img/share/andy-lg.png";
    static ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread);

        img = (ImageView) findViewById(R.id.imgHandlerThread);

        HandlerThread hThread = new HandlerThread("name");
        hThread.start();
        HandlerMain handlerMain = new HandlerMain();
        backgroundHandler = new HandlerBackground(hThread.getLooper(), handlerMain);

    }

    public void HandlerThread_GetImage(View view) {
        //Send a Message to the background thread(HandlerThread) so it can download an Image
        Message msg = backgroundHandler.obtainMessage(GET_DOWNLOAD_IMG, URL_IMG);
        backgroundHandler.sendMessage(msg);
    }

    static class HandlerBackground extends Handler {
        private final HandlerMain handlerMain;

        public HandlerBackground(Looper looper, HandlerMain handlerMain) {
            super(looper);
            this.handlerMain = handlerMain;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //is run in the same thread as the looper of this worker thread(HandlerThread

            switch (msg.what) {
                case GET_DOWNLOAD_IMG:
                    Message outMsg;
                    try {

                        URL url = new URL(msg.obj.toString());

                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();

                        Bitmap bmp = BitmapFactory.decodeStream(input);

                        outMsg = handlerMain.obtainMessage(RESULT_SUCCESS, bmp);
                        handlerMain.sendMessage(outMsg);

                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        outMsg = handlerMain.obtainMessage(RESULT_FAIL);
                        handlerMain.sendMessage(outMsg);


                    }
                    break;
                //other cases ...
                default:
                    break;

            }
        }
    }

    static class HandlerMain extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case RESULT_SUCCESS:
                    img.setImageBitmap((Bitmap) msg.obj);
                    break;
                case RESULT_FAIL:
                    Toast.makeText(img.getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
