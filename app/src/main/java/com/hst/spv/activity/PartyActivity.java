package com.hst.spv.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hst.spv.R;
import com.hst.spv.adapter.ElectionResultAdapter;
import com.hst.spv.bean.PartyElectionResult;
import com.hst.spv.servicehelpers.ServiceHelper;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.CommonUtils;
import com.hst.spv.utils.SPVConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.GONE;

public class PartyActivity extends AppCompatActivity implements IServiceListener, View.OnClickListener {

    public static final String TAG = PartyActivity.class.getName();

    private ImageView back;
    private TextView partyIdealogy, partyHistory, partyElection, viewTextIdea, viewTextHistory;
    private RelativeLayout viewTextLayout, viewHistLayout;
    private LinearLayout viewListLayout;

    private ListView electionList;
    private ArrayList<PartyElectionResult> resultArrayList;
    private ElectionResultAdapter resultAdapter;

    private ServiceHelper serviceHelper;
    private String resString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);

        back = (ImageView)findViewById(R.id.img_back);
        partyIdealogy = (TextView)findViewById(R.id.ideas);
        partyHistory = (TextView)findViewById(R.id.history);
        partyElection = (TextView)findViewById(R.id.election);
        viewTextIdea = (TextView)findViewById(R.id.viewText);
        viewTextHistory = (TextView)findViewById(R.id.viewTxt);

        electionList = (ListView)findViewById(R.id.eleList);
        resultArrayList= new ArrayList<>();

        viewTextLayout = (RelativeLayout)findViewById(R.id.view_lay);
        viewHistLayout = (RelativeLayout)findViewById(R.id.viewHist);
        viewListLayout = (LinearLayout)findViewById(R.id.viewList);

        partyIdealogy.setOnClickListener(this);
        partyHistory.setOnClickListener(this);
        partyElection.setOnClickListener(this);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);

        getPartyIdeas();
    }

    @Override
    public void onClick(View v) {

        if (v == back){

            Intent backIntent = new Intent(this, MainActivity.class);
            startActivity(backIntent);
        }

        if (v == partyIdealogy){
            viewTextLayout.setVisibility(View.VISIBLE);
            viewHistLayout.setVisibility(GONE);
            viewListLayout.setVisibility(GONE);
            partyIdealogy.setBackground(ContextCompat.getDrawable(this,R.drawable.bt_enabled));
            partyIdealogy.setTextColor(ContextCompat.getColor(this,R.color.white));
            partyHistory.setBackground(null);
            partyHistory.setTextColor(ContextCompat.getColor(this,R.color.party_tab));
            partyElection.setBackground(null);
            partyElection.setTextColor(ContextCompat.getColor(this,R.color.party_tab));
            getPartyIdeas();
        }
        if (v == partyHistory){
            viewTextLayout.setVisibility(GONE);
            viewHistLayout.setVisibility(View.VISIBLE);
            viewListLayout.setVisibility(GONE);
            partyIdealogy.setBackground(null);
            partyIdealogy.setTextColor(ContextCompat.getColor(this,R.color.party_tab));
            partyHistory.setBackground(ContextCompat.getDrawable(this,R.drawable.bt_enabled));
            partyHistory.setTextColor(ContextCompat.getColor(this,R.color.white));
            partyElection.setBackground(null);
            partyElection.setTextColor(ContextCompat.getColor(this,R.color.party_tab));
            getPartyIdeas();
        }
        if (v == partyElection){
            viewTextLayout.setVisibility(GONE);
            viewHistLayout.setVisibility(GONE);
            viewListLayout.setVisibility(View.VISIBLE);
            partyIdealogy.setBackground(null);
            partyIdealogy.setTextColor(ContextCompat.getColor(this,R.color.party_tab));
            partyHistory.setBackground(null);
            partyHistory.setTextColor(ContextCompat.getColor(this,R.color.party_tab));
            partyElection.setBackground(ContextCompat.getDrawable(this,R.drawable.bt_enabled));
            partyElection.setTextColor(ContextCompat.getColor(this,R.color.white));
            getPartyResult();
        }
    }

    private void getPartyIdeas(){

        if (CommonUtils.isNetworkAvailable(this)) {

            resString = "partyIdeas";

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(SPVConstants.KEY_USER_ID, "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String serverUrl = SPVConstants.BUILD_URL + SPVConstants.ABOUT_PARTY;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), serverUrl);
        }
    }

    private void getPartyResult(){

        if (CommonUtils.isNetworkAvailable(this)) {

            resString = "partyElection";

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(SPVConstants.KEY_USER_ID, "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String serverUrl = SPVConstants.BUILD_URL + SPVConstants.PARTY_ELECTION;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), serverUrl);
        }
    }

    private boolean validateResponse(JSONObject response){
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
//                        AlertDialogHelper.showSimpleAlertDialog(this, msg);
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

        try{
            if (validateResponse(response)){
                if (resString.equalsIgnoreCase("partyIdeas")){

                    JSONArray partyArray = response.getJSONArray("party_result");
                    JSONObject object = partyArray.getJSONObject(0);

                    Log.d(TAG, object.toString());

                    String idealogy ="";
                    String history ="";

                    idealogy = object.getString("ideology_en");
                    viewTextIdea.setText(idealogy);
                    history = object.getString("history_en");
                    viewTextHistory.setText(history);
                }

                if (resString.equalsIgnoreCase("partyElection")){

                    JSONArray resultArray = response.getJSONArray("election_result");
                    JSONObject object = resultArray.getJSONObject(0);
                    int getLength = resultArray.length();

                    Log.d(TAG, object.toString());

                    String id = "";
                    String year = "";
                    String leader = "";
                    String won = "";

                    for (int i=0; i<getLength; i++){

                        id = resultArray.getJSONObject(i).getString("id");
                        year = resultArray.getJSONObject(i).getString("election_year");
                        leader = resultArray.getJSONObject(i).getString("party_leader_en");
                        won = resultArray.getJSONObject(i).getString("seats_won");

                        resultArrayList.add(new PartyElectionResult(id,year,leader,won));
                    }
                    resultAdapter = new ElectionResultAdapter(resultArrayList, this);
                    electionList.setAdapter(resultAdapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(String error) {

    }
}
