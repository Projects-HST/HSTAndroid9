package com.hst.spv.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.util.Log.d;


public class NamakaagaUllatchi extends Fragment implements IServiceListener {

    private static final String TAG = NamakaagaInitiatives.class.getName();
    private View rootView;
    private TextView abt_ullatchi;
    private ImageView namakkaga_img;
    private Button visit;
    private String img_url;
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
        namakkaga_img = rootView.findViewById(R.id.namakaaga);
        visit = rootView.findViewById(R.id.visit);

        img_url = SPVConstants.Base_Url + SPVConstants.BANNER_IMAGES;

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

        dialogHelper.showProgressDialog(getResources().getString(R.string.progress_bar));
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
                JSONArray namakkagaDetails = response.getJSONArray("namakkaga_result");
                JSONObject namakkaga_result = namakkagaDetails.getJSONObject(0);

                Log.d(TAG, namakkaga_result.toString());

                String content = "";
                String banner = "";
                String image = "";

                for (int i=0; i < namakkagaDetails.length(); i++) {

                    banner = namakkaga_result.getString("namakkaga_banner");
                    image = img_url.concat(banner);
                    content = namakkaga_result.getString("namakkaga_text_en");
                    abt_ullatchi.setText(content);
                }
                if (image.length() > 0){

                    Picasso.get().load(image).fit().placeholder(R.drawable.party_logo)
                            .error(R.drawable.party_logo).into(namakkaga_img);
                }
                else {

                    namakkaga_img.setImageResource(R.drawable.party_logo);
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