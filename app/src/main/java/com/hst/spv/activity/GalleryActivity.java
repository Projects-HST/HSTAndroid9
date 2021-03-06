package com.hst.spv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.hst.spv.R;
import com.hst.spv.adapter.GalleryListAdapter;
import com.hst.spv.adapter.ImageListAdapter;
import com.hst.spv.adapter.NallaramTrustAdapter;
import com.hst.spv.bean.Gallery;
import com.hst.spv.bean.GalleryImageList;
import com.hst.spv.bean.GalleryVideoList;
import com.hst.spv.bean.NewsFeedList;
import com.hst.spv.helper.AlertDialogHelper;
import com.hst.spv.helper.ProgressDialogHelper;
import com.hst.spv.interfaces.DialogClickListener;
import com.hst.spv.servicehelpers.ServiceHelper;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.CommonUtils;
import com.hst.spv.utils.SPVConstants;
import com.hst.spv.utils.SPVValidator;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.GONE;

public class GalleryActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, GalleryListAdapter.OnItemClickListener, ImageListAdapter.OnItemClickListener, View.OnClickListener {

    private static final String TAG = GalleryActivity.class.getName();
    private View view;
    private ImageView back, touchImage;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    int cc = 0;
    private GridLayoutManager mLayoutManager, mLayoutManagerImages;
    private RelativeLayout imgLay, vidLay, touchViewLay;
    private RecyclerView recyclerView, recyclerViewImages;
    SwipeRefreshLayout swipeRefreshLayout, swipeRefreshLayoutImages;
    int listcount = 0;
    GalleryImageList galleryImageList;
    GalleryVideoList galleryVideoList;
    ArrayList<Gallery> galleryArrayList = new ArrayList<>();
    ArrayList<Gallery> galleryImagesArrayList = new ArrayList<>();
    GalleryListAdapter mAdapter;
    ImageListAdapter mAdapterImages;
    //    private LinearLayout images, videos;
    private TextView viewMoreImages, viewMoreVideos;


    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        viewMoreImages = findViewById(R.id.photos);
        viewMoreVideos = findViewById(R.id.videos);

        back = findViewById(R.id.img_back);
        touchImage = findViewById(R.id.viewImage);
        back.setOnClickListener(this);

        imgLay = (RelativeLayout)findViewById(R.id.imageLayout);
        vidLay = (RelativeLayout)findViewById(R.id.videoLayout);
        touchViewLay = (RelativeLayout)findViewById(R.id.touchView);

        touchViewLay.setOnClickListener(this);
        viewMoreImages.setOnClickListener(this);
        viewMoreVideos.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.list_refresh);

        recyclerViewImages = (RecyclerView) findViewById(R.id.recycler_view_images);
        swipeRefreshLayoutImages = (SwipeRefreshLayout) findViewById(R.id.list_refresh_images);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });
        swipeRefreshLayoutImages.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayoutImages.setRefreshing(false);

            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {

                    if (!recyclerView.canScrollVertically(1)) {

                        swipeRefreshLayout.setRefreshing(false);

//                        loadmore();

                    }
                }
                return false;
            }
        });

        recyclerViewImages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {

                    if (!recyclerViewImages.canScrollVertically(1)) {

                        swipeRefreshLayoutImages.setRefreshing(false);

//                        loadmore();

                    }
                }
                return false;
            }
        });

        getNewsfeed(String.valueOf(listcount));

    }

    @Override
    public void onClick(View v) {

        if (v == back){
            Intent backIntent = new Intent(this, MainActivity.class);
            startActivity(backIntent);
            finish();
        }
        if (v == viewMoreImages) {
            imgLay.setVisibility(View.VISIBLE);
            vidLay.setVisibility(GONE);
            viewMoreImages.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_gallery_selected));
            viewMoreImages.setTextColor(ContextCompat.getColor(this, R.color.white));
            viewMoreVideos.setBackground(null);
            viewMoreVideos.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            galleryImagesArrayList.clear();
            getNewsfeed(String.valueOf(listcount));
        }
        if (v == viewMoreVideos) {
            vidLay.setVisibility(View.VISIBLE);
            imgLay.setVisibility(GONE);
            viewMoreVideos.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_gallery_selected));
            viewMoreVideos.setTextColor(ContextCompat.getColor(this, R.color.white));
            viewMoreImages.setBackground(null);
            viewMoreImages.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            galleryArrayList.clear();
            getNewsfeed(String.valueOf(listcount));
        }
        if (v == touchViewLay){
            touchViewLay.setVisibility(GONE);
            imgLay.setVisibility(View.VISIBLE);
            viewMoreImages.setClickable(true);
            viewMoreVideos.setClickable(true);
        }
    }

    private void loadmore() {
        listcount = listcount + 50;
        getNewsfeed(String.valueOf(listcount));
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            Gson gson = new Gson();
            galleryVideoList = gson.fromJson(response.toString(), GalleryVideoList.class);
            galleryArrayList.addAll(galleryVideoList.getGalleryArrayList());
            mAdapter = new GalleryListAdapter(galleryArrayList, this);
            mLayoutManager = new GridLayoutManager(this, 2);
            mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mAdapter.getItemViewType(position) > 0) {
                        return mAdapter.getItemViewType(position);
                    } else {
                        return 1;
                    }
                    //return 2;
                }
            });
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            swipeRefreshLayout.setRefreshing(false);

            galleryImageList = gson.fromJson(response.toString(), GalleryImageList.class);
            galleryImagesArrayList.addAll(galleryImageList.getGalleryArrayList());
            mAdapterImages = new ImageListAdapter(galleryImagesArrayList, this);
            mLayoutManagerImages = new GridLayoutManager(this, 4);
            mLayoutManagerImages.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mAdapterImages.getItemViewType(position) > 0) {
                        return mAdapterImages.getItemViewType(position);
                    } else {
                        return 1;
                    }
                    //return 2;
                }
            });
            recyclerViewImages.setLayoutManager(mLayoutManagerImages);
            recyclerViewImages.setAdapter(mAdapterImages);
            swipeRefreshLayoutImages.setRefreshing(false);

            if (galleryArrayList.isEmpty()) {
                vidLay.setVisibility(GONE);
            }
            if (galleryImagesArrayList.isEmpty()) {
                imgLay.setVisibility(GONE);
            }
        }

    }

    private void getNewsfeed(String count) {
        if (CommonUtils.isNetworkAvailable(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(SPVConstants.KEY_OFFSET, count);
                jsonObject.put(SPVConstants.KEY_ROWCOUNT, "50");

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = SPVConstants.BUILD_URL + SPVConstants.GET_ALL_NEWS;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.error_no_net));
        }
    }

    private boolean validateResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(SPVConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (status.equalsIgnoreCase("success")) {
                        signInSuccess = true;
                    } else {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
//                        AlertDialogHelper.showSimpleAlertDialog(this, msg);
                        if (listcount == 0) {
                            swipeRefreshLayout.setVisibility(GONE);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onItemClick(View view, int position) {
        Gallery meeting = null;
        meeting = galleryArrayList.get(position);
        viewMoreImages.setClickable(false);
//        Intent intent;
//        intent = new Intent(this, ViewVideoActivity.class);
//        intent.putExtra("meetingObj", meeting.getId());
//        intent.putExtra("page", "img");
//        startActivity(intent);
    }

    @Override
    public void onItemCClick(View view, int position) {
        Gallery meeting = null;
        meeting = galleryImagesArrayList.get(position);
//        imgLay.setClickable(true);
        String imageUrl = SPVConstants.ASSETS_URL + SPVConstants.ASSETS_URL_NEWSFEED + meeting.getCoverImage();
        if (SPVValidator.checkNullString(meeting.getCoverImage())){
            touchViewLay.setVisibility(View.VISIBLE);
            touchImage.setVisibility(View.VISIBLE);
            viewMoreVideos.setClickable(false);
            Picasso.get().load(imageUrl).into(touchImage);
        }
       else{
           touchViewLay.setVisibility(GONE);
           touchImage.setVisibility(GONE);
        }
    }
}
