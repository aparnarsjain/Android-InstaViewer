package com.example.aparna.instaviewer;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by aparna on 2/2/16.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    //what data do we need from the activity
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        InstagramPhoto photo = getItem(position);
        //check if we're using a recycled view, if not we need to inflate
        if (convertView == null) {
            //create a new view from the template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);

        }
        TextView tvCaption = (TextView)convertView.findViewById(R.id.tvCaption);
        TextView tvUsername = (TextView)convertView.findViewById(R.id.tvUsername);
        TextView tvCreatedSince = (TextView)convertView.findViewById(R.id.tvTime);
        TextView tvLikes = (TextView)convertView.findViewById(R.id.tvLikes);
        TextView tvComments1 = (TextView)convertView.findViewById(R.id.tvComments1);
        TextView tvComments2 = (TextView)convertView.findViewById(R.id.tvComments2);


        ImageView ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
        ImageView ivProfilePic = (ImageView)convertView.findViewById(R.id.ivProfilePic);

        tvCaption.setText(photo.caption);
        tvUsername.setText(photo.username);
        tvCreatedSince.setText(getTimeAgo(photo.createdTime));
        tvLikes.setText(getLikesText(photo.likesCount));
        try{
            JSONObject comment = photo.comments.getJSONObject(0);
            tvComments1.setText(comment.getString("text"));
            comment = photo.comments.getJSONObject(1);
            tvComments2.setText(comment.getString("text"));
        }catch (JSONException e){
            Log.d("debug", e.toString());
        }

        //clear out the imageview
        ivPhoto.setImageResource(0);

        //insert the image using picasso
        Picasso.with(getContext())
                .load(photo.imageUrl)
                .placeholder(R.drawable.placeholder).into(ivPhoto);

        ivProfilePic.setImageResource(0);
        Picasso.with(getContext()).load(photo.profileImageUrl).into(ivProfilePic);

        return  convertView;
    }
    private String getLikesText(int likesCount){
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedNumber = formatter.format(likesCount);

        String likesWord = "likes";
        return formattedNumber + " " + likesWord;
    }
    private String getTimeAgo(long time){
        DateTime createdDateTime = new DateTime(time*1000);
        DateTime now = new DateTime();
        String timeAgo = "";

        int minuteDifference = Math.abs(Minutes.minutesBetween(createdDateTime.toLocalDateTime(), now.toLocalDateTime()).getMinutes());
        if (minuteDifference < 60) {
            timeAgo = Integer.toString(minuteDifference)+"m";
            timeAgo = Integer.toString(minuteDifference)+"m";
        } else if (Math.abs(Hours.hoursBetween(createdDateTime.toLocalDateTime(), now.toLocalDateTime()).getHours()) < 24) {
            timeAgo = Integer.toString(Hours.hoursBetween(createdDateTime.toLocalDateTime(),now.toLocalDateTime()).getHours())+"h";
        } else if (Math.abs(Months.monthsBetween(createdDateTime.toLocalDateTime(), now.toLocalDateTime()).getMonths()) < 1) {
            timeAgo = Integer.toString(Weeks.weeksBetween(createdDateTime.toLocalDateTime(), now.toLocalDateTime()).getWeeks()) + "w";
        } else {
            timeAgo = Integer.toString(Months.monthsBetween(createdDateTime.toLocalDateTime(), now.toLocalDateTime()).getMonths())+"m";
        }
        return timeAgo;
    }

    private String convertTimeFormat(long givenTime) {
        long now = System.currentTimeMillis();
        if (givenTime< 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            givenTime*= 1000;
        }
        return DateUtils.getRelativeTimeSpanString(givenTime, now, DateUtils.SECOND_IN_MILLIS ).toString();
    }
}
