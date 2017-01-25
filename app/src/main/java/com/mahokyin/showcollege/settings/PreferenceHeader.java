package com.mahokyin.showcollege.settings;

/**
 * Created by Joe Ma on 8/19/2016.
 */
public enum PreferenceHeader implements PrefsTag {
    SCREEN_IMAGE("screen_image"),IMAGE_ID("IMAGE_ID");

    private String headerName;

    PreferenceHeader(String headerName) {
        this.headerName = headerName;
    }

    public String getName() {
        return headerName;
    }
}
