package com.hst.spv.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hst.spv.R;
import com.hst.spv.bean.AwardList;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AwardListAdapter extends BaseAdapter {

    private ArrayList<AwardList> list;
    Context context;
    String className;

    public AwardListAdapter( Context cont, ArrayList<AwardList> list, String className) {
        this.list = list;
        this.context = cont;
        this.className = className;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.award_list, parent, false);

            holder = new ViewHolder();

            holder.award_content = (TextView) convertView.findViewById(R.id.awd_details);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        AwardList awards = list.get(position);

        holder.award_content.setText(awards.getAwards_text_en());

        return convertView;
    }

    public class ViewHolder {

        TextView award_content;
    }

}
