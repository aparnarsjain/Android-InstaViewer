package com.example.aparna.instaviewer;

import android.content.Context;
import android.text.format.DateUtils;
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

        ImageView ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
        ImageView ivProfilePic = (ImageView)convertView.findViewById(R.id.ivProfilePic);

        tvCaption.setText(photo.caption);
        tvUsername.setText(photo.username);
        tvCreatedSince.setText(getTimeAgo(photo.createdTime));
        tvLikes.setText(getLikesText(photo.likesCount));

        //clear out the imageview
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
        Picasso.with(getContext()).load(photo.profileImageUrl).into(ivProfilePic);

        //insert the image using picasso
        return  convertView;
    }
    private String getLikesText(int likesCount){
        String likesWord = "likes";
        return likesCount + " " + likesWord;
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
 /*   public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }*/

    private String convertTimeFormat(long givenTime) {
        long now = System.currentTimeMillis();
        if (givenTime< 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            givenTime*= 1000;
        }

//        long givenTimeinMillis = givenTime * 1000;
        return DateUtils.getRelativeTimeSpanString(givenTime, now, DateUtils.SECOND_IN_MILLIS ).toString();
    }
}
