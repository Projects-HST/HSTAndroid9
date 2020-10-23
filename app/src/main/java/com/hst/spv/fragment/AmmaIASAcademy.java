package com.hst.spv.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hst.spv.R;
import com.hst.spv.activity.NamakaagaInitiatives;
import com.hst.spv.helper.AlertDialogHelper;
import com.hst.spv.helper.ProgressDialogHelper;
import com.hst.spv.interfaces.DialogClickListener;
import com.hst.spv.servicehelpers.ServiceHelper;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.CommonUtils;
import com.hst.spv.utils.SPVConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.util.Log.d;

public class AmmaIASAcademy extends Fragment implements IServiceListener, DialogClickListener {

    private static final String TAG = NamakaagaInitiatives.class.getName();
    private View rootView;
    private TextView abt_academy, abt_upsc, abt_tnpsc, cr_title_1, cr_title_2;
    private ImageView aca_banner, cr_img_1, cr_img_2;
    private String assets_url;
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

        aca_banner = rootView.findViewById(R.id.namakkaga);
        abt_academy = rootView.findViewById(R.id.abt_academy);
        cr_img_1 = rootView.findViewById(R.id.upsc_logo);
        cr_img_2 = rootView.findViewById(R.id.tnpsc_logo);
        abt_upsc = rootView.findViewById(R.id.abt_upsc);
        abt_tnpsc = rootView.findViewById(R.id.abt_tnpsc);
        cr_title_1 = rootView.findViewById(R.id.course_title_1);
        cr_title_2 = rootView.findViewById(R.id.course_title_2);
        visit = rootView.findViewById(R.id.visit);

        assets_url = SPVConstants.BUILD_URL + SPVConstants.ASSETS_URL_ACADEMY;

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);

        dialogHelper = new ProgressDialogHelper(getActivity());
    }

    private void academy(){
        if (CommonUtils.isNetworkAvailable(getContext())) {

            JSONObject object = new JSONObject();

            try {
                object.put(SPVConstants.KEY_USER_ID, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialogHelper.showProgressDialog(getResources().getString(R.string.progress_bar));
            String serverUrl = SPVConstants.BUILD_URL + SPVConstants.ACADEMY_URL;
            serviceHelper.makeGetServiceCall(object.toString(), serverUrl);
        }
        else {
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

        try{

            if(validateSignInResponse(response)) {

                JSONArray academyResult = response.getJSONArray("academy_result");
                JSONObject resAcademy = academyResult.getJSONObject(0);

                Log.d(TAG, resAcademy.toString());

                String academyContent = "";
                String academyBanner = "";
                String bannerImage = "";

                for (int i = 0; i < academyResult.length(); i++) {

                    academyBanner = resAcademy.getString("academy_banner");
                    bannerImage = assets_url.concat(academyBanner);
                    academyContent = resAcademy.getString("academy_text_en");
                    abt_academy.setText(academyContent);
                }
                if (bannerImage.length() > 0){

                    Picasso.get().load(bannerImage).fit().placeholder(R.drawable.academy_img)
                                .error(R.drawable.academy_img).into(aca_banner);
                }
                else {

                    aca_banner.setImageResource(R.drawable.academy_img);
                }
            }
            if(response.getString("status").equalsIgnoreCase("Success")) {

                JSONArray academyResult = response.getJSONArray("course_result");
                JSONObject resAcademy = academyResult.getJSONObject(0);

                Log.d(TAG, resAcademy.toString());

                String title_image_1 = "";
                String title_1 = "";
                String aboutUpsc = "";
                String title_image_2;
                String title_2 = "";
                String aboutTnpsc = "";
                String image_1 = "";
                String image_2 = "";

                for (int i = 0; i < academyResult.length(); i++) {

                    title_image_1 = academyResult.getJSONObject(0).getString("course_image");
                    image_1 = assets_url.concat(title_image_1);
                    title_1 = academyResult.getJSONObject(0).getString("course_title_en");
                    cr_title_1.setText(title_1);
                    aboutUpsc = academyResult.getJSONObject(0).getString("course_details_en");
                    abt_upsc.setText(aboutUpsc);
                    title_image_2 = academyResult.getJSONObject(1).getString("course_image");
                    image_2 = assets_url.concat(title_image_2);
                    title_2 = academyResult.getJSONObject(1).getString("course_title_en");
                    cr_title_2.setText(title_2);
                    aboutTnpsc = academyResult.getJSONObject(1).getString("course_details_en");
                    abt_tnpsc.setText(aboutTnpsc);
                }
                if ((image_1.length() > 0) && (image_2.length() > 0)){

                    Picasso.get().load(image_1).fit().placeholder(R.drawable.party_logo)
                            .error(R.drawable.party_logo).into(cr_img_1);

                    Picasso.get().load(image_2).fit().placeholder(R.drawable.party_logo)
                            .error(R.drawable.party_logo).into(cr_img_2);
                }
                else {
                    cr_img_1.setImageResource(R.drawable.party_logo);
                    cr_img_2.setImageResource(R.drawable.party_logo);
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

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}