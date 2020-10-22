package com.hst.spv.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hst.spv.R;
import com.hst.spv.activity.YourSpvActivity;
import com.hst.spv.adapter.AwardListAdapter;
import com.hst.spv.bean.AwardList;
import com.hst.spv.helper.AlertDialogHelper;
import com.hst.spv.helper.ProgressDialogHelper;
import com.hst.spv.servicehelpers.ServiceHelper;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.SPVConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.util.Log.d;
import static android.util.Log.i;


public class AwardFragment extends Fragment implements IServiceListener {

    private static final String TAG = YourSpvActivity.class.getName();
    private View rootView;
    private TextView award_title;
    private ListView listView;
    private ArrayList<AwardList> awardList;
    private AwardListAdapter listAdapter;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper dialogHelper;
    private String className;

    public static AwardFragment newInstance(int position) {
        AwardFragment fragment = new AwardFragment();
        Bundle args = new Bundle();
        args.putInt("awards", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_award, container, false);
        initView();
        return rootView;
    }

    private void initView(){

        listView = (ListView) rootView.findViewById(R.id.awd_list);
//        className = this.getClass().getSimpleName();
        award_title = (TextView)rootView.findViewById(R.id.awd_cont);

        awardList = new ArrayList<>();

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);

        dialogHelper = new ProgressDialogHelper(getActivity());

        awards();
    }

    private void awards(){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(SPVConstants.KEY_USER_ID, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialogHelper.showProgressDialog(getResources().getString(R.string.progress_bar));
        String serverUrl = SPVConstants.BUILD_URL + SPVConstants.AWARDS_URL;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), serverUrl);
    }

    private boolean validateSignInResponse(JSONObject response){

        boolean signInSuccess = false;

        if ((response != null)) {

            try {
                String status = response.getString("status");
                String msg = response.getString(SPVConstants.PARAM_MESSAGE);
                d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (((status.equalsIgnoreCase("activationError")) || (status.equalsIgnoreCase("alreadyRegistered")) ||
                            (status.equalsIgnoreCase("notRegistered")) || (status.equalsIgnoreCase("error")))) {
                        signInSuccess = false;
                        d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(getActivity(), msg);

                    } else {
                        signInSuccess = true;
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

        dialogHelper.hideProgressDialog();
        try {

            if (validateSignInResponse(response)){

                JSONArray result = response.getJSONArray("award_result");
                JSONObject awd_result = result.getJSONObject(0);

                Log.d(TAG, awd_result.toString());

                String id = "";
                String details = "";

                for (int i=0; i < result.length(); i++){

                    id = result.getJSONObject(i).getString("id");
                    details = result.getJSONObject(i).getString("awards_text_en");

                    awardList.add(new AwardList(id, details));
                }
                listAdapter = new AwardListAdapter(getActivity(),awardList);
                listView.setAdapter(listAdapter);
            }
            if (response.getString("status").equalsIgnoreCase("Success")){

                JSONArray awardHeading = response.getJSONArray("award_page_heading");
                JSONObject award = awardHeading.getJSONObject(0);

                String awd_title = "";

                for (int i=0; i < awardHeading.length(); i++){

                    awd_title = award.getString("page_title_en");
                    award_title.setText(awd_title);
                }
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

        dialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(getActivity(), error);
    }
}