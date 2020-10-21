package com.hst.spv.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.hst.spv.R;
import com.hst.spv.activity.NamakaagaInitiatives;
import com.hst.spv.activity.NewsfeedDetailActivity;
import com.hst.spv.activity.YourSpv;
import com.hst.spv.adapter.NewsFeedListAdapter;
import com.hst.spv.bean.NewsFeed;
import com.hst.spv.bean.NewsFeedList;
import com.hst.spv.helper.AlertDialogHelper;
import com.hst.spv.helper.ProgressDialogHelper;
import com.hst.spv.interfaces.DialogClickListener;
import com.hst.spv.servicehelpers.ServiceHelper;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.CommonUtils;
import com.hst.spv.utils.SPVConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsAllFragment extends Fragment implements IServiceListener, DialogClickListener, NewsFeedListAdapter.OnItemClickListener{

    private static final String TAG = HomeFragment.class.getName();
    private View view;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    int cc = 0;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    int listcount = 0;
    NewsFeedList newsFeedList;
    ArrayList<NewsFeed> newsFeedArrayList = new ArrayList<>();
    NewsFeedListAdapter mAdapter;
    private TextView txtYouSPV, txtGallery, txtNamakaaga, txtParty;

    public static HomeFragment newInstance(int position) {
        HomeFragment frag = new HomeFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_news, container, false);
        serviceHelper = new ServiceHelper(view.getContext());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(view.getContext());

        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.list_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {

                    if (!recyclerView.canScrollVertically(1)) {

                        swipeRefreshLayout.setRefreshing(true);

                        loadmore();

                    }
                }
                return false;
            }
        });

        getNewsfeed(String.valueOf(listcount));

        return view;
    }
    private void loadmore() {
        listcount = listcount + 50;
        getNewsfeed(String.valueOf(listcount));
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
//            try {
//                constituentCount.setText(response.getString("meeting_count") + " Records");
//                cc = response.getJSONArray("meeting_details").length();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            Gson gson = new Gson();
            newsFeedList = gson.fromJson(response.toString(), NewsFeedList.class);
            newsFeedArrayList.addAll(newsFeedList.getNewsFeedArrayList());
            mAdapter = new NewsFeedListAdapter(newsFeedArrayList, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            recyclerView.scrollToPosition(listcount);
            swipeRefreshLayout.setRefreshing(false);

        }
    }

    private void getNewsfeed(String count) {
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            JSONObject jsonObject = new JSONObject();
            try {
//                if (val == 0) {
//                    paguthiID = "ALL";
//                }
                jsonObject.put(SPVConstants.KEY_OFFSET, count);
                jsonObject.put(SPVConstants.KEY_ROWCOUNT, "50");

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = SPVConstants.BUILD_URL + SPVConstants.GET_ALL_NEWS;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        } else {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), getString(R.string.error_no_net));
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
                    if (status.equalsIgnoreCase("Success")) {
                        signInSuccess = true;
                    } else {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
//                        AlertDialogHelper.showSimpleAlertDialog(getActivity(), msg);
                        if (listcount == 0) {
                            swipeRefreshLayout.setVisibility(View.GONE);
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
        NewsFeed meeting = null;
        meeting = newsFeedArrayList.get(position);
        Intent intent = new Intent(getActivity(), NewsfeedDetailActivity.class);
        intent.putExtra("meetingObj", meeting.getId());
        startActivity(intent);
    }

}