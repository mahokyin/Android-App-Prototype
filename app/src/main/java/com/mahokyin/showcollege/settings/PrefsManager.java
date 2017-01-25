package com.mahokyin.showcollege.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.mahokyin.showcollege.AppController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by mahokyin on 7/30/16.
 */
public class PrefsManager {

    private PrefsManager() {

    }

    public static void writeToPrefs(PreferenceKey key, boolean value) {
        save(key, value);
    }

    public static void writeToPrefs(PreferenceKey key, String value) {
        save(key, value);
    }

    private static void save(PrefsTag prefsTag, String value) {
        SharedPreferences.Editor editor = getAppPrefs().edit();
        editor.putString(prefsTag.getName(), value);
        editor.apply();
    }

    private static void save(PrefsTag prefsTag, boolean value) {
        SharedPreferences.Editor editor = getAppPrefs().edit();
        editor.putBoolean(prefsTag.getName(), value);
        editor.apply();
    }

    private static void save(PrefsTag prefsTag, int value) {
        SharedPreferences.Editor editor = getAppPrefs().edit();
        editor.putInt(prefsTag.getName(), value);
        editor.apply();
    }

    public static boolean getFromPrefs(PreferenceKey key, boolean defaultValue) {
        return getAppPrefs().getBoolean(key.getName(), defaultValue);
    }

    public static String getFromPrefs(PreferenceKey key, String defaultValue) {
        return getAppPrefs().getString(key.getName(), defaultValue);
    }

    private static SharedPreferences getAppPrefs() {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(AppController.getInstance());
    }

    public static void writeToPrefs(PreferenceHeader preferenceHeader, String key, String value) {
        SharedPreferences.Editor editor = AppController.getInstance()
                .getSharedPreferences(preferenceHeader.getName(), Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getFromPrefs(PreferenceHeader preferenceHeader, String key, String defaultValue) {
        SharedPreferences sharedPreferences = AppController.getInstance()
                .getSharedPreferences(preferenceHeader.getName(), Context.MODE_PRIVATE);

        return sharedPreferences.getString(key, defaultValue);
    }

    public static Map<String, String> convertToConcurrentHashMap(PreferenceHeader preferenceHeader) {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(preferenceHeader.getName(), Context.MODE_PRIVATE);
        return new ConcurrentHashMap(sharedPreferences.getAll());
    }
}

