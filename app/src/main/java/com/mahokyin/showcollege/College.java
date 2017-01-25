package com.mahokyin.showcollege;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


/**
 * Created by mahokyin on 1/22/17.
 */

public class College implements Parcelable {
    @SerializedName("name")
    private String name;
    @SerializedName("domain")
    private String domain;
    @SerializedName("web_page")
    private String webPage;
    @SerializedName("country")
    private String country;
    @SerializedName("alpha_two_code")
    private String countryCode;

    public College() {

    }

    public College(String name, String domain, String webPage, String country, String countryCode) {
        this.name = name;
        this.domain = domain;
        this.webPage = webPage;
        this.country = country;
        this.countryCode = countryCode;
    }

    private College(Parcel in) {
        name = in.readString();
        domain = in.readString();
        webPage = in.readString();
        country = in.readString();
        countryCode = in.readString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public String getDomain() {
        return domain;
    }

    public String getWebPage() {
        return webPage;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(domain);
        dest.writeString(webPage);
        dest.writeString(country);
        dest.writeString(countryCode);
    }

    public static final Parcelable.Creator<College> CREATOR = new Parcelable.Creator<College>() {
        public College createFromParcel(Parcel in) {
            return new College(in);
        }

        public College[] newArray(int size) {
            return new College[size];
        }
    };
}
