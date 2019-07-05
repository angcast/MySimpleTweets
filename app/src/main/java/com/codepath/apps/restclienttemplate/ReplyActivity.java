package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class ReplyActivity extends AppCompatActivity {

    int charsLeft;
    TextView viewCharsLeft;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        viewCharsLeft = (TextView) findViewById(R.id.replyWordCount);
        charsLeft = 240;
        textViewParam(charsLeft);

        client = TwitterApp.getRestClient(this);
        textChange();
    }

    public void textViewParam (int count){
        viewCharsLeft.setText(String.valueOf(count));
    }

    public  void textChange(){
        EditText et = (EditText) findViewById(R.id.etReplyWords);
        et.setText("@"+getIntent().getStringExtra("username")+" ");
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
        EditText et = (EditText) findViewById(R.id.etReplyWords);
        String message = et.getText().toString();
        long uid = getIntent().getLongExtra("uid",0);
        client.sendReply(message, uid, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                finish();
            }
        });

    }



}
