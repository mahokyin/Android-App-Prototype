package com.mahokyin.showcollege.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mahokyin.showcollege.AppController;
import com.mahokyin.showcollege.rest.BingApiClient;
import com.mahokyin.showcollege.rest.BingApiInterface;
import com.mahokyin.showcollege.College;
import com.mahokyin.showcollege.rest.ImageJson;
import com.mahokyin.showcollege.R;
import com.mahokyin.showcollege.rest.ImageJsonList;
import com.mahokyin.showcollege.settings.PreferenceHeader;
import com.mahokyin.showcollege.settings.PreferenceKey;
import com.mahokyin.showcollege.settings.PrefsManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class CollegeCardAdapter extends RecyclerView.Adapter<CollegeCardAdapter.MyViewHolder> {

    private Context mContext;
    private List<College> CollegeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public CollegeCardAdapter(Context mContext, List<College> CollegeList) {
        this.mContext = mContext;
        this.CollegeList = CollegeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.college_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final College college = CollegeList.get(position);
        if (college == null) {
            return;
        }
        holder.title.setText(college.getName());
        holder.count.setText(college.getWebPage());

        // loading album cover using Glide library
        String url = PrefsManager.getFromPrefs(PreferenceHeader.IMAGE_ID, college.getName(), "-1");
        if (url.equals("-1")) {

            BingApiInterface apiService =
                    BingApiClient.getClient().create(BingApiInterface.class);

            Call<ImageJsonList> call = apiService.getFirstImage(college.getName(), BingApiClient.API_KEY);
            call.enqueue(new Callback<ImageJsonList>() {
                @Override
                public void onResponse(Call<ImageJsonList> call, Response<ImageJsonList> response) {
                    if (response.body() != null && response.body().getContacts() != null) {
                        ImageJson imageJson = response.body().getContacts().get(0);
                        String url = imageJson.getUrl();
                        Picasso.with(AppController.getInstance())
                                .load(url)
                                .into(holder.thumbnail);
                        PrefsManager.writeToPrefs(PreferenceHeader.IMAGE_ID, college.getName(), url);
                    }
                }

                @Override
                public void onFailure(Call<ImageJsonList> call, Throwable t) {
                    // Log error here since request failed
                    Timber.e(t.toString());
                }
            });
        } else {
            Picasso.with(AppController.getInstance())
                    .load(url)
                    .into(holder.thumbnail);
        }

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, college.getDomain(), Toast.LENGTH_SHORT).show();
                /*
                if (mContext instanceof MainActivity) {
                    ((MainActivity) mContext).startActivity(new Intent(mContext, MapActivity.class));
                }
                */
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return CollegeList.size();
    }
}
