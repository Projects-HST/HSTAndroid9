package com.hst.spv.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hst.spv.R;
import com.hst.spv.activity.NamakaagaInitiatives;
import com.hst.spv.helper.AlertDialogHelper;
import com.hst.spv.helper.ProgressDialogHelper;
import com.hst.spv.servicehelpers.ServiceHelper;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.SPVConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.util.Log.d;

public class AmmaIASAcademy extends Fragment implements IServiceListener {

    private static final String TAG = NamakaagaInitiatives.class.getName();
    private View rootView;
    private TextView abt_academy;
    private TextView abt_upsc;
    private TextView abt_tnpsc;
    private Button visit;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper dialogHelper;

    public static AmmaIASAcademy newInstance(int position) {
        AmmaIASAcademy fragment = new AmmaIASAcademy();
        Bundle args = new Bundle();
        args.putInt("ARG_PARAM1", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_amma_i_a_s_academy, container, false);
        initView();
        academy();
        return rootView;
    }

    public void initView(){

        abt_academy = rootView.findViewById(R.id.abt_academy);
        abt_upsc = rootView.findViewById(R.id.abt_upsc);
        abt_tnpsc = rootView.findViewById(R.id.abt_tnpsc);
        visit = rootView.findViewById(R.id.visit);

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);

        dialogHelper = new ProgressDialogHelper(getActivity());
    }

    private void academy(){

        JSONObject object = new JSONObject();

        try {

            object.put(SPVConstants.KEY_USER_ID, "");

        } catch (Exception e) {
            e.printStackTrace();
        }

        dialogHelper.showProgressDialog("Loading");
        String serverUrl = SPVConstants.Base_Url + SPVConstants.ACADEMY_URL;
        serviceHelper.makeGetServiceCall(object.toString(), serverUrl);
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

            try{

                if(validateSignInResponse(response)) {

                    JSONArray academyResult = response.getJSONArray("academy_result");
                    JSONObject resAcademy = academyResult.getJSONObject(0);

                    Log.d(TAG, resAcademy.toString());

                    String academyContent = "";

                    for (int i = 0; i < academyResult.length(); i++) {

                        academyContent = resAcademy.getString("academy_text_en");
                        abt_academy.setText(academyContent);
                    }
                }
                if(response.getString("status").equalsIgnoreCase("Success")) {

                    JSONArray academyResult = response.getJSONArray("course_result");
                    JSONObject resAcademy = academyResult.getJSONObject(0);

                    Log.d(TAG, resAcademy.toString());

                    String aboutUpsc = "";
                    String aboutTnpsc = "";

                    for (int i = 0; i < academyResult.length(); i++) {

                        aboutUpsc = academyResult.getJSONObject(0).getString("course_details_en");
                        abt_upsc.setText(aboutUpsc);
                        aboutTnpsc = academyResult.getJSONObject(1).getString("course_details_en");
                        abt_tnpsc.setText(aboutTnpsc);
                    }

                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
    }
    @Override
    public void onError(String error) {

        dialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(getActivity(), error);

    }
}