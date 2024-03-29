package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    int charsLeft;
    TextView viewCharsLeft;
    TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        viewCharsLeft = (TextView) findViewById(R.id.wordCount);
        charsLeft = 240;
        textViewParam(charsLeft);



        client = TwitterApp.getRestClient(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#38A1F3"));
        toolbar.setTitle("New Tweet");

        textChange();
    }

    public void textViewParam (int count){
        viewCharsLeft.setText(String.valueOf(count));
    }


    public  void textChange(){
        EditText et = (EditText) findViewById(R.id.etWords);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                charsLeft = charsLeft+ (before-count);
                textViewParam(charsLeft);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    public void onClickTweet(View view){
        EditText et = (EditText) findViewById(R.id.etWords);
        String message = et.getText().toString();
        client.sendTweet(message, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Intent i = new Intent();

                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    i.putExtra("tweet", Parcels.wrap(tweet));
                    setResult(RESULT_OK,i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                finish();
            }
        });

    }

    public void onClickClose(View view){
        finish();
    }

}
