package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 21;
    TwitterClient client;
    ArrayList<Tweet> tweets;
    TweetAdapter tweetAdapter;
    RecyclerView rvTweets;
    private SwipeRefreshLayout swipeContainer;
    MenuItem miActionProgressItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


        client = TwitterApp.getRestClient(this);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets);
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#38A1F3"));

        swipeGesture();
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Twitter");

        populateTimeline();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        miActionProgressItem.setVisible(false);
    }


    public void swipeGesture(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });
    }

    public void fetchTimelineAsync(int page){
        client.getHomeTimeLine(new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                tweetAdapter.clear();
                populateTimeline();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG","Fetch timeline error"+throwable.toString());
            }

        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        return true;
    }

    private void deserialize(JSONArray response){
            for (int i = 0; i < response.length() ; i++){
                try {
                    Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                    tweets.add(tweet);
                    tweetAdapter.notifyItemInserted(tweets.size() - 1);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
    }

    private void logResponse(String response){
        Log.d("TwitterClient",response);
    }

    public void failureAction(Throwable e){
        Log.d("DEBUG", "Fetch timeline error: " + e.toString());
    }

    private void populateTimeline(){
        client.getHomeTimeLine(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                logResponse(response.toString());
                //successAction();
                deserialize(response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //successAction();
                logResponse(response.toString());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logResponse(responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                logResponse(errorResponse.toString());
                throwable.printStackTrace();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void onCompose(MenuItem item){
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && resultCode == REQUEST_CODE) {
            Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }
    }


}
