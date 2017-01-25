package com.mahokyin.showcollege.settings;

/**
 * Created by mahokyin on 7/30/16.
 */
public enum PreferenceKey implements PrefsTag {
    // Setting stuffs
    FIRST_USE("FIRST_USE");

    private String keyName;

    PreferenceKey(String keyName) {
        this.keyName = keyName;
    }

    public String getName() {
        return keyName;
    }
}
