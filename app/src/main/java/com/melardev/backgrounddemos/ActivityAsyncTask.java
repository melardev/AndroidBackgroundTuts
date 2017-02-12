package com.melardev.backgrounddemos;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class ActivityAsyncTask extends AppCompatActivity {

    private ImageView image;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);
        image = (ImageView) findViewById(R.id.img);
        progressDialog = new ProgressDialog(this);
    }

    public void downloadImage(View view) {
        new ImageAsyncDownloader().execute("http://as.com/img/motor/formula_1/2016/coches/858x200/mclaren.png");
    }

    private class ImageAsyncDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading please wait");
            progressDialog.show();
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            progressDialog.dismiss();
            image.setImageBitmap(result);
        }
    }
}
