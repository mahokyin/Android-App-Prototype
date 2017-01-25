package com.mahokyin.showcollege.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahokyin on 1/22/17.
 */

public class ImageJsonList {
    @SerializedName("value")
    @Expose
    private List<ImageJson> contacts = new ArrayList<>();

    /**
     * @return The contacts
     */
    public List<ImageJson> getContacts() {
        return contacts;
    }

    /**
     * @param contacts The contacts
     */
    public void setContacts(ArrayList<ImageJson> contacts) {
        this.contacts = contacts;
    }
}
