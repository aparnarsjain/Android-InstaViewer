package com.example.aparna.instaviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aparna on 2/7/16.
 */
public class CommentsFragmentAdapter extends ArrayAdapter<Comment> {
    public CommentsFragmentAdapter(Context context, ArrayList<Comment> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Comment comment= getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment, parent, false);
        }
        // Lookup view for data population
        TextView tvUser = (TextView) convertView.findViewById(R.id.tvUser);
        TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment);
        // Populate the data into the template view using the data object
        tvUser.setText(comment.commentText);
        // Return the completed view to render on screen
        return convertView;
    }

}
