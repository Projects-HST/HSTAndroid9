package com.hst.spv.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.hst.spv.R;
import com.hst.spv.customview.AViewFlipper;
import com.hst.spv.helper.AlertDialogHelper;
import com.hst.spv.helper.ProgressDialogHelper;
import com.hst.spv.interfaces.DialogClickListener;
import com.hst.spv.servicehelpers.ServiceHelper;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.PreferenceStorage;
import com.hst.spv.utils.SPVConstants;
import com.hst.spv.utils.SPVValidator;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsfeedDetailActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, View.OnClickListener {

    private static final String TAG = "NewsfeedDetailActivity";
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private String meetingID;
    public TextView txtNewsfeedTitle, txtNewsDate, txtNewsfeedDescription, txtShares;
    public LinearLayout newsfeedLayout;
    public ImageView newsImage;
    private ArrayList<String> imgUrl = new ArrayList<>();
    AViewFlipper aViewFlipper;

    private LinearLayout dotsLayout;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed_detail);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        aViewFlipper = findViewById(R.id.view_flipper);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);


        meetingID = getIntent().getStringExtra("meetingObj");

        newsImage = (ImageView) findViewById(R.id.news_img);
        txtNewsfeedTitle = (TextView) findViewById(R.id.news_title);
        txtNewsDate = (TextView) findViewById(R.id.news_date);
        txtNewsfeedDescription = (TextView) findViewById(R.id.news_description);
        txtShares = (TextView) findViewById(R.id.shares_count);

        getVideoDetail();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {

            float sensitvity = 100;
            if (imgUrl.size() >= 1) {
                if ((e1.getX() - e2.getX()) > sensitvity) {
                    SwipeLeft();
                } else if ((e2.getX() - e1.getX()) > sensitvity) {
                    SwipeRight();
                }
            }

            return true;
        }

    };

    GestureDetector gestureDetector = new GestureDetector(simpleOnGestureListener);


    private void SwipeLeft() {
        aViewFlipper.setInAnimation(this, R.anim.in_from_right);
        aViewFlipper.showNext();
        addBottomDots(aViewFlipper.getDisplayedChild());
    }


    private void SwipeRight() {
        aViewFlipper.setInAnimation(this, R.anim.in_from_left);
        aViewFlipper.showPrevious();
        addBottomDots(aViewFlipper.getDisplayedChild());
    }

    private void getVideoDetail() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(SPVConstants.NEWSFEED_ID, meetingID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = SPVConstants.BUILD_URL + SPVConstants.GET_NEWSFEED_DETAIL;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
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
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            try {
                JSONArray data = response.getJSONArray("news_result");

                if (PreferenceStorage.getLang(txtNewsfeedTitle.getContext()).equalsIgnoreCase("english")) {
                    txtNewsfeedTitle.setText(data.getJSONObject(0).getString("title_en"));
                    txtNewsfeedDescription.setMovementMethod(LinkMovementMethod.getInstance());
                    txtNewsfeedDescription.setText(HtmlCompat.fromHtml(data.getJSONObject(0).getString("description_en"), HtmlCompat.FROM_HTML_MODE_LEGACY));
//                    txtLikes.setText(data.getJSONObject(0).getString("likes_count") + " Likes");
//                    txtComments.setText(data.getJSONObject(0).getString("comments_count") + " Comments");
//                    txtShares.setText(data.getJSONObject(0).getString("share_count") + " Shares");
                } else {
                    txtNewsfeedTitle.setText(data.getJSONObject(0).getString("title_en"));
                    txtNewsfeedDescription.setMovementMethod(LinkMovementMethod.getInstance());
                    txtNewsfeedDescription.setText(HtmlCompat.fromHtml(data.getJSONObject(0).getString("description_ta"), HtmlCompat.FROM_HTML_MODE_LEGACY));
//                    txtLikes.setText(data.getJSONObject(0).getString("likes_count") + " விருப்ப");
//                    txtComments.setText(data.getJSONObject(0).getString("comments_count") + " கருத்து");
//                    txtShares.setText(data.getJSONObject(0).getString("share_count") + " பகிர்");
                }
                txtNewsDate.setText(getserverdateformat(data.getJSONObject(0).getString("news_date")));
                if (SPVValidator.checkNullString(data.getJSONObject(0).getString("nf_cover_image"))) {
                    imgUrl.add(data.getJSONObject(0).getString("nf_cover_image"));
                }
                JSONArray images = response.getJSONArray("image_result");
                if (imgUrl.size() >= 0) {
                    for (int i = 0; i < images.length(); i++) {
                        imgUrl.add(images.getJSONObject(i).getString("gallery_url"));
                    }
                    for (int a = 0; a < imgUrl.size(); a++) {
                        setImageInFlipr(imgUrl.get(a));
                        addBottomDots(aViewFlipper.getDisplayedChild());
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String getserverdateformat(String dd) {
        String serverFormatDate = "";
        if (dd != null && dd != "") {

            String date = dd;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date testDate = null;
            try {
                testDate = formatter.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            serverFormatDate = sdf.format(testDate);
            System.out.println(".....Date..." + serverFormatDate);
        }
        return serverFormatDate;
    }

    private void setImageInFlipr(String imgUrl) {
        ImageView image = new ImageView(this);
        Picasso.get().load(imgUrl).into(image);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        aViewFlipper.addView(image);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[imgUrl.size()];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(colorsActive[currentPage]);
            dots[currentPage].setTextSize(35);
        }

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}