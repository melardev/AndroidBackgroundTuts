package com.melardev.backgrounddemos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityHandler extends AppCompatActivity {


    private static final String URL_IMG = "https://www.android.com/static/2016/img/share/andy-lg.png";
    private Handler mHandler;
    public static ImageView imgHandler;
    public static TextView txtInfo;

    public static final int DOWNLOAD_FINISHED = 0;
    public static final int DOWNLOAD_BEGIN = 1;
    public static final int DOWNLOAD_FAILED = 2;
    public static final int DOWNLOAD_CONNECTING = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        txtInfo = (TextView) findViewById(R.id.txtInfo);
        imgHandler = (ImageView) findViewById(R.id.imgHandler);

        /*
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //Called when we receive a message
                DownloadTask task = (DownloadTask) msg.obj;
                switch (msg.what) {
                    case DOWNLOAD_CONNECTING:
                        Toast.makeText(ActivityHandler.this, "Connecting ...", Toast.LENGTH_SHORT).show();
                    case DownloadTask.DOWNLOAD_BEGIN:
                        Toast.makeText(ActivityHandler.this, "Downloading ...", Toast.LENGTH_SHORT).show();
                        break;
                    case DownloadTask.DOWNLOAD_FINISHED:
                        imgHandler.setImageBitmap(task.bmp);
                        Toast.makeText(ActivityHandler.this, "Done ...", Toast.LENGTH_SHORT).show();
                        break;
                    case DownloadTask.DOWNLOAD_FAILED:
                        Toast.makeText(ActivityHandler.this, "Failed ...", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };*/

        mHandler = new Handler(new DownloadTask2(this));

    }

    public void downloadImage_Handler(View view) {
        new DownloadTask(mHandler).execute(URL_IMG);
    }

    static class DownloadTask2 implements Handler.Callback {

        private final AppCompatActivity activityHandler;

        public DownloadTask2(ActivityHandler activityHandler) {
            this.activityHandler = activityHandler;
        }

        @Override
        public boolean handleMessage(Message msg) {

            //Called when we receive a message
            DownloadTask task = (DownloadTask) msg.obj;
            switch (msg.what) {
                case DOWNLOAD_CONNECTING:
                    Toast.makeText(activityHandler, "Connecting ...", Toast.LENGTH_SHORT).show();
                case DOWNLOAD_BEGIN:
                    Toast.makeText(activityHandler, "Downloading ...", Toast.LENGTH_SHORT).show();
                    break;
                case DOWNLOAD_FINISHED:
                    ActivityHandler.imgHandler.setImageBitmap(task.bmp);
                    Toast.makeText(activityHandler, "Done ...", Toast.LENGTH_SHORT).show();
                    break;
                case DOWNLOAD_FAILED:
                    Toast.makeText(activityHandler, "Failed ...", Toast.LENGTH_SHORT).show();
                    break;
            }

            return true;
        }
    }


    static class DownloadTask {

        Handler myHandler;
        Bitmap bmp;
        private String url;
        public int count;

        public DownloadTask(Handler myHandler) {
            this.myHandler = myHandler;
        }


        public void setBitmap(Bitmap bitmap) {
            this.bmp = bitmap;
        }

        public void execute(String url) {
            this.url = url;
            new Thread(new DownloadRunnable(this)).start();
        }

        public void updateStatus(int status) {
            //Sending a Message
            Message msg = myHandler.obtainMessage(status, this);
            msg.sendToTarget();

            //Sending A Runnable Object from Background Thread
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    //This will run in the Looper's Thread, so in the UI Thread
                    count += 1;
                    txtInfo.setText("I Have updated the state " + count + " times");
                }
            });

            //To remove a Message from runnable use callback you can use removeMessages() or removeCallbacks();
            //To check if a message exists use hasMessages()
        }
    }

    static class DownloadRunnable implements Runnable {
        DownloadTask downloadTask;

        public DownloadRunnable(DownloadTask downloadTask) {
            this.downloadTask = downloadTask;
        }

        @Override
        public void run() {
            try {

                URL url = new URL(downloadTask.url);

                downloadTask.updateStatus(DOWNLOAD_CONNECTING);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();

                downloadTask.updateStatus(DOWNLOAD_BEGIN);

                Bitmap bmp = BitmapFactory.decodeStream(input);
                downloadTask.setBitmap(bmp);

                downloadTask.updateStatus(DOWNLOAD_FINISHED);

                input.close();
            } catch (IOException e) {
                e.printStackTrace();

                downloadTask.updateStatus(DOWNLOAD_FAILED);
            }
        }
    }
}
