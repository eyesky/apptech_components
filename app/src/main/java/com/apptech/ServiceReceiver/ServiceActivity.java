package com.apptech.ServiceReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.apptechcomponents.R;

public class ServiceActivity extends AppCompatActivity {

    private TextView textView;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            handleResult(bundle);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        textView = (TextView) findViewById(R.id.status);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(DownloadService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void onClick(View view) {

        Intent intent = new Intent(this, DownloadService.class);
        // add infos for the service which file to download and where to store
        intent.putExtra(DownloadService.FILENAME, "index.html");
        intent.putExtra(DownloadService.URL, "http://www.vogella.com/index.html");
        startService(intent);

        textView.setText("Service started");
    }

    private void handleResult(Bundle bundle) {
        if (bundle != null) {
            String string = bundle.getString(DownloadService.FILEPATH);
            int resultCode = bundle.getInt(DownloadService.RESULT);
            if (resultCode == RESULT_OK) {
                Toast.makeText(ServiceActivity.this, "Download complete. Download URI: " + string, Toast.LENGTH_LONG).show();
                textView.setText("Download done");
            } else {
                Toast.makeText(ServiceActivity.this, "Download failed", Toast.LENGTH_LONG).show();
                textView.setText("Download failed");
            }
        }
    }

}
