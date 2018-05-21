package com.apptech;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.apptech.BottomTabLayout.BottomTabLayout;
import com.apptech.CircularProgressBar.CircularProgressBar;
import com.apptech.ComplexRecyclerView.Activity.ComplexRecyclerView;
import com.apptech.ContentPlaceholder.ContentPlaceholderActivity;
import com.apptech.ExtractingColorsFromImages.ExtractingColorsFromImages;
import com.apptech.Gps.GpsActivity;
import com.apptech.LocalService.LocalServiceActivity;
import com.apptech.Networking.NetworkActivity;
import com.apptech.RecyclerviewSearch.RecyclerviewSearch;
import com.apptech.RuntimePermission.RuntimePermission;
import com.apptech.Screenshot.ScreenshotActivity;
import com.apptech.SelectionWithProgress.SelectionWithProgressBar;
import com.apptech.ServiceReceiver.ServiceActivity;
import com.apptech.Stopwatch.StopwatchActivity;
import com.apptech.ZipDownload.ZipDownloadActivity;
import com.apptech.ZipUnzip.ZipUnzipActivity;
import com.apptech.apptechcomponents.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button extractColor, bottomTabLayout, complexRecyclerView, screenshot, gps, serviceReceiver, localService,
            stopwatch, recyclerViewSearch, circularProgressBar, itemSelectionWithProgressBar, dexterPermission,
            zipDownloadRetrofit, zipUnzip, contentPlaceholder, network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialized();
        setListener();
    }

    private void initialized() {
        extractColor = findViewById(R.id.extracting_colors_from_images);
        bottomTabLayout = findViewById(R.id.bottom_tab_layout);
        complexRecyclerView = findViewById(R.id.complex_rv);
        screenshot = findViewById(R.id.screenshot);
        gps = findViewById(R.id.gps);
        serviceReceiver = findViewById(R.id.service_receiver);
        localService = findViewById(R.id.local_service);
        stopwatch = findViewById(R.id.stopwatch);
        recyclerViewSearch = findViewById(R.id.rv_search);
        circularProgressBar = findViewById(R.id.circular_progress_bar);
        itemSelectionWithProgressBar = findViewById(R.id.item_selection_with_progress_bar);
        dexterPermission = findViewById(R.id.runtime_permissions);
        zipDownloadRetrofit = findViewById(R.id.zip_download);
        zipUnzip = findViewById(R.id.zip_unzip);
        contentPlaceholder = findViewById(R.id.content_placeholder);
        network = findViewById(R.id.network);
    }

    private void setListener() {
        extractColor.setOnClickListener(this);
        bottomTabLayout.setOnClickListener(this);
        complexRecyclerView.setOnClickListener(this);
        screenshot.setOnClickListener(this);
        gps.setOnClickListener(this);
        serviceReceiver.setOnClickListener(this);
        localService.setOnClickListener(this);
        stopwatch.setOnClickListener(this);
        recyclerViewSearch.setOnClickListener(this);
        circularProgressBar.setOnClickListener(this);
        itemSelectionWithProgressBar.setOnClickListener(this);
        dexterPermission.setOnClickListener(this);
        zipDownloadRetrofit.setOnClickListener(this);
        zipUnzip.setOnClickListener(this);
        contentPlaceholder.setOnClickListener(this);
        network.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.extracting_colors_from_images:
                startActivity(new Intent(this, ExtractingColorsFromImages.class));
                break;

            case R.id.bottom_tab_layout:
                startActivity(new Intent(this, BottomTabLayout.class));
                break;

            case R.id.complex_rv:
                startActivity(new Intent(this, ComplexRecyclerView.class));
                break;

            case R.id.screenshot:
                startActivity(new Intent(this, ScreenshotActivity.class));
                break;

            case R.id.gps:
                startActivity(new Intent(this, GpsActivity.class));
                break;

            case R.id.service_receiver:
                startActivity(new Intent(this, ServiceActivity.class));
                break;

            case R.id.local_service:
                startActivity(new Intent(this, LocalServiceActivity.class));
                break;

            case R.id.stopwatch:
                startActivity(new Intent(this, StopwatchActivity.class));
                break;

            case R.id.rv_search:
                startActivity(new Intent(this, RecyclerviewSearch.class));
                break;

            case R.id.circular_progress_bar:
                startActivity(new Intent(this, CircularProgressBar.class));
                break;

            case R.id.item_selection_with_progress_bar:
                startActivity(new Intent(this, SelectionWithProgressBar.class));
                break;

            case R.id.runtime_permissions:
                startActivity(new Intent(this, RuntimePermission.class));
                break;

            case R.id.zip_download:
                startActivity(new Intent(this, ZipDownloadActivity.class));
                break;

            case R.id.zip_unzip:
                startActivity(new Intent(this, ZipUnzipActivity.class));
                break;

            case R.id.content_placeholder:
                startActivity(new Intent(this, ContentPlaceholderActivity.class));
                break;

            case R.id.network:
                startActivity(new Intent(this, NetworkActivity.class));
                break;

            default:
                break;
        }
    }
}
