package com.hst.spv.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Space;
import android.widget.TextView;

import com.hst.spv.R;
import com.hst.spv.activity.NamakaagaInitiatives;
import com.hst.spv.adapter.NamakaagaAdapter;
import com.hst.spv.helper.AlertDialogHelper;
import com.hst.spv.helper.ProgressDialogHelper;
import com.hst.spv.servicehelpers.ServiceHelper;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.SPVConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.util.Log.d;


public class NamakaagaUllatchi extends Fragment implements IServiceListener {

    private static final String TAG = NamakaagaInitiatives.class.getName();
    private View rootView;
    private TextView abt_ullatchi;
    private Button visit;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper dialogHelper;

    public static NamakaagaUllatchi newInstance(int position) {
        NamakaagaUllatchi fragment = new NamakaagaUllatchi();
        Bundle args = new Bundle();
        args.putInt("ullatchi", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_namakaaga_ullatchi, container, false);
        initView();
        namakaaga();
        return rootView;
    }

    public void initView(){

        abt_ullatchi = rootView.findViewById(R.id.ullatchi_cont);
        visit = rootView.findViewById(R.id.visit);

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);

        dialogHelper = new ProgressDialogHelper(getActivity());
    }

    private void namakaaga(){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(SPVConstants.KEY_USER_ID, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        dialogHelper.showProgressDialog("Loading");
        String serverUrl = SPVConstants.Base_Url + SPVConstants.NAMAKAAGA_URL;
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

        if(validateSignInResponse(response)){

            try{
                JSONArray posResult = response.getJSONArray("position_result");
                JSONObject resUllatchi = posResult.getJSONObject(0);

                Log.d(TAG, resUllatchi.toString());

                String content = "";

                for (int i=0; i < posResult.length(); i++) {

                    content = resUllatchi.getString("namakkaga_text_en");
                    abt_ullatchi.setText(content);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onError(String error) {

        dialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(getActivity(), error);
    }
}