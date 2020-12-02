package com.hst.spv.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hst.spv.R;
import com.hst.spv.bean.AwardList;
import com.hst.spv.bean.CourseList;
import com.hst.spv.utils.SPVConstants;
import com.hst.spv.utils.SPVValidator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.MyViewHolder> {

    private ArrayList<CourseList> crList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView abt_course, cr_title;
        private ImageView cr_img;

        public MyViewHolder(@NonNull View view) {
            super(view);

            cr_img = (ImageView) view.findViewById(R.id.course_logo);
            cr_title = (TextView) view.findViewById(R.id.course_title);
            abt_course = (TextView) view.findViewById(R.id.abt_course);
        }
    }

    public CourseListAdapter(Context cont, ArrayList<CourseList> list) {

        this.crList = list;
        this.context = cont;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_course, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CourseList list = crList.get(position);

        String imageUrl = SPVConstants.ASSETS_URL + SPVConstants.ASSETS_URL_ACADEMY;
        String getImage = list.getCourse_image();
        String courseImage = imageUrl.concat(getImage);

        holder.cr_title.setText(list.getCourse_title_en());
        holder.abt_course.setText(list.getCourse_details_en());

        if (SPVValidator.checkNullString(courseImage)) {

            Picasso.get().load(courseImage).fit().placeholder(R.drawable.tnpsc_logo)
                    .error(R.drawable.tnpsc_logo).into(holder.cr_img);
        } else {

            holder.cr_img.setImageResource(R.drawable.tnpsc_logo);
        }
    }

    @Override
    public int getItemCount() {
        return crList.size();
    }
}