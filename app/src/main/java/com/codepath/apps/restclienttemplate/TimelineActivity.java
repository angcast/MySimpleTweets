package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    TwitterClient client;
    ArrayList<Tweet> tweets;
    TweetAdapter tweetAdapter;
    RecyclerView rvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets);
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);

        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetAdapter);

        getSupportActionBar().setTitle("Twitter");

        populateTimeline();


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
    //TODO why only 2 on success?
    private void populateTimeline(){
        client.getHomeTimeLine(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                logResponse(response.toString());
                deserialize(response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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
}
