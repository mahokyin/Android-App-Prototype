package com.mahokyin.showcollege.helper;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mahokyin.showcollege.AppController;
import com.mahokyin.showcollege.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import timber.log.Timber;

/**
 * Created by hokyin.ma on 8/19/16.
 */
public class PicassoHelper {

    private static final PicassoHelper mInstance = new PicassoHelper();

    private Target target;

    public static PicassoHelper getInstance() {
        return mInstance;
    }

    public void loadFromDisk(final Uri uri, final ViewGroup layout, boolean resize, int width, int height) {
        Timber.d("loadFromDisk(Target) is running !");
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    layout.setBackgroundDrawable(new BitmapDrawable(AppController.getInstance().getResources(), bitmap));
                } else {
                    layout.setBackground(new BitmapDrawable(AppController.getInstance().getResources(), bitmap));
                }
                target = null;
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        if (resize) {
            Picasso.with(AppController.getInstance()).load(uri).networkPolicy(NetworkPolicy.OFFLINE).resize(width, height).into(target);
        } else {
            Picasso.with(AppController.getInstance()).load(uri).networkPolicy(NetworkPolicy.OFFLINE).into(target);
        }

    }

    public static void loadImage(final Uri uri, final ImageView imageView, boolean resize, int width, int height) {
        if (resize) {
            Picasso.with(AppController.getInstance())
                    .load(uri)
                    .networkPolicy(NetworkPolicy.OFFLINE).resize(width, height)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Timber.d("loadImage() --> Disk / MEMORY");
                        }

                        @Override
                        public void onError() {
                            Timber.d("loadImage() --> Online");
                            //Try again online if cache failed
                            Picasso.with(AppController.getInstance())
                                    .load(uri)
                                    .into(imageView);
                        }
                    });
        } else {
            Picasso.with(AppController.getInstance())
                    .load(uri)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Timber.d("loadImage() --> Disk / MEMORY");
                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Timber.d("loadImage() --> Online");

                            Picasso.with(AppController.getInstance())
                                    .load(uri)
                                    .into(imageView, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Picasso.with(AppController.getInstance())
                                                    .load(R.drawable.ic_dots)
                                                    .into(imageView);
                                        }
                                    });
                        }
                    });
        }
    }

    // TODO: Deal with the google photos
}
