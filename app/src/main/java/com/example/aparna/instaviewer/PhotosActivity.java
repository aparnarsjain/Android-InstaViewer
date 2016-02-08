package com.example.aparna.instaviewer;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class PhotosActivity extends AppCompatActivity implements CommentsFragment.OnMoreCommentsSelectedListener {
    public  static final String CLIENT_ID="e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onMoreCommentsSelected(int position) {
        InstagramPhoto selectedPhoto = photos.get(position);
        CommentsFragment commentsFragment = CommentsFragment.newInstance();
        getFragmentManager().beginTransaction().add(android.R.id.content, commentsFragment).commit();
//        commentsFragment.show(getFragmentManager(), "tag");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                photos.clear();
                aPhotos.clear();
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        photos = new ArrayList<>();
        aPhotos = new InstagramPhotosAdapter(this, photos);

        ListView lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        //Fetch the popular photos
        fetchPopularPhotos();
    }
//    public void showMoreComments(View view) {
//        CommentsFragment commentsFragment = CommentsFragment.newInstance();
////        commentsFragment.show(getFragmentManager(), "tag");
////        getFragmentManager().beginTransaction().add(android.R.id.content, commentsFragment).commit();
//
//        FragmentManager fm = getFragmentManager();
//
//        if (fm.findFragmentById(android.R.id.) == null) {
//            CommentsFragment list = new CommentsFragment();
//            fm.beginTransaction().add(android.R.id.content, list).commit();
//        }
//    }


    public void fetchPopularPhotos(){
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        //create a network client
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            //on success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //expecting a JSON object

                //iterate each item and decode the item into a java object

                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    //iterate
                    for (int i = 0; i < photosJSON.length(); i++) {
                        //get the JSON obj
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        //decode the attributes of the json into a data model
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.profileImageUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photo.createdTime = photoJSON.getLong("created_time");
                        photo.type = photoJSON.getString("type");
                        photo.comments = photoJSON.getJSONObject("comments").getJSONArray("data");
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                aPhotos.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
