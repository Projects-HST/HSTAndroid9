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


public class PositionsFragment extends Fragment implements IServiceListener {

    private static final String TAG = YourSpv.class.getName();
    private View rootView;
    private TextView govt, party, ministry;
    private TextView title_govt, title_party, title_ministry;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper dialogHelper;

    public static PositionsFragment newInstance(int position) {
        PositionsFragment fragment = new PositionsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_positions, container, false);
        initView();
        return rootView;
    }

    private void initView(){

        title_govt = rootView.findViewById(R.id.govt);
        govt = rootView.findViewById(R.id.govt_cont);
        title_party = rootView.findViewById(R.id.party);
        party = rootView.findViewById(R.id.party_cont);
        title_ministry = rootView.findViewById(R.id.ministry);
        ministry = rootView.findViewById(R.id.ministry_cont);

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);

        dialogHelper = new ProgressDialogHelper(getActivity());

        positionsHeld();
    }

    private void positionsHeld(){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(SPVConstants.KEY_USER_ID, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String serverUrl = SPVConstants.BUILD_URL + SPVConstants.POSITIONS_URL;
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

                String title_1 = "";
                String gov = "";
                String title_2 = "";
                String katchi = "";
                String title_3 = "";
                String cabinet = "";

                for (int i=0; i<pos_details.length(); i++){

                    title_1 = pos_details.getJSONObject(0).getString("title_en");
                    title_govt.setText(title_1);
                    gov = pos_details.getJSONObject(0).getString("position_text_en");
                    govt.setText(gov);
                    title_2 = pos_details.getJSONObject(1).getString("title_en");
                    title_party.setText(title_2);
                    katchi= pos_details.getJSONObject(1).getString("position_text_en");
                    party.setText(katchi);
                    title_3 = pos_details.getJSONObject(2).getString("title_en");
                    title_ministry.setText(title_3);
                    cabinet = pos_details.getJSONObject(2).getString("position_text_en");
                    ministry.setText(cabinet);


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