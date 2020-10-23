package com.hst.spv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hst.spv.R;
import com.hst.spv.bean.NewsFeed;
import com.hst.spv.utils.PreferenceStorage;
import com.hst.spv.utils.SPVConstants;
import com.hst.spv.utils.SPVValidator;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NallaramTrustAdapter extends RecyclerView.Adapter<NallaramTrustAdapter.MyViewHolder> {

    private ArrayList<NewsFeed> newsFeedArrayList;
    Context context;
    private OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtNewsfeedTitle, txtNewsDate, txtNewsfeedDescription, txtLikes, txtComments, txtShares;
        public LinearLayout newsfeedLayout;
        public ImageView newsImage;
        public MyViewHolder(View view) {
            super(view);
            newsfeedLayout = (LinearLayout) view.findViewById(R.id.newsfeed_layout);
            newsImage = (ImageView) view.findViewById(R.id.news_img);
            newsfeedLayout.setOnClickListener(this);
            txtNewsfeedTitle = (TextView) view.findViewById(R.id.news_title);
            txtNewsDate = (TextView) view.findViewById(R.id.news_date);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
//            else {
//                onClickListener.onClick(Selecttick);
//            }
        }
    }


    public NallaramTrustAdapter(ArrayList<NewsFeed> newsFeedArrayList, OnItemClickListener onItemClickListener) {
        this.newsFeedArrayList = newsFeedArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_gallery, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NewsFeed newsFeed = newsFeedArrayList.get(position);

        if (PreferenceStorage.getLang(holder.txtNewsfeedTitle.getContext()).equalsIgnoreCase("english")) {
            holder.txtNewsfeedTitle.setText(capitalizeString(newsFeed.getTitleEnglish()));

        } else {
            holder.txtNewsfeedTitle.setText(capitalizeString(newsFeed.getTitleTamil()));
        }
        holder.txtNewsDate.setText(getserverdateformat(newsFeed.getNewsDate()));
        if (SPVValidator.checkNullString(newsFeed.getCoverImage())) {
            String url = SPVConstants.BUILD_URL + SPVConstants.ASSETS_URL_NEWSFEED + newsFeed.getCoverImage();
            Picasso.get().load(url).into(holder.newsImage);
        } else {
//            newsImage.setImageResource(R.drawable.news_banner);
        }
    }

    private String getserverdateformat(String dd) {
        String serverFormatDate = "";
        if (dd != null && dd != "") {

            String date = dd;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date testDate = null;
            try {
                testDate = formatter.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            serverFormatDate = sdf.format(testDate);
            System.out.println(".....Date..." + serverFormatDate);
        }
        return serverFormatDate;
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
    public int getItemCount() {
        return newsFeedArrayList.size();
    }

}