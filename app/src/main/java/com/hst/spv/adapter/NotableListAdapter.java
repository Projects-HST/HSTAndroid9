package com.hst.spv.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hst.spv.R;
import com.hst.spv.bean.NotableList;
import com.hst.spv.bean.PositionList;

import java.util.ArrayList;

public class NotableListAdapter extends BaseAdapter {

    private ArrayList<NotableList> list;
    Context context;
//    String className;

    public NotableListAdapter( Context cont, ArrayList<NotableList> list) {
        this.list = list;
        this.context = cont;
//        this.className = className;
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
            convertView = inflater.inflate(R.layout.notable_list, parent, false);

            holder = new ViewHolder();

            holder.title = (TextView) convertView.findViewById(R.id.notable);
            holder.cont_description = (TextView) convertView.findViewById(R.id.notable_cont);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        NotableList positions = list.get(position);

        holder.title.setText(positions.getTitle_en());
        holder.cont_description.setText(positions.getNoteable_text_en());

        return convertView;
    }

    public class ViewHolder {

        TextView title, cont_description;
    }

}
