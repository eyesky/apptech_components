package com.apptech.CircularProgressBar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.apptech.apptechcomponents.R;

public class CircularProgressBar extends AppCompatActivity {

    private HoloCircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holo_circular_progress_bar);

        circularProgressBar = (HoloCircularProgressBar) findViewById(R.id.progress_circul);
        circularProgressBar.setProgress(0f);

        new MyAsyncTask().execute();

    }

    public class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Loop through the task
            for (int i = 0; i <= 10; i++) {

                // Publish the async task progress
                publishProgress(i);

                // Sleep the thread for 1 second
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.e("dddd", "onProgressUpdate: " + values[0]/ (100f)*10);
            circularProgressBar.setProgress(values[0]/ (100f)*10);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            circularProgressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Progress Finished", Toast.LENGTH_SHORT).show();
        }
    }
}
