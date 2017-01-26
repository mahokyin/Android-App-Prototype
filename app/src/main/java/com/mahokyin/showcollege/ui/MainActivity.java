package com.mahokyin.showcollege.ui;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mahokyin.showcollege.AppController;
import com.mahokyin.showcollege.College;
import com.mahokyin.showcollege.R;
import com.mahokyin.showcollege.helper.ThreadUtils;
import com.mahokyin.showcollege.settings.PreferenceKey;
import com.mahokyin.showcollege.settings.PrefsManager;

import org.reactivestreams.Subscriber;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import icepick.State;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MainRecycleFragment.MainActInterface {

    private List<College> collegeList;
    private MainRecycleFragment mainRecycleFragment;
    private Bundle bundle;

    @State int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bundle = savedInstanceState;
        buildFragment(savedInstanceState);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        if (collegeList != null) {
            if (!(collegeList instanceof ArrayList)) {
                collegeList = new ArrayList<>(collegeList);
            }
            outState.putParcelableArrayList("collegeList", (ArrayList) collegeList);
        }
        super.onSaveInstanceState(outState);
    }


    private void buildFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mainRecycleFragment = MainRecycleFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, mainRecycleFragment).commit();
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null && fragment instanceof MainRecycleFragment) {
                mainRecycleFragment = (MainRecycleFragment) fragment;
            } else {
                mainRecycleFragment = MainRecycleFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mainRecycleFragment)
                        .commit();
            }
        }
    }

    private String loadJSONFromAsset() {
        Timber.d("loadJSON() main Thread-->" + ThreadUtils.isMainThread());
        String json = null;
        try {
            InputStream is = getAssets().open("college.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void init(final View view) {
        if (bundle == null) {
            int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }
            final ProgressDialog ringProgressDialog =
                    ProgressDialog.show(this, "", "Loading JSON file (On Main Thread: " + ThreadUtils.isMainThread() + ")");
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> e) throws Exception {
                    e.onNext("Loading JSON file (On Main Thread: " + ThreadUtils.isMainThread() + ")");
                    String json = loadJSONFromAsset();
                    Timber.d("main Thread" + ThreadUtils.isMainThread());
                    e.onNext("Parsing JSON file (On Main Thread: " + ThreadUtils.isMainThread() + ")");
                    Type listType = new TypeToken<ArrayList<College>>() {
                    }.getType();
                    List<College> list = new Gson().fromJson(json, listType);

                    collegeList = new ArrayList<>();
                    if (PrefsManager.getFromPrefs(PreferenceKey.FIRST_USE, true)) {
                        if (list != null) {
                            for (int i = 1; i <= 1000; i++) {
                                AppController.getDbHelper().addCollege(list.get(i));
                            }

                            for (int i = 1; i <= 10; i++) {
                                collegeList.add(AppController.getDbHelper().getCollege(i));
                            }
                            count = collegeList.size();
                        }
                        PrefsManager.writeToPrefs(PreferenceKey.FIRST_USE, false);
                        Timber.d("Finish loading from default file");
                        e.onNext("Finish loading from default file");
                    } else {
                        for (int i = 1; i <= 10; i++) {
                            collegeList.add(AppController.getDbHelper().getCollege(i));
                        }
                        count = collegeList.size();
                        Timber.d("Finish loading from SQL DB");
                        e.onNext("Finish loading from SQL DB");
                    }
                    e.onComplete();
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {}

                @Override
                public void onNext(String value) {
                    if (value.equals("Finish loading from SQL DB") || value.equals("Finish loading from default file")) {
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else {
                        ringProgressDialog.setMessage(value);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(getApplicationContext(), "I/O Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Timber.e(e.getMessage());
                }

                @Override
                public void onComplete() {
                    PrefsManager.writeToPrefs(PreferenceKey.FIRST_USE, false);
                    Timber.d("onComplete() --> main Thread" + ThreadUtils.isMainThread());
                    ringProgressDialog.dismiss();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    mainRecycleFragment.buildRecycleView(view);
                }
            });
        } else {
            Timber.d("Instance retained !!");
            Toast.makeText(this, "Instance retained !!", Toast.LENGTH_SHORT).show();
            collegeList = bundle.getParcelableArrayList("collegeList");
            mainRecycleFragment.buildRecycleView(view);
        }
    }

    @Override
    public void updateCollegeList() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                List<College> updateList = new ArrayList<>();
                for (int i = count + 1; i <= count + 10; i++) {
                    updateList.add(AppController.getDbHelper().getCollege(i));
                }
                collegeList.addAll(updateList);
                count = collegeList.size();
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(String value) {}

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplicationContext(), "I/O Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                Timber.e(e.getMessage());
            }

            @Override
            public void onComplete() {
                Toast.makeText(getApplicationContext(), "Count: " + count, Toast.LENGTH_LONG).show();
                mainRecycleFragment.updateRecycleView();
                mainRecycleFragment.setLoadingStatus(true);
            }
        });
    }


    @Override
    public List<College> getList() {
        return collegeList;
    }

}
