package com.apptech.ZipUnzip;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.apptech.apptechcomponents.R;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class ZipUnzipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_unzip);

        findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAndUnzipContent();
            }
        });
    }


    private void downloadAndUnzipContent(){
        String url = "https://github.com/NanoHttpd/nanohttpd/archive/master.zip";
        final String downloadLocation = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + FilenameUtils.getName(url);
        DownloadFileAsync download = new DownloadFileAsync(downloadLocation, this, new DownloadFileAsync.PostDownload(){
//        DownloadFileAsync download = new DownloadFileAsync("/sdcard/content.zip", this, new DownloadFileAsync.PostDownload(){
            @Override
            public void downloadDone(File file) {
                Log.i("TAG", "file download completed");

                // check unzip file now
                Decompress unzip = new Decompress(ZipUnzipActivity.this, file);
                unzip.unzip();

                Log.i("TAG", "file unzip completed");

                File downloadedZip = new File(downloadLocation);
                if (downloadedZip.exists()) {
                    downloadedZip.delete();
                    Log.i("TAG", "zip file deleted");
                }
            }
        });
        download.execute(url);
    }
}
