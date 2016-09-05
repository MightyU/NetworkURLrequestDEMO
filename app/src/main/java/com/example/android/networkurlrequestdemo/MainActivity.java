package com.example.android.networkurlrequestdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ConnectivityManager networkCheck;
    private ImageView resultImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkCheck = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        resultImage = (ImageView)findViewById(R.id.reultImageView);
    }

    public void onRefreshImage (View view){

        final String TAG = "Refresh";
        String imagePath = "http://lorempixel.com/640/480";

        if (networkCheck != null) {
            NetworkInfo info = networkCheck.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                new DownloadImageTask().execute(imagePath);
            }
            else {
                Toast.makeText(this, "Network Not Available", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class DownloadImageTask extends com.example.android.networkurlrequestdemo.DownloadImageTask {

        @Override
        protected Bitmap doInBackground(String... urls) {
            return downloadImage(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){

            if (resultImage != null){
                resultImage.setImageBitmap(bitmap);
                resultImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }

        }




    }

    public Bitmap downloadImage (String path){
        final String TAG = "Download Task";
        Bitmap bitmap = null;
        InputStream inStream;

        try {
            URL url = new URL(path);
            HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setConnectTimeout(5000);
            urlConn.setReadTimeout(2500);
            urlConn.setRequestMethod("GET");
            urlConn.setDoInput(true);
            urlConn.connect();
            inStream = urlConn.getInputStream();
            bitmap = BitmapFactory.decodeStream(inStream);

        }catch (MalformedURLException e){
            Log.e(TAG, "URL error : " + e.getMessage());
        }catch (IOException e){
            Log.e(TAG, "Download failed : " + e.getMessage());
        }

        return bitmap;
    }
}


