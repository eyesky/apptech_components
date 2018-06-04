package com.apptech;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.apptech.BottomTabLayout.BottomTabLayout;
import com.apptech.CircularProgressBar.CircularProgressBar;
import com.apptech.ComplexRecyclerView.Activity.ComplexRecyclerView;
import com.apptech.ContentPlaceholder.ContentPlaceholderActivity;
import com.apptech.ExpandableListview.ExpandableListActivity;
import com.apptech.ExtractingColorsFromImages.ExtractingColorsFromImages;
import com.apptech.Gps.GpsActivity;
import com.apptech.LocalService.LocalServiceActivity;
import com.apptech.RecyclerviewSearch.RecyclerviewSearch;
import com.apptech.RuntimePermission.RuntimePermission;
import com.apptech.Screenshot.ScreenshotActivity;
import com.apptech.SelectionWithProgress.SelectionWithProgressBar;
import com.apptech.ServiceReceiver.ServiceActivity;
import com.apptech.Stopwatch.StopwatchActivity;
import com.apptech.ZipDownload.ZipDownloadActivity;
import com.apptech.ZipUnzip.ZipUnzipActivity;
import com.apptech.apptechcomponents.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public Typeface typefaceCaviarDreams;
    protected Toolbar toolbar;
    private Button extractColor, bottomTabLayout, complexRecyclerView, screenshot, gps, serviceReceiver, localService,
            stopwatch, recyclerViewSearch, circularProgressBar, itemSelectionWithProgressBar, dexterPermission,
            zipDownloadRetrofit, zipUnzip, expandableList, contentPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setStatusBarColor(getWindow(), this, getResources().getColor(R.color.colorPrimaryDark), true);

        initialized();
        setListener();
    }

    /**
     * initialized all global variable
     */
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
        expandableList = findViewById(R.id.expandable_list);
        contentPlaceholder = findViewById(R.id.content_placeholder);

        toolbar = findViewById(R.id.toolbar);
        initToolbar();
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
        expandableList.setOnClickListener(this);
        contentPlaceholder.setOnClickListener(this);
    }

    protected void initToolbar() {
        setSupportActionBar(toolbar);


        setTitle("AppTech Component");
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
//            actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_white); // for custom back icon
//            actionBar.setDisplayHomeAsUpEnabled(true); // for build in back icon
        }
        TextView titleTextView = getTextViewFrom(toolbar, "mTitleTextView");
        titleTextView.setTextColor(Color.WHITE);
        if (titleTextView != null) {
            typefaceCaviarDreams = Typeface.createFromAsset(getAssets(), "font/CaviarDreams.ttf");
            titleTextView.setTypeface(typefaceCaviarDreams);
        }
    }

    public TextView getTextViewFrom(Toolbar toolbar, String declaredField) {
        if (toolbar == null || declaredField == null) return null;
        TextView textView = null;
        Field f = null;
        try {
            f = toolbar.getClass().getDeclaredField(declaredField);
            f.setAccessible(true);
            textView = (TextView) f.get(toolbar);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }

        return textView;
    }

    /**
     * This method for change the pre-lollipop status bar color
     * using lib that define in gradle. eg.
     * implementation 'com.readystatesoftware.systembartint:systembartint:1.0.4'
     * @param window
     * @param activity
     * @param colorId
     * @param isNavi
     */
    public void setStatusBarColor(Window window, Activity activity, int colorId, boolean isNavi) {
        if (window != null && activity != null) {
            //window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tint = new SystemBarTintManager(activity);
            tint.setStatusBarTintEnabled(true);
            //tint.setNavigationBarTintEnabled(isNavi && getIsPortraitMode(activity));
            tint.setTintColor(colorId);

        }
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

            case R.id.expandable_list:
                startActivity(new Intent(this, ExpandableListActivity.class));
                break;

            default:
                break;
        }
    }
}
