package com.apptech.ComplexRecyclerView.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtil {

    public SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;

    private final String IS_FIRST_TIME = "is_first_time";
    private final String SELECTED_LANG = "selected_lang";
    private final String IS_DATABASE_EMPTY = "is_database_empty";

    public PreferenceUtil(Context mContext) {
        super();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public boolean getIsFirstTime() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME, true);
    }

    public void setIsFirstTime(boolean isFirst) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(IS_FIRST_TIME, isFirst);
        spEditor.commit();
    }
    public boolean getIsDatabaseEmpty() {
        return sharedPreferences.getBoolean(IS_DATABASE_EMPTY, true);
    }

    public void setIsDatabaseEmpty(boolean isFirst) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(IS_DATABASE_EMPTY, isFirst);
        spEditor.commit();
    }
    public String getSelectedLang() {
        return sharedPreferences.getString(SELECTED_LANG, "");
    }

    public void setSelectedLang(String lang) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(SELECTED_LANG, lang);
        spEditor.commit();
    }
}