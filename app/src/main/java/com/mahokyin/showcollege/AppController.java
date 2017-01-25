package com.mahokyin.showcollege;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.mahokyin.showcollege.helper.DBHelper;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

/**
 * Created by mahokyin on 7/29/16.
 */
public class AppController extends Application {

    private static AppController app;

    private static DBHelper mDbHelper;


    public static AppController getInstance() {
        return app;
    }

    public static DBHelper getDbHelper() {
        return mDbHelper;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();


        Timber.plant(new Timber.DebugTree());
        Timber.d("Done with the initialization with Timber");
        LeakCanary.install(this);
        Timber.d("Done with the initialization with LeakCanary");
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Timber.d("Done with the initialization with Picasso in debug mode");
        mDbHelper = new DBHelper(this);
        Timber.d("Done with the initialization with SQL DB in debug mode");

        Picasso.setSingletonInstance(built);

        Timber.i("Done with app initilization...");
    }

}
