package com.mahokyin.showcollege.ui;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mahokyin.showcollege.College;
import com.mahokyin.showcollege.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import icepick.State;
import timber.log.Timber;

/**
 * Created by mahokyin on 1/22/17.
 */

public class MainRecycleFragment extends Fragment {

    private MainActInterface mainActInterface;
    private RecyclerView recyclerView;
    private CollegeCardAdapter adapter;

    @State int previousTotal = 0;
    @State boolean loading = true;
    @State int visibleThreshold = 5;
    @State int firstVisibleItem;
    @State int visibleItemCount;
    @State int totalItemCount;
    @State int pastVisiblesItems;

    public MainRecycleFragment() {
        // Required empty public constructor
    }

    public static MainRecycleFragment newInstance() {
        MainRecycleFragment fragment = new MainRecycleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mainActInterface.init(view);
        return view;
    }

    public void buildRecycleView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        if (mainActInterface.getList() != null) {
            adapter = new CollegeCardAdapter(getContext(), mainActInterface.getList());
            final GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if(dy > 0) //check for scroll down
                    {
                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                        if (loading)
                        {
                            if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                            {
                                loading = false;
                                mainActInterface.updateCollegeList();
                                //Do pagination.. i.e. fetch new data
                            }
                        }
                    }
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    public void updateRecycleView() {
        adapter.notifyDataSetChanged();
    }

    public void setLoadingStatus(boolean value) {
        loading = value;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActInterface) {
            mainActInterface = (MainActInterface) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet the interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActInterface = null;
    }

    protected int getLayoutRes() {
        return R.layout.fragment_main;
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public interface MainActInterface {
        ArrayList<College> getList();
        void init(View view);
        void updateCollegeList();
    }
}
