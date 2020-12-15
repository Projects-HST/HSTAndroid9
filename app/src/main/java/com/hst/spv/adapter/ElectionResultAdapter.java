package com.hst.spv.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hst.spv.R;
import com.hst.spv.bean.PartyElectionResult;
import com.hst.spv.utils.PreferenceStorage;

import java.util.ArrayList;

public class ElectionResultAdapter extends BaseAdapter {

    private ArrayList<PartyElectionResult> electionResult;
    private Context context;

    public ElectionResultAdapter(ArrayList<PartyElectionResult> electionResult, Context context) {
        this.electionResult = electionResult;
        this.context = context;
    }

    @Override
    public int getCount() {
        return electionResult.size();
    }

    @Override
    public Object getItem(int position) {
        return electionResult.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_election, parent, false);

            holder = new ViewHolder();

            holder.eleYear = (TextView)convertView.findViewById(R.id.yr);
            holder.partyLeader = (TextView)convertView.findViewById(R.id.ldr);
            holder.won = (TextView)convertView.findViewById(R.id.won);

            convertView.setTag(holder);
        }
        else {

            holder = (ViewHolder)convertView.getTag();
        }
        PartyElectionResult resultList = electionResult.get(position);

        holder.eleYear.setText(resultList.getElection_year());

//        if (PreferenceStorage.getLang(holder.partyLeader.getContext()).equalsIgnoreCase("english")) {
            holder.partyLeader.setText(capitalizeString(resultList.getParty_leader_en()));

//        } else {
//            holder.partyLeader.setText(capitalizeString(resultList.getParty_leader_ta()));
//        }

        holder.won.setText(resultList.getSeats_won());

        return convertView;
    }
    public class ViewHolder {

        TextView eleYear, partyLeader, won;
    }

}
