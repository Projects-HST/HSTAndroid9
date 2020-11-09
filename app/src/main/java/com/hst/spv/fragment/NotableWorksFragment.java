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
import com.hst.spv.adapter.NotableListAdapter;
import com.hst.spv.adapter.PositionListAdapter;
import com.hst.spv.bean.NotableList;
import com.hst.spv.bean.PositionList;
import com.hst.spv.helper.AlertDialogHelper;
import com.hst.spv.helper.ProgressDialogHelper;
import com.hst.spv.interfaces.DialogClickListener;
import com.hst.spv.servicehelpers.ServiceHelper;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.CommonUtils;
import com.hst.spv.utils.SPVConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.util.Log.d;
import static android.util.Log.i;


public class NotableWorksFragment extends Fragment implements IServiceListener, DialogClickListener {

    private static final String TAG = YourSpvActivity.class.getName();
    private View rootView;
    private ListView noteList;

    private ArrayList<NotableList> notableList;
    private NotableListAdapter notableAdapter;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper dialogHelper;

    public static NotableWorksFragment newInstance(int position) {
        NotableWorksFragment fragment = new NotableWorksFragment();
        Bundle args = new Bundle();
        args.putInt("notable", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notable_works, container, false);
        initView();
        return rootView;
    }

    private void initView(){

        noteList = (ListView) rootView.findViewById(R.id.note_list);

        notableList = new ArrayList<>();


        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);

        dialogHelper = new ProgressDialogHelper(getActivity());

        notableWorks();
    }

    private void notableWorks(){

        if (CommonUtils.isNetworkAvailable(getContext())) {

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(SPVConstants.KEY_USER_ID, "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialogHelper.showProgressDialog(getResources().getString(R.string.progress_bar));
            String serverUrl = SPVConstants.BUILD_URL + SPVConstants.NOTABLE_URL;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), serverUrl);
        }else {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), getString(R.string.error_no_net));
        }
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

            if (validateSignInResponse(response)) {

                JSONArray pos_details = response.getJSONArray("position_result");
                JSONObject res_position = pos_details.getJSONObject(0);

                Log.d(TAG, res_position.toString());

                String id = "";
                String title = "";
                String cont_description = "";

                for (int i=0; i<pos_details.length(); i++){

                    id = pos_details.getJSONObject(i).getString("id");
                    title = pos_details.getJSONObject(i).getString("title_en");
                    cont_description = pos_details.getJSONObject(i).getString("noteable_text_en");

                    notableList.add(new NotableList(id, title, cont_description));
                }
                notableAdapter = new NotableListAdapter(getActivity(), notableList);
                noteList.setAdapter(notableAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

        dialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(getActivity(), error);
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}