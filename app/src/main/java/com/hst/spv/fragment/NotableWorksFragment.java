package com.hst.spv.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hst.spv.R;
import com.hst.spv.activity.YourSpv;
import com.hst.spv.helper.AlertDialogHelper;
import com.hst.spv.helper.ProgressDialogHelper;
import com.hst.spv.servicehelpers.ServiceHelper;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.SPVConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.util.Log.d;
import static android.util.Log.i;


public class NotableWorksFragment extends Fragment implements IServiceListener {

    private static final String TAG = YourSpv.class.getName();
    private View rootView;
    private TextView rwh, loc_govern, welfare;
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

        rwh = rootView.findViewById(R.id.rwh_cont);
        loc_govern = rootView.findViewById(R.id.loc_cont);
        welfare = rootView.findViewById(R.id.well_cont);

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);

        dialogHelper = new ProgressDialogHelper(getActivity());

        notableWorks();
    }

    private void notableWorks(){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(SPVConstants.KEY_USER_ID, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String serverUrl = SPVConstants.Base_Url + SPVConstants.NOTABLE_URL;
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

        try {

            if (validateSignInResponse(response)) {

                JSONArray pos_details = response.getJSONArray("position_result");
                JSONObject res_position = pos_details.getJSONObject(0);

                Log.d(TAG, res_position.toString());

                String rain_water = "";
                String loc_gov = "";
                String wel = "";

                for (int i=0; i<pos_details.length(); i++){

                    rain_water = pos_details.getJSONObject(0).getString("noteable_text_en");
                    rwh.setText(rain_water);
                    loc_gov = pos_details.getJSONObject(1).getString("noteable_text_en");
                    loc_govern.setText(loc_gov);
                    wel = pos_details.getJSONObject(2).getString("noteable_text_en");
                    welfare.setText(wel);
                }
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
}