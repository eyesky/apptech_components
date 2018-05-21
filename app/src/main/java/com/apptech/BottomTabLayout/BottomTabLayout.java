package com.apptech.BottomTabLayout;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apptech.apptechcomponents.R;

public class BottomTabLayout extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    public static String tabExplore = "Explore";
    public static String tabAssistance = "Assistance";
    public static String tabSettings = "Settings";

    public TabLayout tabLayout;
    private String[] tabNames;
    private int[] tabIconsSelected = {R.drawable.explore_selected, R.drawable.assistance_selected, R.drawable.settings_selected};
    private int[] tabIconsUnSelected = {R.drawable.explore_unselected, R.drawable.assistance_unselected, R.drawable.settings_unselected};

    private View tabIndicator1, tabIndicator2, tabIndicator3;
    private ImageView[] tabIcons;
    private TextView[] tabTextViews;

    private Typeface typeFaceBold, typeFaceNormal;

    public int currentTab;
    public int previousTab;
    private DisplayMetrics metrics;

    private TextView tabTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tab_layout);

        initViews();
        setTabResources();
        setListener();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadData() {
        selectFragment(0);
        setTabIndicatorIconAndTextColor(0);
    }

    private void initViews() {
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        tabIndicator1 = findViewById(R.id.tab_indicator_1);
        tabIndicator2 = findViewById(R.id.tab_indicator_2);
        tabIndicator3 = findViewById(R.id.tab_indicator_3);

        tabIcons = new ImageView[tabIconsSelected.length];
        tabTextViews = new TextView[tabIconsSelected.length];

        typeFaceNormal = Typeface.createFromAsset(getAssets(), "font/Nissan Brand Regular.otf");
        typeFaceBold = Typeface.createFromAsset(getAssets(), "font/Nissan Brand Bold.otf");

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabNames = this.getResources().getStringArray(R.array.tab_names);
        setupTabLayout();
    }

    public void setTabResources() {
        tabNames = this.getResources().getStringArray(R.array.tab_names);
        for (int i = 0; i < tabNames.length; i++) {
            tabTextViews[i].setText(tabNames[i]);
        }
    }

    public void setTabIndicatorIconAndTextColor(int tabPositon) {
        switch (tabPositon) {
            case 0:
                tabIndicator1.setBackgroundColor(getResources().getColor(R.color.tab_indicator_selected));
                tabIndicator2.setBackgroundColor(getResources().getColor(R.color.tab_indicator_unselected));
                tabIndicator3.setBackgroundColor(getResources().getColor(R.color.tab_indicator_unselected));

                tabIcons[0].setBackgroundResource(tabIconsSelected[0]);
                tabIcons[1].setBackgroundResource(tabIconsUnSelected[1]);
                tabIcons[2].setBackgroundResource(tabIconsUnSelected[2]);

                tabTextViews[0].setTextColor(getResources().getColor(R.color.tab_text_color_selected));
                tabTextViews[1].setTextColor(getResources().getColor(R.color.tab_text_color_unselected));
                tabTextViews[2].setTextColor(getResources().getColor(R.color.tab_text_color_unselected));

                tabTextViews[0].setTypeface(typeFaceBold);
                tabTextViews[1].setTypeface(typeFaceNormal);
                tabTextViews[2].setTypeface(typeFaceNormal);

                break;

            case 1:
                tabIndicator1.setBackgroundColor(getResources().getColor(R.color.tab_indicator_unselected));
                tabIndicator2.setBackgroundColor(getResources().getColor(R.color.tab_indicator_selected));
                tabIndicator3.setBackgroundColor(getResources().getColor(R.color.tab_indicator_unselected));

                tabIcons[0].setBackgroundResource(tabIconsUnSelected[0]);
                tabIcons[1].setBackgroundResource(tabIconsSelected[1]);
                tabIcons[2].setBackgroundResource(tabIconsUnSelected[2]);

                tabTextViews[0].setTextColor(getResources().getColor(R.color.tab_text_color_unselected));
                tabTextViews[1].setTextColor(getResources().getColor(R.color.tab_text_color_selected));
                tabTextViews[2].setTextColor(getResources().getColor(R.color.tab_text_color_unselected));

                tabTextViews[0].setTypeface(typeFaceNormal);
                tabTextViews[1].setTypeface(typeFaceBold);
                tabTextViews[2].setTypeface(typeFaceNormal);

                break;

            case 2:
                tabIndicator1.setBackgroundColor(getResources().getColor(R.color.tab_indicator_unselected));
                tabIndicator2.setBackgroundColor(getResources().getColor(R.color.tab_indicator_unselected));
                tabIndicator3.setBackgroundColor(getResources().getColor(R.color.tab_indicator_selected));

                tabIcons[0].setBackgroundResource(tabIconsUnSelected[0]);
                tabIcons[1].setBackgroundResource(tabIconsUnSelected[1]);
                tabIcons[2].setBackgroundResource(tabIconsSelected[2]);

                tabTextViews[0].setTextColor(getResources().getColor(R.color.tab_text_color_unselected));
                tabTextViews[1].setTextColor(getResources().getColor(R.color.tab_text_color_unselected));
                tabTextViews[2].setTextColor(getResources().getColor(R.color.tab_text_color_selected));

                tabTextViews[0].setTypeface(typeFaceNormal);
                tabTextViews[1].setTypeface(typeFaceNormal);
                tabTextViews[2].setTypeface(typeFaceBold);

                break;
        }
    }

    private void setupTabLayout() {

        TabLayout.Tab tab;
        for (int i = 0; i < tabNames.length; i++) {

            tab = tabLayout.newTab();
            LinearLayout rootLayout = (LinearLayout)
                    LayoutInflater.from(this).inflate(R.layout.custom_tab, tabLayout, false);

            tabTextView = (TextView) rootLayout.findViewById(R.id.tab_title);
            tabTextViews[i] = tabTextView;
            tabTextView.setText(tabNames[i]);
            ImageView tabImageView = (ImageView) rootLayout.findViewById(R.id.iv_tab);
            tabIcons[i] = tabImageView;
            tab.setCustomView(rootLayout);
            tabLayout.addTab(tab);
        }

    }

    private void setListener() {
        tabLayout.addOnTabSelectedListener(this);
    }

    public void selectFragment(int tab) {
        Fragment frag = null;
        String tag = null;
        switch (tab) {
            case 0:
                tag = tabExplore;
                frag = ExploreFragment.newInstance();
                break;

            case 1:
                tag = tabAssistance;
                frag = AssistanceFragment.newInstance();
                break;

            case 2:
                tag = tabSettings;
                frag = SettingsFragment.newInstance();
                break;
        }

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (currentTab > previousTab) {
                ft.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
            } else {
                ft.setCustomAnimations(R.anim.left_in, R.anim.right_out, R.anim.right_in, R.anim.left_out);
            }
            ft.replace(R.id.container, frag);
            ft.addToBackStack("");
            ft.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        currentTab = tab.getPosition();
        selectFragment(tab.getPosition());
        setTabIndicatorIconAndTextColor(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        previousTab = tab.getPosition();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();

            if(!tag.equalsIgnoreCase("")) {
                selectFragment(tab.getPosition());
                setTabIndicatorIconAndTextColor(tab.getPosition());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
            Log.e("onBackPressed: ", tag);
            if (tabExplore.equalsIgnoreCase(tag)) {
                backStack();
            } else if (tabAssistance.equalsIgnoreCase(tag)) {
                backStack();
            } else if (tabSettings.equalsIgnoreCase(tag)) {
                backStack();
            } else {
                backAlert();
            }
        } else {
            backAlert();
        }
    }

    private void backStack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            backAlert();
        }
    }

    public void backAlert() {
        final Dialog dialog = langDialog();

        TextView txtViewTitle = (TextView) dialog.findViewById(R.id.txt_title);
        txtViewTitle.setText(getResources().getString(R.string.exit_alert));

        Button btnYes = (Button) dialog.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private Dialog dialog;
    public Dialog langDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.download_lang_popup);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

}
